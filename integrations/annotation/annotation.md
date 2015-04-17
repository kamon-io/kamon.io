---
title: kamon | Annotation | Documentation
layout: documentation
---
Annotation Module
=================
The Annotation module contains a set of annotations that provides a simple way to integrate the Kamon [instruments] and also start `Traces` and `Segments` in our projects.

The Annotations
---------------
Even though `Kamon` is based strongly in `Scala` we want to extend it to all `JVM` based languages, for that reason we think that using vanilla java annotations are the best option to achieve a smooth integration in all java world.

The currently supported annotations are the following:

* `@Time` when a method is marked with this annotation will be create a `kamon.metric.instrument.Histogram` and each time the method is invoked, the latency of execution  will be recorded. 

* `@Histogram` when a method is marked with this annotation will be create a `kamon.metric.instrument.Histogram` and each time the method is invoked the return value of the annotated method will be recorded. 

* `@Count` when a method is marked with this annotation will be create a `kamon.metric.instrument.Counter` and each time the method is invoked, the counter will be incremented.

* `@MinMaxCount` when a method is marked with this annotation will be create a `kamon.metric.instrument.MinMaxCounter` and each time the method is invoked, the counter will be decremented when the method returns, counting the current invocations of the annotated method.


* `@Trace`

* `@Segment`


The Activation
---------------





[instruments]: /core/metrics/instruments/
