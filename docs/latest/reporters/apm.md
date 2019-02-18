---
title: 'Sending Metrics and Spans to Kamon APM | Kamon Documentation'
layout: docs
redirect_from:
  - /documentation/1.x/reporters/kamino/
---

{% include toc.html %}

Reporting Metrics and Traces to Kamon APM
=========================================

[Kamon APM][1] is a hosted monitoring and debugging platform for microservices. Kamon APM was designed from the ground
up with one goal in mind: create a monitoring platform that can accept all the metrics and tracing data exactly as Kamon
records it: no averages, no summaries, no downsampling, no data quality loss. Having the entire data that Kamon captures
means proper aggregation of data across instances and proper percentiles calculation, which translates in better,
accurate and relevant alerts and insight on your application's behavior.


## Installation and Startup

Add the `kamon-apm` dependency to your build:
  - Group ID: `io.kamon`
  - Package ID: `kamon-apm`
  - Scala Versions: 2.11 / 2.12
  - Latest Version: [![Kamon APM Reporter](https://maven-badges.herokuapp.com/maven-central/io.kamon/kamon-apm-reporter_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.kamon/kamon-apm-reporter_2.12)

Adding the dependency to SBT would look like this:

{% code_block scala %}
libraryDependencies += "io.kamon" %% "kamon-apm" % "1.1.3"
{% endcode_block scala %}

Once the reporter is on your classpath you can add it like any other Kamon reporter:

{% code_block scala %}
import kamon.apm.KamonApm

Kamon.addReporter(new KamonApm())
{% endcode_block scala %}


## Configuration

At a minimum you should provide your Kamon APM API key to start reporting data using the `kamon.apm.api-key`
configuration setting in your `application.conf` file:

{% code_block typesafeconfig %}
kamon.api {
  api-key = "abcdefghijklmnopqrstuvwxyz"
}
{% endcode_block scala %}

You can find your API key in Kamon APM's administration section.


## Visualization and Fun

Kamon APM has a deep understanding on all metrics reported by Kamon and provides ready to use dashboards that make it
super easy to start looking at service, JVM, Hosts, Akka-related metrics, Traces and more. Here are some examples from a
demo application:

<img class="img-fluid my-4" src="/assets/img/apm-services-dashboard.png">
<img class="img-fluid my-4" src="/assets/img/apm-service-overview.png">
<img class="img-fluid my-4" src="/assets/img/apm-actor-details.png">
<img class="img-fluid my-4" src="/assets/img/apm-jvm-metrics.png">
<img class="img-fluid my-4" src="/assets/img/apm-traces.png">

[1]: /apm/
