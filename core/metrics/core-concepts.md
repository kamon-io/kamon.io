---
title: Kamon | Core | Documentation
layout: documentation
---

Metrics Module
==============

The Kamon Metrics module is responsible of controlling the registration of entities being tracked either by user code or
by instrumentation provided with other Kamon modules, as well as providing some necessary infrastructure around this
functionality like filtering, configuring instrument factories and dispatching metrics subscriptions to all interested
parties.

From a very general perspective, you can think of the Metrics module as a big map full of entities and instruments
associated with them, that collects all the available information and sends it to subscribers on a fixed interval, know
as a tick interval.


Entities
--------

In the early stages of our Metrics extension, we realized that the most common case when measuring the performance of a
given application is to have sets of metrics related to a single entity, and, many occurrences of the same set of
metrics as similar entities exist in the application. For example, if you are monitoring a thread pool you will want to
get the total number of threads, the active thread counts and the queue size (among other metrics, of course), and if
you have five thread pools in your application you will want to get the exact same metrics set for each thread pool. In
Kamon, that will translate to having five entities with different names but with the same category of `thread-pool`, all
sharing the same set of metrics.

The regular operation of the Metrics module consists of basically two stages, as described in this simple graph:

<img class="img-responsive" src="/assets/img/diagrams/metrics-module-overview.png">

On the left, we have the real time recording side of the process where Entities work as the keys in that "big map" that
we mentioned before, pointing to a entity recorder that contains all the metric instruments associated  with that given
entity. This is the mutable side of our Metrics infrastructure, where everything gets updated as your application
generates operates and its behavior is being measured. Upon every tick, the Metrics extension collects the metrics of
all available entity recorders and generates entity snapshots that represent all the values being recorded since the
last tick. This side is completely immutable and safely shareable, in fact, all the parties (usually backend modules)
interested in the same entity will receive the exact same instances of the given entity snapshots (immutability FTW :D).



### Creating your own Entity Recorder ###

Ideally, if you want to track a an entity with Kamon you will need to create your own `kamon.metric.EntityRecorder`
implementation and register it with the Kamon Metrics module, so that Kamon can collect and report it's data to all the
interested subscribers. Fortunately, Kamon offers a base class for facilitating the creation of entity recorders, here
is a brief example of creating a actor-like entity using the provided facilities (in fact, it's pretty much the same
as the actor entity recorder provided with the kamon-akka module):

{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/CreatingEntityRecorders.scala tag:creating-entity-recorders %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/CreatingEntityRecorders.java tag:creating-entity-recorders %}
{% endcode_example %}

The name provided when creating your histograms will be the same name included in the entity snapshots sent to the
subscribers interested in your entity. Additionally to the metric names, you can provide further configuration to the
instrument being created, like in this case where we are specifying that the numbers that we plan to store in the
`timeInMailbox` and `processingTime` metrics represent time in nanoseconds. If you don't provide any further
configuration then a set of default values will be used. Please read the Instrument Factory section for details on how
the instruments are configured. The `EntityRecoderFactory` definitions will only be required if you are doing managed
entity registration, as described in the next section.




### Registering your Entity Recorder ###

Once you created your Entity Recorder class, you need to let Kamon know about it so that it can be collected and flushed
on every tick. Kamon provides two approaches to registering entities with it:

  - Managed Registration: In this approach, you give Kamon the entity name and a implementation of
    `kamon.metric.EntityRecorderFactory` and Kamon will verify the registered filters for the given category, the
    existence of a Entity with the provided details, configure the relevant instruments and create the Entity Recorder
    if necessary. If the entity you are trying to register is already available then Kamon will retrieve it's recorder
    instead of creating a new one.
  - Manual Registration: In this approach you create the entity recorder yourself and you tell the Metrics module to
    include it in its metrics infrastructure. No filtering or instruments configuration is done. If the entity was
    already registered then the old entity recorder will be cleaned up and replaced with the provided entity recorder.

If you opt for using the managed registration approach you will need to create a `EntityRecorderFactory`, for which you
just need to provide a category name and a factory function that Kamon will call with an properly configured instrument
factory for the given category. The code would look like:

{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/CreatingEntityRecorders.scala tag:entity-registration %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/CreatingEntityRecorders.java tag:entity-registration %}
{% endcode_example %}

In the development of other Kamon modules we favor the managed registration option since it allows users to provide
filters via the configuration file, but it is up to you which option plays best with the problem you have at hand.



### Filtering Entities ###

All entities registered via managed registration will go through the configurable filters pre-registration step. The
concept of filtering is very simple: for a given category a set of includes and excludes patterns are read from the
configuration file and all entities that match at least one includes pattern and do not match any excludes patterns will
be accepted. Additionally, the `kamon.metric.track-unmatched-entities` setting decides whether to track or not the
entities that do not match any includes or excludes.


{% code_example %}
{%   language typesafeconfig kamon-core-examples/src/main/resources/application.conf tag:filters-configuration label:"application.conf" %}
{% endcode_example %}

with the example filters provided above, all the entities with category `trace` will be included, but no entities with
category `actor` and a name matching the `system/*` pattern will ever be tracked by Kamon, if registered via managed
registration.

The patterns matcher included in Kamon is a very simple implementation of GLOB patterns matcher that allows the
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
