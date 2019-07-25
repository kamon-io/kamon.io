---
title: 'Sending Spans to Zipkin with Kamon | Kamon Documentation'
layout: docs
redirect_from:
  - /documentation/1.x/reporters/zipkin/
---

{% include toc.html %}

Zipkin Reporter
===============

[Zipkin][1] is distributed tracing system. It was originally created at Twitter but currently is run as a completely
independent open source project. Most people would recognize Zipkin as _the_ distributed tracing platform since it has
a really strong and active community and it is integrated with most major libraries and toolkits on the JVM.

The `kamon-zipkin` module translates Kamon's representation of Spans and sends them to Zipkin's v2 API.


## Installation

{% include dependency-info.html module="kamon-zipkin" version=site.data.versions.latest.zipkin %}

Once the reporter is on your classpath it will be automatically picked up by Kamon.

## Configuration

This module points to `localhost` by default, but you can easily set the Zipkin address, port and protocol with these
configuration settings:

{% code_block hcl %}
kamon.zipkin {

  # Hostname and port where the Zipkin Server is running
  #
  host = "localhost"
  port = 9411

  # Decides whether to use HTTP or HTTPS when connecting to Zipkin
  protocol = "http"
}
{% endcode_block %}


## Teasers

These are extracted from our [Elementary Akka Setup][2] guide, head over there to learn more about how to get
started with Monitoring Akka with Kamon!

Tracer view in Zipkin:

<img class="img-fluid my-4" src="/assets/img/recipes/quickstart-zipkin-trace.png">

Span details:

<img class="img-fluid my-4" src="/assets/img/recipes/quickstart-zipkin-span-detail.png">

[1]: https://zipkin.io/
[2]: ../../guides/frameworks/elementary-akka-setup/
