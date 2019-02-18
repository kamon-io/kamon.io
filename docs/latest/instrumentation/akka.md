---
title: 'Akka Instrumentation | Kamon Documentation'
description: 'Automatically extract metrics, distributed traces and perform context propagation on Akka applications'
layout: docs
redirect_from:
  - /documentation/1.x/instrumentation/akka/
  - /documentation/0.6.x/kamon-akka/overview/
---

{% include toc.html %}

Akka Instrumentation Overview
=============================

Kamon ships instrumentation modules for Akka, each addressing a different aspect of the toolkit:


## Local Actor Systems

This module is the foundation of all Akka-related instrumentation, including:

* __[Metrics collection][metrics]__: Metrics for actor systems, actors, routers, dispatchers and actor groups. You must
  explicitly configure which components to track.
* __[Message Tracing][tracing]__: Generates Spans for actor messages. You must explicitly configure which actors to
  trace.
* __[Context Propagation][context]__: Make sure that Kamon's Context gets propagated across actor messages in the local
  actor system.
* __[Ask Pattern Timeout Warning][ask-pattern-warning]__: A utility that logs a warning with additional information when
  a usage of the Ask Pattern times out.

The following artifacts are published, pick the right one for your Akka version:

  * `kamon-akka-2.4` for Akka 2.4.20+
  * `kamon-akka-2.5` for Akka 2.5.8+

## Dependency For Local Actor Systems
{% include dependency-info.html module="kamon-akka-2.5" version="1.1.2" %}
{% include instrumentation-agent-notice.html %}



## Remoting and Cluster

Provides instrumentation for remoting channels in Akka. Since Akka cluster builds on top of remoting, the metrics and
context propagation features included with this module will also benefit cluster users.

* __[Remoting Metrics Collection][remoting-metrics]__: Metrics for message serialization/deserialization time and
  message sizes for all incoming and outgoing messages to remote actor systems.
* __[Context Propagation][context]__: Propagates Kamon's Context to remote actor systems. This is specially important
  for supporting distributed tracing.

The following artifacts are published, pick the right one for your Akka version:

  * `kamon-akka-remote-2.4` for Akka 2.4.20+
  * `kamon-akka-remote-2.5` for Akka 2.5.8+

## Dependency For Remoting and Cluster
{% include dependency-info.html module="kamon-akka-remote-2.5" version="1.1.0" %}
{% include instrumentation-agent-notice.html %}


[metrics]: ./metrics/
[context]: ./context-propagation/
[tracing]: ./tracing/
[ask-pattern-warning]: ./ask-pattern-timeout-warning/
[remoting-metrics]: ./metrics/#remoting-metrics
