---
title: Kamon > Documentation > Instrumentation > Futures
layout: documentation-1.x
---


Context Propagation with Futures
================================

The `kamon-futures` module provides bytecode instrumentation for Scala, Twitter and Scalaz Futures that automatically
propagates the current `Context` across the asynchronous operations that might be scheduled for a given `Future`.

<p class="alert alert-info">
The <b>kamon-futures</b> module requires you to start your application using the AspectJ Weaver Agent.
</p>

The following artifacts are published, pick the right one for your Akka version:

  * `kamon-scala-future` for Scala 2.10, 2.11 and 2.12.
  * `kamon-twitter-future` for `util-core` 6.34 in Scala 2.10 and `util-core` 6.40 for Scala 2.11 and 2.12.
  * `kamon-scalaz-future` for `scalaz-concurrent` 7.2.8 with Scala 2.10, 2.11 and 2.12.


### Future's Body and Callbacks ###

In the following piece of code, the body of the future will be executed asynchronously on a thread provided by the
ExecutionContext available in implicit scope, but Kamon will capture the current `Context` available when the future
was created and make it available while executing the future's body.

{% code_example %}
{%   language scala kamon-1.x/basic-akka-monitoring/src/main/scala/kamon/akka/examples/scala/ContextPropagationWithFutures.scala tag:future-body %}
{% endcode_example %}

Also, when you transform a future by using map/flatMap/filter and friends or you directly register a callback on a
future (onComplete/onSuccess/onFailure), Kamon will capture the current `Context` available when transforming
the future and make it available when executing the given callback. The code snippet above would print the same
`userID` that was available when creating the future, during its body execution and during the execution of all
the asynchronous operations scheduled on it.
