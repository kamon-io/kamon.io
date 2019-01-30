---
title: Kamon > Documentation > Reporters > StatsD
layout: docs
redirect_from:
  - /documentation/0.6.x/kamon-statsd/overview/
  - /documentation/1.x/reporters/statsd/
---

{% include toc.html %}

Reporting Metrics to StatsD
===========================

[StatsD] is a simple network daemon that continuously receives metrics over UDP and periodically sends aggregate metrics
to upstream services like (but not limited to) Graphite. Because it uses UDP, sending metrics data to StatsD is very
fast with little to no overhead.


## Installation and Startup

Add the `kamon-statsd` dependency to your build:
  - Group ID: `io.kamon`
  - Package ID: `kamon-statsd`
  - Scala Versions: 2.10 / 2.11 / 2.12
  - Latest Version: [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.kamon/kamon-statsd_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.kamon/kamon-statsd_2.11).


To get started with SBT, simply add the following to your `build.sbt` file:

```scala
libraryDependencies += "io.kamon" %% "kamon-statsd" % "1.0.0"
```

and add the StatsD reporter to Kamon:

```scala
Kamon.addReporter(new StatsDReporter())
```

## Network Settings

At the very basic level, you will certainly want to use the `kamon.statsd.hostname` and `kamon.statsd.port` configuration
keys to ensure your data is being sent to wherever your StatsD instance is running.



## Metric Names

Since StatsD has a hierarchical metrics model this module has to apply special rules to convert Kamon's metric name + tags
into a single metric name that contains all this inforamtion. By default the following format will be used:

```typesafeconfig
[service-name].[hostname].[metric-name].[tag[0].name].[tag[0].value].[tag[1].name].[tag[1].value]...
```

Where tags are ordered alphabetically before being added to the metric name. If you wish to change the the way metric
keys are generated you can supply your own implementation of `kamon.statsd.MetricKeyGenerator` using the
`kamon.statsd.metric-key-generator` setting.



## Metric Units

Kamon keeps all timing measurements in nanoseconds and memory measurements in bytes. In order to scale those to other
units before sending to StatsD, set `time-units` and `memory-units` config keys to desired units. Supported units are:

```typesafeconfig
n  - nanoseconds
µs - microseconds
ms - milliseconds
s  - seconds

b  - bytes
kb - kilobytes
mb - megabytes
gb - gigabytes
```

For example, the following setting:

```typesafeconfig
kamon.statsd.time-units = "ms"
```

will make the `kamon-statsd` module scale all timing measurements to milliseconds right before sending to StatsD.



## Visualization and Fun

StatsD is widely used and there are many integrations available, even alternative implementations that can receive UDP
messages with the StatsD protocol, you just have to pick the option that best suits you. For our internal testing we
choose to use [Graphite] as the StatsD backend and [Grafana] to create beautiful dashboards with very useful metrics.
Have an idea of how your metrics data might look like in Grafana with the screenshot bellow or use our [docker image] to
get up and running in a few minutes and see it with your own metrics!

TODO: Update the dashboards and images.

<img class="img-fluid my-4" src="http://kamon.io/assets/img/kamon-statsd-grafana.png">

<img class="img-fluid my-4" src="http://kamon.io/assets/img/kamon-system-metrics.png">


[StatsD]: https://github.com/etsy/statsd/
[get started]: /documentation/get-started/
[Graphite]: http://graphite.wikidot.com/
[Grafana]: http://grafana.org
[docker image]: https://github.com/kamon-io/docker-grafana-graphite
[Datadog Module]: ../../kamon-datadog/overview/
