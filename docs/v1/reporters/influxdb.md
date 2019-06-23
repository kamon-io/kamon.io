---
title: 'Sending Metrics to InfluxDB with Kamon | Kamon Documentation'
layout: docs
redirect_from:
  - /documentation/1.x/reporters/influxdb/
  - /documentation/0.6.x/kamon-influxdb/overview/
---

{% include toc.html %}

Reporting Metrics to InfluxDB
===================

[InfluxDB] is an open-source time series database developed by InfluxData. It is written in Go and optimized for fast,
high-availability storage and retrieval of time series data in fields such as operations monitoring, application metrics,
Internet of Things sensor data, and real-time analytics.


## Installation and Startup

{% include dependency-info.html module="kamon-influxdb" version="1.0.2" %}

Once you have the dependency on your classpath, start the reporter:

```scala
Kamon.addReporter(new InfluxDBReporter())
```

## Integration Notes

This module sends all the metrics data to InfluxDB over HTTP. Kamon metric names will become InfluxDB's "measurement name",
all tags will be transmitted unchanged and depending on the metric type, the following fields will be added:
  - Counters will be reported with a single `count` field.
  - Gauges will be reported with a single `value` field.
  - Histograms and Range Samplers will be reported with `count`, `sum`, `min`, `max` and `pXX` fields, where each of the
    `pXX` fields represent a percentile for the given distribution of values. These percentiles can be configured using
    the `kamon.influxdb.percentiles` setting.

The following settings are available:

```typesafeconfig
kamon.influxdb {

  # Hostname and port in which your InfluxDB is running
  hostname = "127.0.0.1"
  port = 8086

  # The database where to write in InfluxDB.
  database = "mydb"

  # For histograms, which percentiles to count
  percentiles = [50.0, 70.0, 90.0, 95.0, 99.0, 99.9]

  # Allow including environment information as tags on all reported metrics.
  additional-tags {

    # Define whether specific environment settings will be included as tags in all exposed metrics. When enabled,
    # the service, host and instance tags will be added using the values from Kamon.environment().
    service = yes
    host = yes
    instance = yes

    # Specifies which Kamon environment tags should be ignored. All unmatched tags will be always added to al metrics.
    blacklisted-tags = []
  }
}

```


Visualization and Fun
---------------------

Here is a simple example showing some Actor and Span metrics using InfluxDB and Grafana together:

<img class="img-fluid my-4" src="/assets/img/influxdb-dashboard.png">

[Datadog]: http://www.datadoghq.com/
[get started]: /docs/latest/guides/getting-started/
