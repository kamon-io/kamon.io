---
title: 'Context Propagation with Futures | Kamon Documentation'
layout: docs
redirect_from:
  - /documentation/1.x/instrumentation/futures/
  - /documentation/0.6.x/kamon-scala/overview/
---

{% include toc.html %}

Futures Instrumentation
=======================

The Futures instrumentation ensures that the same `Context` available when a Future is created will be available while
the Future body runs. Same goes for all transformations applied to a Future, like `.map`, `.flatmap`, and so on.



Context Propagation
-------------------

In the example below, the body of the future will be executed asynchronously on a thread provided by the
`ExecutionContext`. When the code runs, Kamon will capture the `Context` available when the future was created and make
it available while executing the Future's body.

{% code_example %}
{%   language scala instrumentation/futures/src/main/scala/kamon/examples/futures/ContextPropagation.scala tag:future-body %}
{% endcode_example %}

The same `Context` propagation works when you transform a Future by calling map/flatMap/filter and friends, or you
directly register a callback on a Future using onComplete/onSuccess/onFailure. Kamon will capture the current `Context`
available when transforming the Future and make it available while executing the given callback. 

The code snippet above would print the same `userID` that was available when creating the future, even though it will
happen asynchronously and (probably) in another thread.




Creating Spans from Futures
---------------------------

Since Kamon [2.1.21](https://github.com/kamon-io/Kamon/releases/tag/v2.1.21) you can use the `Kamon.span` function to
wrap a code block and create a Span out of it, regardless of whether the code returns a Future instance or not:

{% code_block scala %}
import kamon.Kamon.span

span("myOperationName") {
  // Do your stuff here  
}
{% endcode_block %}

The `span` function creates a new Span with the operation name you passed as argument, and additionally it will: 
  - Store the Span in the current `Context` while the wrapped code block executes. This ensures that other Spans you
    create later on will be connected to the same trace as the current Span.
  - Fail the Span with any exception thrown by the wrapped code block.
  - Finish the Span when the wrapped code block finishes executing. With a little twist: if the wrapped code block
    returns a Scala `Future` or a `CompletionStage`, then the Span will be finished when the asynchronous computation
    finishes.


#### Wrapping the Future vs Wrapping the Future's Body

Beware that there is a difference between wrapping an entire Future with `Kamon.span`, or only wrapping the code inside
the Future. Take these two example methods:

{% code_example %}
{%   language scala instrumentation/futures/src/main/scala/kamon/examples/futures/ContextPropagation.scala tag:wrapping-futures %}
{% endcode_example %}

Scala Futures are always scheduled for execution right away, but that doesn't mean they will actually run immediately, it all depends on how busy the `ExecutionContext` is at the moment. From the code example above:
  - If you wrap the entire Future, the Span will count the time it takes to create the Future, schedule it for
    execution, and execute.
  - If you wrap the code inside the Future, you will only measure the time it takes for that specific code block to
    execute, completely ignoring any time spent creating the Future or waiting for it to start executing.



Creating Spans with Future Chaining (deprecated)
------------------------------------------------

{% include warning.html message="The Future Chaining instrumentation was deprecated in Kamon 2.2.0 and will be completely removed in Kamon 2.3.0. This is for legacy reference only. Please, don't write any new code using these functions." %}

The recommended way to create Spans from Futures is using the `Kamon.span` function as described in the section above.
If you need to enable the Future Chaining instrumentation in Kamon 2.2.0+, you must add these settings to your
`application.conf` file:

{% code_block typesafeconfig %}
kanela.modules {
  executor-service {
    exclude += "scala.concurrent.impl.*"
  }

  scala-future {
    enabled = true
  }
  
  akka-http {
    instrumentations += "kamon.instrumentation.akka.http.FastFutureInstrumentation"
  }  
}
{% endcode_block %}

It is possible to create Spans that represent an entire asynchronous computation, a Future's body or asynchronous
transformations applied to them by using these functions:

- `trace(operationName: String) { ... }` which accepts a call-by-name block that produces a Future and creates a Span
  that will be automatically finished when the Future finishes.
- `traceBody(operationName: String) { ... }` which accepts a call-by-name block that can be used to wrap the Future body
  and creates a Span that will be automatically finished when the Future's body finishes executing.
- `traceFunc(operationName: String) { ... }` which accepts a function that can be used to wrap any of the transformations
  than can be applied on a Future and creates a Span that will be automatically finished when the transformation finishes
  executing.

The `traceBody` and `traceFunc` functions work very similarly, the only reason for them not being the same is the
mismatch between the types expected when creating a Future and the types expected when applying transformations to them.
Let's see this in a small example:

{% code_example %}
{%   language scala instrumentation/futures/src/main/scala/kamon/examples/futures/ContextPropagation.scala tag:future-spans %}
{% endcode_example %}

In the code example above, the instrumentation will automatically create a Span called `future-body` that measures how
long did it take to execute the Future's body, as well as additional Spans for the `calculate-length` and `to-string`
steps of the computation. Note that even though the rest of the transformations in this example will not get dedicated
Spans, they will still benefit from Context propagation, just as before!

{% include warning.html message="Do not use these functions if you are not running your application without the
instrumentation agent, since that will lead to threads not being properly cleaned up from previous request's Contexts." %}

Manual Installation
-------------------

In case you are not using the Kamon Bundle, add the dependency below to your build.

{% include dependency-info.html module="kamon-scala-future" version=site.data.versions.latest.futures %}
{% include instrumentation-agent-notice.html %}

[executors]: ../executors/
