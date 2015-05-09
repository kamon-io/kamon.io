---
title: Kamon | LogReporter | Documentation
layout: documentation
---

Using the LogReporter
=====================

The `kamon-log-reporter` module is a very simple metrics reporter that subscribes to certain supported categories and
dumps the available data in table-like format on every tick.

This module is not meant to be used in production environments, but it certainly is a convenient way to test Kamon
without having to install a full-fledged metrics backend for development purposes.


Installation
------------

Add the `kamon-log-reporter` dependency to your project and ensure that it is in your classpath at runtime, that's it.
Kamon's module loader will detect that the log reporter is in the classpath and automatically start it.


Reported Metrics
----------------

This reporter will automatically subscribe itself to the following categories:

* __counter, histogram, min-max-counter and gauge__: All single-instrument entities are reported.
* __trace__: Displaying several percentiles for the `elapsed-time` metric.
* __akka-actor__: Displaying several percentiles for the `processing-time` and `time-in-mailbox` metrics, as well as
the number of `errors` and the `mailbox-size` bounds.
* __akka-dispatcher__: Displaying summary metrics for both `fork-join-pool` and `thread-pool-executor` dispatchers.
* __system-metric__: Only the `cpu`, `process-cpu`, `network` and `context-switches` metrics are logged with this reporter.

All entities but single-instrument entities are logged using separate log statements. Single-instrument entities are all
grouped and logged together. Here is a log example that can give you an idea of what to expect when using the log reporter:

{% code_block text %}
[INFO] [05/09/2015 01:54:42.699] [kamon-akka.actor.default-dispatcher-6] [akka://kamon/user/kamon-log-reporter]
+--------------------------------------------------------------------------------------------------+
|                                                                                                  |
|    Trace: RespondWithOK-3                                                                        |
|    Count: 0                                                                                      |
|                                                                                                  |
|  Elapsed Time (nanoseconds):                                                                     |
|    Min: 0            50th Perc: 0              90th Perc: 0              95th Perc: 0            |
|                      99th Perc: 0            99.9th Perc: 0                    Max: 0            |
|                                                                                                  |
+--------------------------------------------------------------------------------------------------+
[INFO] [05/09/2015 01:54:42.699] [kamon-akka.actor.default-dispatcher-6] [akka://kamon/user/kamon-log-reporter]
+--------------------------------------------------------------------------------------------------+
|                                                                                                  |
|    Actor: test/user/simple-service-actor                                                         |
|                                                                                                  |
|   Processing Time (nanoseconds)      Time in Mailbox (nanoseconds)         Mailbox Size          |
|    Msg Count: 1506                       Msg Count: 1506                     Min: 0              |
|          Min: 153                              Min: 496                     Avg.: 0.0            |
|    50th Perc: 3280                       50th Perc: 51456                    Max: 15             |
|    90th Perc: 99840                      90th Perc: 440320                                       |
|    95th Perc: 141312                     95th Perc: 1433600                                      |
|    99th Perc: 667648                     99th Perc: 5963776                Error Count: 0        |
|  99.9th Perc: 3211264                  99.9th Perc: 7012352                                      |
|          Max: 6127616                          Max: 7045120                                      |
|                                                                                                  |
+--------------------------------------------------------------------------------------------------+
[INFO] [05/09/2015 01:54:42.699] [kamon-akka.actor.default-dispatcher-6] [akka://kamon/user/kamon-log-reporter]
+--------------------------------------------------------------------------------------------------+
|                                                                                                  |
|    Actor: kamon/user/kamon-log-reporter                                                          |
|                                                                                                  |
|   Processing Time (nanoseconds)      Time in Mailbox (nanoseconds)         Mailbox Size          |
|    Msg Count: 1                          Msg Count: 1                        Min: 0              |
|          Min: 1417216                          Min: 11200                   Avg.: 0.0            |
|    50th Perc: 1417216                    50th Perc: 11200                    Max: 1              |
|    90th Perc: 1417216                    90th Perc: 11200                                        |
|    95th Perc: 1417216                    95th Perc: 11200                                        |
|    99th Perc: 1417216                    99th Perc: 11200                  Error Count: 0        |
|  99.9th Perc: 1417216                  99.9th Perc: 11200                                        |
|          Max: 1417216                          Max: 11200                                        |
|                                                                                                  |
+--------------------------------------------------------------------------------------------------+
[INFO] [05/09/2015 01:54:42.700] [kamon-akka.actor.default-dispatcher-6] [akka://kamon/user/kamon-log-reporter]
+--------------------------------------------------------------------------------------------------+
|  Thread-Pool-Executor                                                                            |
|                                                                                                  |
|  Dispatcher: test/akka.io.pinned-dispatcher                                                      |
|                                                                                                  |
|  Core Pool Size: 1                                                                               |
|  Max  Pool Size: 1                                                                               |
|                                                                                                  |
|                                                                                                  |
|                         Pool Size        Active Threads          Processed Task                  |
|           Min              1                   1                      0                          |
|           Avg              1.0                 1.0                    0.0                        |
|           Max              1                   1                      0                          |
|                                                                                                  |
+--------------------------------------------------------------------------------------------------+
[INFO] [05/09/2015 01:54:42.700] [kamon-akka.actor.default-dispatcher-6] [akka://kamon/user/kamon-log-reporter]
+--------------------------------------------------------------------------------------------------+
|  Fork-Join-Pool                                                                                  |
|                                                                                                  |
|  Dispatcher: test/akka.actor.default-dispatcher                                                  |
|                                                                                                  |
|  Paralellism: 24                                                                                 |
|                                                                                                  |
|                 Pool Size       Active Threads     Running Threads     Queue Task Count          |
|      Min           18                0                   0                   0                   |
|      Avg           20.0              0.0                 0.0                 0.0                 |
|      Max           24                24                  8                   4                   |
|                                                                                                  |
+--------------------------------------------------------------------------------------------------+
[INFO] [05/09/2015 01:54:42.701] [kamon-akka.actor.default-dispatcher-6] [akka://kamon/user/kamon-log-reporter]
+--------------------------------------------------------------------------------------------------+
|  Fork-Join-Pool                                                                                  |
|                                                                                                  |
|  Dispatcher: kamon/akka.actor.default-dispatcher                                                 |
|                                                                                                  |
|  Paralellism: 10                                                                                 |
|                                                                                                  |
|                 Pool Size       Active Threads     Running Threads     Queue Task Count          |
|      Min           10                5                   2                   0                   |
|      Avg           10.0              8.0                 3.0                 0.0                 |
|      Max           10                10                  7                   0                   |
|                                                                                                  |
+--------------------------------------------------------------------------------------------------+

[INFO] [05/09/2015 01:54:42.702] [kamon-akka.actor.default-dispatcher-6] [akka://kamon/user/kamon-log-reporter]
+--------------------------------------------------------------------------------------------------+
|                                                                                                  |
|    Trace: OKFuture                                                                               |
|    Count: 500                                                                                    |
|                                                                                                  |
|  Elapsed Time (nanoseconds):                                                                     |
|    Min: 268288       50th Perc: 577536         90th Perc: 1884160        95th Perc: 3751936      |
|                      99th Perc: 7143424      99.9th Perc: 26738688             Max: 26738688     |
|                                                                                                  |
+--------------------------------------------------------------------------------------------------+
[INFO] [05/09/2015 01:54:42.702] [kamon-akka.actor.default-dispatcher-6] [akka://kamon/user/kamon-log-reporter]
+--------------------------------------------------------------------------------------------------+
|                                                                                                  |
|                                         Counters                                                 |
|                                       -------------                                              |
|                                   requests  =>  500                                              |
|                                                                                                  |
|                                                                                                  |
|                                        Histograms                                                |
|                                      --------------                                              |
|                                                                                                  |
|                                      MinMaxCounters                                              |
|                                    -----------------                                             |
|                                                                                                  |
|                                          Gauges                                                  |
|                                        ----------                                                |
|                                                                                                  |
+--------------------------------------------------------------------------------------------------+
{% endcode_block %}
