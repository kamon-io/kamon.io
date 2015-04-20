---
title: kamon | Akka Toolkit | Documentation
layout: documentation
---

Akka Integration
================

Kamon's integration with Akka comes in the form of two modules: `kamon-akka` and `kamon-akka-remote` that bring bytecode
instrumentation to gather metrics and perform automatic `TraceContext` propagation on your behalf.

<p class="alert alert-info">
Both the <b>kamon-akka</b> and <b>kamon-akka-remote</b> modules require you to start your application using the AspectJ
Weaver Agent. Kamon will warn you at startup if you failed to do so.
</p>

Here is a quick list of the functionalities included on each module:


### kamon-akka ###

* __[Actor, Router and Dispatcher Metrics]__: This module hooks into Akka's heart to give you a robust set of metrics
based on the concepts already exposed by our metrics module.
* __[Automatic TraceContext Propagation]__: This allows you to implicitly propagate the `TraceContext` across actor messages
without having to change a single line of code and respecting the "follow the events" rather than "stick to the thread"
convention as described in the [event based threading model section].
* __[Ask Pattern Timeout Warning]__: A utility that logs a warning with additional information when a usage of the Ask
Pattern timesout.


### kamon-akka-remote ###

* __Remote TraceContext Propagation__: This bit of instrumentation allows basic `TraceContext` information to be
propagated across the remoting channel provided by Akka. This hooks in the low level remoting implementation that ships
with Akka, meaning that it will propagate the `TraceContext` when using plaing remoting or when using the Akka Cluster
as well.


[event based threading model section]: /core/tracing/threading-model-considerations/
[Ask Pattern Timeout Warning]: /integrations/akka/ask-pattern-timeout-warning/
[Actor, Router and Dispatcher Metrics]: /integrations/akka/actor-router-and-dispatcher-metrics/
[Automatic TraceContext Propagation]: /integrations/akka/automatic-trace-context-propagation/
