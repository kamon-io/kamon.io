---
title: kamon | Annotation | Documentation
layout: documentation
---
Annotation Module
=================

The Annotation module contains a set of annotations that provides a simple way to integrate the Kamon [instruments] and also create [Traces] and [Segments] in our projects.

<p class="alert alert-info">
The <b>Kamon-annotation</b> module require you to start your application using the AspectJ Weaver Agent. Kamon will warn you at startup if you failed to do so.
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

When the weaver finds the __@EnableKamon__  this inject `Java bytecode` into the target class in order to provide the necessary structure to register and manage the corresponding [instruments] that will be created based on the annotations of the declared methods.

###EL Expression Support###

Currently the `tags` property of the [instruments] is evaluated as [EL] expression for convenience and also we need take in account that the expression will be evaluated __only once__ at the creation of the instrument. 

{% code_example %}
{%   language scala kamon-annotation-examples/scala/src/main/scala/kamon/annotation/Annotated.scala tag:el-support %}
{%   language java kamon-annotation-examples/java/src/main/java/kamon/annotation/Annotated.java tag:el-support %}
{% endcode_example %}

Optionally, the instrument name can be resolved with an [EL] expression that evaluates to a `String`.

{% code_example %}
{%   language scala kamon-annotation-examples/scala/src/main/scala/kamon/annotation/Annotated.scala tag:el-support-instrument-name %}
{%   language java kamon-annotation-examples/java/src/main/java/kamon/annotation/Annotated.java tag:el-support-instrument-name %}
{% endcode_example %}

In the above example, Kamon will create a `kamon.metric.instrument.Counter` instance with the name obtained from the evaluation of the expresion `${'count:' += this.id}` in the moment of  instantiation of the `Annotated` class. If the value of __Id__ attribute changes over time, the the [EL] expresion won't be __re-evaluated__.

### Static Methods###

The execution of static methods also can be monitored using __Kamon annotations__.

{% code_example %}
{%   language scala kamon-annotation-examples/scala/src/main/scala/kamon/annotation/Annotated.scala tag:static-methods %}
{%   language java kamon-annotation-examples/java/src/main/java/kamon/annotation/Annotated.java tag:static-methods %}
{% endcode_example %}

In this example,  `Kamon` will instrument the `AnnotatedObject/AnnotatedStatic` class so that, when it's loaded, it can introduce the machinery for manage the kamon [instruments] and then, the first time it's invoked the method annotated with __@Count__  will be proceed to create and register a `kamon.metric.instrument.Counter` with the given name and then increment it. In the following method invocation, will get the counter already registered and will be incremented. 	

### Limitations ###

The __Kamon annotations__ are not inherited whether these are declared on a parent class or an implemented interface method. The root causes of that limitation, according to the [JLS], are:

* Non-type annotations are not inherited,
* Annotations on types are only inherited if they have the __@Inherited__ meta-annotation,
* Annotations on interfaces are not inherited irrespective to having the __@Inherited__ meta-annotation.


[instruments]: /core/metrics/instruments/
[JLS]: http://docs.oracle.com/javase/specs/jls/se7/html/jls-9.html#jls-9.6
[Trace]: /core/tracing/core-concepts/#the-tracecontext
[Segment]: /core/tracing/core-concepts/#trace-segments
[Traces]: /core/tracing/trace-context-manipulation/#creating-and-finishing-a-tracecontext
[Segments]: /core/tracing/trace-context-manipulation/#creating-and-finishing-segments
[Limitations]: #limitations
[EL]: https://jcp.org/en/jsr/detail?id=341
