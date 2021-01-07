---
title: 'Akka Metrics | Kamon Documentation'
layout: docs
redirect_from:
  - /documentation/0.6.x/kamon-akka/actor-router-and-dispatcher-metrics/
  - /documentation/1.x/instrumentation/akka/actor-system-metrics/
---

{% include toc.html %}

Actor System Metrics
====================

Kamon is able to gather metrics from all core Akka components: actor systems, actors, dispatchers, routers, remoting
channels and, one addition of ours, actor groups; all thanks to bytecode instrumentation shipping with Kamon modules.

{% alert info %}
You must start your application with the AspectJ Weaver agent if you want to collect any Akka metrics.
{% endalert %}


Base Metrics
------------

All actor systems will get the following metrics:

* __akka.system.dead-letters__ (Counter). Number of dead letters seen for an actor system. Tags:
  * __system__: Actor system name.

* __akka.system.unhandled-messages__ (Counter). Number of unhandled messages seen for an actor system. Tags:
  * __system__: Actor system name.

* __akka.system.active-actors__ (Range Sampler). Number of active actors running on the actor system, regardless
of them being tracked for metrics or not. Tags:
  * __system__: Actor system name.

* __akka.system.processed-messages__ (Counter). Number of processed messages in the actor system. Tags:
  * __system__: Actor system name.
  * __tracked__: Whether the count is for tracked (tracked=true) or non-tracked actors (tracked=false).


Filtered Metrics
----------------

Since your application might be creating thousands of different actors, routers or even several dispatchers, Kamon won't
simply catch them all but rather rely on filters to decide which components to track. Filters are configured in the
`kamon.util.filters` configuration key and these are the filters expected by this module:

* __akka.tracked-actor__: Decides which actors should be instrumented for metrics collection.
* __akka.tracked-router__: Decides which routers should be instrumented for metrics collection.
* __akka.tracked-dispatcher__: Decides which dispatcher should be instrumented for metrics collection.

Here is how this would look like in your configuration file:

{% code_example %}
{%   language typesafeconfig instrumentation/akka/src/main/resources/application.conf tag:akka-filters label:"application.conf" %}
{% endcode_example %}

In the example filters above, for an actor system named `my-app`, all system actors and the `user/worker-helper` actor
are excluded but the `user/job-manager` and all the `user/worker-*` actors should be tracked. With regards to
dispatchers, only the default dispatcher in the application's actor system and the `database-dispatcher` are tracked.
Finally, the `user/some-router` router is the only one to be tracked by Kamon.

Once your filters are all set, you will be getting these metrics:


### Actor Metrics

* __akka.actor.time-in-mailbox__ (Histogram): Tracks the time measured from the moment a message was enqueued into an
actor's mailbox until the moment it was dequeued for processing.
* __akka.actor.processing-time__ (Histogram): Tracks how long did it take for the actor to process every message.
* __akka.actor.mailbox-size__ (Range Sampler): Tracks the size of the actor's mailbox.
* __akka.actor.errors__ (Counter): Number of failures the actor has experienced.

All actor metrics will have the following tags:
* __path__: Actor's path in the system.
* __system__: Actor system name.
* __dispatcher__: Configured dispatcher for the actor.
* __class__: Actor's class.



### Router Metrics

* __akka.router.routing-time__ (Histogram): Tracks how long did it take to the router itself to decide which of it's
routees will process the message.
* __akka.router.time-in-mailbox__ (Histogram): Tracks the combined time measured from the moment a message was enqueued
into a routee's mailbox until the moment it was dequeued for processing. The measurements from all routees are combined
here.
* __akka.router.processing-time__ (Histogram): Tracks how long did it take for the routees to process incoming messages.
This histogram includes measurements from all routees.
* __akka.router.pending-messages__ (Range Sampler): Tracks how many messages are waiting to be processed across all
routees on the router.
* __akka.router.members__ (Range Sampler): Tracks the number of routees in a router.
* __akka.router.errors__ (Counter): Number of failures of the routees in the router.

All router metrics will have the following tags:
* __path__: Router's path in the system.
* __system__: Actor system name.
* __dispatcher__: Configured dispatcher for the router.

Please note that actor and router metrics are independent from each other and each actor will be tracked as either a
regular actor or a routee, but not both.

### Dispatcher Metrics

Dispatcher metrics rely on the [`kamon-executors`][1] module to capture metrics on the underlying executor service for
Akka dispatchers. The following metrics will be exposed:

* __executor.threads__ (Histogram). Samples the number of threads in the executor service. Tags:
  * __state__: Active (state=active) and total (state=total).

* __executor.tasks__ (Counter). Tracks the number of tasks processed by the executor service. Tags:
  * __state__: Submitted (state=submitted) and completed (state=completed).

* __executor.queue__ (Histogram). Samples the queue size for the executor service.

Additionally, all executor service metrics will also have the following tags:
* __name__: Dispatcher name.
* __actor-system__: Actor system name.
* __type__: Fork Join Pool (type=fjp) or Thread Pool Executor (type=tpe).


#### Additional Metrics for Thread Pool Executors

* __executor.pool__ (Gauge). Tracks several configuration settings for the executor service . Tags:
  * __setting=min__: Minimum pool size.
  * __setting=max__: Maximum pool size.
  * __setting=corePoolSize__: Core pool size.

#### Additional Metrics for Fork Join Pools

* __executor.pool__ (Gauge). Tracks several configuration settings for the executor service . Tags:
  * __setting=min__: Minimum pool size.
  * __setting=max__: Maximum pool size.
  * __setting=parallelism__: Parallelism.


Actor Group Metrics
-------------------

There are situations in which you don't want to track how a specific actor instance is behaving but rather, how a group
of similar actors are behaving. The typical use cases for actor groups include:

* Worker actors.
* Per-request actors.
* Short lived actors that perform support work for other bigger pieces of the system.

Configuring actor groups is a two step process:
1. Create a filter in `kamon.util.filters` that matches all actors you would like to be included in a given group. The
name of the filter is up to you, just make sure the name doesn't clash with any other predefined filter.
2. Include your filter name in the `kamon.akka.actor-groups` setting.

Here is an example of how to configure an actor group:

{% code_example %}
{%   language typesafeconfig instrumentation/akka/src/main/resources/application.conf tag:akka-actor-groups label:"application.conf" %}
{% endcode_example %}

All actor groups will get the following metrics:

* __akka.group.time-in-mailbox__ (Histogram): Tracks the time measured from the moment a message was enqueued into an
actor's mailbox until the moment it was dequeued for processing.
* __akka.group.processing-time__ (Histogram): Tracks how long did it take for the actor to process every message.
* __akka.group.pending-messages__ (Range Sampler): Tracks how many messages are waiting to be processed across of all
member actors. Previously was called `akka.group.mailbox-size`.
* __akka.group.members__ (Range Sampler): Tracks the number of group members.
* __akka.group.errors__ (Counter): Number of failures experienced by group members.

Additionally, all actor group metrics will have the following tags:
* __group__: Group name.
* __system__: Actor system name.


Remoting Metrics
----------------

If you are including the `kamon-akka-remote` module you will also get the following metrics:

* __akka.remote.message-size__ (Histogram). Tracks the size of incoming and outgoing messages. Tags:
  * __system__: Actor system name.
  * __direction__: Incoming (direction=in) or outgoing (direction=out).

* __akka.remote.serialization-time__ (Histogram). Tracks the time spent serializing and deserializing messages. Tags:
  * __system__: Actor system name.
  * __direction__: Incoming message deserialization (direction=in) or outgoing message serialization (direction=out).


[1]: ../../executors/
