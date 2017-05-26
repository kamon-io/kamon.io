---
title: Kamon | Core | Documentation
tree_title: Subscription Protocol
layout: module-documentation
---

Metrics Subscription Protocol
=============================

The Metrics subscriptions protocol provides you a simple way to tell the Kamon metrics module that you are interested in
receiving entity snapshots for a selection of entities upon every tick. You can subscribe to metrics by calling any of
the `subscribe` method variants in the metrics module, specifying the category and name pattern for the entities of
your interest in and the Akka actor that will receive the metrics information on every tick. On real code,
subscriptions look like this:

{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/MetricsSubscriptions.scala tag:metrics-subscriptions %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/MetricsSubscriptions.java tag:metrics-subscriptions %}
{% endcode_example %}

Regardless of how many subscriptions you do or if an entity matches several of the provided subscription patterns (as
the "test-trace" does in this example), each subscriber will receive a single `TickMetricSnasphot` message containing
all the entities matched by it's subscriptions on every tick.

The `TickMetricSnapshot` message received by all subscribers contains the from and to timestamps and a immutable map
from entities to entity snapshots with all the measurements recorded by all the matched entities since the last tick to
the current one. The entity snapshots are the immutable counterpart of the entity recorders discussed in the [core
concepts] section; while the entity recorders are considered unstable because their state changes as your application's
behavior is being measured, the entity snapshots are freely shareable because once created their state will never
change.

Each `EntitySnapshot` simply contains a map from `MetricKey` to `InstrumentSnapshot`. Metric keys are used to identify
each of the metrics being tracked for a given entity both in the mutable side as well as in the immutable side of Kamon
and the consist of the following attributes:

* __name__: A simple text describing the metric being tracked for that entity. In the cases of traces and segments you
will always find the `elapsed-time` metric inside the correspondent entity snapshot, or in your own custom entity
recorders you will find the metric keys with the very same name that you provided when defining the entity recorder. In
the case of single instrument recorders as the ones used to support simple histograms, counters and so on, you will find
a single metric key in each entity snapshot, named after the contained instrument type; for a histogram entity snapshot
there will be single metric named `histogram`, for a counter entity snapshot there will be a single metric named
`counter`, and the same applies for min max counters and gauges.

* __unit of measurement__: optional, it describes what kind of values are being recorded in the given instrument. This
member will usually be useful for metric backends as they might need to scale the reported values from the originally
measured unit to the one supported by the backend. If not provided then Kamon will use `UnitOfMeasurement.Unknown` as
the default value.



Creating a Simple Subscriber
----------------------------
Before moving forward, please keep in mind that the process described bellow covers the case in which your application
will make use of the metrics information collected by Kamon in order to provide some functionality, be it load
balancing, throttling or the like.

The best way to understand how the subscriptions and snapshots work is with a simple example; here we will create a very
simple subscriber that displays all the counters and a specific histogram in the console upon every tick.

First, we need to define our subscriber actor's behavior, it will simply wait for `TickMetricSnapshot` messages as
described above and print all the counters and histograms included in the message. Wait, wasn't it just for a single
histogram?, yes, it is! but you can subscribe to one specific histogram entity while still keeping the subscriber code
able to handle more than one histogram in case you decide to do so in the future. This is how the subscriber should look
like:

{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/MetricsSubscriptions.scala tag:subscriber %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/MetricsSubscriptions.java tag:subscriber %}
{% endcode_example %}

After you have defined your subscriber actor then you need to create the correspondent actor instance and use it to
subscribe to the counters and the histogram data we are interested in. If we want to receive all the counters but only the
histogram named `test-histogram` then the subscriptions should look like this:

{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/MetricsSubscriptions.scala tag:custom-subscriptions %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/MetricsSubscriptions.java tag:custom-subscriptions %}
{% endcode_example %}

And that's it! Depending on what you intent to do with the metrics information the amount of code in your subscriber
will vary, but the basic steps will remain the same:

1. Create an actor that knows how to handle `TickMetricSnapshot` messages.
2. Subscribe it to the desired entities using the `Kamon.metrics.subscribe(..)` method.
3. Profit!


[core concepts]: ../core-concepts/
