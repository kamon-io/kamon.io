---
title: Kamon | Core | Documentation
tree_title: Core Concepts
layout: module-documentation
---

Tracing Module
==============

The tracing module is responsible of providing the base APIs that allow you to record information about meaningful
pieces of functionality executed in your application, allowing you to get deeper and more specialized data on how your
application and its internal components are behaving.

While our metrics module guides you towards independently measuring specific components in your application, our tracing
module seeks to work at a higher level where the interaction between components to provide concrete user-facing
functionality is the main subject of interest. Here, the main goal is to know how functionalities "X" and "Y" are
behaving and, if possible, know how the components that make up these functionalities are behaving.

Imagine there is this Twitter-like application that you want to measure and monitor. It provides two basic
functionalities: "post new status" and "fetch user time line". In this very simple example all data is available in a
database. When you measure the "post new status" functionality, you obviously want to know how long it took to process
each new status, but additionally you might want to know how the section of code that does the database query is
behaving as usually that tends to become a bottleneck. Maybe there is a internally shared functionality called "get user
details" that is being used from both "post new status" and "fetch user time line" and when that happens, it is not
enough to measure the "get user details" section alone, but rather keep enough context to know how it behaves when
being called from the "post new status" or the "fetch user time line" functionality.

Translating the example above into Kamon terms, we refer to the invocation of a functionality as a `Trace` and to each
of the components being used to make up the trace as `Segments`. Each request to "post new status" is generating a new
trace, and, each usage of the "get user details" section is a segment that belongs to the aforementioned trace. All the
information related to a trace is stored in a single `TraceContext` instance, as you will discover bellow.



The TraceContext
----------------

The `TraceContext` is a fundamental piece in the tracing infrastructure provided by Kamon and it is very important that
you understand how it works and how it should be propagated in order to make the most effective use of it.

Each time a traced functionality is used in your application, a new `TraceContext` should be generated to store all the
performance and diagnostic information related to the correspondent trace. As a starter, a `TraceContext` has the
following attributes:

* __name__: A user-friendly name that describes the functionality being used in a traced request. Examples might be
"GetUserDetails", "PublishStatus" or "GenerateSalesReport". A trace can be renamed at any point during the execution of
a request. As you might guess, many traces in your application can and should share the same name.
* __trace-token__: A automatically generated id for the `TraceContext`. Once a `TraceContext` is created, it's trace token
will remain the same until the end of the trace. Contrary to the trace name, the trace token is unique and must never be
repeated during the lifetime of the application.
* __metadata__: Simple key-value pairs containing additional information about the execution of a trace.

New `TraceContext` instances are obtained by requesting them to the Kamon tracer and you shouldn't try to instantiate
them directly as their dependencies are carefully controlled and supplied by Kamon accordingly to the provided
configuration. Creating a `TraceContext` and finishing it after waiting a few seconds looks like this:

{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/TraceContextManipulation.scala tag:creating-a-trace-context %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/TraceContextManipulation.java tag:creating-a-trace-context %}
{% endcode_example %}

In the example above we achieved the most basic operation possible on a trace: measure it's elapsed time. When we
created the new TraceContext, Kamon automatically recorded it's start timestamp and by calling `.finish()` we tell Kamon
that the trace is over and the elapsed time metric for that trace is recorded. There are more details related to how
trace metrics work, you can read more about it in the [trace metrics section].

That was an extremely simple example that will rarely be reproduced in real world applications. In reality, you will
have several components in your application and the life cycle of a trace will be governed by the interaction of those
components, but still, each component must have access to the same `TraceContext` in order to add or enhance the
diagnostic and performance data contained in it, thus the need to have unique and predictable place to lookup and store
the contexts.

You could simply pass the `TraceContext` instance around to all the related components from start to finish in your
application, that will certainly give you full control of the trace life cycle and will clearly describe the stages
covered by it, but would also be incredibly verbose and tedious, besides the fact that doing so would couple a decent
portion of your application code with Kamon's tracing infrastructure, that doesn't seem good. Instead, we defined a
single place to look for and store the `TraceContext` of the functionality being executed in the current thread and we
make sure that the information hosted there is consistent and predictable, regardless of the threading model being used.
See the [trace context storage] section for more details.


### Trace Segments ###

As mentioned above, segments are focused on how the sections that make up the trace are behaving, thus, segments can
never be found alone and by themselves in Kamon but you rather need to have a trace first and then create a segment that
belongs to that trace. Once a segment is created for a trace, it will always belong to that trace. Similarly to the
`TraceContext`, segments also have a few attributes for themselves:

* __category__: Specifies, in a general sense, what the segment is about. Some of our modules create segments for the
`http-client` and `database` categories but you are not limited to that, this is just for informative purposes.
* __name__: The segment name should tell you specifically what the segment is trying to achieve. Think of "StoreUserCredentials"
or "UpdateAccountsCache" as names might see for segments.
* __library__: The library attribute is primarily used by the instrumentation modules to inform for which library a
segment was created.
* __metadata__: Simple key-value pairs containing additional information about the execution of a segment.

The life cycle of a segment is not tied to the life cycle of it's correspondent trace. That a segment can be created and
finished at any time if you have a trace available, regardless of whether the trace is still open or not.



Tracing Levels
--------------

Our tracing module aims to provide two different trace levels that can be used to gather monitoring information about
your application which can be configured via the `kamon.trace.level-of-detail` configuration key, these levels are:


### Metrics Only ###

This is what you get by default. The traces and segments are only used to gather the elapsed time information for each
of them and you will only get the metrics information described in the [trace metrics section].

### Simple Tracing ###

Additionally to gathering trace metrics, the simple tracing level stores information about when each segment was
executed relative to the trace, which can enable you to display a simple Gantt-like chart of the trace and all it's
segments. Doing so incurs in a bit of overhead compared to the metrics only level, that's why you can configure the
sampling mechanism to be used when deciding whether a trace should collect additional info or stay with just metrics.
The available sampling mechanisms are:

* __all__: Every single trace is selected and reported.
* __random__: Uses a random number generator to generate numbers between 1 and 100, if the generated number is less or
equal to the `chance` setting, the trace will selected and reported.
* __ordered__: Pick one trace every `sample-interval` traces and reports it.
* __threshold__: This one might be the most interesting around. This sampling method will select all traces for collecting
information, but will only report those whose elapsed time is beyond certain threshold.

Use the `kamon.trace.sampling` configuration key to select your sampling mechanism. By default, Kamon will use random
sampling with a 10% chance of selecting traces.




[trace manipulation]: /core/tracing/trace-context-manipulation/
[trace metrics section]: /core/tracing/trace-metrics/
[trace context storage]: /core/tracing/trace-context-storage/
