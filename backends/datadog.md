---
title: Kamon | Datadog | Documentation
layout: documentation
---

Reporting Metrics to Datadog
===========================
<hr>

[Datadog] is a monitoring service for IT, Operations and Development teams who write and run applications at scale, and
want to turn the massive amounts of data produced by their apps, tools and services into actionable insight.

Installation
------------

To use the Datadog module just add the `kamon-datadog` dependency to your project and start your application using the
Aspectj Weaver agent. Please refer to our [get started] page for more info on how to add dependencies to your project
and starting your application with the AspectJ Weaver.


Configuration
-------------

First, include the Kamon(Datadog) extension under the `akka.extensions` key of your configuration files as shown here:

```
akka {
  extensions = ["kamon.datadog.Datadog"]
}
```

Then, tune the configuration settings according to your needs. Here is the `reference.conf` that ships with kamon-datadog
which includes a brief explanation of each setting:

```
kamon {
  datadog {
    # Hostname and port in which your Datadog is running. Remember that Datadog packets are sent using UDP and
    # setting unreachable hosts and/or not open ports wont be warned by the Kamon, your data wont go anywhere.
    hostname = "127.0.0.1"
    port = 8125

    # Interval between metrics data flushes to Datadog. It's value must be equal or greater than the
    # kamon.metrics.tick-interval setting.
    flush-interval = 1 second

    # Max packet size for UDP metrics data sent to Datadog.
    max-packet-size = 1024 bytes

    # Subscription patterns used to select which metrics will be pushed to Datadog. Note that first, metrics
    # collection for your desired entities must be activated under the kamon.metrics.filters settings.
    includes {
      actor      =  [ "*" ]
      trace      =  [ "*" ]
      dispatcher =  [ "*" ]
    }

    # Application prefix for all metrics pushed to Datadog. The default namespacing scheme for metrics follows
    # this pattern:
    #    application.entity-name.metric-name
    application-name = "kamon"
  }
}

```


Integration Notes
-----------------

* Contrary to other Datadog client implementations, we don't flush the metrics data as soon as the measurements are ç
  taken but instead, all metrics data is buffered by the `Kamon(Datadog)` extension and flushed periodically using the
  configured `kamon.statsd.flush-interval` and `kamon.statsd.max-packet-size` settings.
* Metrics for all entities of the same kind are reported with the same name and a tag is defined with the entity kind
  and entity name. For example, all mailbox size measurements are reported under the `application.actor.mailbox_size`
  metric and additional tags similar to `actor:/user/example-actor` or `actor:/user/other-actor` are provided to allow
  breaking down in charts easily.
* Currently only Actor, Trace and Dispatcher metrics are being sent to Datadog.
* It is advisable to experiment with the `kamon.datadog.flush-interval` and `kamon.datadog.max-packet-size` settings to
  find the right balance between network bandwidth utilisation and granularity on your metrics data.
* All timing measurements are sent in nanoseconds, if you want to scale them to milliseconds or to any other unit you
  need to manually edit the graph JSON definition to include a "/ 1000000" as show here:

<img class="img-responsive" src="/assets/img/datadog-scaling-metrics.png">

Visualisation and Fun
---------------------

Creating a dashboard in the Datadog user interface is really simple, just start typing the application name ("kamon" by
default) in the metric selector and all metric names will start to show up. You can also break it down based on the entity
names. Here is a very simple example of a dashboard created with metrics reported by Kamon:

<img class="img-responsive" src="/assets/img/datadog-dashboard.png">

[Datadog]: http://www.datadoghq.com/
[get started]: /introduction/get-started/
