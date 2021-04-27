---
title: 'Akka Distributed Message Tracing | Kamon Documentation'
layout: docs
redirect_from:
  - /documentation/1.x/instrumentation/akka/message-tracing/
---

{% include toc.html %}

Message Tracing
===============

Additionally to all metrics and context propagation that you can get with Kamon, you can also use the instrumentation to
generate Spans for actor messages using Kamon's Tracing API, effectively giving you distributed tracing for your Akka
applications, wohoo!


Tracing Actors
--------------

Tracing must be enabled on a per-actor basis using the `actors.trace` filter within the Akka configuration:

{% code_example %}
{%   language typesafeconfig instrumentation/akka/src/main/resources/application.conf tag:akka-message-tracing label:"application.conf" %}
{% endcode_example %}

With these settings, all actors matching the filter above will generate Spans for their messages, but only if there is
an ongoing trace already! In most situations that is not a problem at all because traces are already started a previous
step in the application logic. For example, if there is a thin Akka HTTP API on receiving requests that then get
processed by actors, the Akka HTTP instrumentation will take care of starting a trace and the filtered actors will
participate on it.

All Spans generated for actor messages will start when a message is sent to an actor and finish when the message processing
has finished. Additionally, these Spans will get:
* A __akka.actor.dequeued__ mark when the message is taken out of the mailbox to start processing.
* All these tags:
  * __component=akka.actor__: Component marker. It's constant on all Spans.
  * __akka.system__: Actor system name.
  * __akka.actor.path__: Actor path.
  * __akka.actor.class__: Actor class.
  * __akka.actor.message-class__: Message class.


Starting Traces
---------------

Additionally to participating in traces, it is possible to configure Actors to automatically start a trace while they
are processing a message, if there is no ongoing trace to participate in. Again, this is configured with a filter! In
this case the filter is called `actors.start-trace`:

{% code_example %}
{%   language typesafeconfig instrumentation/akka/src/main/resources/application.conf tag:akka-start-message-tracing label:"application.conf" %}
{% endcode_example %}

Be careful with this filter since making too many actors start traces could end up in generating many irrelevant spans
that bury any useful information behind spans for scheduled messages and other potentially irrelevant messages that get
processed by actors.


Customizing Spans
-----------------

In case you would like to modify the Span automatically created by instrumentation, you can access it using the
`Kamon.currentSpan()` shortcut and do anything you want with it! This example below adds a custom tag to the Span:

{% code_example %}
{%   language scala instrumentation/akka/src/main/scala/kamon/examples/akka/scala/ContextPropagation.scala tag:customizing-a-span label:"Customizing the Spans" %}
{% endcode_example %}
