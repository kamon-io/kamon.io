---
title: Kamon | Documentation
layout: documentation
---

Automatic TraceContext Propagation
==================================

Besides the metrics recording side of our Akka integration, we also provide bytecode instrumentation that will
automatically propagate the available `TraceContext` through certain specific events in order to keep the principle of
having a single and predictable place to look for the "current" context.

In the examples bellow we will explore the conditions under which Kamon will automatically propagate the currently
available context. Please note that even while in these examples we are explicitly wrapping the code sections with a new
`TraceContext`, it is very unlikely that you will need to do so yourself if you are using other supported toolkits such
as Spray and Play!. You will commonly need a context to be present only when the first event is generated and then Kamon will
take care of propagating the `TraceContext` to all related events, under the conditions explained bellow.


### Tell, ! and Forward ###

If a `TraceContext` is available when sending a message to an actor, Kamon will capture that `TraceContext` and make it
available when processing that message in receiving actor, and __only__ when processing that message. This remains true
regardless of whether your are doing a regular tell, using the `!` operator or forwarding a message to another actor.

{% code_example %}
{%   language scala kamon-akka-examples/src/main/scala/kamon/akka/examples/scala/AutomaticTraceContextPropagation.scala tag:tell %}
{% endcode_example %}

In this particular case, the two messages will propagate the same `TraceContext`, since they were originated from the
same block of code.


### Ask, ? ###

When you send a message using the ask pattern the `TraceContext` is also propagated, but additionally the same `TraceContext`
is also available when executing any callbacks registered in the returned `Future`.

{% code_example %}
{%   language scala kamon-akka-examples/src/main/scala/kamon/akka/examples/scala/AutomaticTraceContextPropagation.scala tag:ask %}
{% endcode_example %}


### Pipe Pattern ###

When using the pipe pattern, the `TraceContext` available when the pipe call was made (not when completing the future)
is also made available when processing the message in the target actor.


### Log Events ###

When you are using the logging facilities provided by Akka, Kamon will attach the current `TraceContext` available when
the log statement is executed and make the same `TraceContext` available when that log event is actually processed by
your logger.

### Supervision Messages ###

When one of your actor fails it is the responsibility of its parent to determine what action to take based on the
provided supervision strategy. The failure notification as well as the delivery of the supervision directive to apply
happens through system messages that are not directly visible to users and do not use to the same mailbox as the regular
messages we send to actors, and Kamon hooks in these internals as well to propagate the `TraceContext`. It is really
important to keep the same `TraceContext` when this happens, since all possible error messages being logged or any other
action taken will be correctly related to the failing request.


### Actor Creation ###

You might be thinking that actor creation happens in the same thread where the call to `ActorRefFactory.actorOf(..)` is
being made, but that is not necessarily true. In fact, a `Create` system message is sent to the newly created actor cell
and it might be execute later depending on whether you are creating a top-level actor or not. Kamon also instruments this
system message to make sure that if a `TraceContext` was available when requesting your `ActorRefFactory` to create an
actor, the same `TraceContext` will be available when that actor is actually created.



Crossing the JVM Borders
------------------------

If you are using Akka Remoting or the Akka Cluster, the same rules for `TraceContext` propagation apply but you will
need to ensure that the `kamon-akka-remote` module is in your classpath as well. Also, please note that not all the information
available locally for a `TraceContext` is propagated to remote actor systems, but rather a reduced subset that contains
the trace name, token, start timestamp and a whether the `TraceContext` is still open or not. Although that seems like a
very restrained set of information, by sending the trace token along you already have huge wins in terms of traceability
of actions across your distributed setup.
