---
title: Kamon | Riemann | Documentation
tree_title: Sending Metrics to Riemann
layout: module-documentation
---

Reporting Metrics to Riemann
===========================

[Riemann] is a monitoring tool for distributed systems.  Riemann aggregates events emitted from servers using a special
stream processing syntax,  generate alerts,  track latency distributions, combine statistics etc.

Installation
------------

Add the `kamon-riemann` dependency to your project and ensure that it is in your classpath at runtime, that’s it. Kamon’s
module loader will detect that the Riemann module is in the classpath and automatically start it.


Configuration
-------------

Set the `kamon.riemann.hostname` and `kamon.riemann.port` config keys to ensure that metrics are being sent to the proper
instance of Riemann. Additionally to that, you can configure the metric categories to which this module will subscribe depending
on the protocol using the `kamon.riemann.udp.subscriptions` and/or `kamon.riemann.tcp.subscriptions` keys

Riemann accepts events over TCP as well UDP protocol. Both protocols are supported. Default subscriptions - all metrics will be sent via UDP:

{% code_block typesafeconfig %}
kamon.riemann {
    udp {
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
    tcp {
      subscriptions {
        histogram       = [ "" ]
        min-max-counter = [ "" ]
        gauge           = [ "" ]
        counter         = [ "" ]
        trace           = [ "" ]
        trace-segment   = [ "" ]
        akka-actor      = [ "" ]
        akka-dispatcher = [ "" ]
        akka-router     = [ "" ]
        system-metric   = [ "" ]
        http-server     = [ "" ]
      }
    }
}
{% endcode_block %}

If you are interested in reporting additional entities to Riemann please ensure that you include the categories and name
patterns accordingly.

You can control flush interval independently for TCP and UDP `kamon.riemann.udp.flush-interval` and `kamon.riemann.tcp.flush-interval` 
By default both settings are 10 seconds. Note that the flush interval should be equal or greater than internal snapshot interval
which is controlled by `kamon.metric.tick-interval` (Default 10 seconds)


### Metric Mapper ###

Each Riemann event can contain one of a number of optional fields including: host, service, state,
a time and description, a metric value or a TTL. They can also contain custom fields. For example:

{% code_block typesafeconfig %}
:host riemann.example.com, :service disk /, :state ok, :description 11% used, :metric 0.11, :custom_field "000", :time 1419373427, :ttl 10.0
{% endcode_block %}

Additional mapping between Kamon metric and Riemann event should be performed. By default kamon-riemann is shipped with
default mapper `kamon.riemann.DefaultMetricsMapper` which extracts Kamon entity.tags and builds a Riemann event.
Two basic fields of Rieman event can be pre-set in configuration. For example:

{% code_block typesafeconfig %}
kamon.riemann.default-metrics-mapper {
  host = "riemann.example.com"
  service = "disk /"
}
{% endcode_block %}

Additional fields can be set using tags map. For example:

{% code_block typesafeconfig %}
val someHistogram = Kamon.metrics.histogram("some-histogram",
    Map(
      riemann.tagKeyDescription -> "11% used",
      riemann.tagKeyState -> "ok",
      riemann.tagKeyTtl ->  "10",
      "custom_field" -> "000"
    )
  )
  ...
someHistogram.record(0.11)  
{% endcode_block %}

Note that currently kamon tags is a `Map[String,String]` and all values have to be expressed as a `String` including numbers and timestamps.

You can create custom mapper by implememting `kamon.riemann.MetricsMapper` and setting classname under `kamon.riemann.metrics-mapper` config key. 

[Riemann]: http://riemann.io/
