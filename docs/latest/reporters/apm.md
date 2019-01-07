---
title: Kamon > Documentation > Reporters > Kamino
layout: docs
---

{% include toc.html %}

Reporting Metrics and Traces to Kamon APM
=========================================

[Kamino][1] is a Monitoring and Troubleshooting platform for Microservices. Kamino was founded by the creators of Kamon
and designed from the ground up with one goal in mind: create a monitoring platform that can accept all the metrics and
tracing data exactly as Kamon records it: no averages, no summaries, no downsampling, no data quality loss. Having the
entire data that Kamon captures means proper aggregation of data across instances and proper percentiles calculation,
which translates in better, accurate and relevant alerts and insight on your application's behavior.


## Installation and Startup

Add the `kamino-reporter` dependency to your build:
  - Group ID: `io.kamon`
  - Package ID: `kamino-reporter`
  - Scala Versions: 2.11 / 2.12
  - Latest Version: [![Kamino Reporter](https://maven-badges.herokuapp.com/maven-central/io.kamon/kamino-reporter_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.kamon/kamino-reporter_2.12)

Adding the dependency to SBT would look like this:

{% code_block scala %}
libraryDependencies += "io.kamon" %% "kamino-reporter" % "1.0.0"
{% endcode_block scala %}

Once the reporter is on your classpath you can add it like any other Kamon reporter. Take into account that there are
two reporters:
  - `kamon.kamino.KaminoReporter` sends Metrics data to Kamino.
  - `kamon.kamino.KaminoTracingReporter` sends Tracing data to Kamino.

Make sure you add both reporters:

{% code_block scala %}
import kamon.kamino.{KaminoReporter, KaminoTracingReporter}

Kamon.addReporter(new KaminoReporter())
Kamon.addReporter(new KaminoTracingReporter())
{% endcode_block scala %}


## Configuration

At a minimum you should provide your Kamino API key to start reporting data using the `kamino.api-key` configuration
setting in your `application.conf` file:

{% code_block typesafeconfig %}
kamino {
  api-key = "abcdefghijklmnopqrstuvwxyz"
}
{% endcode_block scala %}

You can find your API key in Kamino's Administration section.


## Visualization and Fun

Kamino has a deep understanding on all metrics reported by Kamon and provides ready to use dashboards that make it super
easy to start looking at service, JVM, Hosts, Akka-related metrics, Traces and more. Here are some examples from a demo
application:

<img class="img-fluid my-4" src="/assets/img/kamino-services-dashboard.png">
<img class="img-fluid my-4" src="/assets/img/kamino-service-overview.png">
<img class="img-fluid my-4" src="/assets/img/kamino-actor-details.png">
<img class="img-fluid my-4" src="/assets/img/kamino-jvm-metrics.png">
<img class="img-fluid my-4" src="/assets/img/kamino-traces.png">

[1]: https://kamino.io/?utm_source=kamon&utm_medium=docs&utm_campaign=kamon
