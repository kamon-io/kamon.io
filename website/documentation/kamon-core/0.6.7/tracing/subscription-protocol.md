---
title: Kamon | Core | Documentation
tree_title: Subscription Protocol
layout: documentation
---

Traces Subscription Protocol
============================

When you configure your application to use the "Simple Tracing" level, you can subscribe with the tracing module to
receive `TraceInfo` messages every time a trace is selected to be reported. To subscribe, simply use the
`Kamon.tracer.subscribe(...)` method as shown in the example bellow:

{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/TracesSubscriptions.scala tag:subscribe-to-traces %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/TracesSubscriptions.java tag:subscribe-to-traces %}
{% endcode_example %}

As simple as that. Once you do this, your subscriber will be receiving `TraceInfo` messages every time a trace is
sampled.

<p class="alert alert-warning">
Please note that the simple traces subscriptions are very likely to change in future Kamon versions since we plan to
enhance it's capabilities to allow for more detailed tracing as well as distributed tracing.
</p>



Creating a Simple Subscriber
----------------------------

The `TraceInfo` messages have a very simple structure, they have the basic attributes you would expect from a
`TraceContext`: name, token, timestamp, elapsed time and a collection of `SegmentInfo` objects, which have their own
name, category, library, timestamp and elapsed time attributes. With that in mind, let's first create a trace with
some segments on it and then a simple reporter that just dumps this information in the console.

{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/TracesSubscriptions.scala tag:create-some-traces-and-segments %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/TracesSubscriptions.java tag:create-some-traces-and-segments %}
{% endcode_example %}

The code above is simply creating a trace and then creating a couple segments that will have different durations; the
first segment is named "quick-segment" and should have a elapsed time of around 100 milliseconds and the second segment
is named "slow-segment" and should have a elapsed time of around 3 seconds.

Bellow we have the code for a subscriber actor that would just wait for `TraceInfo` messages and print them in the console
with some formatting.

{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/TracesSubscriptions.scala tag:subscriber %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/TracesSubscriptions.java tag:subscriber %}
{% endcode_example %}

Once the trace is finished and reported, the generated output looks like this:

{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/TracesSubscriptions.scala tag:example-output label:"Example Output" %}
{% endcode_example %}
