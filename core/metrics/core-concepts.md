---
title: Kamon | Core | Documentation
layout: documentation
---

Metrics
=======

The Kamon Metrics module is responsible of controlling the registration of entities being tracked either by user code or
by instrumentation provided with other Kamon modules, as well as providing some infrastructure necessary around this
core functionality like filtering, instrument factories and dispatching metrics subscriptions to all interested parties.

From a very general perspective, you can think of the Metrics module as a big map full of entities and instruments
associated with them, that collects all the information and sends it to subscribers on a fixed interval, know as a tick
interval.


Entities
--------

In the early stages of our Metrics extension, we realized that the most common case when measuring the performance of a
given application is to have sets of metrics related to a single entity, and, many occurrences of the same set of
metrics as similar entities exist in the application. For example, if you are monitoring a thread pool you will want to
get the total number of threads, the active thread counts and the queue size (among other metrics, of course), and if
you have five thread pools in your app you will want to get the exact same metric set for each thread pool. In Kamon,
that will translate to having five entities with different names but with the same category of `thread-pool`.








Back in the day, the most common approach to get metrics out of an Akka/Spray application for production monitoring was
doing manual instrumentation: select your favorite metrics collection library, wrap you messages with useful metadata,
wrap your actor's receive function with metrics measuring code and, finally, push that metrics data out to somewhere you
can keep it, graph it and analyse it whenever you want.

Each metrics collection library has it's own strengths and weaknesses, and each developer has to choose wisely according
to the requirements they have in hand, leading them in different paths as they progress with their applications. Each
path has different implications with regards to introduced overhead and latency, metrics data accuracy and memory
consumption. Kamon takes this responsibility away from you and tries to make the best choice to provide high performance
metrics collection instruments while keeping the inherent overhead as low as possible.


Collection and Flushing
-----------------------

All the metrics infrastructure in Kamon was designed around two concepts: collection and flushing. Metrics collection
happens in real time, as soon as the information is available for being recorded. Let's see a simple example: as soon as
a actor finishes processing a message, Kamon knows the elapsed time for processing that specific message and it is
recorded right away. If you have millions of messages passing through your system, then millions of measurements will be
taken.

Flushing happens recurrently after a fixed amount of time has passed, a tick. Upon each tick, Kamon will collect all
measurements recorded since the last tick, flush the collected data and reset all the instruments to zero. Let's explore
a little bit more on how this two concepts are modeled inside Kamon.

<img class="img-responsive" src="/assets/img/diagrams/metric-collection-concepts.png">

A metric group contains various individual metrics that are related to the same entity, for example, if the entity we
are talking about is an actor, the metrics related to processing time, mailbox size and time in mailbox for that
specific actor are grouped inside a single metric group, and each monitored actor gets its own metric group. As you
might disguise from the diagram above, on the left we have the mutable side of the process that is constantly recoding
measurements as the events flow through your application and on the right we have the immutable side, containing
snapshots representing all the measurements taken during a specific period on time for a metric group.

Head over the [instruments] section to learn more about what instruments are used for recording metrics in Kamon.


Filtering Entities
------------------

By default Kamon will not include any entity for metrics collection and you will need to explicitly include all the
entities you are interested in, be it a actor, a trace, a dispatcher or any other entity monitored by Kamon. The
`kamon.metrics.filters` key on your application's configuration controls which entities must be included/excluded from
the metrics collection infrastructure. Includes and excludes are provided as lists of strings containing the
corresponding GLOB patterns for each group, and the logic behind is simple: include everything that matches at least one
`includes` pattern and does not match any of the `excludes` patterns. The following configuration file sample includes
the `user/job-manager` actor and all the worker actors, but leaves out all system actors and the `user/worker-helper`
actor.

{% code_example %}
{%   language typesafeconfig kamon-core-examples/src/main/resources/application.conf label:"application.conf" %}
{% endcode_example %}

[instruments]: /core/metrics/instruments/
