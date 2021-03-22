---
title: 'Spring MVC Documentation'
description: 'Automatically extract metrics, traces and perform context propagation on Spring applications'
layout: docs
---
{% include toc.html %}

Spring MVC Instrumentation
=======================
Since __2.1.13__

{% alert info %}
This is an experimental feature that is currently enabled by default.
You can disable it by adding `kanela.modules.spring.enabled = no`
to your configuration.
{% endalert %}

The Spring MVC server instrumentation gives you traces and metrics for incoming requests.

HTTP Server Tracing
-------------------
The Spring MVC instrumentation automatically enables Context propagation and distributed tracing for incoming requests
processed with Spring MVC, as well as lower level HTTP server metrics. The gist of the features provided by the instrumentation is:

* Context will be automatically propagated using HTTP headers.
* Server HTTP request Spans will be automatically created and propagated.
* Spans for view rendering operations.
* Lower level HTTP server metrics will be collected for the HTTP server side.


Here's an example trace from [spring-petclinic]:
<img class="img-fluid rounded" src="/assets/img/spring-petclinic-example-trace.png">

{% alert info %}
If You're using DeferredResult for asynchronous controllers, there's good news and bad news.
The good news is that we measure full execution time of response processing! The bad news is that You're in control of 
where the code is run, not Spring, so You'll need to manually propagate the [context], or use an [instrumented executor].
{% endalert %}



HTTP Server Metrics
-------------------

As a lower level part of the instrumentation, Kamon will track the performance of the HTTP Server. You can control
whether HTTP Server metrics will be recorded or not by using the `enabled` setting:

```hcl
kamon.instrumentation.spring {
  server.metrics {
    enabled = yes
  }
}
```

This feature is enabled by default and will collect the following metrics:

{%  include metric-detail.md name="http.server.requests" %}
{%  include metric-detail.md name="http.server.request.active" %}
{%  include metric-detail.md name="http.server.request.size" %}
{%  include metric-detail.md name="http.server.response.size" %}
{%  include metric-detail.md name="http.server.connection.lifetime" %}
{%  include metric-detail.md name="http.server.connection.usage" %}
{%  include metric-detail.md name="http.server.connection.open" %}

Manual Installation
-------------------

In case you are not using the Kamon Bundle, add the dependency below to your build.

{% include dependency-info.html module="kamon-spring" version=site.data.versions.latest.core %}
{% include instrumentation-agent-notice.html %}

[context]: /docs/latest/core/context
[instrumented executor]: /docs/latest/instrumentation/executors/
[spring-petclinic]: https://github.com/spring-projects/spring-petclinic
