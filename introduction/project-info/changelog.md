---
title: Kamon | Changelog
layout: documentation
---

Changelog
=========

<hr>
Version 0.4.0 <small>(2015-05-09)</small>
------------------------------------------------------------------------------------------------

* kamon-core
  * Traces subscriptions V1. Check out our [Metrics subscriptions protocol documentation](/core/metrics/subscription-protocol/) for more details.
  * Lift the MDC tools of LoggerLikeInstrumentation into something generic (see [issue 100](https://github.com/kamon-io/Kamon/issues/100)).
  * Store in TraceLocal useful data to diagnose errors (see [issue 6](https://github.com/kamon-io/Kamon/issues/6)).
  * Introduced support for metric tags (see [pull 164](https://github.com/kamon-io/Kamon/pull/164)).
  * Single kamon instance per JVM (see [pull 156](https://github.com/kamon-io/Kamon/pull/156)).
  * Use ModuleSupervisor to init all auto-start modules (see [pull 152](https://github.com/kamon-io/Kamon/pull/152)).
  * Improve the metric recorders infrastructure (see [pull 151](https://github.com/kamon-io/Kamon/pull/151)).
  * Fix ThresholdSampler to use minimum-elapsed-time setting (see [pull 129](https://github.com/kamon-io/Kamon/pull/129)).
  * Improve the scheduling of measurement-taking future callbacks (see [issue 143](https://github.com/kamon-io/Kamon/issues/143)).
  * Provide a way to subscribe to metrics with plain string patterns (see [issue 141](https://github.com/kamon-io/Kamon/issues/141)).
  * Make the metric filters merge friendly (see [issue 138](https://github.com/kamon-io/Kamon/issues/138)).
  * Create some sort of `KamonLoader` extension (see [issue 137](https://github.com/kamon-io/Kamon/issues/137)).
  * Separate akka, scala and scalaz instrumentation from `kamon-core` (see [issue 136](https://github.com/kamon-io/Kamon/issues/136)).

* kamon-akka
  * Measure the routees metrics when doing router metrics (see [issue 139](https://github.com/kamon-io/Kamon/issues/139)).
  * Separate all akka-related stuff into it's own module (see [pull 145](https://github.com/kamon-io/Kamon/pull/145)).
  * Akka instrumentation namespace (see [pull 108](https://github.com/kamon-io/Kamon/pull/108)).
  * Exact actor metric filter (see [issue 116](https://github.com/kamon-io/Kamon/issues/116)).
  * A more lightweight way for tracing the `Akka.ask` timeouts (see [issue 113](https://github.com/kamon-io/Kamon/issues/113)).
  * NPE in ActorCellInstrumention.beforeInvokeFailure (see [issue 184](https://github.com/kamon-io/Kamon/issues/184)).
  * Root guardian actor should not be picked up by ActorMetrics (see [issue 157](https://github.com/kamon-io/Kamon/issues/157)).

* kamon-system-metrics
  * Include more metrics (see [issue 131](https://github.com/kamon-io/Kamon/issues/131))
  * Reported GC time is way too high (see [issue 135](https://github.com/kamon-io/Kamon/issues/135))

* kamon-play
  * Overriding `Global.doFilter` breaks RequestInstrumentation (see [issue 122](https://github.com/kamon-io/Kamon/issues/122))
  * Store in TraceLocal useful data to diagnose errors (see [issue 6](https://github.com/kamon-io/Kamon/issues/6)).
  * Update `kamon-play-example` to latest version (see [pull 164](https://github.com/kamon-io/Kamon/pull/164)).
  * Compile pattern for path normalization only once (see [pull 159](https://github.com/kamon-io/Kamon/pull/159)).
  * Improve the scheduling of measurement-taking future callbacks  (see [issue 143](https://github.com/kamon-io/Kamon/issues/143)).

* kamon-spray
  * Store in TraceLocal useful data to diagnose errors (see [issue 6](https://github.com/kamon-io/Kamon/issues/6)).
  * Improve the scheduling of measurement-taking future callbacks  (see [issue 143](https://github.com/kamon-io/Kamon/issues/143)).

* kamon-log-reporter
  * Provide the ability to report system metrics (see [issue 72](https://github.com/kamon-io/Kamon/issues/72)).
  * Include dispatcher metrics in LogReporter (see [issue 163](https://github.com/kamon-io/Kamon/issues/163)).

* kamon-newrelic
  * Missing NewRelic Web Application Metrics (see [issue 112](https://github.com/kamon-io/Kamon/issues/112)).

* kamon-system-metrics
  * Introduce Context Swtitches in System Metrics (see [issue 66](https://github.com/kamon-io/Kamon/issues/66)).
  * Decouple kamon-system-metrics from sigar through to kamon-sigar-loader (see [issue 110](https://github.com/kamon-io/Kamon/issues/110)).
  * Context switches metric causes file descriptor leak (see [issue 148](https://github.com/kamon-io/Kamon/issues/148)).
  * Two-phase sigar loading (see [pull 124](https://github.com/kamon-io/Kamon/pull/124))
  * Divide by Zero exception with sigar (see [issue 194](https://github.com/kamon-io/Kamon/issues/194)).

* kamon-annotation
  * This new module provide a simple way to integrate the kamon instruments using annotations! Check out our [Annotation documentation](/integrations/annotation/annotation/) for more details.

* kamon-jdbc <span class="label label-warning">experimental</span>
  * This new module collects metrics related to JDBC (see [issue 107](https://github.com/kamon-io/Kamon/issues/107)) for more detail.

* kamon-statsd
  * StatsD extension now defers the creation of the InetSocketAddress instance until needed (see [pull 174](https://github.com/kamon-io/Kamon/pull/174))
  * Trace segments no longer reported to statsd (see [issue 166](https://github.com/kamon-io/Kamon/issues/166))
  * Report HttpServerMetrics (see [issue 132](https://github.com/kamon-io/Kamon/issues/132))

* kamon-datadog
  * Report HttpServerMetrics (see [issue 132](https://github.com/kamon-io/Kamon/issues/132))

* site
  * Introduce new design
  * Full documentation

<hr>
Version 0.3.5/0.2.5 <small>(2014-11-11)</small>
------------------------------------------------------------------------------------------------

* kamon
  * 0.3.5 is compatible with Akka 2.3.x, Spray 1.3.x, Play 2.3.x and Scala 2.10.x/2.11.x
  * 0.2.5 is compatible with Akka 2.2.x, Spray 1.2.x, Play 2.2.x and Scala 2.10.x

* kamon-core
  * Provide min, max, percentiles and average functions for Histogram snapshots (see [issue 85](https://github.com/kamon-io/Kamon/issues/85)).
  * Apparently random `NPE` (see [issue 88](https://github.com/kamon-io/Kamon/issues/88)).
  * Provide metrics for routers (see [issue 62](https://github.com/kamon-io/Kamon/issues/62)).
  * `CallingThreadDispatcher` cannot be cast to `DispatcherMetricCollectionInfo` (see [issue 95](https://github.com/kamon-io/Kamon/issues/95)).
  * Remove KamonWeaverMessageHandler to avoid dependencies issues (see [issue 97](https://github.com/kamon-io/Kamon/issues/97)).
  * `Aspectj` dependency scope (see [issue 106](https://github.com/kamon-io/Kamon/issues/106)).

* kamon-spray
  * External naming for HTTP traces and segments (see [issue 65](https://github.com/kamon-io/Kamon/issues/65))

* kamon-play
  * Review the trace name in play applications (see [issue 82](https://github.com/kamon-io/Kamon/issues/82)).
  * TraceContext is not propagated when call WS outside an Action (see [issue 33](https://github.com/kamon-io/Kamon/issues/33)).
  * External naming for HTTP traces and segments (see [issue 65](https://github.com/kamon-io/Kamon/issues/65))

* kamon-log-reporter
  * Provide the ability to report system metrics (see [issue 72](https://github.com/kamon-io/Kamon/issues/72)).

* kamon-newrelic
  * Implement error handling with NewRelic Agent (see [issue 7](https://github.com/kamon-io/Kamon/issues/7)).
  * Avoid reporting data to Newrelic if no metrics have been collected (see [issue 17](https://github.com/kamon-io/Kamon/issues/17)).
  * Report New Relic errors with correct URI (see [issue 103](https://github.com/kamon-io/Kamon/issues/103)).
  * Report HTTP client call times to New Relic (see [issue 63](https://github.com/kamon-io/Kamon/issues/63)).

* kamon-system-metrics
  * Introduce Context Swtitches in System Metrics (see [issue 66](https://github.com/kamon-io/Kamon/issues/66)).


* kamon-akka-remote <span class="label label-warning">experimental</span>
  * Provide basic support for akka cluster and remoting (see [issue 61](https://github.com/kamon-io/Kamon/issues/61)).
  * Separate remoting instrumentation from kamon-core (see [issue 99](https://github.com/kamon-
  io/Kamon/issues/99)).

* site
  * Include instruments documentation on our site (see [issue 90](https://github.com/kamon-
  io/Kamon/issues/90)).


<hr>
Version 0.3.4/0.2.4 <small>(2014-08-13)</small>
------------------------------------------------------------------------------------------------

* kamon
   * 0.3.4 is compatible with Akka 2.3, Spray 1.3, Play 2.3 and Scala 2.10.x/2.11.x
   * 0.2.4 is compatible with Akka 2.2, Spray 1.2, Play 2.2 and Scala 2.10.x

* kamon-core
   * Fix OutOfBoundsException being thrown when recording values from a MinMaxCounter (see [issue 71](https://github.com/kamon-io/Kamon/issues/71)).
   * Use the inline variant of TraceRecorder.withTraceContext.
   * Avoid having any other copies of the AspectJ weaver around in runtime by marking the weaver dependency as "provided".

* kamon-spray
   * Use the inline variant of TraceRecorder.withTraceContext.

* kamon-play
   * Use the inline variant of TraceRecorder.withTraceContext.

* kamon-log-reporter
   * Provide the ability to report system metrics.

* kamon-system-metrics (Experimental)
   * Minor changes in the banner displayed when starting the system metrics module.


<hr>
Version 0.3.3/0.2.3 <small>(2014-08-05) <span class="label label-danger">unstable</span></small>
------------------------------------------------------------------------------------------------

* kamon
   * 0.3.3 is compatible with Akka 2.3, Spray 1.3, Play 2.3 and Scala 2.10.x/2.11.x
   * 0.2.3 is compatible with Akka 2.2, Spray 1.2, Play 2.2 and Scala 2.10.x

* kamon-core
   * A NullPointerException was thrown when a actor is stopped (see [issue 69](https://github.com/kamon-io/Kamon/issues/69)).

* kamon-statsd
   * Report user metrics (Histograms, Counters, MinMaxCounters and Gauges).

* kamon-datadog
   * Report user metrics (Histograms, Counters, MinMaxCounters and Gauges).

* kamon-system-metrics (Experimental)
   * The `kamon-system-metrics` module artifacts didn't include all the Sigar related files.


<hr>
Version 0.3.2/0.2.2 <small>(2014-07-29) <span class="label label-danger">unstable</span></small>
-----------------------------------------------------------------------------------------------

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
