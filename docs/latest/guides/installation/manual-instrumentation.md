---
title: 'Getting Started with Kamon | Kamon Documentation'
description: 'Learn how to setup Kamon from scratch'
layout: docs
redirect_from:
  - /documentation/0.6.x/get-started/
  - /documentation/1.x/get-started/
  - /documentation/get-started/
  - /introduction/get-started/
---

{% include toc.html %}

Installation on a Plain Application
===================================

This guide is an "extended" version of the offical [Get Started][get-started] steps for manual instrumentation, but here
we dive a little bit more into what is being achieved in each step. The general idea stays the same as in the official
guide, there are three simple steps to follow:
  1. [Install Kamon](#install-kamon).
  2. [Verify the Installation](#verify-the-installation).
  2. [Install Reporters](#install-reporters).


Install Kamon
-------------

### Add the Core Dependency

First of all, add the `kamon-core` dependency using your build system of choice. The core dependency contains all the
Context Propagation, Metrics and Tracing APIs, but does not include any instrumentation. Here is how it would look like
in your build:

{% include dependency-info.html module="kamon-cre" version=site.data.versions.latest.core prefix="plain" %}

The core dependency is available for Java 8+ and published for Scala 2.11, 2.12 and 2.13. If you are not familiar with
the Scala version suffix then just pick the greatest Scala version available.


### Include the Status Page

This is not really necessary, but it is really helpful to have the Status Page module installed so that the installation
can be verified and in general, have an idea of how is Kamon doing. To add the module, just add the dependency to your
build system:

{% include dependency-info.html module="kamon-status-page" prefix="manual-sp" version=site.data.versions.latest.core %}


### Initializing Kamon

The last step is to initialize Kamon, which is just about calling `Kamon.init()` as the very first thing when your main
method is invoked:

{% code_example %}
{%   language scala guides/install/sbt/src/main/scala/kamon/example/Start.scala tag:load-modules %}
{%   language kotlin guides/install/gradle/src/main/kotlin/kamon/example/Start.kt tag:load-modules %}
{%   language java guides/install/maven/src/main/java/kamon/example/Start.java tag:load-modules %}
{% endcode_example %}

The initialization process find all modules available on the classpath and initialize them, but since the bundle is not
available in the classpath it will not attach the instrumentation agent.



Verify the Installation
-----------------------

Next time you start your application, Kamon should start the Status Page module included in the previous step, which
provides a very convenient way to figure out whether everything is in place or not: just got to
<a href="http://localhost:5266/" target="_blank"><strong>localhost:5266</strong></a> on your browser and something like
this should show up:

<img class="img-fluid" src="/assets/img/kamon-status-page.png" alt="Kamon Status Page">

The important bit here is to ensure that modules are loaded and instrumentation is active. As you start adding more and
more metrics modules and custom telemetry to Kamon, you will probably be coming back to this page to verify that all is
working as expected.


Install Reporters
-----------------

At this point, the only thing you are missing is to install some reporters that will take the metrics and trace data
collected by Kamon into an external system. Adding a reporter is just about adding the appropriate dependency to your
build. For example, if you wanted to add the [Kamon APM reporter][apm-reporter] to your build, just add the appropriate dependency:

{% include dependency-info.html module="kamon-apm-reporter" version=site.data.versions.latest.apm prefix="reporter" %}

And that is all, Kamon will automatically pick up the module from the classpath and initialize it during startup! Head
over to the [Reporters Section][reporters] to see all available reporters, including the ones for [Prometheus][prometheus],
[Zipkin][zipkin], [InfluxDB][influxdb], [Datadog][datadog] and several more!


[get-started]: /get-started/
[reporters]: ../../../reporters/
[apm-reporter]: ../../../reporters/apm/
[prometheus]: ../../../reporters/prometheus/
[zipkin]: ../../../reporters/zipkin/
[influxdb]: ../../../reporters/influxdb/
[datadog]: ../../../reporters/datadog/