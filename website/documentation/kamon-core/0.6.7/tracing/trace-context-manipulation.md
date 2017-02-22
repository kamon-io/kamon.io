---
title: Kamon | Core | Documentation
tree_title: Trace Context Manipulation
layout: documentation
---

TraceContext Manipulation
=========================

Collecting trace information is all about interacting with the tracing API and the current `TraceContext` to store
information as it is being generated throughout your application. This section will teach all the basic operations that
allow you to create, enhance and finish a `TraceContext` and segments related to it.

Please note that even while understanding how to manipulate a `TraceContext` is very important, some Kamon modules such
as our Akka, Scala, Spray and Play! modules already provide bytecode instrumentation that automatically creates,
propagates and finishes traces and segments in specific conditions, so, you might not need to ever manipulate a
`TraceContext` yourself.



Creating and Finishing a TraceContext
-------------------------------------

You can create a new `TraceContext` by using the `.newContext(..)` methods available in the `Kamon.tracer` member. As
described in the getting started section, make sure you start Kamon before using this API. When you create a new context
you need to at least provide a name for it, which you can change at any point of time before finishing the trace.
Additionally, you can provide a trace token to identify the trace, if you don't, Kamon will generate one for you.

{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/TraceContextManipulation.scala tag:new-context %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/TraceContextManipulation.java tag:new-context %}
{% endcode_example %}

Finishing a `TraceContext`, as shown in the example is just about calling the `.finish()` method on a context. Once a
trace is finished it can no longer be renamed again.

Additionally, the `Tracer` companion object provides alternative `.withNewTraceContext(..)` methods that will not only
create a new `TraceContext` for you, but also make it the the current trace context and optionally finishing it after
the supplied piece of code finishes it's execution as shown in the example bellow:


{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/TraceContextManipulation.scala tag:new-context-block %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/TraceContextManipulation.java tag:new-context-block %}
{% endcode_example %}

It is really important to understand that the new `TraceContext` will **only** be available during the execution of the
specified code block and removed from the trace context storage once the method returns.



TraceContext Storage
--------------------

You might have noticed that we mentioned "the current trace context" in the section above. When we say this, we refer to
the `TraceContext` associated with the trace being executed in the current thread, which is effectively stored in a
internal trace-local variable. Please read the [threading model considerations] section for advice related to propagating
a `TraceContext` depending on your application's threading model.

The `Tracer` companion object provides the required APIs to get, set and clear the context that is available to the
current thread. It is very important to ensure that you always clean up the threads once you don't need a `TraceContext`
as current anymore, thus, it will be better for you if instead of using the `Tracer.setCurrentContext(...)` and
`Tracer.clearCurrentContext()` functions directly you work with the `Tracer.withContext(...)` variants which will ensure
that the trace context storage is set to whatever was there before once the specified piece of code finishes execution.


{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/TraceContextManipulation.scala tag:storing-the-trace-context %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/TraceContextManipulation.java tag:storing-the-trace-context %}
{% endcode_example %}

Note that the `Tracer.withContext(...)` variants will note ever try to finish a trace once they're done.



Creating and Finishing Segments
-------------------------------

The API for creating segments is not directly available through the `Tracer` companion object, but rather through the
`TraceContext` instance that you are dealing with. Once you have access to a `TraceContext` you can call the
`.startSegment(...)` method to get a segment that is tied to that `TraceContext`.

{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/TraceContextManipulation.scala tag:creating-segments %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/TraceContextManipulation.java tag:creating-segments %}
{% endcode_example %}

Remember that a segment can be finished after the correspondent trace has finished, so don't feel forced into finishing
them while still in the `.withContext(...)` block, the segment will know for sure what `TraceContext` it belongs to.

The method mentioned above is the most flexible one, as you can do whatever you want with the segment instance, but the
`TraceContext` also provides the `.withNewSegment(...)` methods which create a segment and finish it automatically when
the supplied code finishes execution.

{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/TraceContextManipulation.scala tag:managed-segments %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/TraceContextManipulation.java tag:managed-segments %}
{% endcode_example %}

Obviously you don't need to get the context using `Tracer.currentContext` but you can start a segment in any
`TraceContext` that you have at hand.

Finally, an additional goody for Scala developers is the `.withNewAsyncSegment` that can create a new segment from a
piece of code that returns a `Future[T]` and automatically finish the segment when the future is completed. Here is how
the usage looks like:

{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/TraceContextManipulation.scala tag:async-segment %}
{% endcode_example %}

[threading model considerations]: /core/tracing/threading-model-considerations/
