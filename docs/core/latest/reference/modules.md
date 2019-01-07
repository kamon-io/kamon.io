---
title: Kamon Modules
layout: docs
---

{% include toc.html %}

Official Modules
================

Core
----
<h5>
  Latest Release:
  [1.1.3](https://search.maven.org/artifact/io.kamon/kamon-core_2.12/1.1.3/jar) -
  [Release Notes](https://github.com/kamon-io/Kamon/releases/tag/v1.1.3) -
  [Code](https://github.com/kamon-io/Kamon)
</h5>

The `kamon-core` library contains the metrics, context propagation and distributed tracing APIs upon which the entire
Kamon ecosystem is built. It does not include, however, any bytecode instrumentation facilities.


Instrumentation
---------------

Instrumentation modules contain bytecode instrumentation that specifically target libraries and JVM components to gather
metrics, perform context propagation or enable distributed tracing without requiring you to apply code changes to your
applications. Make sure that you [setup][agent] the AspectJ Weaver agent for these modules to work properly.

### System Metrics
<h5>
  Latest Release:
  [1.0.0](https://search.maven.org/artifact/io.kamon/kamon-system-metrics_2.12/1.0.0/jar) -
  [Release Notes](https://github.com/kamon-io/kamon-system-metrics/releases/tag/v1.0.0) -
  [Code](https://github.com/kamon-io/kamon-system-metrics)
</h5>

Automatically collects Host system metrics like CPU, memory, disk and network utilization and JVM-specific metrics,
including Garbage Collection times and Memory pools utilization.


### Executors
<h5>
  Latest Release:
  [1.0.2](https://search.maven.org/artifact/io.kamon/kamon-executors_2.12/1.0.2/jar) -
  [Release Notes](https://github.com/kamon-io/kamon-executors/releases/tag/v1.0.2) -
  [Code](https://github.com/kamon-io/kamon-executors)
</h5>

Provides utilities that can extract metrics from Thread Pool Executors and Fork Join Pools. It can also "wrap" an
executor service to provide context propagation to all scheduled tasks.


### Logback
<h5>
  Latest Release:
  [1.0.3](https://search.maven.org/artifact/io.kamon/kamon-logback_2.12/1.0.3/jar) -
  [Release Notes](https://github.com/kamon-io/kamon-logback/releases/tag/v1.0.3) -
  [Code](https://github.com/kamon-io/kamon-logback)
</h5>

Provides automatic instrumentation that propagate context in logging events when using the AsyncAppender and converters
to facilitate including trace identifiers in the log patterns.


### JDBC
<h5>
  Latest Release:
  [1.0.2](https://search.maven.org/artifact/io.kamon/kamon-jdbc_2.12/1.0.2/jar) -
  [Release Notes](https://github.com/kamon-io/kamon-jdbc/releases/tag/v1.0.2) -
  [Code](https://github.com/kamon-io/kamon-jdbc)
</h5>

Provides automatic instrumentation that create Spans on JDBC calls. This module also tracks metrics for the Hikari
connection pool library.


### Futures
<h5>
  Latest Release:
  [1.0.0](https://search.maven.org/artifact/io.kamon/kamon-scala-future_2.12/1.0.0/jar) -
  [Release Notes](https://github.com/kamon-io/kamon-futures/releases/tag/v1.0.0) -
  [Code](https://github.com/kamon-io/kamon-futures)
</h5>

Provides automatic context propagation for Scala, Twitter and Scalaz futures. This modules publishes three artifacts:
  - **kamon-scala-future** instruments `scala.concurrent.Future` from the Scala Library.
  - **kamon-twitter-future** instruments `com.twitter.util.Future` from Scalaz.
  - **kamon-scalaz-future** instruments `scalaz.concurrent.Future` from Twitter's util-core.


### Akka
<h5>
  Latest Release:
  [1.1.2](https://search.maven.org/artifact/io.kamon/kamon-akka-2.4_2.12/1.1.2/jar) -
  [Release Notes](https://github.com/kamon-io/kamon-akka/releases/tag/v1.1.2) -
  [Code](https://github.com/kamon-io/kamon-akka)
</h5>

Provides automatic automatic context propagation, metrics and distributed tracing for Akka actor systems, actors,
routers, dispatchers and actor groups. This modules publishes two artifacts:
  - **kamon-akka-2.4** which works for Akka 2.4.x releases.
  - **kamon-akka-2.5** which works for Akka 2.5.x releases.


### Akka Remote
<h5>
  Latest Release:
  [1.1.0](https://search.maven.org/artifact/io.kamon/kamon-akka-remote-2.5_2.12/1.1.0/jar) -
  [Release Notes](https://github.com/kamon-io/kamon-akka-remote/releases/tag/v1.1.0) -
  [Code](https://github.com/kamon-io/kamon-akka-remote)
</h5>

Provides automatic automatic context propagation, metrics and distributed tracing across an Akka remote channel,
including Cluster and Cluster sharding metrics. This modules publishes two artifacts:
  - **kamon-akka-remote-2.4** which works for Akka 2.4.x releases.
  - **kamon-akka-remote-2.5** which works for Akka 2.5.x releases.


### Akka HTTP
<h5>
  Latest Release:
  [1.1.0](https://search.maven.org/artifact/io.kamon/kamon-akka-http-2.4_2.12/1.1.1/jar) -
  [Release Notes](https://github.com/kamon-io/kamon-akka-http/releases/tag/v1.1.0) -
  [Code](https://github.com/kamon-io/kamon-akka-http)
</h5>

Provides automatic HTTP request tracing, context propagation and HTTP server metrics for applications developed using
the Akka HTTP toolkit. This modules publishes two
artifacts:
  - **kamon-akka-http-2.4** which works for Akka 2.4.x releases.
  - **kamon-akka-http-2.5** which works for Akka 2.5.x releases.


### Play Framework
<h5>
  Latest Release:
  [1.1.1](https://search.maven.org/artifact/io.kamon/kamon-play-2.6_2.12/1.1.1/jar) -
  [Release Notes](https://github.com/kamon-io/kamon-play/releases/tag/v1.1.1) -
  [Code](https://github.com/kamon-io/kamon-play)
</h5>

Provides automatic HTTP request tracing, context propagation and HTTP server metrics for applications developed using
the Play Framework. This modules publishes three artifacts:
  - **kamon-play-2.4** which works for Play 2.4.x releases.
  - **kamon-play-2.5** which works for Play 2.5.x releases.
  - **kamon-play-2.6** which works for Play 2.6.x releases.


### HTTP4S
<h5>
  Latest Release:
  [1.0.10](https://search.maven.org/artifact/io.kamon/kamon-http4s_2.12/1.0.10/jar) -
  [Release Notes](https://github.com/kamon-io/kamon-http4s/releases/tag/v1.0.10) -
  [Code](https://github.com/kamon-io/kamon-http4s)
</h5>

Includes middlewares that provide HTTP request tracing and context propagation on both client and server side as well
as HTTP server metrics for applications developed using HTTP4S.



Reporters
---------

Reporting modules allow to expose or send the metrics and trace data to external systems.

### Prometheus
<h5>
  Latest Release:
  [1.1.1](https://search.maven.org/artifact/io.kamon/kamon-prometheus_2.12/1.1.1/jar) -
  [Release Notes](https://github.com/kamon-io/kamon-prometheus/releases/tag/v1.1.1) -
  [Code](https://github.com/kamon-io/kamon-prometheus)
</h5>

Exposes metrics data in the Prometheus format.


### InfluxDB
<h5>
  Latest Release:
  [1.1.1](https://search.maven.org/artifact/io.kamon/kamon-prometheus_2.12/1.1.1/jar) -
  [Release Notes](https://github.com/kamon-io/kamon-prometheus/releases/tag/v1.1.1) -
  [Code](https://github.com/kamon-io/kamon-prometheus)
</h5>

Sends metrics data to InfluxDB over the HTTP API.


### StatsD
<h5>
  Latest Release:
  [1.0.0](https://search.maven.org/artifact/io.kamon/kamon-statsd_2.12/1.0.0/jar) -
  [Release Notes](https://github.com/kamon-io/kamon-statsd/releases/tag/v1.0.0) -
  [Code](https://github.com/kamon-io/kamon-statsd)
</h5>

Sends metrics data to StatsD over UDP.


### Graphite
<h5>
  Latest Release:
  [1.2.1](https://search.maven.org/artifact/io.kamon/kamon-graphite_2.12/1.2.1/jar) -
  [Release Notes](https://github.com/kamon-io/kamon-graphite/releases/tag/v1.2.1) -
  [Code](https://github.com/kamon-io/kamon-graphite)
</h5>

Sends metrics data to StatsD over UDP.


### Zipkin
<h5>
  Latest Release:
  [1.0.0](https://search.maven.org/artifact/io.kamon/kamon-zipkin_2.12/1.0.0/jar) -
  [Release Notes](https://github.com/kamon-io/kamon-zipkin/releases/tag/v1.0.0) -
  [Code](https://github.com/kamon-io/kamon-zipkin)
</h5>

Sends spans to Zipkin.


### Jaeger
<h5>
  Latest Release:
  [1.0.2](https://search.maven.org/artifact/io.kamon/kamon-jaeger_2.12/1.0.2/jar) -
  [Release Notes](https://github.com/kamon-io/kamon-jaeger/releases/tag/v1.0.2) -
  [Code](https://github.com/kamon-io/kamon-jaeger)
</h5>

Sends spans to Jaeger.


### Kamino
<h5>
  Latest Release:
  [1.1.2](https://search.maven.org/artifact/io.kamon/kamino-reporter_2.12/1.1.2/jar) -
  [Release Notes](https://github.com/kamino-apm/kamino-reporter/releases/tag/v1.1.2) -
  [Code](https://github.com/kamino-apm/kamino-reporter)
</h5>

Sends metrics and spans to Kamino.


### Sematext SPM
<h5>
  Latest Release:
  [1.1.3](https://search.maven.org/artifact/io.kamon/kamon-spm_2.12/1.1.3/jar) -
  [Release Notes](https://github.com/kamino-apm/kamino-spm/releases/tag/v1.1.3) -
  [Code](https://github.com/kamon-io/kamon-spm)
</h5>

Sends metrics to Sematext SPM.



### Datadog
<h5>
  Latest Release:
  [1.0.0](https://search.maven.org/artifact/io.kamon/kamon-datadog_2.12/1.0.0/jar) -
  [Release Notes](https://github.com/kamino-apm/kamino-datadog/releases/tag/v1.0.0) -
  [Code](https://github.com/kamon-io/kamon-datadog)
</h5>

Sends metrics to Datadog.


Community Modules
=================

TODO: Gather all community modules.


[agent]: agent