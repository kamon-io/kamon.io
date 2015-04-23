---
title: kamon | Annotation | Documentation
layout: documentation
---
Annotation Module
=================

The Annotation module contains a set of annotations that provides a simple way to integrate the Kamon [instruments] and also create [Traces] and [Segments] in our projects.

<p class="alert alert-info">
The <b>kamon-annotation</b> module require you to start your application using the AspectJ Weaver Agent. Kamon will warn you at startup if you failed to do so.
</p>

### The Annotations ###

Even though `Kamon` is based strongly in `Scala` we want to extend it to all `JVM` based languages, for that reason we think that using vanilla java annotations are the best option to achieve a smooth integration in all java world.

Kamon provides the following set of annotations:

* __@EnableKamon__: when a type is marked with this annotation, tells Kamon that this class must be scanned and instrumented.

* __@Time__: when a method is marked with this annotation will be create a `kamon.metric.instrument.Histogram` and each time the method is invoked, the latency of execution  will be recorded. 

* __@Histogram__: when a method is marked with this annotation will be create a `kamon.metric.instrument.Histogram` and each time the method is invoked the return value of the annotated method will be recorded. 

* __@Count__: when a method is marked with this annotation will be create a `kamon.metric.instrument.Counter` and each time the method is invoked, the counter will be incremented.

* __@MinMaxCount__: when a method is marked with this annotation will be create a `kamon.metric.instrument.MinMaxCounter` and each time the method is invoked, the counter will be decremented when the method returns, counting the current invocations of the annotated method.

* __@Trace__: when a method is marked with this annotation a new [Trace] will be created in each method invocation.

* __@Segment__: when a method is marked with this annotation a new [Segment] will be created only if in the moment of the method execution exists a running `TraceContext`.


### Activation ###

In order to point to kamon about a particular class, it must be annotated with __@EnableKamon__ annotation:

{% code_example %}
{%   language scala kamon-annotation-examples/scala/src/main/scala/kamon/annotation/Annotated.scala tag:activation %}
{%   language java kamon-annotation-examples/java/src/main/java/kamon/annotation/Annotated.java tag:activation %}
{% endcode_example %}

When the weaver find the __@EnableKamon__ annotation this mix the target class with a kamon specific type that contains the necessary structure to register and manage the corresponding [instruments] that will be eventually created based in the marked annotations in the declared methods.   

### Limitations ###
Like AspectJ follows the [JLS]:

<p class="alert alert-warning">
The <b>Kamon annotations</b> are not inherit whether these are declared on a super class or an implemented interface method.
</p>


[instruments]: /core/metrics/instruments/
[JLS]: http://docs.oracle.com/javase/specs/jls/se7/html/jls-9.html#jls-9.6
[Trace]: /core/tracing/trace-context-manipulation/
[Segment]: /core/tracing/trace-context-manipulation/
[Traces]: /core/tracing/trace-context-manipulation/
[Segments]: /core/tracing/trace-context-manipulation/
