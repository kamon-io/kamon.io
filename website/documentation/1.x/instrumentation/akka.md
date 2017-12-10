---
title: Kamon > Documentation > Instrumentation > Akka
layout: documentation-1.x
---

Akka Instrumentation
====================

<p class="alert alert-info">
You must start your application with the AspectJ Weaver agent when using any Akka module.
</p>

Kamon ships four instrumentation modules for Akka, each addressing a different aspect of the toolkit:


### kamon-akka ###

This module is the foundation of all Akka-related instrumentation, including:

* __[Metrics collection][1]__: Metrics for actor systems, actors, routers, dispatchers and actor groups. You must
explicitly configure which components to track.
* __[Message Tracing][3]__: Generates Spans for actor messages. You must explicitly configure which actors to trace.
* __[Context Propagation][2]__: Make sure that Kamon's Context gets propagated across actor messages in the local actor
system.
* __[Ask Pattern Timeout Warning][4]__: A utility that logs a warning with additional information when a usage of the Ask
Pattern times out.

The following artifacts are published, pick the right one for your Akka version:

  * `kamon-akka-2.3` for Akka 2.3.15
  * `kamon-akka-2.4` for Akka 2.4.20
  * `kamon-akka-2.5` for Akka 2.5.8

<p class="alert alert-warning">
This is the only module with Akka 2.3 support. If you need remoting and HTTP metrics please consider upgrading to the
latest supported Akka version.
</p>

### kamon-akka-remote ###

Provides instrumentation for remoting channels in Akka. Since Akka cluster builds on top of remoting, the metrics and
context propagation features included with this module will also benefit cluster users.

* __[Remoting Metrics Collection][5]__: Metrics for message serialization/deserialization time and message sizes for
all incoming and outgoing messages to remote actor systems.
* __[Context Propagation][2]__: Propagates Kamon's Context to remote actor systems. This is specially important for
supporting distributed tracing.

The following artifacts are published, pick the right one for your Akka version:

  * `kamon-akka-remote-2.4` for Akka 2.4.20
  * `kamon-akka-remote-2.5` for Akka 2.5.8


[1]: ./actor-system-metrics/
[2]: ./context-propagation/
[3]: ./message-tracing/
[4]: ./ask-pattern-timeout-warning/
[5]: ./actor-system-metrics/#remoting-metrics
