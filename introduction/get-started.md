---
title: Kamon | Get Started
layout: documentation
---

Get Started with Kamon
======================

Kamon is distributed as a core module with all the metric recording and trace manipulation APIs and optional modules
that provide bytecode instrumentation and/or reporting capabilities to your application. All the modules are added to
your  application as simple library dependencies and additionally, if you are using instrumentation modules then the
AspectJ Weaver agent needs to be included as a JVM startup parameter when running your application.

Let's dig into these steps with more detail:


Include the modules you want in your project.
---------------------------------------------

All Kamon components are available through Sonatype and Maven Central and no special repositories need to be configured.
If you are using SBT, you will need to add something like this to your build definition:


{% code_example %}
{%   language scala kamon-core-examples/build.sbt start:3 end:12 label:"SBT" %}
{% endcode_example %}

<br>  

The modules currently available are:

* kamon-core
* kamon-spray <span class="label label-info">requires aspectj</span>
* kamon-play <span class="label label-info">requires aspectj</span>
* kamon-statsd
* kamon-newrelic
* kamon-datadog
* kamon-log-reporter
* kamon-system-metrics
* kamon-akka-remote <span class="label label-info">requires aspectj</span> <span class="label label-warning">experimental</span>

#### Compatibility Notes: ####
* 0.3.x releases are compatible with Akka 2.3, Spray 1.3, Play 2.3 and Scala 2.11.x/2.10.x
* 0.2.x releases are compatible with Akka 2.2, Spray 1.2, Play 2.2 and Scala 2.10.x

<br>  

### Optional: Start your application with the AspectJ Weaver ###

This step is only required if any of the modules that you included in your application requires AspectJ, if that is not
your case you can jump directly to the enjoy section!

Starting your application with the AspectJ weaver is dead simple, just add the `-javaagent` JVM startup parameter
pointing to the weaver's file location and you are done. The details on how to do this vary depending on your preferred
way of deployment, here are some quick notes for the most common deployment scenarios:

{% code_example %}
{%   language text manually-adding-aspectj-weaver-agent/readme.md start:4 end:7 label:"Manually" %}
{%   language scala using-sbt-aspectj-plugin/build.sbt start:7 label:"sbt-aspectj" %}
{% endcode_example %}

If your deployment method is not listed here and you can't figure out how to proceed, please ask for help in our
[mailing list].

<br>  

### Optional: Register the Metrics Extension ###

If you correctly configured your application to start using the AspectJ Weaver agent then the Metrics extension will be
loaded when the first instrumentation point that stores metrics gets executed. If you don't configure the agent properly
then the Metrics extension will log a warning when it is loaded, but if the agent is not present and you are not
recording any metrics manually then the Metrics wont load and you wont see the warning. By adding the Metrics extension
to your application configuration as shown bellow, you ensure that it is loaded when your actor system is started and
the warning will be displayed is necessary:

{% code_example %}
{%   language typesafeconfig using-sbt-aspectj-plugin/src/main/resources/application.conf start:1 end:3 label:"application.conf" %}
{% endcode_example %}

Enjoy!
------

Refer to module's documentation to find out more about the core concepts of [tracing] and [metrics], and learn how to
report your metrics data to external services like [StatsD], [Datadog] and [New Relic].


[sbt-aspectj]: https://github.com/sbt/sbt-aspectj/
[load-time weaving example]: https://github.com/sbt/sbt-aspectj/tree/master/src/sbt-test/weave/load-time/
[tracing]: /core/tracing/core-concepts/
[metrics]: /core/metrics/core-concepts/
[logging]: /core/tracing/logging/
[StatsD]: /backends/statsd/
[Datadog]: /backends/datadog/
[New Relic]: /backends/newrelic/
[mailing list]: https://groups.google.com/forum/#!forum/kamon-user
