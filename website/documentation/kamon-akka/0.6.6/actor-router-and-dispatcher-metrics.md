---
title: Kamon | Documentation
tree_title: Actor, Router and Dispatcher Metrics
tree_index: 1
layout: module-documentation
---

Actor, Router and Dispatcher Metrics
====================================

Kamon is able to gather metrics from the actors, dispatchers and routers that you create in your application by joining
bytecode instrumentation that melds into Akka's internals with our metrics module.

Since your application might be creating thousands of different actors, routers or even several dispatchers, Kamon wont
simply catch them all but rather rely on the [entity filters] described in the metrics section for you to decide which
actors, routers and dispatchers should be measured and which don't. The supported categories are prefixed with `akka-`
and the name patterns must include the actor system name as it's first component.

{% code_example %}
{%   language typesafeconfig kamon-akka-examples/src/main/resources/application.conf tag:akka-filters label:"application.conf" %}
{% endcode_example %}

In the example filters above, for an actor system named `my-app`, all system actors and the `user/worker-helper` actor
are excluded but the `user/job-manager` and all the `user/worker-*` actors should be tracked. With regards to
dispatchers, only the default dispatcher in the application's actor system and the `database-dispatcher` are tracked.
Finally, the `user/some-router` router is the only one to be tracked by Kamon.

Once one of these entities is accepted by the configured filters, the correspondent entity recorder will be registered
and work as any other entity recorded in the metrics module, meaning that you can also configure the instruments via the
configuration file or subscribe to it's metrics data using the provided categories.

Now let's dig into the specific metrics provided for each of these entities.



Actor Metrics
-------------

When an actor is tracked, the following metrics will be recorded:

* __time-in-mailbox__: a histogram that tracks the time measured from the moment a message was enqueued into an actor's
mailbox until the moment it was dequeued for processing.
* __processing-time__: a histogram that tracks how long did it take for the actor to process every message.
* __mailbox-size__: a min max counter that tracks the size of the actor's mailbox.
* __errors__: a simple counter with the number of failures the actor has experienced.



Router Metrics
--------------

For each tracked router you will get:

* __routing-time__: a histogram that tracks how long did it take to the router itself to decide which of it's routees
will process the message.
* __time-in-mailbox__: a histogram that tracks the combined time measured from the moment a message was enqueued into a
routee's mailbox until the moment it was dequeued for processing. The measurements from all routees are combined here.
* __processing-time__: a histogram that tracks how long did it take for each of the routees to process incoming messages.
* __errors__: a simple counter with the number of failures of the routees in the router.

Please note that actor and router metrics are independent from each other, you could be tracking a router without having
to track each of the routees independently, or, you could track a individual actor that is part of a router, without
having to track the router itself. Or you could do both router and routees, if you want.



Dispatcher Metrics
------------------

The metrics provided for each dispatcher will vary depending on the type of dispatcher at hand. To know for sure what
kind of dispatcher you are looking at, we will always include a tag named `dispatcher-type` whose value will always be
present and be either `fork-join-pool` for `thread-pool-executor`, matching the type of dispatcher that you actually
created.


### Fork Join Pool Dispatchers ###

When your dispatchers are `fork-join-pool` dispatchers, you will get:

* __parallelism__: a min max counter with the desired parallelism value. This value will remain steady over time.
* __pool-size__: a gauge tracking the number of worker threads that have started but not yet terminated. This value will
differ that of `parallelism` if threads are created to maintain parallelism when others are cooperatively blocked.
* __active-threads__: a gauge tracking an estimate of the number of threads that are currently stealing or executing
tasks.
* __running-threads__: a gauge tracking an estimate of the number of worker threads that are not blocked waiting to join
tasks or for other managed synchronization.
* __queued-task-count__: a gauge tracking  the total number of tasks currently held in queues by worker threads (but not
including tasks submitted to the pool that have not begun executing). This value is only an approximation, obtained by
iterating across all threads in the pool.


### Thread Pool Executor Dispatchers ###

On the other hand, when your dispatcher type is `thread-pool-executor` you will get:

* __core-pool-size__: a gauge tracking the core pool size of the executor.
* __max-pool-size__: a gauge tracking the maximum number of threads allowed by the executor.
* __pool-size__: a gauge tracking the current number of threads in the pool.
* __active-threads__: a gauge tracking the number of threads that are actively executing tasks.
* __processed-tasks__: a gauge tracking the number of processed tasks for the executor. Please not that even while the
`ThreadPoolExecutor` provides us with the total number of tasks ever processed by the executor, this metrics is effectively
tracking the number of tasks as a differential from the last recorded value.



[entity filters]: /documentation/kamon-core/0.6.6/metrics/core-concepts/
