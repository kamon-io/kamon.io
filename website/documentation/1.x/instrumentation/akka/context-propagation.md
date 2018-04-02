---
title: Kamon > Documentation > Instrumentation > Akka > Context Propagation
layout: documentation-1.x
---

Context Propagation
===================

Besides the metrics recording side of our Akka integration, we also provide bytecode instrumentation that will
automatically propagate Kamon's Context through certain specific events in order to keep the principle of having a
single and predictable place to look for the "current" context.

In the examples bellow we will explore the conditions under which Kamon will automatically propagate the currently
available context. Please note that even while in these examples we are explicitly wrapping the code sections with a new
`Context`, it is very unlikely that you will need to do so yourself if you are using other supported toolkits such
as Akka HTTP and Play Framework. You will commonly need a context to be present only when the first event is generated
and then Kamon will take care of propagating the `Context` to all related events, under the conditions explained bellow.


### Tell, ! and Forward ###

If a `Context` is available when sending a message to an actor, Kamon will capture that `Context` and make it
available when processing that message in receiving actor, and __only__ when processing that message. This remains true
regardless of whether your are doing a regular tell, using the `!` operator or forwarding a message to another actor.

{% code_example %}
{%   language scala kamon-1.x/basic-akka-monitoring/src/main/scala/kamon/akka/examples/scala/ContextPropagation.scala tag:tell %}
{% endcode_example %}

In this particular case, the two messages will propagate the same `Context`, since they were originated from a block of
code where the same `Context` is available.


### Ask, ? ###

When you send a message using the ask pattern the `Context` is also propagated, but additionally the same `Context`
is also available when executing any callbacks registered in the returned `Future`.

{% code_example %}
{%   language scala kamon-1.x/basic-akka-monitoring/src/main/scala/kamon/akka/examples/scala/ContextPropagation.scala tag:ask %}
{% endcode_example %}


### Pipe Pattern ###

When using the pipe pattern, the `Context` available when the pipe call was made (not when completing the future)is also
made available when processing the message in the target actor.


### Log Events ###

When you are using the logging facilities provided by Akka, Kamon will capture the current `Context` available when the
log statement is executed and make the same `Context` available when that log event is actually processed by your logger.


### Supervision Messages ###

When one of your actor fails it is the responsibility of its parent to determine what action to take based on the
provided supervision strategy. The failure notification as well as the delivery of the supervision directive to apply
happens through system messages that are not directly visible to users and do not use to the same mailbox as the regular
messages we send to actors, and Kamon hooks in these internals as well to propagate the `Context`. It is really
important to keep the same `Context` when this happens, since all possible error messages being logged or any other
action taken will be correctly related to the failing request.


### Actor Creation ###

You might be thinking that actor creation happens in the same thread where the call to `ActorRefFactory.actorOf(..)` is
being made, but that is not necessarily the case. In fact, a `Create` system message is sent to the newly created actor cell
and it might be executed later depending on whether you are creating a top-level actor or not. Kamon also instruments this
system message to make sure that if a `Context` was available when requesting your `ActorRefFactory` to create an
actor, the same `Context` will be available when that actor is actually created.



Crossing the JVM Borders
------------------------

If you are using Akka Remoting or the Akka Cluster, the same rules for `Context` propagation apply but you will
need to ensure that the `kamon-akka-remote` module is in your classpath as well.
