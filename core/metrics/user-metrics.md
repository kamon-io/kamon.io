---
title: Kamon | Core | Documentation
layout: documentation
---

User Metrics
------------

If you already went through the [core concepts] section then you should already know that the preferred way to register
things to be tracked by Kamon is by creating a proper entity and the required entity recorder for it. But sometimes you
don't want or need to track full blown entities but rather have a few instruments directly registered and record metrics
with them, that's exactly what the User Metrics module does for you.

When using the User Metrics module you just request an instrument from it and use it directly. Under the hood, Kamon is
registering a single entity called `user-metrics` with category `user-metrics` as well, that is available to all
interested subscribers over the subscriptions protocol, just like any other entity would.

To use the User Metrics module, just access it through your Kamon instance and follow the rather intuitive API:

{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/UserMetrics.scala start:6 end:14 %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/UserMetrics.java start:9 end:18 %}
{% endcode_example %}




[core concepts]: /core/metrics/core-concepts/
