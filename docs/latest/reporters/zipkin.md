---
title: 'Sending Spans to Zipkin with Kamon | Kamon Documentation'
description: 'How to set up sending spans collected with Kamon Telemetry to Zipkin'
layout: docs
redirect_from:
  - /documentation/1.x/reporters/zipkin/
---

{% include toc.html %}

Zipkin Reporter
===============

Zipkin is distributed tracing system. It was originally created at Twitter but currently is run as an
independent open source project. 

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

{% lightbox /assets/img/recipes/quickstart-zipkin-trace.png %}
Zipkin Trace View
{% endlightbox %}


Span details:

{% lightbox /assets/img/recipes/quickstart-zipkin-span-detail.png %}
Zipkin Span Details
{% endlightbox %}


[1]: https://zipkin.io/
[2]: ../../guides/frameworks/elementary-akka-setup/
