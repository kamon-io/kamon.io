---
title: 'Migrating from 1.x to 2.0 | Kamon Documentation'
layout: docs
---

Migrating from Kamon 1.x to 2.0
===============================

Most of the work put into Kamon `2.0` has been geared towards having cleaner, easier to use APIs and instrumentation
mechanisms and some of those improvements resulted in breaking changes that we are enumerating bellow. The amount of
effort needed to upgrade can vary based on whether you were just using plain Kamon to gather standard metrics or you
were actively using the APIs to manage context and create your own metrics and traces, but in general this should not be
a big effort and you are like to remove lines rather than add.

There is no step by step guide to migrate. Our suggestion would be to read every item and apply the changes if needed.
If there is something preventing you from upgrade and not mentioned here, please drop a line on [Gitter][gitter] and
we'll give you a hand and update this guide accordingly.


### There is a new Kamon.init() method

The `Kamon.init()` method takes care of a few common tasks performed during initialization:
  - It will try to attach the instrumentation agent to the current JVM if you have the bundle dependency (more on that
    bellow).
  - It will scan your classpath for modules and automatically start them.
  - It can optionally take a new `Config` instance to be used by Kamon.

If you were manually calling `Kamon.loadReportersFromConfig()` and/or `Kamon.addReporter(...)`, you don't need to do so
anymore.

A single call to `Kamon.init()` as the first line in your `main` method should be enough to set everything up!


### We moved from AspectJ to Kanela

As of this release, we are only shipping instrumentation created using our brand new instrumentation agent: [Kanela][kanela],
which is the result of many months of hard work towards simplifying how instrumentation is created and applied in
runtime. Kanela used [ByteBuddy][bytebuddy] and is Open Source as well, feel free to dig into the code and help us make
it better!


### The New Kamon Bundle

There is a new package we are distributing called `kamon-bundle`. The bundle, as you might imagine, bundles all
instrumentation modules available in Kamon and the Kanela agent and it becomes the preferred way of installing Kamon in
any application. The bundle:
  - Only has a dependency on kamon-core.
  - Will automatically attach the agent when `Kamon.init()` is called.
  - Helps you stay up to date since the only dependency you will ever need to update is the bundle dependency.

We are still releasing and publishing all modules as independent libraries in case you want to continue adding them as
in the previous Kamon versions.

The way to register kamon reporters has changed:

{% code_block scala %}

// Kamon 1.x
Kamon.addReporter(reporter);

// Kamon 2.0
Kamon.registerModule("reporter name", reporter);
{% endcode_block %}


### Metrics are now Tagged instead of Refined

The `refine` method has been renamed to `withTag`, which return a new instrument with the specified tags. This also
allows for chaining calls to `withTag` and the parent tags will be preserved.

Also, it was possible to call instrument actions directly on a metric (see the example bellow) which would result in
recording values on a instrument without any tags. In order to keep the separation between a metric and its instruments
as clearly defined as possible, those APIs are no longer available and if you were doing this, you will need to
explicitly call `withoutTags` to get the instrument without tags:

{% code_block scala %}

val counter = Kamon.counter("my-counter")

// Kamon 1.x
counter.increment()
counter.refine("zone", "east").increment()

// Kamon 2.0
counter.withoutTags().increment()
counter.withTag("zone", "east").increment()
{% endcode_block %}

#### Metrics changes

Gauges changed as show bellow:

{% code_block scala %}
// Kamon 1.x
gauge.set(value)

// Kamon 2.0
gauge.update(value)
{% endcode_block %}


### Context Tags

One super common pattern we see with most of our users is the need to propagate a few bits of information across all
microservices and we set had the "broadcast strings" feature for that, but it wasn't as simple to use as we would like
it to be, specially because it was mandatory to add explicit configuration and mappings for it to work properly.

To replace that feature, we introduced Context Tags, which are simple key/value pairs built on top of the very same
abstraction that we use for instrument and span tags, but attached to the Context. With this release, the Context can
hold both entries and tags, and since tags are made out of known types (String, Long or Boolean) automatically propagate
them without additional intervention across HTTP and Binary propagation channels.

Context instances are immutable and you can create a new Context that includes or overrides certain tag using the
`withTag` function as show bellow:

{% code_block scala %}

val context = Context.Empty
  .withTag("zone", "east")
  .withTag("shard", 42)
  .withTag("has-gpu", true)

{% endcode_block %}

Remember though, creating a Context has nothing to do with making it current or propagating it, make sure you use the
appropriate functions for that (see more bellow).

### Tags and metrics names
You might notice that some metrics names have changed. A prime example is [kamon-system-metrics](kamon-system-metrics).
The reason why was that our naming approach was flawed. In Kamon 1.x was the first time we started using tags in all our integrations and we just went sort of crazy with it, more than we should have. When moving to 2.x we tried to make sure that the definition of "what" is being measured is completely encoded in the metric name and the definition of "from where" is being measured is completely encoded in the tags.

There are a few things that helped us test whether the metric/tag were right.. one of them was asking ourselves: if we aggregated all timeseries of the same metrics, would the resulting timeseries be completely useless? In cases like host.cpu where the same metric had different tags for free and used it was obviously a bad choice since even though the two measurements come from the same thing, their meanings are really different. Same goes with jvm.memory and pretty much anything where we had something and the opposite of that something encoded in tags of the same metric.

Rest assured that we really don't want to make any more breaking changes like that.. it was just a necessary pain to have a consistent behavior.

### In-Process Context Propagation Changes

All the functions used to temporarily store a Context in the current Thread changed their name prefix from `with` to
`runWith`. For example, `Kamon.withSpan` became `Kamon.runWithSpan` and so on. The semantics are exactly the same, just
the names changed.


### Filter Changes

Starting in this release, we will no longer host filters under the `kamon.util.filters` configuration path. Instead,
each module requiring filter will define its own paths for them, which will make them easier to manage. The structure of
filters remain exactly the same, though, so you can keep your includes/excludes, just need to move them to the right
place.

The Akka instrumentation uses filters heavily and all its filters have been moved to a new location under the Akka path.
The new filter paths are:
  - `kamon.instrumentation.akka.filters.actors.track` controls which actors will get dedicated metrics.
  - `kamon.instrumentation.akka.filters.actors.trace` controls which actors will participate in traces that have
    already been started in the application (i.e. just generate Spans for the messages they process).
  - `kamon.instrumentation.akka.filters.actors.start-trace` controls which actors will participate in traces and
    optionally start new traces if none is available when they start processing their messages.
  - `kamon.instrumentation.akka.filters.routers` controls which routers will get dedicated metrics.
  - `kamon.instrumentation.akka.filters.dispatchers` controls which dispatchers will get dedicated metrics.
  - `kamon.instrumentation.akka.filters.groups` controls the actor grouping mechanism.

You can read more about the filters and their effects on the [Akka Instrumentation][akka] section of the documentation.



[gitter]: https://gitter.im/kamon-io/kamon
[kanela]: https://github.com/kamon-io/kanela
[bytebuddy]: http://bytebuddy.net
[akka]: ../../../instrumentation/akka/metrics/
[kamon-system-metrics]: https://github.com/kamon-io/kamon-system-metrics
