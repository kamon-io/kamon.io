---
title: Kamon | Changelog
layout: documentation
---

Changelog
=========


<hr>
Version 0.6.0 <small>(2016-01-xx)</small>
------------------------------------------------------------------------------------------------

* kamon-all
  * Ensure that Kamon becomes test friendly. (see issue [#202](https://github.com/kamon-io/Kamon/issues/202)).
  * Improve Kamon tooling for tests. (see issue [#248](https://github.com/kamon-io/Kamon/issues/248)).

* kamon-core:
  * Avoid `StackOverflowError` shutting down JVM. (see issue [#295](https://github.com/kamon-io/Kamon/issues/295)).
  * Unsupported major.minor version in GlobPathFilter. (see issue [#250](https://github.com/kamon-io/Kamon/issues/250)).
  * Generalize `ThreadPoolExecutors` metrics. (see issue [#247](https://github.com/kamon-io/Kamon/issues/247)).
  * Fix typo in kamon `auto-start error`. (see [pull #262](https://github.com/kamon-io/Kamon/pull/262)).
  * Don't throw an `NPE` during shutdown if Kamon hasn't been started. (see [pull #263](https://github.com/kamon-io/Kamon/pull/263)).
  * Provide generic way to scale time and memory metrics. see [pull #294](https://github.com/kamon-io/Kamon/pull/294)).  
  * Don't throw MatchError when auto-start is disabled for a module. see [pull #302](https://github.com/kamon-io/Kamon/pull/302)).

* kamon-akka:
  * Error thrown in dispatcher instrumentation when using custom dispatchers. (see issue [#290](https://github.com/kamon-io/Kamon/issues/290)).
  * Akka `2.4` support. (see issue [#224](https://github.com/kamon-io/Kamon/issues/224)).
  * Balancing pool router shows incorrect `time-in-mailbox` and `mailbox-size metrics`. (see issue [#271](https://github.com/kamon-io/Kamon/issues/271)).
  * Provide `actor-group` metrics. (see issue [#101](https://github.com/kamon-io/Kamon/issues/101)).

* kamon-akka-remote
  * Akka `2.4` support. (see issue [#224](https://github.com/kamon-io/Kamon/issues/224)).

* kamon-system-metrics:
  * Class loading metrics should have no unit in kamon-system-metrics. (see issue [#297](https://github.com/kamon-io/Kamon/issues/297)).
  * Avoid updating the `totalCount` on our histograms. (see issue [#293](https://github.com/kamon-io/Kamon/issues/293)).
  * Histogram recorded value cannot be negative in `ProcessCpuMetrics`. (see issue [#291](https://github.com/kamon-io/Kamon/issues/291)).
  * Fix heap metrics update. (see [pull #260](https://github.com/kamon-io/Kamon/pull/260)).
  * Expose memory buffer pool metrics from `JMX`. (see [pull #317](https://github.com/kamon-io/Kamon/pull/317)).

* kamon-newrelic:
  * Prevent `NPE` when errors are logged without New Relic Agent. (see [pull #279](https://github.com/kamon-io/Kamon/pull/279)).
  * Support the use of multiple names for a `New Relic` app. (see issue [#255](https://github.com/kamon-io/Kamon/issues/255)).
  * Match error when segments are not `http-client`. (see issue [#253](https://github.com/kamon-io/Kamon/issues/253)).
  * Add `ssl` support to agent. (see [pull #268](https://github.com/kamon-io/Kamon/pull/268)).
  * Associate logged errors with correct transaction. (see [pull #269](https://github.com/kamon-io/Kamon/pull/269)).

* kamon-statsd:
  * Allow custom `statsd senders` + add simple statsd sender which doesn't batch stats. (see [pull #270](https://github.com/kamon-io/Kamon/pull/270)).
  * Allow `time` and `memory` metrics be scaled before sending to statsd. see [pull #294](https://github.com/kamon-io/Kamon/pull/294)).  
  * Fix time unit naming in `reference.conf`. see [pull #298](https://github.com/kamon-io/Kamon/pull/298)).  

* kamon-datadog:
  * Allow `time` and `memory` metrics be scaled before sending to datadog. see [pull #294](https://github.com/kamon-io/Kamon/pull/294)).
  * Fix time unit naming in `reference.conf`. see [pull #298](https://github.com/kamon-io/Kamon/pull/298)).  

* kamon-autoweave:
  * This new module allow attach the `AspectJ loadtime weaving agent to a JVM after it has started`. (see [pull #292](https://github.com/kamon-io/Kamon/pull/292)).
  * Doesn't attach Mac JVM properly. (see [pull #308](https://github.com/kamon-io/Kamon/pull/308)).

* kamon-jmx:
  * Reporting Metrics to `JMX MBeans`. (see [pull #258](https://github.com/kamon-io/Kamon/pull/258)).

* kamon-fluentd:
  * This `kamon-fluentd` module provides capabilities to send kamon metrics to fluentd server. (see [pull #264](https://github.com/kamon-io/Kamon/pull/264)).

* kamon-spm:
  * Fix sending metrics failure message. (see [pull #280](https://github.com/kamon-io/Kamon/pull/280)).

* kamon-spray:
  * Check for trace-token header in case-insensitive manner. (see [pull #299](https://github.com/kamon-io/Kamon/pull/299)).

<hr>
Version 0.5.2 <small>(2015-10-06)</small>
------------------------------------------------------------------------------------------------

* kamon-core:
  * Avoid NPE thrown if you shutdown Kamon and it wasn't started yet. (see [pull #263](https://github.com/kamon-io/Kamon/pull/263)).

* kamon-newrelic:
  * Fix a match error when non-http client segments are recorded. (see issue [#253](https://github.com/kamon-io/Kamon/issues/253)).
  * Support for multiple application names. (see issue [#255](https://github.com/kamon-io/Kamon/issues/255)).

* kamon-system-metrics:
  * Ensure that heap metrics collect new instances of `MemoryUsage` rather than keeping the first seen instance. (see [pull #260](https://github.com/kamon-io/Kamon/pull/260)).


<hr>
Version 0.5.1 <small>(2015-08-31)</small>
------------------------------------------------------------------------------------------------

* kamon:
  * Revert the need to run on Java 8.

* kamon-core:
  * Make Kamon test-friendly. (see issue [#202](https://github.com/kamon-io/Kamon/issues/202)).
  * Pull the basics of dispatcher metrics for Akka into a more general ThreadPoolExecutor metrics.

* kamon-akka-remote:
  * Avoid breaking Kryo serialization on remote messages. (see issue [#160](https://github.com/kamon-io/Kamon/issues/160)).

* kamon-play-24:
  * Use the URI's authority instead of the full URL when naming WS Client request segments.

* kamon-system-metrics:
  * Report metrics for all JVM memory pools. (see [pull #244](https://github.com/kamon-io/Kamon/pull/244)).


<hr>
Version 0.5.0 <small>(2015-08-17)</small>
------------------------------------------------------------------------------------------------

* kamon-core
  * Ensure that the TraceLocalStorage can be used from Java (see [issue 196](https://github.com/kamon-io/Kamon/issues/196)).
  * Memory leak when removing entities with MinMaxCounter (see [issue 227](https://github.com/kamon-io/Kamon/issues/227)).
  * Introduce new Sampler `clock-sampler` (see [pull 208](https://github.com/kamon-io/Kamon/pull/208)).
  * Fix `ordered-sampler` (see [pull 201](https://github.com/kamon-io/Kamon/pull/201)).
  * Fix usage of `GaugeKey` for gauges in MetricsModule (see [pull 198](https://github.com/kamon-io/Kamon/pull/198)).
  * Allow creation of counters with units (see [pull 236](https://github.com/kamon-io/Kamon/pull/236)).
  * Allow custom `kamon.trace.token-generator` (see [pull 223](https://github.com/kamon-io/Kamon/pull/223)).
  * The `withNewAsyncSegment` method actually evaluates the supplied code twice (see [issue 204](https://github.com/kamon-io/Kamon/issues/204)).

* kamon-akka
  * NPE when initializing a balancing-pool router from configuration (see [issue 199](https://github.com/kamon-io/Kamon/issues/199)).
  * Avoid runtime exceptions logged on ActorCell shutdown (see [pull 220](https://github.com/kamon-io/Kamon/pull/220)).
  * Change map to foreach for side-effecting behaviour on Option (see [pull 212](https://github.com/kamon-io/Kamon/pull/212)).

* kamon-play
  * Play trace name for emulated HEAD requests  (see [issue 237](https://github.com/kamon-io/Kamon/issues/237)).
  * Create a Play(2.4) module for manage the lifecycle of kamon  (see [issue 169](https://github.com/kamon-io/Kamon/issues/169)).

* kamon-spray
  * Memory leak with Spray (see [issue 213](https://github.com/kamon-io/Kamon/issues/213)).
  * Fix segment finishing on errors (see [pull 205](https://github.com/kamon-io/Kamon/pull/205)).

* kamon-newrelic
  * Newrelic is not subscribing to single-instrument entities (see [issue 197](https://github.com/kamon-io/Kamon/issues/197)).
  * Remove compile dependency from kamon-newrelic to kamon-testkit (see [pull 231](https://github.com/kamon-io/Kamon/pull/231)).
  * Add possibility to send akka metrics to the Newrelic (see [pull 228](https://github.com/kamon-io/Kamon/pull/228)).

* kamon-system-metrics
  * Split/Allow disabling of system sigar and JVM JMX metrics in system-metrics module (see [issue 234](https://github.com/kamon-io/Kamon/issues/234)).
  * SigarNotImplementedException exceptions on windows 7 (see [issue 235](https://github.com/kamon-io/Kamon/issues/235)).

* kamon-spm
  * This new module send Kamon Akka metrics to [SPM](http://sematext.com/spm/index.html) (see [pull 240](https://github.com/kamon-io/Kamon/pull/240)).

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
  * This new module provide a simple way to integrate the kamon instruments using annotations! Check out our [Annotation documentation](/integrations/annotation/using-annotations/) for more details.

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
