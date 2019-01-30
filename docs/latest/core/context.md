---
title: Install Kamon
layout: docs
redirect_from:
  - /documentation/1.x/core/basics/context/
  - /documentation/0.6.x/kamon-core/tracing/trace-context-manipulation/
---

{% include toc.html %}

Context
=======

A `Context` is a immutable set of key-value pairs that contain state specific to the execution of a particular request
in your application. Each request should have its own Context and all instrumentation and manual Context manipulation must
ensure that Contexts from different requests are never mixed together.

The most common use case for a Context is to store additional request information like a request ID, user ID, user
language or correlation IDs. If there are pieces of information that you would typically store in a `ThreadLocal`
or the `MDC`, those are good candidates to be moved to Kamon's Context. Additionaly, Kamon uses the Context propagation
mechanisms to carry around the tracer's current Span.

Most of the time Kamon's instrumentation will take care of creating, propagating and manipulating a Context for each
request.


## Keys

Context keys are used to create new Context instances and to retrieve items from the Context. All keys encode four
pieces of information required for Context propagation to work:
  - The key name. Key names must be unique and they will be used on configuration settings.
  - The scope, be it `local` or `broadcast`. Local Context keys will only be propagated within the process running the
    service while broadcast Context keys will be automatically propagated across process boundaries.
  - The value type. The return value of `.get(...)` calls is tied to the Key.
  - The default value. This is the value to be returned when retrieving a key that doesn't exist in the Context.

Keys are created by calling the appropriate methods on the `Key` companion object:

{% code_example %}
{%   language scala reference/core/src/main/scala/kamon/examples/scala/ContextBasics.scala tag:context-keys label:"Scala" %}
{% endcode_example %}

There are three keys defined in the example above:
  - `UserID` as a local key, with type `String` and a default value of `"undefined"`. This key will always be propagated
    with the context within the same process.
  - `SessionID` as a broadcast key, with type `Option[Int]` and a default value of `None`. This key will always be
    propagated within the same process and across processes.
  - `RequestID` uses a shorter syntax for the very common case of having a broadcast key with type `Option[String]`.

It is recommended (although not necessary) to create Context keys as static members and reuse the key instance wherever
it is needed.



## Manupulating a Context

As mentioned above, Context instances are immutable. Adding a key-value pair to a Context is achieved by actually creating
a new Context that includes or overrides a given key. Values can be retrived by calling `.get(key)` on any Context instance:

{% code_example %}
{%   language scala reference/core/src/main/scala/kamon/examples/scala/ContextBasics.scala tag:creating-a-context label:"Scala" %}
{% endcode_example %}



## Current Context and Scopes

The current Context always refers to the Context associated with the request currently being executed and it can be
accessed using the `Kamon.currentContext()` method. Once a Context is created for a given request it will be set as the
current Context for a finite period of time, controlled by a `Scope`. When a `Scope` is closed the previously current
Context is restored, which by default is `Context.Empty`.

The context storage works similar to Stack; every time you store a Context it becomes current, doing it several times
will make the most recently stored context be the current (like the top of a stack) but as the associated scopes get
closed, the previous contexts will become current again until eventually the current Context is `Context.Empty` again.

<img class="img-fluid my-3" src="/assets/img/diagrams/context-storage.png">

Effectively, the current Context is stored in a `ThreadLocal` but depending on the threading model of the frameworks and
libraries used to build your service a Context might need to be made current on several threads and for brief periods
of time. It is very important to close all created scopes timely, otherwise you risk leaving "dirty threads" that
might cause subsequent requests to see a previous and completely unrelated Context as the current Context.

A `Context` instance can become the current context by using any of the following methods:
  - `Kamon.storeContext(context)` which returns a `Scope` instance that must be manually closed.
  - `Kamon.withContext(context) { ... }` which takes a Context instance and makes it current while the code block executes.
  - `Kamon.withContextKey(key, value) { ... }` creates a new Context by adding the provided key to the current Context
    and makes it current while the code block executes.
  - `Kamon.withSpan(span) { ... }` is similar to the above but explicitly works with the Span context Key.

{% code_example %}
{%   language scala reference/core/src/main/scala/kamon/examples/scala/ContextBasics.scala tag:storing-a-context label:"Scala" %}
{% endcode_example %}

It is recommended to use the `Kamon.withXxx()` variants as they will ensure that Scopes will be closed appropriately.



## Codecs

Codecs are used to encode and decode broadcast Context keys when crossing the process boundaries. There are two supported
mediums for the codecs:
  - **HTTP Headers**: Each entry codec is able to write any number of HTTP headers to encode its state and read any number
    of HTTP headers to decode it. This medium is used in all HTTP frameworks instrumentation like Akka HTTP, Play
    Framework, Http4s, etc.
  - **Binary**: Each entry codec must be able to encode and decode a value from and to a `ByteBuffer`. This medium
    is used when a binary representation is more appropriate, like when sending messages to remote actor systems via
    Akka remoting or storing the Context in a message broker.

The codecs are configurable under the `kamon.context.codecs` section. Here is an extract from the default configuration
which sets the codecs for propagating the tracer's Span:

{% code_example %}
{%   language typesafeconfig reference/core/src/main/resources/application.conf tag:context-codecs label:"Scala" %}
{% endcode_example %}


### Broadcast String Codecs

Since broadcast strings are so simple (just `String` values) Kamon can automatically provide codecs for them in the case
of binary mediums, but one additional piece of configuration is required for HTTP Headers: defining the header name to
be used:

{% code_example %}
{%   language typesafeconfig reference/core/src/main/resources/application.conf tag:string-keys label:"Scala" %}
{% endcode_example %}


### Custom Codecs

When in need to create a custom codec the `Codecs.ForEntry[T]` trait must be implemented and the fully qualified class
name for the implementation must be provided via configuration. The trait looks like the following:

{% code_example %}
{%   language scala reference/core/src/main/scala/kamon/examples/scala/ContextBasics.scala tag:creating-a-codec label:"Scala" %}
{% endcode_example %}

A few important details to know when creating custom codecs:
  - The implementation class must have a no-parameters constructor.
  - A codec is responsible of encoding/decoding only one Context entry.
  - There are only two allowed types for `T`: `TextMap` and `ByteBuffer`.
  - The `encode` function might return an empty `TextMap` or `ByteBuffer` if the provided Context doesn't contain the
    key that a codec is responsible for.
  - The `decode` function is expected to use the provided Context instance as a base for its return value. Typically a
    codec will try to read a value from the medium and either return the result of `context.withKey(key, readValue)` with
    the decoded value or simply return the provided Context if no value could be read.
