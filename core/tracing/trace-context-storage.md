---
title: Kamon | Core | Documentation
layout: documentation
---





Storing the TraceContext
------------------------

If you ever had the chance to work with a traditional tracing and/or monitoring systems you will notice one common
denominator when it comes to storing trace data: a `ThreadLocal`, and we follow the same technique, but taking special
care in understanding the situation we are in to ensure thread safety and consistency in the expected results.

Using a thread local variable can be best solution for this

The Traditional Model
---------------------

<img class="img-responsive" src="/assets/img/diagrams/traditional-thread-model.png">

The traditional way of doing things (like when using Servlets) is to tie the execution of all code related to a request
to a single thread, effectively meaning that if you do a JDBC call to a database your request thread gets blocked until
the response comes back from the database; if you send a HTTP request to a external service using a blocking client,
again, your thread will be blocked while waiting for the response back from the external service. When everything
happens in the same thread then answering the question above is a no-brainer: ThreadLocals are the simplest way to
share/store trace information because all the executed code for servicing a request is tied to a thread.

The Enhanced Traditional Model
------------------------------

Sometimes, waiting for everything to happen sequentially is not an option or it is even a requirement to do things
concurrently in order to minimize the experienced latency by the user. When doing so, the common approach is to add
a couple of fork points in the execution of a traditional flow, allowing some parts of the application code to execute
concurrently and then joining the results at some later point on the request thread. You are still blocking threads, in
fact, you are blocking more threads than with the traditional model, but you do it in such a way that the application
can work "faster". We tend to call this the "enhanced traditional model" and, to compare with the traditional model you
can picture it like this:

<img class="img-responsive" src="/assets/img/diagrams/enhanced-traditional-thread-model.png">

You might need to add a couple of utilities to make sure that the work being done in the worker threads is properly
related to the trace happening in the request thread, but even if you can't do that the request thread still knows the
forks and joins and knows for sure when the request starts and ends.

The Reactive Model
------------------

That was a nice history lesson but we are here because we are developing reactive applications and reactive applications
rely on asynchronous message passing and non-blocking operations which effectively mean that the senders and recipients
of these messages are not necessarily tied to a thread. Here is how it looks like, compared to the previous diagrams:

<img class="img-responsive" src="/assets/img/diagrams/reactive-model.png">

As you can see from the diagram, everything happens asynchronously as a reaction to a previous event and all the event
chain starts with a request, flows through a arbitrary number of stages and finishes ar some point in the future when
the desired response is available. Some parts of the flow might still need to block a thread, like when using JDBC to
connect to a database, but still the majority of the the code is executed asynchronously on different threads. Can we
use ThreadLocals to store trace information when using the reactive model?, in short: No.
