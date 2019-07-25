---
title: 'Akka Instrumentation | Kamon Documentation'
description: 'Automatically extract metrics, distributed traces and perform context propagation on Akka applications'
layout: docs
redirect_from:
  - /documentation/1.x/instrumentation/akka/
  - /documentation/0.6.x/kamon-akka/overview/
---

{% include toc.html %}

Akka Instrumentation
====================

Overview
--------

Kamon supports instrumenting Akka 2.4 and Akka 2.5 and bring all of these features out of the box:

* __[Metrics collection][metrics]__: Metrics for actor systems, actors, routers, dispatchers and actor groups, as well
  as remoting/cluster components of the actor system.
* __[Message Tracing][tracing]__: The instrumentation can automatically generate spans out of messages processed by
  selected actors in the system.
* __[Context Propagation][context]__: Ensure that  Context gets propagated across actor messages sent to both local and
  remote actors.
* __[Ask Pattern Timeout Warning][ask-pattern-warning]__: An utility that logs a warning with additional information when
  a usage of the Ask Pattern times out.


Manual Installation
-------------------

In case you are not using the Kamon Bundle, add the dependency bellow to your build to instrument Akka 2.4 and Akka 2.5
applications.

{% include dependency-info.html module="kamon-akka" version=site.data.versions.latest.akka %}
{% include instrumentation-agent-notice.html %}


[metrics]: ./metrics/
[context]: ./context-propagation/
[tracing]: ./tracing/
[ask-pattern-warning]: ./ask-pattern-timeout-warning/
[remoting-metrics]: ./metrics/#remoting-metrics
