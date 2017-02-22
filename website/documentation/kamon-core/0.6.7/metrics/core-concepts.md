---
title: Kamon | Core | Documentation
tree_title: Core Concepts
layout: documentation
---

Metrics Module
==============

The Kamon Metrics module is responsible of controlling the registration of entities being tracked either by user code or
by instrumentation provided with other Kamon modules, as well as providing some necessary infrastructure like filtering,
configuring instrument factories and dispatching metrics subscriptions to all interested parties.

From a very general perspective, you can think of the Metrics module as a big map full of entities and instruments
associated with them that collects all the available information and sends it to subscribers on a fixed interval, know
as a tick interval.


Entities
--------

In the early stages of our Metrics module, we realized that the most common case when measuring the performance of a
given application is to have sets of metrics related to a single entity, and, many occurrences of the same set of
metrics as similar entities exist in the application. For example, if you are monitoring a thread pool you will want to
get the total number of threads, the active thread counts and the queue size (among other metrics, of course), and if
you have five thread pools in your application you will want to get the exact same metrics set for each thread pool. In
Kamon, that will translate to having five entities with different names but with the same category of `thread-pool`, all
sharing the same set of metrics.

The regular operation of the Metrics module consists of basically two stages, as described in this simple diagram:

<img class="img-fluid" src="/assets/img/diagrams/metrics-module-overview.png">

On the left, we have the real time recording side of the process where Entities work as the keys in that "big map" that
we mentioned before, pointing to a entity recorder that contains all the metric instruments associated  with that given
entity. This is the mutable side of our Metrics infrastructure, where everything gets updated as your application
operates and its behavior is being measured. Upon every tick, the Metrics extension collects the metrics of all
available entity recorders and generates entity snapshots that represent all the values recorded since the last tick.
This side is completely immutable and safely shareable, in fact, all the parties (usually backend modules) interested in
the same entity will receive the exact same instances of the given entity snapshots (immutability FTW :D).

All entities have three identifying properties:

* a __name__, that should be unique among entities in the same category. In the thread pools monitoring case mentioned
above you might have a thread pool named "jdbc-pool" and another one named "netty-pool" and both will have their own,
separate entity recorders.

* a __category__, that should be shared among similar entities. Following the example, both "jdbc-pool" and "netty-pool"
have the same set of metrics (pool size, number of active threads, etc.) and should share the same category of
"thread-pool".

* __tags__, which are optional. The tags are a simple map of String keys to String values, used to provide dimensions
that encode additional information about the entity being tracked.

If you have two entities with the same name and category but different tags then they are effectively two different
entities in Kamon, each with its own, separate entity recorder. Typically, tags are used to provide additional
information about the entity being measured; the most common example found when talking about tags is that of trying to
measure how an application behaves when using algorithm "X" or algorithm "Y" to perform a certain task. You could simply
choose one of the algorithms randomly in your application and measure both of them with histograms named "some-task" and
have a tag named "algorithm" with value "X" or "Y" depending on which algorithm was selected. Within Kamon the tags wont
have a big effect, but if this information can be pushed to your metrics backend of choice it will greatly increase your
ability to slice and dice with the available metrics.



Built-in Entity Recorders
-------------------------

If you are coming from using another metrics library it is very likely that you are not used to the idea of having these
groups of metrics (entity recorders) but rather simple and direct instruments like histograms, counters, gauges and so
on. More so, sometimes you just want to measure a very simple characteristic of your application and grouping it with
some other metrics to create a new entity recorder just makes no sense; for these cases Kamon ships with entity
recorders that contain a single instrument (histogram, gauge, counter or min max counter) and are automatically managed
by the metrics module.

Effectively, you don't need to know that these entity recorders exists as they are a detail implementation within Kamon,
but we mention it to make clear the point that our metrics module works just with entities and entities have groups of
metrics which, as in this case, might have just a single metric inside. Please take a look at the [recording metrics]
section to learn how you can get a hold any of this recorders using the metrics module.



Defining your own Entity Recorder
---------------------------------

Basically, anything that implements `kamon.metric.EntityRecorder` can work as en entity recorder in Kamon and chances
are that you will want to create your own entity recorders and personalize the way you use Kamon accordingly to your
needs. For such purpose Kamon offers the `kamon.metric.GenericEntityRecorder` base class which facilitates the
definition of entity recorders, it is not required for you to use this base class but it is definitely the simplest way
to create a custom entity recorder class. Here is a brief example of creating a actor-like entity using the provided
facilities (in fact, it's pretty much the same as the actor entity recorder provided with the kamon-akka module):

{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/CreatingEntityRecorders.scala tag:creating-entity-recorders %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/CreatingEntityRecorders.java tag:creating-entity-recorders %}
{% endcode_example %}

The names provided when creating your histograms will be the same names included in the entity snapshots sent to the
subscribers interested in your entity. Additionally to the metric names, you can provide further configuration to the
instrument being created, like in this case where we are specifying that the numbers that we plan to store in the
`timeInMailbox` and `processingTime` metrics represent time in nanoseconds. If you don't provide any further
configuration then a set of default values will be used. Please read the Instrument Factory section for details on how
the instruments are configured.

Now, you might be asking yourself, what is that `EntityRecoderFactory` for? Well, with Kamon you will never create an
instance of the entity recorders yourself but rather ask Kamon to create for your and Kamon will do it only if it is
necessary. If you ask Kamon five times for the same entity then a new entity recorder will be created during the first
call and the same entity recorder will be returned in all subsequent calls. Additionally, Kamon will provide your custom
entity recorder with an instrument factory specifically configured for your entity's category, which allows you to later
customize parameters of it's instruments from the configuration file. See the instruments factory section bellow to
understand how that works and also the [recording metrics] section to learn how to use your custom entity recorder.



Filtering Entities
------------------

We provide instrumentation for several libraries that will automatically measure and record metrics for you, that's
nice, but we know that there is no win on blindly monitoring every entity that shows up in your application, that's why
we provide entity filters. The concept of filtering is very simple: for a given category a set of includes and excludes
patterns are read from the configuration file and all entities that match at least one include pattern and do not match
any exclude patterns will be accepted. Additionally, the `kamon.metric.track-unmatched-entities` setting decides whether
to track or not the entities that do not match any includes or excludes.

{% code_example %}
{%   language typesafeconfig kamon-core-examples/src/main/resources/application.conf tag:filters-configuration label:"application.conf" %}
{% endcode_example %}

As shown in the example configuration file above, all filters are defined under the
`kamon.metric.filters.$category_name` configuration key. With the example filters provided, all the entities with
category `akka-dispatcher` will be included, but no entities with category `akka-actor` and a name matching the
`my-app/system/**` pattern will ever be tracked by Kamon. To achieve this filtering behavior all of the modules provided
by Kamon that automatically register entities with the metric module will programatically call
`Kamon.metrics.shouldTrack(..)` to determine if the entity should be registered or not, and you can do that as well if
desired.

The pattern matcher included in Kamon is a very simple implementation of GLOB pattern matcher that allows the
following constructs:

  - `*` match any number of characters up to the next '/' character found in the entity name.
  - `?` match exactly one character, other than '/'.
  - `**` match any number of characters, regardless of any '/' character found after this wildcard.
  - exact entity name match if not wildcards are provided.



Instrument Factories
--------------------

As you have noticed from the previous section, constructing a entity recorder based on the `GenericEntityRecorder`
class provided by Kamon requires you to supply a `InstrumentFactory` which is responsible of creating the actual
histograms, min-max-counters, gauges or counters that you will include in your entity recorder. When doing managed
registration, Kamon will retrieve the correct instrument factory and supply it to your `.createRecorder(..)`
implementation whereas if you are doing manual registration you will need to find or create an instrument factory
yourself. When the Metrics module starts it will read the available configurations and generate a instrument factories
that follow this order of priorities when configuring a instrument (top wins):

  - Any configuration provided under the `kamon.metric.instrument-settings.$category.$metric-name` configuration key.
  - Any configuration provided by code when creating the entity recorder.
  - Default configurations for each instrument type in the `kamon.metric.default-instrument-settings` configuration key.

All three sources of settings will be merged to define the actual configuration to be used for any given instrument
created by Kamon.

For example, changing the refresh interval of the `mailbox-size` min-max-counter for actors, as defined in the example
above we would need to provide the following configuration:

{% code_example %}
{%   language typesafeconfig kamon-core-examples/src/main/resources/application.conf tag:custom-instrument-settings label:"application.conf" %}
{% endcode_example %}

Since no additional settings were provided by code or configuration for this category/metric combination, the dynamic
range settings will be the default values defined in the `kamon.metric.default-instrument-settings.min-max-counter`
configuration key.




[instruments]: /core/metrics/instruments/
[recording metrics]: /core/metrics/recording-metrics/
