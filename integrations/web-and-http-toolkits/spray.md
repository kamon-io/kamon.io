---
title: kamon | Spray | Documentation
layout: documentation
---

Spray Integration
=================


The `kamon-spray` module brings bytecode instrumentation and tools that provide what we consider base functionality for
a supported HTTP toolkit.

<p class="alert alert-info">
The <b>kamon-spray</b> module requires you to start your application using the AspectJ Weaver Agent. Kamon will warn you
at startup if you failed to do so.
</p>



Server Side Tools
-----------------

One of the main goals in our Spray integration is to measure the behavior of your HTTP server and for that, we
automatically start and finish traces for you, but there is one caveat here, though. When you start traces with Kamon
you need to provide a name for it and that name will be used for metric tracking purposes; it is really important that
you give a meaningful name to your traces, but our integration the traces are not started by a piece of code that you
can control but rather by instrumentation injected in Spray's internals, so you need to ensure that a proper name is
assigned to the traces for them to be really useful. In order to do so, the `kamon-spray` module provides you with two
tools:


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

The second choice you have is to provide your own implementation of `kamon.spray.NameGenerator`. Kamon will always
use the configured name generator to give the initial name to a trace, by replacing the default implementation via the
`kamon.spray.name-generator` configuration key you can ensure that Kamon will get the appropriate name from the very
beginning, making it unnecessary to rename the traces down the road. This is specially useful if you need to monitor
routes provided by third party libraries or when you simply don't want to touch your routing tree but still want to get
the best out of Kamon.

Our recommendation? Use the `traceName` directive whenever possible.



### Keeping the Right Context ###

Additionaly to automatically starting and finishing a trace as requests are processed, Kamon will also ensure that the
same `TraceContext` that was created when the request was received is the one present when the response for that request
is being processed. Ideally, the instrumentation provided by the Akka and Scala modules should be enough to keep the
right contexts in place, but some times, specially when using libraries not yet supported by Kamon or when doing having
event flows that break the automatic trace context propagation rules, it can happen that the `TraceContext` available
when processing the HTTP response isn't the same as the one generated when the HTTP request was received, under that
circumstance we will log one of the following messages:

* *EmptyTraceContext present while closing the trace with token [some-token]*, When there is no current
`TraceContext` while processing the HTTP response.
* *Different trace token found when trying to close a trace, original: [some-token] - incoming: [some-other-token]*,
When the current `TraceContext` is not the same as the one created when the HTTP request arrived.

When under these situations, we will still finish the correct trace since we have a reference to it on the server side
instrumentation, but you should still track down the reason for loosing/changing the `TraceContext` and fix it.




Client Side Tools
-----------------

If you are using `spray-client` to send HTTP requests to other services then we also have something to offer on this
side. The bytecode instrumentation provided by the `kamon-spray` module hooks into Spray's internals to automatically
start and finish segments for requests that are issued within a trace. This translates into you having metrics about how
the services you are calling are behaving.

As you might already know, `spray-client` comes with three different levels of abstraction that can be used to issue
HTTP requests, namely the Request-level API, the Host-level API and the Connection-level API. If you are using any of
the first two options, then our instrumentation can automatically create and finish segments for you, whereas if you are
using the Connection-level API you will need to manage segments on your own.

Since each of the client API levels provided in Spray builds on top of the previous level, you will beed to use the
`kamon.spray.client.instrumentation-level` configuration key to tell Kamon at which level you want the segments to be
measured. The available options are:

* __request-level__: measures the time during which the user application code is waiting for a `spray-client` request to
complete, by attaching a callback to the Future[HttpResponse] returned by `spray.client.pipelining.sendReceive`.
If `spray.client.pipelining.sendReceive` is not used, the segment measurement wont be performed.

* __host-level__: measures the internal time taken by spray-client to finish a request. Sometimes the user application
code has a finite future timeout (like when using `spray.client.pipelining.sendReceive`) that doesn't match
the actual amount of time spray might take internally to resolve a request, counting retries, redirects,
connection timeouts and so on. If using the host level instrumentation, the measured time will include the entire time
since the request has been received by the corresponding `HttpHostConnector` until a response is sent back
to the requester.



### Naming HTTP Client Segments ###

By default, the name generator bundled with the `kamon-spray` module will use the `Host` header available in the request
to assign a name to the automatically generated segment. Currently, the only way to override that name would be to
provide your own implementation of `kamon.spray.NameGenerator` which is used to assign names both on the server and
client sides of our instrumentation.





[tracing core concepts section]: /core/metrics/core-concepts/
