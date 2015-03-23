---
title: Kamon | Core | Documentation
layout: documentation
---

Metrics Subscription Protocol
-----------------------------

The Metrics subscriptions protocol provides you a simple way to tell the Kamon Metrics module that you are interested in
receiving entity snapshots for a selection of entities upon every tick. You can subscribe to metrics by calling any of
the `subscribe` method variants in the Metrics extension, specifying the category and name pattern for the entities of
you are interest in and the Akka actor that will receive the metrics information on every tick. On real code,
subscriptions look like this:

{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/MetricsSubscriptions.scala tag:metrics-subscriptions %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/MetricsSubscriptions.java tag:metrics-subscriptions %}
{% endcode_example %}

Regardless of how many subscriptions you do or if an entity matches several of the provided subscription patterns (as
the "test-trace" does in this example), each subscriber will receive a single `TickMetricSnasphot` message containing
all the entities matched by it's subscriptions on every tick.

Usually the only people interested in subscribing to metrics are those creating reporting modules
