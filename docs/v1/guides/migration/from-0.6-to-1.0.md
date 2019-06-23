---
title: 'Migrating from 0.6 to 1.0 | Kamon Documentation'
layout: docs
redirect_from:
  - /documentation/1.x/recipes/migrating-from-kamon-0.6/
---

Migrating from Kamon 0.6
========================

A lot has changed since `0.6` and you will be required to do a few changes to your configuration and initialization code
in order to jump on Kamon `1.0`. The amount of work can vary based on whether you were just using plain Kamon to
gather standard metrics or you were actively using the APIs to manage context and record metrics.

There is no step by step guide to migrate. Our suggestion would be to read every item and apply the changes if needed.
If there is something preventing you from upgrade and not mentioned here, please drop a line on [Gitter][1] and we'll
give you a hand and update this guide accordingly.


### No more Kamon.start(...)

The only thing you actually need to do when your application starts is add the reporters you would like to use. This can
be done in a couple ways:

{% code_block scala %}
Kamon.loadReportersFromConfig()
Kamon.addReporter(new PrometheusReporter())
Kamon.addReporter(new Zipkin())
{% endcode_block bash %}

When loading reporters from configuration you will need to set the FQCN of all desired reporters in the `kamon.reporters`
configuration key.

If you were using `Kamon.start(...)` with a custom configuration object you will now have to call `Kamon.reconfigure(config)`
instead, this will propagate the new configuration to all internal components. Ideally you should do this as the
first thing during application startup.

### Access to the Metrics and Tracing API

Everything you need to gather metrics and traces is on the Kamon companion object (or Kamon static members if you are in
the Java world):

{% code_block scala %}
// In 0.6.x you would write:
Kamon.metrics.counter(...)

// now it becomes just
Kamon.counter(...)
Kamon.histogram(...)
Kamon.buildSpan(...)

{% endcode_block bash %}


### Metric Entity is No More

We switched from having grouped instruments in a `Entity` to just having metrics of a given instrument type and tags
that refine it to specific dimensions. This was motivated to the fact that even though logically we should always track
a group of metrics together (e.g. processing time, errors and mailbox size for a specific actor), we would always need
to split these into individual metrics because not a single metrics platform supported this concept of grouped metrics.

Kamon followed the trend and moved to a simpler model that at its core has just a metric name and a set of tags that
uniquely identify each metric. Metrics returned by the `Kamon` companion object don't have any tags associated to
them and you will need to `.refine(...)` them if you want to get a incarnation with a specific set of tags:

{% code_block scala %}
val myCustomMetric = Kamon.histogram("my.custom.metric", time.nanoseconds)

// If you don't need tags, simply record on the plain metric:
myCustomMetric.record(42)

// Metrics can be refined to get specific dimensions:
val customMetricInProd = myCustomMetric.refine("env" -> "prod")
customMetricInProd.record(42)

// then removing the metric when it's not needed anymore:
myCustomMetric.remove("env" -> "prod")
{% endcode_block bash %}

Typically you will have the definition of these base metrics in a static initialization code and then refine it from
there as you need them.

There is one thing that doesn't have a direct counterpart in the new version: the lifecycle of all related metrics was
tied to the `Entity` object itself and cleaning the `Entity` would automatically clean all the related metrics. Currently
that bookkeeping work has to be done manually. Suggestions on a nice API to bring this functionality would be appreciated.


### Filters are Configured Differently

When you read anything asking for a filter name you should know that all filters are configured in the `kamon.util.filters`
configuration key. The format looks as follows:

{% code_block typesafeconfig %}
kamon.util.filters {
  "akka.tracked-actor" {
    includes = [ "**" ]
    excludes = [ "helloAkka/user/howdyGreeter" ]
  }

  "akka.traced-actor" {
    includes = [ "**" ]
  }

  "my.filter" {
    excludes = [ "not-wanted**" ]
  }
}
{% endcode_block bash %}

The evaluation rules for a filter remain the same, though. All inputs matched by any of the `includes` patterns and not
matched by any of the `excludes` patterns will be accepted. Instrumentation modules (most notably the Akka instrumentation)
use predefined filter to decide what components to track and trace. Additionally, you can configure and use filters with
the `Kamon.filter(...)` function.


### MinMaxCounter is now RangeSampler

Simply put, RangeSampler better defines what this type of instrument does: sample the range on which certain variable is
operating, most commonly used for tracking queues that are constantly going up and down (thing Actor mailboxes). The
underlying details are the same as before.


### Gauges Track a Single Value

A better name for our previous Gauge should have been Sampler as it was recording many samples of the observed variable
during each interval. Now the Gauge generates a single value instead of a distribution of values on each interval and
this makes it better suited for tracking variables that move slowly or not at all, like configuration settings.


### Propagation of Context instead of TraceContext

Everything you saw in the past related to TraceContext propagation has a twist now: there is no TraceContext anymore and
instead we now have a more general `Context` abstraction that is propagated. The `Context` is like a immutable map of
keys and values, sort of `Map[Key[T], T]`. You must have a `Key` instance in order to create new contexts with a given
entry and to retrieve entries from a existent `Context` instance. The `currentTraceContext` is now the `currentContext`
and from they you can get what you need.

One additional feature on the new `Context` is the keys can be defined to be either:
  - **local**: the context entry is only propagated within the same JVM, or
  - **broadcast**: the context entry should be propagated across JVM boundaries, typically via HTTP headers or binary
    encoding of the data.

You can think of the `Context` as a lower level solution that replaces when we had before as `TraceLocalStorage`. The
very first user of these facilities is our own tracer, which uses the `Context` to propagate the current Span, but there
are no limitations on usage of the context, you can put whatever you want in there.

This should give you a gross idea of how it works:

{% code_block scala %}

// Define a Key with a default value.
val UserID = Key.broadcast[Option[String]]("user-id", None)
val context = Context.create(UserID, Some("1111"))

// There is no current context and it were, it wouldn't have this key
// and the default value is returned.
Kamon.currentContext().get(UserID) == None

Kamon.withContext(context) {
  // The current context has the UserID key and returns its value,
  // only while this piece of code is executing.
  Kamon.currentContext().get(UserID) == Some("1111")
}

// The context's scope is gone, we are back to the same context
// that was here before.
Kamon.currentContext().get(UserID) == None

{% endcode_block bash %}



### The Subscriptions Protocol is Gone

We are no longer using Akka in Kamon core libraries and the need to create actors to report metrics and traces is gone
with it. By default all metrics reporters will be receiving all metrics tracked by Kamon on every tick but you can have
a result similar to subscriptions by providing a filter when adding a reporter:

{% code_block scala %}
Kamon.addReporter(new PrometheusReporter(), "filtered-prometheus", "my-filter-name")
{% endcode_block bash %}

When doing it like this, only metrics with names matching the `my-filter-name` filter will be send to the reporter.



[1]: https://gitter.im/kamon-io/kamon