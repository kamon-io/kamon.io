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
channels and, one addition of ours, actor groups. All thanks to bytecode instrumentation shipping with Kamon modules.

{% alert info %}
You must start your application with the Kanela agent if you want to collect any Akka metrics.
{% endalert %}


Base Metrics
------------

These metrics are collected for all actor systems, without any special configuration or filtering:

{%  include metric-detail.md name="akka.system.active-actors" %}
{%  include metric-detail.md name="akka.system.dead-letters" %}
{%  include metric-detail.md name="akka.system.unhandled-messages" %}
{%  include metric-detail.md name="akka.system.processed-messages" %}


Filtered Metrics
----------------

Since your application might be creating thousands of different actors, routers or even several dispatchers, Kamon won't
simply catch them all but rather rely on filters to decide which components to track. Filters are configured under the
`kamon.instrumentation.akka.filters` configuration path. Within this path, these filters are expected:

* __actors.track__: Decides which actors should be instrumented for dedicated metrics collection. This means that each
  actor matched by this filter will get its own set of Actor metrics.
* __routers__: Decides which routers should be instrumented for metrics collection.
* __dispatchers__: Decides which dispatcher should be instrumented for metrics collection.

Here is how this would look like in your configuration file:

{% code_example %}
{%   language typesafeconfig instrumentation/akka/src/main/resources/application.conf tag:akka-filters label:"application.conf" %}
{% endcode_example %}

In the example filters above, for an actor system named `my-app`, all system actors and the `user/worker-helper` actor
are excluded but the `user/job-manager` and all the `user/worker-*` actors should be tracked. With regards to
dispatchers, only the default dispatcher in the application's actor system and the `database-dispatcher` are tracked.
Finally, the `user/some-router` router is the only one to be tracked by Kamon.

### The Doomsday Wildcard

It might be very tempting to track all actors by adding `**` in the `actors.track` filter, which might be very useful
for testing purposes but is definitely a bad practice for running in production where thousands of tracked actors could
eat all your memory. To avoid this, Kamon does not allow using `**` on the `actors.track` filter, but you can disable
this safeguard by adding `actors.doomsday-wildcard = on` setting to your config. Think twice before doing it, since
most likely all you will need is to create actor groups, use the auto-grouping feature or target specific actors.


### Actor Metrics

These metrics are recorded for all tracked actors:

{%  include metric-detail.md name="akka.actor.processing-time" %}
{%  include metric-detail.md name="akka.actor.time-in-mailbox" %}
{%  include metric-detail.md name="akka.actor.mailbox-size" %}
{%  include metric-detail.md name="akka.actor.errors" %}


### Router Metrics

When a router is instrumented, metrics are recorded both from the router itself (measuring how long it takes to process
the routing logic) and from all the routees behind the router, recording the following metrics together:

{%  include metric-detail.md name="akka.router.routing-time" %}
{%  include metric-detail.md name="akka.router.processing-time" %}
{%  include metric-detail.md name="akka.router.time-in-mailbox" %}
{%  include metric-detail.md name="akka.router.pending-messages" %}
{%  include metric-detail.md name="akka.router.members" %}
{%  include metric-detail.md name="akka.router.errors" %}


### Dispatcher Metrics

The Dispatchers instrumentation relies on the [`kamon-executors`][1] module to capture metrics on the underlying executor
service for Akka dispatchers. The following metrics will be recorded:

{%  include metric-detail.md name="executor.threads.min" %}
{%  include metric-detail.md name="executor.threads.max" %}
{%  include metric-detail.md name="executor.parallelism" %}
{%  include metric-detail.md name="executor.threads.active" %}
{%  include metric-detail.md name="executor.threads.total" %}
{%  include metric-detail.md name="executor.tasks.completed" %}
{%  include metric-detail.md name="executor.tasks.submitted" %}
{%  include metric-detail.md name="executor.time-in-queue" %}
{%  include metric-detail.md name="executor.queue-size" %}

Besides the default tags added to all metrics be the Executors module, the Dispatchers instrumentation will add a `system`
tag with the name of the Actor System to which the dispatcher belongs.


Actor Groups
------------

There are situations in which you don't want to track how a specific actor instance is behaving but rather, how a group
of similar actors are behaving. The typical use cases for actor groups include:

* Worker actors.
* Per-request actors.
* Short lived actors that perform support work for other bigger pieces of the system.

### Auto-grouping

As part of the default instrumentation, Kamon will automatically create groups that contain all actors of the same type
at the same level in the Actor System tree. For example, in a system that has the following actors:

* `myApp/user/parent` with class `ParentActor`
* `myApp/user/parent/child-1` with class `ChildActor`
* `myApp/user/parent/child-2` with class `ChildActor`

The instrumentation will automatically create two actor groups: one for all the `ParentActor` instances in the first
level and another for all `ChildActor` instances in the second level. If you want to control which groups are eligible
for auto-grouping use the filters for the `auto-grouping` group.

Finally, take into account that auto-grouping will only pick up actors that are not being explicitly tracked in any
other way (e.g. the actor is not explicitly tracked nor included in an explicit group).


### Explicit Groups

Besides auto-grouping, you can explicitly create groups be defining filters under the `groups` section in the
configuration:

{% code_example %}
{%   language typesafeconfig instrumentation/akka/src/main/resources/application.conf tag:akka-actor-groups label:"application.conf" %}
{% endcode_example %}

The name of the group will be the name of the configuration (`worker-actors` in the example above) and the group will
include all actors matching the include/excludes patterns.


### Actor Group Metrics

Regardless of how the group was created (auto-grouping or explicitly) the following metrics are recorded for all
groups:

{%  include metric-detail.md name="akka.group.processing-time" %}
{%  include metric-detail.md name="akka.group.time-in-mailbox" %}
{%  include metric-detail.md name="akka.group.pending-messages" %}
{%  include metric-detail.md name="akka.group.members" %}
{%  include metric-detail.md name="akka.group.errors" %}


Remoting Metrics
----------------

If you are using Akka Remote or Akka Cluster, the instrumentation will also record these metrics for the remoting
infrastructure:

{%  include metric-detail.md name="akka.remote.messages.inbound.size" %}
{%  include metric-detail.md name="akka.remote.messages.outbound.size" %}
{%  include metric-detail.md name="akka.remote.serialization-time" %}
{%  include metric-detail.md name="akka.remote.deserialization-time" %}

[1]: ../../executors/
