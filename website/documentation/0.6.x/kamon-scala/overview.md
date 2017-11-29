---
title: kamon | Akka Toolkit | Documentation
layout: documentation-0.6.x
---

Automatic TraceContext Propagation with Futures
===============================================

The `kamon-scala` module provides bytecode instrumentation for both Scala and Scalaz Futures that automatically
propagates the `TraceContext` across the asynchronous operations that might be schedulede for a given `Future`.

<p class="alert alert-info">
The <b>kamon-scala</b> module require you to start your application using the AspectJ Weaver Agent. Kamon will warn you
at startup if you failed to do so.
</p>


### Future's Body and Callbacks ###

In the following piece of code, the body of the future will be executed asynchronously on some other thread provided by
the ExecutionContext available in implicit scope, but Kamon will capture the `TraceContext` available when the future
was created and make it available while executing the future's body.

{% code_example %}
{%   language scala kamon-akka-examples/src/main/scala/kamon/akka/examples/scala/AutomaticTraceContextPropagationWithFutures.scala tag:future-body %}
{% endcode_example %}

Also, when you transform a future by using map/flatMap/filter and friends or you directly register a
onComplete/onSuccess/onFailure callback on a future, Kamon will capture the `TraceContext` available when transforming
the future and make it available when executing the given callback. The code snippet above would print the same
`TraceContext` that was available when creating the future, during it's body execution and during the execution of all
the asynchronous operations scheduled on it.
