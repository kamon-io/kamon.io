---
title: 'Context Propagation with Futures | Kamon Documentation'
layout: docs
redirect_from:
  - /documentation/1.x/instrumentation/futures/
  - /documentation/0.6.x/kamon-scala/overview/
---

{% include toc.html %}

Futures Instrumentation
=======================

The `kamon-futures` module provides bytecode instrumentation for performing automatic Context Propagation with Scala
Futures, as well as utilities to create Spans from a Future's body and callbacks. In previous versions of the this
module, there was a dedicated Context propagation instrumentation for the Twitter and Scalaz Futures, but those are no
longer necessary thanks to the instrumentation distributed with the [executors instrumentation][executors] module.


Context Propagation
-------------------

In the following piece of code, the body of the future will be executed asynchronously on a thread provided by the
ExecutionContext available in implicit scope, when the code runs, Kamon will capture the current `Context` available
when the future was created and make it available while executing the future's body.

{% code_example %}
{%   language scala instrumentation/futures/src/main/scala/kamon/examples/futures/ContextPropagation.scala tag:future-body %}
{% endcode_example %}

Also, when you transform a future by using map/flatMap/filter and friends or you directly register a callback on a
future (onComplete/onSuccess/onFailure), Kamon will capture the current `Context` available when transforming
the future and make it available when executing the given callback. The code snippet above would print the same
`userID` that was available when creating the future, during its body execution and during the execution of all
the asynchronous operations scheduled on it.


Creating Spans
--------------

It is possible to create Spans that represent either the Future body itself or asynchronous transformations applied to
them by using one of these two functions:

- `trace(operationName: String) { ... }` which accepts a call-by-name block that can be used to wrap the Future body.
- `traceAsync(operationName: String) { ... }` which accepts a function that can be used for any of the transformations
  than can be applied on a Future.

These two functions work very similarly, the only reason for them not being the same is the mismatch between the types
expected when creating a Future and the types expected when applying transformations to them. Let's see this in a small
example:

{% code_example %}
{%   language scala instrumentation/futures/src/main/scala/kamon/examples/futures/ContextPropagation.scala tag:future-spans %}
{% endcode_example %}

In the code example above, the instrumentation will automatically create a Span called `future-body` that measures how
long did it take to executore the Future's body, as well as additional Spans for the `calculate-length` and `to-string`
steps of the computation. Note that even though the rest of the transformations in this example will not get dedicated
Spans, they will still benefit from Context propagation, just as before!

{% include warning.html message="Do not use these functions if you are not running your application without the
instrumentation agent, since that will lead to threads not being properly cleaned up from previous request's Contexts." %}

Manual Installation
-------------------

In case you are not using the Kamon Bundle, add the dependency bellow to your build.

{% include dependency-info.html module="kamon-scala-future" version=site.data.versions.latest.futures %}
{% include instrumentation-agent-notice.html %}

[executors]: ../executors/