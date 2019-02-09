---
title: Install Kamon
layout: docs
redirect_from:
  - /documentation/0.6.x/kamon-core/metrics/core-concepts/
  - /documentation/1.x/core/basics/metrics/
  - /documentation/0.6.x/kamon-core/metrics/recording-metrics/
---

{% include toc.html %}

Metrics
=======

Kamon provides five instrument types that can be used for recording metrics:
  - **Counters**: Track the number of times certain event happened. It can only be increased. Useful for counting errors,
    cache misses, etc.
  - **Gagues**: Track a single value that can be increased, decreased or explicitly set. The common use case for gauges
    are slowly moving values like disk usage, number of loaded classes or configuration settings.
  - **Histograms**: Record the distribution of values within a configurable range and precision. Typically used for
    recording latency, message sizes and so on.
  - **Timers**: Are just sugar on top of histograms that make it simple to track latency.
  - **Range Samplers**: Used to track variables that increase and decrease very quickly, the most notable use case being
    tracking the number of concurrent requests or the number of elements in a queue.

You can find more details in the [Metric Instruments][1] section.


## Creating and Removing Metrics

To record metrics you need to request the appropriate instrument from the `Kamon` companion object (or static members, if
on Java), optionally *refine* it with tags and then you are ready to record metrics. Let's look at a example:


{% code_example %}
{%   language scala reference/core/src/main/scala/kamon/examples/scala/MetricBasics.scala tag:creating-metrics label:"Scala" %}
{% endcode_example %}

  1. Simple metrics that do not require tags can be looked up and used in one-liners. `(1)`
  2. Defining a metric with a counter instrument `(2)`
  3. Creating two "refined" (or tagged, if that helps to understand better) versions of the metric, one using the
     `class=InvalidUser` `(3)` tag and another using the `class=InvalidPassword` tag `(4)`.
  4. Incrementing one of the refined metrics `(5)`
  5. Removing a specific instance of the `app.error` metric with the `class=InvalidUser` tag from Kamon. `(6)`

There are a few important things that you should keep in mind when using metrics:
  - When possible, define and refine your metrics up front, as a static member or instance members on the instrumented
    components of your services. This saves a bit of time in looking up instruments. If you can't do so, rest assured
    that Kamon will always return the same instrument instance if requested with the same arguments (metric name and tags)
    from anywhere in your code.
  - Always provide a measurement unit when your metric is tracking either time (milliseconds, microseconds, etc) or
    information (byes, kilobytes, etc). This will ensure that reporting modules can scale the recorded data appropriately.
  - Follow our metrics naming conventions: we use namespaced metric names that describe "what" is being measured and use
    tags to specify from "where" we are getting this information. For example, we use the `span.processing-time` metric
    to track Span latencies and we add an `operation=login` or `operation=search` tag (plus some more) to identify from
    where we are recording such measurements.
  - **Avoid high cardinality variables as metric tags** since they will most likely overwhelm your monitoring systems or make
    your bill go through the roof. Things like user identifiers, IP addresses, session identifiers and so on are a big
    *no no* for metric tags.



## Counters

Counters can only do one thing: increment. You can either `increment()` by one or `increment(times)` a specific number of
times at once.

{% code_example %}
{%   language scala reference/core/src/main/scala/kamon/examples/scala/MetricBasics.scala tag:working-with-counters label:"Scala" %}
{% endcode_example %}


## Gauges

A gauge can be `set(value)` to as specific value, `increment()`, `increment(times)`, `decrement()` and `decrement(times)`.


{% code_example %}
{%   language scala reference/core/src/main/scala/kamon/examples/scala/MetricBasics.scala tag:working-with-gauges label:"Scala" %}
{% endcode_example %}


## Histograms

Histograms record the entire distribution of values for a given metric. The only available operations are `record(value)`
and `record(times)`.


{% code_example %}
{%   language scala reference/core/src/main/scala/kamon/examples/scala/MetricBasics.scala tag:working-with-histograms label:"Scala" %}
{% endcode_example %}


## Timers

Timers assume that you are measuring latency so all you need to do is provide a name and `start()` a timer. You will get
back a `StartedTimer` instance that can be `stop()`ed whenever the operation you are measuring finishes.


{% code_example %}
{%   language scala reference/core/src/main/scala/kamon/examples/scala/MetricBasics.scala tag:working-with-timers label:"Scala" %}
{% endcode_example %}


## Range Samplers

A range sampler is always paired with a component or piece of state that starts at zero and can increment and decrement,
like a message queue size or the number of concurrent requests being processed by a service. The exposed operations are
similar to those offered by gauges, namely `increment()`, `increment(times)`, `decrement()` and `decrement(times)`.


{% code_example %}
{%   language scala reference/core/src/main/scala/kamon/examples/scala/MetricBasics.scala tag:working-with-range-samplers label:"Scala" %}
{% endcode_example %}

[1]: ../advanced/metric-instruments/
