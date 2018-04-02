---
title: Kamon | Core | Documentation
layout: documentation-0.6.x
---

Recording Metrics
=================

Recording metrics is dead simple, all you need to do is ask the metrics module for the entity recorder you are
interested in and start using it to measure your application's behavior. If you request the same entity several times
then you will get back the exact same recorder instance on every call, only the first call will trigger the
instantiation of the necessary entity recorder.



Using Simple Recorders
----------------------

As mentioned in the [core concepts] section, Kamon provides a few built-in entity recorders for single metric entities
that can be used by simply requesting a histogram, counter, gauge or min max counter to the metrics module as show in
this example:

{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/RecordingMetrics.scala tag:simple-metrics %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/RecordingMetrics.java tag:simple-metrics %}
{% endcode_example %}

When using these simple entity recorders you will not get back an instance of the internal `EntityRecorder`
implementation but rather the plain instrument that you requested. Kamon still has that single instrument wrapped in a
`EntityRecorder` internally, but that is just an implementation detail.

The `.histogram(..)`, `.counter(..)`, `.gauge(..)` and `.minMaxCounter(..)` methods come with several overloads that
allow you to provide custom settings and tags for the requested instruments, please feel free to navigate through them
and get familiar with the available options.



Using Entity Recorders
----------------------

The API for using entity recorders is similar to that of simple recorders: you ask for an entity and you get back an
entity recorder, the only additional requirement is to provide a implementation of
`kamon.metric.EntityRecorderFactory<T>` where `T` is the type of the entity recorder you want. The reason for this
requirement is that Kamon will only instantiate your recorder once and only if it has not been already instantiated,
this can only be achieved if you tell Kamon how to create the desired entity recorder.

{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/RecordingMetrics.scala tag:entity-metrics %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/RecordingMetrics.java tag:entity-metrics %}
{% endcode_example %}



Removing Recorders
------------------

Typically the entities that you want to monitor will be around during the entire lifetime of your application and you
rarely will want to manually remove a recorder from Kamon but, in case you need to do so the metrics module provides
several remove methods for both simple recorders and entities. Remember that all entities are identified by three main
attributes: a name, a category and tags; remember to provide the same tags that you used when registered the desired
entity to ensure that the correct one is found and removed. Here is a quick example of removing the recorders created in
the previous examples of this page:

{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/RecordingMetrics.scala tag:cleanup %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/RecordingMetrics.java tag:cleanup %}
{% endcode_example %}




[core concepts]: ../core-concepts/
