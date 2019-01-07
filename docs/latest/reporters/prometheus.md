---
title: Kamon > Documentation > Reporters > Prometheus
layout: docs
---

Exposing Metrics for Prometheus
===============================

[Prometheus][1] is a open source monitoring and alerting toolkit. It has a dimensional data model, a really powerful and
flexible query language and one very important difference in comparison with all other Kamon reporters: data has to be
pulled our of your application instead of pushed to external servers.

This module exposes a scraping endpoint with all Kamon metrics in the Prometheus text format, all you will have to do
after starting this module is add the target to your Prometheus server and start enjoying the awesomeness.


## Installation and Startup

Add the `kamon-prometheus` dependency to your build:
  - Group ID: `io.kamon`
  - Package ID: `kamon-prometheus`
  - Scala Versions: 2.10 / 2.11 / 2.12
  - Latest Version: [![kamon-prometheus](https://maven-badges.herokuapp.com/maven-central/io.kamon/kamon-prometheus_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.kamon/kamon-prometheus_2.12)

Adding the dependency to SBT would look like this:

{% code_block scala %}
libraryDependencies += "io.kamon" %% "kamon-prometheus" % "1.0.0"
{% endcode_block scala %}

And then start the reporter:

{% code_block scala %}
import kamon.prometheus.PrometheusReporter

Kamon.addReporter(new PrometheusReporter())
{% endcode_block scala %}

That's it. Go to <http://localhost:9095/> to see your exposed metrics.


## Configuration

The default configuration is enough to get you started exporting metrics, but you are very likely to need tweaking the
default bucketing configuration. Long story short, this module has to take the high definition data captured by Kamon
and aggregate it into a reasonable number of buckets before exposing it to Prometheus.

It is very important that you iterate on your buckets configuration to find the right tradeoff between granularity and
number a reasonable number of exposed time series per metric (each bucket ends up being a separate time serie in
Prometheus). The quality of the percentiles you get is greatly influenced by your bucketing configuration, take some
time to think it through!

{% code_block typesafeconfig %}
kamon.prometheus.buckets {
    default-buckets = [
      10,
      30,
      100,
      300,
      1000,
      3000,
      10000,
      30000,
      100000
    ]

    time-buckets = [
      0.005,
      0.01,
      0.025,
      0.05,
      0.075,
      0.1,
      0.25,
      0.5,
      0.75,
      1,
      2.5,
      5,
      7.5,
      10
    ]

    information-buckets = [
      512,
      1024,
      2048,
      4096,
      16384,
      65536,
      524288,
      1048576
    ]
  }
{% endcode_block scala %}

The measurement unit on each metric will dictate which bucketing configuration applies:
  - `time-buckets` for metrics with a unit in the time dimension. Everything is scaled to seconds.
  - `information-buckets` for all units in the information dimension. Everything is scaled to bytes.
  - `default-buckets` are used when there is no measurement unit information in a metric.


## Visualization and Fun

These are extracted from our [Monitoring Akka Quickstart][2] recipe, but stay tunned for more as Grafana dashboards for
Kamon are coming soon!

Scraping endpoint exposed by Kamon:

<img class="img-fluid my-4" src="/assets/img/recipes/quickstart-prometheus-metrics.png">

A simple query with actor's processing time in Prometheus' UI:

<img class="img-fluid my-4" src="/assets/img/recipes/quickstart-prometheus-query.png">

[1]: https://prometheus.io/
[2]: /documentation/1.x/recipes/monitoring-akka-quickstart/
