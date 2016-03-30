---
title: kamon | Databases clients | Documentation
layout: documentation
---

Databases clients
===================

The different database modules (jdbc and elasticsearch) provide a series of metrics and the automatic creation of segments regarding the requests made to those
databases using bytecode instrumentation.

<p class="alert alert-info">
The <b>kamon-jdbc</b> and <b>kamon-elasticsearch</b> modules requires you to start your application using the AspectJ Weaver Agent. Kamon will warn you
at startup if you failed to do so.
</p>

### Metrics ###

The following metrics will be recorded:

* __reads__: a histogram that tracks the reads requests latency.
* __writes__: a histogram that tracks the writes requests latency.
* __slows__: a simple counter with the number of measured slow requests.
* __errors__: a simple counter with the number of failures.
