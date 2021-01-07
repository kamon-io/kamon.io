---
title: 'Using the Tracing API | Kamon Documentation'
layout: docs
redirect_from:
  - /documentation/0.6.x/kamon-core/tracing/core-concepts/
  - /documentation/0.6.x/kamon-core/tracing/trace-metrics/
  - /documentation/1.x/core/basics/tracing/
---

{% include toc.html %}

Tracing
=======

Tracing is all about describing operations executed by your services and the causal relationship between them. In
tracing, the main building block are Spans. A `Span` represents a single operation and contains enough information to
determine the trace to which it belongs, how long it took to complete and which Span is its parent. Spans can
additionally have tags and marks that better describe the operations' semantics and effects on the system.

Once you put all related Spans together it is possible to reconstruct the entire trace of a request. You can think of a
trace as a picture of what needed to happen for a request to be processed by your system. That story will usually be
represented by something that like this:


<img class="img-fluid my-3" src="/assets/img/diagrams/tracing-basics.png">

There are several conclusions that can be drawn from the graph above, without even looking at the code:
  - There are two services involved in serving the `/users` endpoint of this system.
  - There are three HTTP calls made from Service A to Service B, apparently to get profiles, permissions and projects.
    These calls happen in parallel.
  - Storing the session token happens after all HTTP calls to Service B have completed.
  - A substantial amount of time was spent on storing the session token, which might be a good place to start optimizing.


## Spans

Spans are created by calling the `spanBuilder(operationName)` method on Kamon's companion object as shown below:


{% code_example %}
{%   language scala reference/core/src/main/scala/kamon/examples/scala/TraceBasics.scala tag:creating-spans label:"Scala" %}
{% endcode_example %}

The `spanBuilder(operationName)` method will return a `SpanBuilder` instance that can be used to customize the Span to a
certain degree and only after calling the `.start()` method on it the actual `Span` instance is created.
We recommend using more specific version of this method with `server|client|producer|consumer` prefix on them.
That will ensure that Spans always have a proper kind and component tags.

###### Important facts about the SpanBuilder:
  - You can only change or decide the Span's parent with the SpanBuilder. By default a newly created Span will have the
    Span from the current context as parent. This can be changed by calling either `asChildOf(span)` or `ignoreParentFromContext()`
    on the SpanBuilder.
  - The start instant for a Span can only be set with the SpanBuilder.
  - Unlike Spans, a `SpanBuilder` instance is not thread-safe and should not be passed around threads.




## Tags

Tags are key-value pairs that provide additional information about the operation represented by a Span. All spans created
by Kamon will typically have the following tags:
  - `component` specifies what library/framework instrumentation generated the Span. E.g: `akka.http.server` or `jdbc`.
  - `span.kind` specifies the role of the Span in an RPC communication. In the case of HTTP communication you will be
    seeing `client` and `server` values for this tag.
  - `error` specifies whether an error has been added to the Span via `span.fail(...)`. 
  - `http.method` specifies the request's HTTP method.
  - `http.url` specifies the request's URL.

Take a look at the [instrumentation guidelines][1] for a more comprehensive list of tags that you can expect to see on
Kamon-generated Spans. Tags can be added on a `SpanBuilder` or `Span` instance. Once a Span as been finished tags can
no longer be added.

{% code_example %}
{%   language scala reference/core/src/main/scala/kamon/examples/scala/TraceBasics.scala tag:adding-tags label:"Scala" %}
{% endcode_example %}



## Marks

Marks are timestamped labels on Spans. Marks can provide additional information regarding events that happen while a
operation is in progress. Marks like `dequeued` for the moment when a message was taken out of a queue for processing or
`connected` once a database connection has been established are good examples.

In Kamon, marks do not have any specific format requirements other than being a string. You are encouraged to use simple,
short and concise names for marks but you are free to add anything you would like in there.


{% code_example %}
{%   language scala reference/core/src/main/scala/kamon/examples/scala/TraceBasics.scala tag:adding-marks label:"Scala" %}
{% endcode_example %}



## Metrics

By default Kamon will track metrics out of all Spans, unless explicitly disabled. Span metrics are not affected by
sampling, every single Span is going to be measured and recorded, regardless of whether the Span is sampled or not. The
metric tracking Spans' latency is called `span.processing-time` and at a minimum will have these tags:
  - `operation` with the Span operation name.
  - `error` specifying whether an error was added to the Span via `span.addError(...)`.
  - `parentOperation` with the name of the operation of the parent Span, if any.
  - Any additional metric tags added via `span.tagMetrics(...)`.

{% alert warning %}
<span class="d-block font-weight-bold" >Important:</span>
It is of extreme importance that a Span's operation name and any metric-related tags are not populated with high cardinality
values; things like user or session identifiers, SQL queries or full URLs should not be used for operation names and
metric tags because an individual time series will be created for each unique combination of these attributes.
{% endalert %}

If necessary, metrics collection can be toggled by calling `trackMetrics()` and `doNotTrackMetrics()` on a `SpanBuilder`
or `Span` instance. Calling these functions will only have effect until the Span is finished.

{% code_example %}
{%   language scala reference/core/src/main/scala/kamon/examples/scala/TraceBasics.scala tag:span-metrics label:"Scala" %}
{% endcode_example %}




## The Current Span

The tracer relies on Kamon's [Context][2] to store the Span representing the currently executing operation. At any moment
the current Span can be accessed either by retrieving it from the current Context or by using the `Kamon.currentSpan()`
function, the later is just a shorthand syntax for the former.

{% code_example %}
{%   language scala reference/core/src/main/scala/kamon/examples/scala/TraceBasics.scala tag:current-span label:"Scala" %}
{% endcode_example %}

Most of the time Spans will be automatically managed by Kamon; Kamon will determine when to start and finish a Span and
when to make it part of the current Context (and thus, make it the current Span). In cases where it is necessary to
manually set a Span as the current Span, the `Kamon.runWithSpan(...)` helper function can be used:


{% code_example %}
{%   language scala reference/core/src/main/scala/kamon/examples/scala/TraceBasics.scala tag:with-span-block label:"Scala" %}
{% endcode_example %}

In the code snippet above the Spans are set as the current Span only while the blocks of code provided to
`Kamon.runWithSpan(...)` are executing, right after that the current Span will be whatever it was before. Under the hood
what is actually exchanged is the current Context, which in turn dictates the current Span. Please refer to the [Context][2]
section to get a better understanding of how this mechanism works.

Keep in mind that setting a Span as current has no effect on the Span's lifecycle. A Span can be made current several
times in several threads at the same time regardless of it being finished or not. There is an overload of the
`Kamon.runWithSpan(...)` method that allows finishing the Span after closing the scope in which it was used as shown above,
this is provided as a shorthand syntax for cases where a quick, one-off Span is needed, but it must remain clear that
finish a Span's time as current has nothing to do with actually `.finish()`ing the Span.



[1]: ../../advanced/instrumentation-guidelines/
[2]: ../context/
