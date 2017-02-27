---
layout: post
title: 'Kamon 0.6.0 is finally here, download while still hot!'
date: 2016-03-29
categories: teamblog, releases
---

Dear community, we are really happy to announce that after a long long wait,
lots of fixes and even new modules being added, Kamon 0.6.0 is finally here!




For now, we will keep it short and just give a huge thanks to all of our
collaborators and users, we are constantly surprised by the number of amazing
people around the world using Kamon and providing great feedback and fixes. To
all of you: Thank You!

We plan to publish follow-up posts digging into some of the most relevant
features of the release but if you are anxious, here is a extract from our
changelog:


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
  * Introduce selective instrumentation for Akka actors. (see [pull #323](https://github.com/kamon-io/Kamon/pull/323)).

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


As always, have fun with Kamon!
