---
title: 'Play Framework Application Performance Monitoring | Kamon Documentation'
description: 'A guide on how to start collecting metrics and traces from Play Framework Application.'
layout: docs
---

{% include toc.html %}

Installation on the Play Framework
==================================

This guide is an "extended" version of the official [Get Started][get-started] steps for a Play Framework app, but here
we dive a little bit more into what is being achieved in each step. The general idea stays the same as in the official
guide, there are three simple steps to follow:

  1. [Install Kamon](#install-kamon).
  2. [Verify the Installation](#verify-the-installation).
  2. [Install Reporters](#install-reporters).


Install Kamon
-------------

### Add SBT plugin

We created a dedicated SBT plugin for users running Play Framework applications, so that you can simply hit `run` from
your SBT console and the instrumentation will just work! To install the `sbt-kanela-runner` plugin for Play, just add
this to your `project/plugins.sbt` file:

{% include kamon-play-plugin.md version="latest" %}

The plugin is published for Play Framework 2.6 and 2.7, make sure you get the suffix right!


### Add the Bundle Dependency

Add the `kamon-bundle` dependency using your build system of choice. The bundle contains **all** the
instrumentation available in Kamon, it even includes Kanela! (our instrumentation agent). Here is how it would look like
in your build:

{% include kamon-play-dependency.md version="latest" %}

Enabling `JavaAgent` plugin in your Play project will make sure all instrumentation is included and attached in your
distribution or running in Production Mode. Depending on your build the details might be a bit different but it is just
about calling `.enablePlugin(JavaAgent)` on the right project instance in your `build.sbt`

After this, whenever you hit
`run` the instrumentation will be applied as expected when running on development mode.

The bundle is available for Java 8+ and published for Scala 2.11, 2.12 and 2.13. If you are not familiar with the Scala
version suffix then just pick the greatest Scala version available.


And that is it. The bundle comes with a Guice module that will automatically initialize Kamon when the Application
Loader is gets called.

{% alert warning %}
If you are using any other type of dependency injection, please make sure that your custom Application Loader performs
the very same actions that the <a href="https://github.com/kamon-io/kamon-play/blob/master/kamon-play/src/main/scala/kamon/instrumentation/play/GuiceModule.scala" target="_blank">Kamon GuiceModule</a> does.
{% endalert %}


Verify the Installation
-----------------------

Next time you start your application, Kamon should start the Status Page module included in the bundle, which provides a
very convenient way to figure out whether everything is in place or not: just got to
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
