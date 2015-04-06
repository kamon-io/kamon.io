---
title: Kamon | Core | Documentation
layout: documentation
---

Tracing Module
==============

Let's get the basics done first, what is a trace? Well, you can think of a trace like a story told by the flow of events
in your application that explain how the execution of a particular functionality went during a given invocation. For
example, if in order to fulfill a `GET` request to the `/users/kamon` resource your application sends a message to an
actor which reads the user data from a database and sends a message back with the user details to finish the request,
all of those events would be considered as part of the same trace.

If the application described above were to handle hundreds of clients requesting for user details, then each invocation
should be considered and measured separately as each individual invocation can generate it's own diagnostic and
performance data, each will be a separate trace.

The purpose of each individual trace is very simple: gather information about the execution of some functionality
provided by your application and expose that information via subscription protocols to be consumed by reporting
backends. The most basic information that can be stored for a trace is it's execution time, as measured from the moment
the trace was started until the moment it was finished, but that can be augmented with segments information and trace
local storage as you will see soon.



The TraceContext
----------------

We said that each functionality invocation can generate it's own diagnostic and performance data, in other words, each
invocation is a unique trace and each trace has a context surrounding it, a context that we model into what we call a
`TraceContext`. The `TraceContext` is a fundamental piece in the tracing infrastructure provided by Kamon and it is very
important that you understand how it works and how it is propagated in order to make the most effective use of it. A
TraceContext has the following attributes:

* __name__: A user-friendly name that describes the functionality being used in a traced request. Examples might be
"GetUserDetails", "PublishStatus" or "GenerateSalesReport". A trace can be renamed at any point during the execution of
a request. As you might guess, many traces in your application can and should share the same name.
* __trace-token__: A automatically generated id for the TraceContext. Once a TraceContext is created, it's trace token
will remain the same until the end of the trace. Contrary to the trace name, the trace token is unique and will never be
repeated during the lifetime of the application.
* __metadata__: Simple key-value pairs containing additional information about the execution of a trace.

To get a hold on a new TraceContext you need ask the Kamon tracer for one as shown in the example bellow:

{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/TraceContextManipulation.scala tag:creating-a-trace-context %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/TraceContextManipulation.java tag:creating-a-trace-context %}
{% endcode_example %}

* __segments__: Represent a group of events related to the execution of a sub-functionality required to complete a
request. For example, making a JDBC call to a database to get the user details might be a sub-functionality that you
use from the "GetProfile" and "Login" functionalities in your app.

Stop by the [trace manipulation] section to learn how to start, manipulate and finish a TraceContext, as well as how and
when Kamon will propagate a TraceContext for your.


there might be a
handful of database access actors handling those requests. When the dispatcher gives an actor some time to execute, it
will process as many messages as possible (as per dispatcher configuration) before the Thread is taken away from it, but
during that time it is incorrect to say that either the actor or the Thread are tied to a trace, because each message
might come from a different source which is probably waiting for a different response.


Storing Trace Data
==================

If you ever had the chance to work with a traditional tracing and/or monitoring system you will notice one common
denominator when it comes to storing trace data: a `ThreadLocal`. Why is it so common? let's take a look at how a
"traditional model" application perform tasks and try to deduce it from there:

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


The TraceContext
================

With reactive applications we can no longer tie trace information to a thread because there is no single thread tied to
a request, instead, we need to start thinking of trace data related to *events*. Kamon introduces the notion of a
`TraceContext` which is attached to all events generated by a request, providing a safe place to store trace information
in reactive applications. A TraceContext has the following attributes:

* __name__: A user-friendly name that describes the name of the functionality being used by a traced request. Examples
might be "GetUserDetails", "PublishStatus" or "GenerateSalesReport". A trace might be renamed at any point during the
execution of a request.
* __trace-token__: A automatically generated id for the TraceContext. Once a TraceContext is created, it's trace token
will remain the same until the end of the trace.
* __segments__: Represent a group of events related to the execution of a sub-functionality required to complete a
request. For example, making a JDBC call to a database to get the user details might be a sub-functionality that you
use from the "GetProfile" and "Login" functionalities in your app.

Stop by the [trace manipulation] section to learn how to start, manipulate and finish a TraceContext, as well as how and
when Kamon will propagate a TraceContext for your.



Trace Levels
------------

Kamon has three different trace levels that can be used to gather monitoring information about your application, these
levels are:

* __OnlyMetrics (default)__: A TraceContext is propagated to all related events that belong to a given request but this
information is only used to gather metrics on the execution of each request. Metrics about the entire trace duration and
all segments duration is recorded.
* __SimpleTrace (not yet implemented)__: Besides gathering metrics for the entire trace and segments, this level collects
additional trace information that allows you to generate a gantt-like graph of the trace and segments executed for
servicing a request.
* __FullTrace (not yet implemented)__: Besides gathering metrics for the entire trace and segments, this level collects
additional trace information that allows you to generate a gantt-like graph of all individual events executed for
servicing a request.



[trace manipulation]: /core/tracing/trace-context-manipulation/
[trace metrics]: /core/tracing/trace-metrics/
