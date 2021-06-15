---
layout: docs
noindex: true
title: 'Kamon Core Overview | Kamon Documentation'
description: 'Learn about the fundamental building blocks of Kamon'
redirect_from:
  - /documentation/0.6.x/kamon-core/overview/
  - /documentation/1.x/core/basics/overview/
---

{% include toc.html %}

Overview
========

Kamon is an instrumentation toolkit for applications running on the JVM. With Kamon you can record metrics, trace
requests and propagate context across distributed systems without locking your service to a specific metrics or tracing
vendor.

From a bird's eye view, Kamon can be decomposed in three main components: the core APIs for metrics, tracing and context
propagation; the automatic instrumentation modules and the reporting modules. Your services' code will only ever
interact with Kamon's APIs and abstract you away from how and where the collected data will end up going to.


<img class="img-fluid mx-auto d-block" src="/assets/img/kamon-animation.svg">

## Core APIs

The `kamon-core` project contains the basic building blocks upon which every other module is built and the only API
surface that you need to understand and interact with if using Kamon directly (see automatic instrumentation). The core
module can be divided into three main sections:
  - The **Metrics API** provides several instrument types (counters, gauges, histograms, timers and range samplers) with
    a dimensional data model for metrics recording.
  - The **Tracing API** allows to create, correlate and tag Spans that represent operations executed across distributed
    systems.
  - The **Context API** which provides means of defining arbitrary pieces of request-specific data that should be
    propagated across threads and processes.


## Automatic Instrumentation

There exists a growing number of automatic instrumentation modules that hook into the internals of several widely used
frameworks and libraries to bring metrics, traces and context propagation without the need to use the Core APIs
directly. Some examples are:
  - The `kamon-akka` and `kamon-akka-remote` modules provides actor, router, dispatcher and actor group metrics,
    distributed message tracing and context propagation both locally and across a cluster.
  - The `kamon-akka-http` and `kamon-play` modules provide automatic context propagation and distributed tracing on both
    the server and client sides of Akka HTTP and Play Framework, respectively.
  - The `kamon-jdbc` module hooks into all JDBC calls, creating Spans for each operation and tracking Hikari CP metrics
    when possible.

When using these modules it is necessary for applications to run with the [bytecode instrumentation agent][agent].

## Reporters

Finally, the reporter modules send and/or expose all the collected data to several monitoring systems. There are two
types of reporting modules:
  - **Metric Reporters** get all the measurements recorded during each "tick" and expose/send this data to systems like
    Prometheus, JMX, Kamon APM, Datadog, etc.
  - **Span Reporters** get batches of Spans to be sent to distributed tracing systems like Zipkin, Jaeger and so on.

Creating new reporters is just about implementing the appropriate interfaces and calling `Kamon.addReporter(...)` when
starting your services.


[agent]: ../guides/setting-up-the-agent/
