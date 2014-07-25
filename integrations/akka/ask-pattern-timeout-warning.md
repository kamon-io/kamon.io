---
title: Kamon | Ask Pattern Timeout Warning
layout: documentation
---

Ask Pattern Timeout Warning
===========================

The ask pattern provides an easy way to send a message to an actor and expect to receive a response from it. For this to
work properly the ask pattern needs to take care of various details, but to keep it short in this section we will
mention only two:

- sending a message to an actor doesn't mean it will reply, it does not even mean that it will process it.
- [Scala Futures] do not timeout, you need to fulfil your promises.

In order to ensure that the `Future` you get when using the ask pattern is always fulfilled an action that completes the
future with a `AskTimeoutException` is scheduled using the provided timeout, and that action is cancelled if the target
actor replies in a timely fashion to the message. By doing this, it doesn't matter if the actor you are _asking_ for
something replies, fails or is unavailable, your future will always be completed with something. So, what happens when
the timeout is reached and there is no reply from the actor? well, you get a message like this one:

```
akka.pattern.AskTimeoutException: Ask timed out on [Actor[akka://ask-pattern-test/user/IO-HTTP#679362795]] after [1000 ms]
  at akka.pattern.PromiseActorRef$$anonfun$1.apply$mcV$sp(AskSupport.scala:333)
  at akka.actor.Scheduler$$anon$7.run(Scheduler.scala:117)
  at scala.concurrent.Future$InternalCallbackExecutor$.scala$concurrent$Future$InternalCallbackExecutor$$unbatchedExecute(Future.scala:694)
  at scala.concurrent.Future$InternalCallbackExecutor$.execute(Future.scala:691)
  at akka.actor.LightArrayRevolverScheduler$TaskHolder.executeTask(Scheduler.scala:467)
  at akka.actor.LightArrayRevolverScheduler$$anon$8.executeBucket$1(Scheduler.scala:419)
  at akka.actor.LightArrayRevolverScheduler$$anon$8.nextTick(Scheduler.scala:423)
  at akka.actor.LightArrayRevolverScheduler$$anon$8.run(Scheduler.scala:375)
  at java.lang.Thread.run(Thread.java:745)
```

If your application code is making use of the ask pattern then you should be familiar (and probably not friend) with
that message. It is nice to know that something timed out, but what is the thing that timed out? it's hard to know. In
fact, all timeouts messages will yield the same stack trace making it really hard to determine the line of code that
triggered the ask pattern.


A Motivating Example
--------------------

If you have used Spray's http client you should already have experienced this issue, because the pipelining helpers use
the ask pattern internally to expect a response back from the underlying connection. This piece of code generated the
exception shown above:

```scala
  val userDetailsFuture = userDetailsEndpoint(Get("/users/kamon"))
  val recentActivityFuture = recentActivityEndpoint(Get("/timeline?user=kamon"))

  val allUserData =
    for(  userD <- userDetailsFuture;
        recentA <- recentActivityFuture) yield {

      // Do some computation with the information you received.
      "Full user details"
  }
```

If the `allUserData` future fails with an `AskTimeoutException`, can you tell from the error message which of the two
related futures failed? well, the answer is simple: you can't. One way to deal with this would be to add a
transformation to the client pipeline that translates a `Failure(AskTimeoutException)` into a
`Failure(UserDetailsUnavailable)` or similar, and probably that's the correct way to use do it, but in the mean time a
little help on knowing where are we using the ask pattern will be really useful, specially you use it indirectly, like
when using Spray's http client.

By turning on the `kamon.trace.ask-pattern-tracing` setting Kamon will automatically take care of this for you and log a
warning message with a stack trace captured at the moment the ask was generated. The code bellow would produce the
following warning:

```
[WARN] [07/25/2014 00:56:40.694] [ask-pattern-test-akka.actor.default-dispatcher-4] [AskPatternTracing] Timeout triggered for ask pattern registered at: akka.pattern.AskableActorRef$.$qmark$extension(AskSupport.scala:144)
spray.client.pipelining$$anonfun$sendReceive$1.apply(pipelining.scala:38)
spray.client.pipelining$$anonfun$sendReceive$1.apply(pipelining.scala:38)
spray.can.client.ClientRequestInstrumentation$$anonfun$aroundRequestLevelApiSendReceive$1.apply(ClientRequestInstrumentation.scala:97)
spray.can.client.ClientRequestInstrumentation$$anonfun$aroundRequestLevelApiSendReceive$1.apply(ClientRequestInstrumentation.scala:96)
test.AskPatternTimeout$delayedInit$body.apply(SimpleRequestProcessor.scala:197)
scala.Function0$class.apply$mcV$sp(Function0.scala:40)
scala.runtime.AbstractFunction0.apply$mcV$sp(AbstractFunction0.scala:12)
scala.App$$anonfun$main$1.apply(App.scala:71)
scala.App$$anonfun$main$1.apply(App.scala:71)
scala.collection.immutable.List.foreach(List.scala:318)
scala.collection.generic.TraversableForwarder$class.foreach(TraversableForwarder.scala:32)
scala.App$class.main(App.scala:71)
test.AskPatternTimeout$.main(SimpleRequestProcessor.scala:186)
test.AskPatternTimeout.main(SimpleRequestProcessor.scala)
sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
java.lang.reflect.Method.invoke(Method.java:606)
com.intellij.rt.execution.application.AppMain.main(AppMain.java:134)
```

If you look carefully to the stack trace you will find this line right before the Spray code gets called:

```
test.AskPatternTimeout$delayedInit$body.apply(SimpleRequestProcessor.scala:197)
```

And that is the exact line in our test application code where the ask pattern gets indirectly called. Now we can easily
go there and fix the issue in whatever way we like.

<p class="alert alert-warning">
Beware that taking stack traces on every call to the ask pattern doesn't come for free. Kamon creates a new Exception and
stores its stack trace during the lifetime of the ask and that will consume some additional CPU and memory.
</p>

[ask pattern]: http://doc.akka.io/docs/akka/snapshot/scala/actors.html
[Scala Futures]: http://docs.scala-lang.org/overviews/core/futures.html
