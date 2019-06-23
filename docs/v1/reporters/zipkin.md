---
title: 'Sending Spans to Zipkin with Kamon | Kamon Documentation'
layout: docs
redirect_from:
  - /documentation/1.x/reporters/zipkin/
---

{% include toc.html %}

Sending Spans to Zipkin
=======================

[Zipkin][1] is distributed tracing system. It was originally created at Twitter but currently is run as a completely
independent open source project. Most people would recognize Zipkin as _the_ distributed tracing platform since it has
a really strong and active community and it is integrated with most major libraries and toolkits on the JVM.

The `kamon-zipkin` module translates Kamon's representation of Spans and sends them to Zipkin's v2 API.


## Installation and Startup

{% include dependency-info.html module="kamon-zipkin" version="1.0.0" %}

Once you have the dependency on your classpath, start the reporter:

{% code_block scala %}
import kamon.zipkin.ZipkinReporter

Kamon.addReporter(new ZipkinReporter())
{% endcode_block scala %}

That's it. Go to the Zipkin UI and start browsing your traces.

## Configuration

It couldn't be simpler. All you need to provide is the host and port where Zipkin is listening.

{% code_block typesafeconfig %}
kamon.zipkin {
  host = "localhost"
  port = 9411
}
{% endcode_block scala %}


## Visualization and Fun

These are extracted from our [Monitoring Akka Quickstart][2] recipe, head over there to learn more about how to get
started with Monitoring Akka with Kamon!

Tracer view in Zipkin:

<img class="img-fluid my-4" src="/assets/img/recipes/quickstart-zipkin-trace.png">

Span details:

<img class="img-fluid my-4" src="/assets/img/recipes/quickstart-zipkin-span-detail.png">

[1]: https://zipkin.io/
[2]: /documentation/1.x/recipes/monitoring-akka-quickstart/
