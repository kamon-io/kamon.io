---
title: kamon | Spray | Documentation
layout: documentation
---

Spray Integration
=================

The `kamon-spray` module brings bytecode instrumentation that heavily simplifyes the process of getting metrics out of
a Spray-based application, both in the server and client side.

<p class="alert alert-info">
The <b>kamon-spray</b> module requires you to start your application using the AspectJ Weaver Agent. Kamon will warn you
at startup if you failed to do so.
</p>

When measuring the performance of a Spray application (or any other HTTP-based application) your main subject of
interest is going to be the latency of the request-response cycles for the services your application provides or the
performance of the HTTP request your application sends to other services using the Spray HTTP client. That should ring a
bell if you already visited our [tracing core concepts section]: Yes, you can use Kamon's tracing facilities to measure
the performance of the services you provide (traces) and the services you call (segments).



Server Side Tools
-----------------

If we are using traces to measure the HTTP performance, the minimum requirement would be to start and finish a trace at
the right places in the right time. The bytecode instrumentation provided by this module hooks into Spray's server side
(`spray-can`) internals so that we can automatically start a new trace as soon as the server pipeline receives an HTTP
request and automatically finish it once the response for that request is sent to client.

There is one caveat here, though. When you start traces with Kamon you need to provide a name for it and that name will
be used for metric tracking purposes, it is really important that you give a meaningful name to your traces. This time
the traces are not started by a piece of code that you can control but rather by instrumentation injected in Spray's
internals, so you need to ensure that a proper name is assigned to the traces for them to be really useful.
In order to do so, the `kamon-spray` module provides you with two tools:


### The traceName Directive ###

The `traceName` directive simply renames the current trace to whatever name you provide to it and pass the request to
it's inner route. It doesn't provide any additional functionality nor do any sort of magic, it's a plain call to
`Tracer.currentContext.rename(...)`. Ideally, you will want to use this directive at the deepest possible level in your
routing tree to provide names that really relate to specific functionality. Here is a quick usage example:

{% code_example %}
{%   language scala kamon-spray-examples/src/main/scala/kamon/spray/examples/SimpleSprayApp.scala tag:traceName-directive %}
{% endcode_example %}

For that routing tree, all `GET` requests to the `/users` resource will be renamed to `GetAllUsers`, which also means
that you will find the metrics fo those requests under the `GetAllUsers` trace name. Request that match the second
branch will be renamed to `GetUserDetails` as you might expect, and by default all traces are named `UnnamedTrace`, if
you see one of those it means you didn't set a proper name for your trace in any point.


### Providing a Name Generator ###

The second choice you have is to provide your own implementation of `kamon.spray.SprayNameGenerator`. Kamon will always
use the configured name generator to give the initial name to a trace, by replacing the default implementation via the
`kamon.spray.name-generator` configuration key you can ensure that Kamon will get the appropriate name from the very
beginning, making it unnecessary to rename the traces down the road. This is specially useful if you need to monitor
routes provided by third party libraries or when you simply don't want to touch your routing tree but still want to get
the best out of Kamon.

Our recommendation? Use the `traceName` directive whenever possible.


[tracing core concepts section]: /core/metrics/core-concepts/
