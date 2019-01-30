---
title: Kamon > Documentation > Instrumentation > Akka > Message Tracing
layout: docs
redirect_from:
  - /documentation/1.x/instrumentation/akka/message-tracing/
---

{% include toc.html %}

Message Tracing
===============

Additionally to all metrics and context propagation that you can get with Kamon, you can also use the instrumentation to
generate Spans for actor messages using Kamon's tracing API, effectively giving you distributed tracing for your Akka
applications, wohoo!

Tracing must be enabled on a per-actor basis using the `akka.traced-actor` filter as shown bellow:

{% code_example %}
{%   language typesafeconfig instrumentation/akka/src/main/resources/application.conf tag:akka-message-tracing label:"application.conf" %}
{% endcode_example %}

All Spans generated for actor messages will start when a message is sent to an actor and finish when the message processing
has finished. Additionally, these Spans will get:
* A __akka.actor.dequeued__ mark when the message is taken out of the mailbox to start processing.
* All these tags:
  * __component=akka.actor__: Component marker. It's constant on all Spans.
  * __akka.system__: Actor system name.
  * __akka.actor.path__: Actor path.
  * __akka.actor.class__: Actor class.
  * __akka.actor.message-class__: Message class.


## Customizing the Message Span

In case you would like to modify the Span automatically created by instrumentation, you can access it using the `Kamon.currentSpan()`
shortcut and do anything you want with it! This example bellow adds a custom tag to the Span:

{% code_example %}
{%   language scala instrumentation/akka/src/main/scala/kamon/examples/akka/scala/ContextPropagation.scala tag:customizing-a-span label:"Customizing the Spans" %}
{% endcode_example %}