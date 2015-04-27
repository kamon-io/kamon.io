---
title: Kamon | Spray | Documentation
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
performance of the HTTP request your application sends to other services using the HTTP client. That should ring a bell
if you already visited our [tracing core concepts section]: Yes, you can use Kamon's tracing facilities to measure the
performance of the services you provide (traces) and the services you call (segments).

In a nutshell, the `kamon-spray` module will give you:

* **[Server Side]** instrumentation that automatically starts and finishes traces when HTTP requests are received.
* **[Client Side]** instrumentation that automatically creates and finishes segments when sending HTTP requests.
* **[Automatic Trace Token Propagation]** for both the server and client sides.


[Server Side]: /integrations/spray/server-side/
[Client Side]: /integrations/spray/client-side/
[Automatic Trace Token Propagation]: /integrations/spray/automatic-trace-token-propagation/
