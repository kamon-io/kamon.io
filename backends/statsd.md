---
title: Kamon | StatsD | Documentation
layout: documentation
---

Reporting Metrics to StatsD
===========================

[StatsD] is a simple network daemon that continuously receives metrics over UDP and periodically sends aggregate metrics
to upstream services like (but not limited to) Graphite. Because it uses UDP, sending metrics data to StatsD is very
fast with little to no overhead.


Installation
------------

Add the `kamon-statsd` dependency to your project and ensure that it is in your classpath at runtime, that's it.
Kamon's module loader will detect that the StatsD module is in the classpath and automatically start it.

Please note that even while the Datadog Agent uses a StatsD-like protocol, the protocol features used by this module are
not compatible with Datadog's implementation. If you need to send data to Datadog then use our [Datadog Module].


Configuration
-------------

At the very basic level, you will certainly want to use the `kamon.statsd.hostname` and `kamon.statsd.port` configuration
keys to ensure your data is being sent to wherever your StatsD instance is running. Additionally to that, you can configure
the metric categories to which this module will subscribe using the `kamon.statsd.subscriptions` key. By default, the
following subscriptions are included:

{% code_block typesafeconfig %}
kamon.statsd {
  subscriptions {
    histogram       = [ "**" ]
    min-max-counter = [ "**" ]
    gauge           = [ "**" ]
    counter         = [ "**" ]
    trace           = [ "**" ]
    trace-segment   = [ "**" ]
    akka-actor      = [ "**" ]
    akka-dispatcher = [ "**" ]
    akka-router     = [ "**" ]
    system-metric   = [ "**" ]
    http-server     = [ "**" ]
  }
}
{% endcode_block %}

If you are interested in reporting additional entities to StatsD please ensure that you include the categories and name
patterns accordingly.


### Metric Key Generators ###

By default, the `kamon-statsd` module will use the entities' information to create a predictable metric key that follows
the pattern `application.host.category.entity-name.metric-name`, where each portion of the pattern corresponds to:

* __application__: Uses the value of the `kamon.statsd.simple-metric-key-generator.application` configuration key.
Defaults to `kamon`.
* __host__: Uses the local host name. You can also change the host value to a arbitrary value using the
`kamon.statsd.simple-metric-key-generator.hostname-override` configuration key or completely remove the host portion
from the key pattern by changing the `kamon.statsd.simple-metric-key-generator.include-hostname` configuration key.
* __category__: The entity's category.
* __entity-name__: The entity's name.
* __metric-name__: The metric name assigned in the entity recorder.

This metric key generation mechanism seems very useful and flexible, but in case it falls short for your needs then you
can create your own implementation of `kamon.statsd.MetricKeyGenerator` and let Kamon know about it by setting the
`kamon.statsd.metric-key-generator` configuration key to the FGCN of your implementation.


Integration Notes
-----------------

* Contrary to many StatsD client implementations, we don't flush the metrics data as soon as the measurements are taken
  but instead, all metrics data is buffered by the StatsD module and flushed periodically using the
  configured `kamon.statsd.flush-interval` and `kamon.statsd.max-packet-size` settings.
* All timing measurements are sent in nanoseconds, make sure you correctly set the scale when plotting or using the
  metrics data.
* It is advisable to experiment with the `kamon.statsd.flush-interval` and `kamon.statsd.max-packet-size` settings to
  find the right balance between network bandwidth utilization and granularity on your metrics data.



Visualization and Fun
---------------------

StatsD is widely used and there are many integrations available, even alternative implementations that can receive UDP
messages with the StatsD protocol, you just have to pick the option that best suits you. For our internal testing we
choose to use [Graphite] as the StatsD backend and [Grafana] to create beautiful dashboards with very useful metrics.
Have an idea of how your metrics data might look like in Grafana with the screenshot bellow or use our [docker image] to
get up and running in a few minutes and see it with your own metrics!

<img class="img-responsive" src="/assets/img/kamon-statsd-grafana.png">

<img class="img-responsive" src="/assets/img/kamon-system-metrics.png">


[StatsD]: https://github.com/etsy/statsd/
[get started]: /introduction/get-started/
[Graphite]: http://graphite.wikidot.com/
[Grafana]: http://grafana.org
[docker image]: https://github.com/kamon-io/docker-grafana-graphite
[Datadog Module]: /backends/datadog/
