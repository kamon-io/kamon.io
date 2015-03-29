---
title: Kamon | Overview
layout: documentation
---

What is Kamon?
==============

Kamon is a reactive-friendly toolkit for monitoring applications that run on top of the Java Virtual Machine. We are
specially enthusiastic to applications built with the Typesafe Reactive Platform (using Scala, Akka, Spray and/or Play!)
but as long as you are on the JVM, Kamon can help get the monitoring information you need from your application.

You can integrate Kamon into your application by either:

* Using the provided APIs to manually create metric recording [instruments] and manipulate traces to measure whatever
you find interesting in your application, or

* Letting Kamon do the heavy work for you, using the provided bytecode instrumentation (via AspectJ) to introduce
metrics measurement and tracing code in several popular libraries and toolkits, allowing you to get immediate
visibility into your application's behavior without having to touch a single line of code, or

* Using a mix of both approaches, using the provided instrumentation to get some general information and the provided
APIs to enhance that information with domain specific metrics and traces.



What do I get Out of the Box?
-----------------------------

Kamon ships several modules for you to select depending on your needs. Some of the modules provide bytecode
instrumentation and thus, require you to use the AspectJ Load Time Weaver when running your application.


### kamon-core ###
The metric instruments and the tracing API is contained in this module, as well as the necessary infrastructure to
provide the metrics and traces subscriptions. All modules depend on kamon-core and, kamon-core doesn't require you to
use AspectJ. This module uses Akka for some internal tasks and relies on actor messages to communicate with the
subscribers to it's metrics and traces information but no instrumentation is needed to achieve that.


### kamon-scala {% requires_aspectj %} ###
Provides bytecode instrumentation for Scala and Scalaz Futures.


### kamon-akka {% requires_aspectj %} ###
Provides bytecode instrumentation to get metrics for Akka actors, routers and dispatchers as well as automatic
TraceContext propagation across actor messages (both system and user messages).


### kamon-akka-remote {% requires_aspectj %} ###
Provides additional bytecode instrumentation to propagate TraceContext across the remoting/cluster boundaries.


### kamon-statsd ###
Reports selected metrics information to StatsD.


### kamon-datadog ###
Reports selected metrics information to Datadog. Please note that even while the Datadog agent uses a protocol based on
StatsD, there are a few subtle differences that make it necessary to provide a separate reporter for it instead of using
the StatsD module.


### kamon-jdbc (experimental) {% requires_aspectj %} ###
Provides bytecode instrumentation to the JDBC API, providing some basic metrics about JDBC performance as well as
creating Database access segments if there is a TraceContext available.


### kamon-log-reporter ###
Simple metrics logger for quick feedback when you want to avoid setting up a external metrics backend.


### kamon-newrelic ###
Reports trace metrics data to [New Relic].


### kamon-play {% requires_aspectj %} ###
Provides bytecode instrumentation to achieve automatic TraceContext management for Play! applications.


### kamon-spray {% requires_aspectj %} ###
Provides bytecode instrumentation to achieve automatic TraceContext management for Spray applications.


### kamon-system-metrics ###
Reports CPU, memory, file system, network, load average and context switch system-wide metrics and JVM specific metrics
such as garbage collection, threads, heap memory, non-heap memory and class loading metrics.


[instruments]: /core/metrics/instruments/
[getting started]: /introduction/get-started/
[New Relic]: http://www.newrelic.com/
