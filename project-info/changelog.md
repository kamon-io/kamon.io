---
title: Kamon | Changelog
layout: default
---

Changelog
=========

<hr>
Version 0.3.2/0.2.2 <small>(2014-07-29)</small>
--------------------------------

* kamon
   * 0.3.2 is compatible with Akka 2.3, Spray 1.3, Play 2.3 and Scala 2.10.x/2.11.x
   * 0.2.2 is compatible with Akka 2.2, Spray 1.2, Play 2.2 and Scala 2.10.x

* kamon-core
    * Introduce a inline variant of `TraceRecorder.withTraceContext` via macros.
    * Refactor the internal implementation metrics collection, more details on this will be blogged soon.
    * Introduce support for User Metrics, now you can ask Kamon for a Histogram, Counter or Gauge and record your own
      measurements.
    * Created `KamonStandalone`, a simple helper that embeds a ActorSystem for people who want metrics but is not using
      the reactive stack.
    * Log a warning message when the Metrics extension is loaded and the AspectJ weaver is missing.

* kamon-play
    * Record HTTP Server Metrics.
    * Introduce a logger instrumentation that allows taking values from `TraceLocalStorate` to `MDC`.

* kamon-spray
    * Record HTTP Server Metrics.

* kamon-log-reporter
    * This new module simply outputs trace, actor and user metrics to the log in a simple ascii table format, useful for
      local testing and debugging.

* kamon-system-metrics (Experimental)
    * We have a new System Metrics module that collects CPU, Memory, Heap, Garbage Collection and Network Traffic metrics.


<hr>
Version 0.3.1/0.2.1 <small>(2014-06-18)</small>
--------------------------------

* kamon
   * 0.3.1 is compatible with Akka 2.3, Spray 1.3, Play 2.3 and Scala 2.10.x/2.11.x
   * 0.2.1 is compatible with Akka 2.2, Spray 1.2, Play 2.2 and Scala 2.10.x

* kamon-core
    * Dispatchers metrics.
    * Actor Errors Counter.
    * Support for TraceLocal Storage.
    * Allow custom dispatcher configuration for Kamon core components.
    * Introducing `MinMaxCounter` for measurement of mailbox size.
    * New `Counter` Instrument.
    * Fix NPE when dispatcher shutdown [#37](https://github.com/kamon-io/Kamon/issues/37)
    * Fix NPE when application start with multiples Actor Systems [#38](https://github.com/kamon-io/Kamon/issues/38)

* kamon-datadog
    * Now you can send Actor, Trace and Dispatchers metrics to Datadog! Check out our [Datadog documentation](/backends/datadog/) for more details.

* kamon-statsd
    * Force the decimal format to use dot `(.)` as decimal point.

* kamon-play
    * Our Play! module is no longer considered experimental!
    * Avoid unnecessary libraries in runtime [#40](https://github.com/kamon-io/Kamon/issues/40)

* kamon-newrelic
   * Fix ClassCastException in NewRelicErrorLogger [#29](https://github.com/kamon-io/Kamon/issues/29)

<hr>
Version 0.3.0/0.2.0 <small>(2014-04-24)</small>
--------------------------------

* Same feature set as 0.0.15 but now available for Akka 2.2 and Akka 2.3:
   * 0.3.0 is compatible with Akka 2.3, Spray 1.3 and Play 2.3.
   * 0.2.0 is compatible with Akka 2.2, Spray 1.2 and Play 2.2.

<hr>
Version 0.0.15 <small>(2014-04-10)</small>
---------------------------

* kamon
    * Now publishing to Sonatype and Maven Central
    * `reference.conf` files are now "sbt-assembly merge friendly"

* kamon-core
    * Control of AspectJ weaving messages through Kamon configuration
    * Avoid the possible performance issues when calling `MessageQueue.numberOfMessages` by keeping a external counter.

* kamon-statsd
    * Now you can send Actor and Trace metrics to StatsD! Check out our [StatsD documentation](/statsd/) for more
      details.

* kamon-play (Experimental)
    * Experimental support to trace metrics collection, automatic trace token propagation and HTTP Client request
      metrics is now available for Play! applications.


<hr>
Version 0.0.14 <small>(2014-03-17)</small>
---------------------------
* kamon-core
    * Improved startup times
    * Remake of trace metrics collection
    * Support for custom metrics collection (Experimental)

* kamon-play
    * Initial support (Experimental)

* site
    * [logging](/core/logging/) (WIP)
    * [tracing](/core/tracing/) (WIP)
