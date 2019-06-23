---
title: 'Context Propagation with Futures | Kamon Documentation'
layout: docs
redirect_from:
  - /documentation/1.x/instrumentation/futures/
  - /documentation/0.6.x/kamon-scala/overview/
---

{% include toc.html %}

Context Propagation with Futures
================================

The `kamon-futures` module provides bytecode instrumentation for Scala, Twitter and Scalaz Futures that automatically
propagates the current `Context` across the asynchronous operations that might be scheduled for a given `Future`.

The following artifacts are published, pick the ones that match the libraries you are using in your services:

  * `kamon-scala-future` for Scala 2.10, 2.11 and 2.12.
  * `kamon-twitter-future` for `util-core` 6.34 in Scala 2.10 and `util-core` 6.40 for Scala 2.11 and 2.12.
  * `kamon-scalaz-future` for `scalaz-concurrent` 7.2.8 with Scala 2.10, 2.11 and 2.12.


## Dependency Installation
{% include dependency-info.html module="kamon-scala-future" version="1.0.0" %}
{% include instrumentation-agent-notice.html %}

## Future's Body and Callbacks ###

In the following piece of code, the body of the future will be executed asynchronously on a thread provided by the
ExecutionContext available in implicit scope, but Kamon will capture the current `Context` available when the future
was created and make it available while executing the future's body.

{% code_example %}
{%   language scala instrumentation/futures/src/main/scala/kamon/examples/futures/ContextPropagation.scala tag:future-body %}
{% endcode_example %}

Also, when you transform a future by using map/flatMap/filter and friends or you directly register a callback on a
future (onComplete/onSuccess/onFailure), Kamon will capture the current `Context` available when transforming
the future and make it available when executing the given callback. The code snippet above would print the same
`userID` that was available when creating the future, during its body execution and during the execution of all
the asynchronous operations scheduled on it.
