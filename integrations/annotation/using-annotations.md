---
title: kamon | Annotation | Documentation
layout: documentation
---
Annotation Module
=================

The Annotation module provides a set of annotations that allow you to easily integrate Kamon's metrics and tracing
facilities with your application.

<p class="alert alert-info">
The <b>kamon-annotation</b> module require you to start your application using the AspectJ Weaver Agent. Kamon will warn
you at startup if you failed to do so.
</p>


Enabling Annotation Support
---------------------------

Besides starting your application with the AspectJ Weaver Agent, there are two additional steps required to use
annotations. First you must add the `@EnableKamon` annotation to the classes you would like to be scanned for any of the
provided annotations, otherwise the rest of the annotations described bellow wont be able to work at all.

All the provided annotations are in the `kamon.annotation` package. Enabling annotation support for a class looks as
simple as this:

{% code_example %}
{%   language scala kamon-annotation-examples/boot-example/src/main/scala/kamon/annotation/examples/scala/ScalaOrdersController.scala tag:enable-kamon %}
{%   language java kamon-annotation-examples/boot-example/src/main/java/kamon/annotation/examples/java/JavaOrdersController.java tag:enable-kamon %}
{% endcode_example %}

In the example above, the controller classes are annotated with `@EnableKamon` to ensure that any method using Kamon
annotations, such as the `listOrders` method will work properly.

The second thing you need to do would be to add a minimal `aop.xml` file that will tell the AspectJ Weaver to actually scan your
project classes to apply annotations to it. Your file might look like this:

{% code_example %}
{%   language markup kamon-annotation-examples/boot-example/src/main/resources/META-INF/aop.xml tag:enable-kamon label:"aop.xml" %}
{% endcode_example %}

Try to keep the filters as specific as possible because all classes matching the provided include filters will be fully
scanned to detect if instrumentation should be applied. Having very broad filters (such as `**`) will significantly
increase the startup time as the AspectJ Weaver will have much more work to do on startup.

You might have recognized the other annotations available in the example as being part of the [Spring Framework], and
they certainly are. As you might know, we love Scala, but we are aware that using annotations in the Scala world is not
a common practice, whereas in the Java world it is very common to describe and extend application functionality via
annotations, thus, all the examples provided in this section are based on a simple [Spring Boot] example application
with controllers created using both Java and Scala.


Manipulating Traces and Segments
--------------------------------

Delimiting traces and segments are one of the most basic tasks you would want to perform to start monitoring your
application using Kamon, and the `@Trace` and `@Segment` annotations allow you to do just that:

* __@Trace__: when a method is marked with this annotation a new [Trace] will be started every time the method is called
and automatically finished once the method returns. Also, the generated `TraceContext` becomes the current context while
the method is executing, making it possible to propagate it at will.
* __@Segment__: when a method is marked with this annotation a new [Segment] will be created only if there is a current
`TraceContext` while the annotated method is called.

Let's see these two annotations in action:

{% code_example %}
{%   language scala kamon-annotation-examples/boot-example/src/main/scala/kamon/annotation/examples/scala/ScalaUsersController.scala tag:traces %}
{%   language java kamon-annotation-examples/boot-example/src/main/java/kamon/annotation/examples/java/JavaUsersController.java tag:traces %}
{% endcode_example %}

In the example above we are annotating the `listUsers` method with `@Trace`, so Kamon will automatically start a new
trace named `ListAllUsers` every time this method is called and finish it once the method returns.

Furthermore, the typical use case for creating segments would be to delimitate pieces of functionality that contribute
to servicing a request in your application. In the example bellow we are using the `@Segment` annotation to
automatically create a segment every time the `findUsers` method is called in the correspondent users service. Since the
`@Trace` annotation in the controller methods is starting a new trace automatically, it is guaranteed that there will be
a current `TraceContext` when the service method is called, and the newly created segment will be scoped to that trace.

{% code_example %}
{%   language scala kamon-annotation-examples/boot-example/src/main/scala/kamon/annotation/examples/scala/ScalaUsersService.scala tag:segments %}
{%   language java kamon-annotation-examples/boot-example/src/main/java/kamon/annotation/examples/java/JavaUsersService.java tag:segments %}
{% endcode_example %}



Manipulating Instruments
------------------------

Additionally to manipulating traces and segments, the `kamon-annotation` module provides annotations that can be used
to create Counters, Histograms and MinMaxCounters that are automatically updated as the annotated methods execute. These
annotations are:

* __@Time__: when a method is marked with this annotation Kamon will create a Histogram tracking the latency of each
invocation to the method. Please keep in mind that in most situations you might want to use `@Segment` if you are tracking
some functionality that is executed within a trace.

* __@Histogram__: when a method is marked with this annotation Kamon will create a Histogram that stores the values
returned every time the method is invoked. Obviously, only methods returning numeric values are accepted.

* __@Count__: when a method is marked with this annotation Kamon will be create a Counter and automatically increment it
every time the method is invoked.

* __@MinMaxCount__: when a method is marked with this annotation Kamon will be create a MinMaxCounter and automatically
increment it every time method is invoked and decremented when the method returns.



### EL Expression Support ###

The `name` and `tags` properties are evaluated as [EL] expressions for all annotations that manipulate instruments. This allows to
conveniently customize names and tags using the annotated class's members as shown bellow:

{% code_example %}
{%   language scala kamon-annotation-examples/src/main/scala/kamon/annotation/examples/Annotated.scala tag:el-support-instrument-name %}
{%   language java kamon-annotation-examples/src/main/java/kamon/annotation/examples/el/name/Annotated.java tag:el-support-instrument-name %}
{% endcode_example %}

In the above example, Kamon will create a Counter instrument with the name obtained from evaluating the expresion
`${'count:' += this.id}` at the moment of instantiation of the `Annotated` class. Please keep in mind that the provided
expressions are evaluated __only once__ when the annotated class is instantiated. If the value of the `id` attribute in
the example above changes over time it won't affect the registered counter.



Limitations
-----------

Annotations are not inherited, regardless of them being declared on a parent class or an implemented interface method.
The root causes of that limitation, according to the [JLS], are:

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
[Spring Framework]: http://projects.spring.io/spring-framework/
[Spring Boot]: http://projects.spring.io/spring-boot/
