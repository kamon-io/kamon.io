---
title: 'Sending Spans to Jaeger with Kamon | Kamon Documentation'
description: ' How to set up sending spans collected with Kamon Telemetry to Jaeger'
layout: docs
redirect_from:
  - /documentation/1.x/reporters/jaeger/
---

{% include toc.html %}

Jaeger Reporter
===============

Jaeger is a distributed tracing system. It was originally created at Uber. The `kamon-jaeger` module translates
Kamon's representation of Spans and sends them to Jaeger using HTTP (collector) or UDP (agent).


## Installation

{% include dependency-info.html module="kamon-jaeger" version=site.data.versions.latest.jaeger %}

Once the reporter is on your classpath it will be automatically picked up by Kamon.


## Configuration



{% code_block hcl %}
kamon {
  jaeger {

    # Define the host/port where the Jaeger Collector/Agent is listening.
    host = "localhost"
    port = 14268

    # Protocol used to send data to Jaeger. The available options are:
    #   - http: Sends spans using jaeger.thrift over HTTP (collector).
    #   - https: Sends spans using jaeger.thrift over HTTPS (collector).
    #   - udp: Sends spans using jaeger.thrift compact over UDP (agent).
    protocol = http

    # Enable or disable including tags from kamon.environment as labels
    include-environment-tags = no
  }
}
{% endcode_block scala %}


## Teasers

These screenshots were taken by running the [Elementary Akka Setup][2] guide with the Jaeger reporter, head over
there to learn more about how to get started with Monitoring Akka with Kamon!

Trace view in Jaeger:

{% lightbox /assets/img/jaeger-trace-search.png %}
Jaeger Trace Search
{% endlightbox %}


Trace Details:

{% lightbox /assets/img/jaeger-trace-detail.png %}
Jaeger Trace Details
{% endlightbox %}

[1]: https://github.com/jaegertracing/jaeger
[2]: ../../guides/frameworks/elementary-akka-setup/
