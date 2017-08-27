---
title: Kamon | Core | Documentation
layout: documentation-0.6.x
---

Threading Model Considerations
==============================

As described in the [trace context manipulation] section, we will always use the `Tracer` companion object to store and
retrieve the `TraceContext` being used in the current trace. The `Tracer`, in turn, will end up storing the
`TraceContext` in a thread-local variable. This is, by far, the most simple and predictable way to share the
`TraceContext` across the entire code base, as well as providing third party libraries the ability to interact with it
as well. For this approach to succeed in your application you need to be completely aware of how the threading model of
your application works, sometimes the models are quite simple and single threaded, but it can get a lot more complex
if you start to walk into event-based lands.

Here we will describe the three general threading models that we have identified and provide a bit of guidance with
regards to how the `TraceContext` should be manipulated and propagated across threads.


The Traditional Model
---------------------

<img class="img-fluid" src="/assets/img/diagrams/traditional-thread-model.png">

The traditional way of doing things, specially when using Servlets, is to tie the execution of all code related to a
request to a single thread. In the picture above, the thick arrow in the background represents a thread while the blocks
in the foreground represent a piece of functionality in your application; that effectively means that if you do a JDBC
call to a database your request thread gets blocked until the response comes back from the database; if you send an HTTP
request to an external service using a blocking client, again, your thread will be blocked while waiting for the response
back from the external service. When everything happens in the same thread you don't need to take any special
consideration, you simply use the tracing API and you are good to go.



The Enhanced Traditional Model
------------------------------

Sometimes, waiting for everything to happen sequentially is not an option or it is even a requirement to do things
concurrently in order to minimize the experienced latency by the user. When doing so, the common approach is to add a
couple of fork points in the execution of a traditional flow, allowing some parts of the application code to execute
concurrently and then joining the results at some later point on the request thread. You are still blocking threads, in
fact, you are blocking more threads than with the traditional model, but you do it in such a way that the application
can serve a particular request "faster". We tend to call this the "enhanced traditional model" and, to compare with the
traditional model you can picture it like this:

<img class="img-fluid" src="/assets/img/diagrams/enhanced-traditional-thread-model.png">

When your application works under this model you might have the need to propagate the `TraceContext` to the additional
supporting threads, it is up to you. If you need to do so, then rely on the tracing API to retrieve the current
`TraceContext` while you are still in the main thread and pass it to the task to be executed in the supporting thread.
It is very important that you clean up the supporting thread after you finish, as it might be used by a different trace
as soon as you release it.



The Event Based Model
---------------------

When you walk into event based ground you need to be especially careful with regards to `TraceContext` propagation, since
it is very likely that the processing of a single request will trigger several asynchronous events that might be
processed by different threads at different points in time, until the request is fulfilled. Visually, the event based
model looks like this:

<img class="img-fluid" src="/assets/img/diagrams/reactive-model.png">

As you can see from the diagram, the processing of a request flows through an arbitrary number of stages and finishes at
some point in the future when the desired response is available. Some parts of the flow might still need to block a
thread, like when using JDBC to connect to a database, but still the majority of the the code is executed asynchronously
on different threads.

One important characteristic of this model is that even while things happen on different threads at unpredictable times,
usually, the processing of the first event related to a trace will trigger other events that are only related to this
specific trace and the cycle repeats. Under this model, the idea of threads is less important and you need to care about
how the `TraceContext` is tied to the events processed by your application.

Event based models are becoming more common nowadays and we already provide support for [Scala] and [Akka] event based
facilities, but in case you have a new tool at hand, the tracing API is there to help you!




[trace context manipulation]: ../trace-context-manipulation/
[Akka]: /documentation/kamon-akka/0.6.6/overview/
[Scala]: /documentation/kamon-scala/0.6.6/overview/
