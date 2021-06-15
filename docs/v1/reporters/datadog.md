---
title: 'Sending Metrics to Datadog with Kamon | Kamon Documentation'
layout: docs
noindex: true
redirect_from:
  - /documentation/1.x/reporters/datadog/
  - /documentation/0.6.x/kamon-datadog/overview/
---

{% include toc.html %}

Reporting Metrics to Datadog
===========================

[Datadog] is a monitoring service for IT, Operations and Development teams who write and run applications at scale, and
want to turn the massive amounts of data produced by their apps, tools and services into actionable insight.

## Installation and Startup

{% include dependency-info.html module="kamon-datadog" version="1.0.0" %}

Once you have the dependency on your classpath, add the Agent or API reporter to Kamon:

```scala
Kamon.addReporter(new DatadogAgentReporter())
// OR
Kamon.addReporter(new DatadogAPIReporter())
```

## Agent Reporter

By default, the Agent reporter assumes that you have an instance of the Datadog Agent running in localhost and listening on
port 8125. If that is not the case you can use the `kamon.datadog.agent.hostname` and `kamon.datadog.agent.port` configuration
keys to point the module at your Datadog Agent installation.

## API Reporter

When using the API reporter you must configure your API key using the `kamon.datadog.http.api-key` configuration setting.
Since Kamon has access to the entire distribution of values for a given period, the API reporter can directly post the
data that would otherwise be summarized and sent by the Datadog Agent. Gauges andAll histogram-backed metrics will be reported as
follows:
  - metric.avg
  - metric.count
  - metric.median
  - metric.95percentile
  - metric.max
  - metric.min

You can refer to the [Datadog documentation](https://docs.datadoghq.com/developers/metrics/#histograms) for more details.

## Metric Units ###

Kamon keeps all timing measurements in nanoseconds and memory measurements in bytes. In order to scale those to other
units before sending to datadog set the `time-units` and `memory-units` config keys to desired units. Supported units are:

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

For example,

```typesafeconfig
kamon.datadog.time-units = "ms"
```

will scale all timing measurements to milliseconds right before sending to datadog.


Integration Notes
-----------------

* Contrary to other Datadog client implementations, we don't flush the metrics data as soon as the measurements are
  taken but instead, all metrics data is buffered by the `kamon-datadog` module and flushed periodically using the
  configured `kamon.metric.tick-interval` and `kamon.datadog.max-packet-size` settings.
* It is advisable to experiment with the `kamon.metric.tick-interval` and `kamon.datadog.agent.max-packet-size` settings to
  find the right balance between network bandwidth utilisation and granularity on your metrics data.


Visualization and Fun
---------------------

Creating a dashboard in the Datadog user interface is really simple, all metric names will match the Kamon metric names
with the additional "qualifier" suffix. Here is a very simple example of a dashboard created with metrics reported by Kamon:

<img class="img-fluid my-4" src="/assets/img/datadog-dashboard.png">

[Datadog]: http://www.datadoghq.com/
[get started]: /docs/latest/guides/getting-started/
