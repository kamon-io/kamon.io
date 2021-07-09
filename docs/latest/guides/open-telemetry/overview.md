---
title: OpenTelemetry with Kamon APM | Installation Guides
description: >-
  Send traces to Kamon APM using OpenTelemetry to get trace preview, trace metrics, and more
layout: docs
---

Using OpenTelemetry with Kamon APM
==================================

This guide describes how [OpenTelemetry] can be used with [Kamon APM] in general terms, without going into the specifics of the languages or frameworks used.
For technology-specific guides, you can view instructions for [NodeJS], [Python], or [Go].

{% lightbox /assets/img/apm/otel/cerdo-trace-details.png %}
Trace Details for Service Instrumented with OpenTelemetry
{% endlightbox %}

[Kamon APM] can ingest OpenTelemetry [traces], connect them across services, and provide useful metrics by automatically processing and analyzing the span and trace data.
There exist a large number of instrumentations for [OpenTelemetry], which can be viewed in the [registry](https://opentelemetry.io/registry/).

{% alert info %}
At this time, only OpenTelemetry traces can be ingested by Kamon APM. OpenTelemetry logs and metrics are not supported.
{% endalert %}

The traces can be sent from the application code or via the [OpenTelemetry Collector], via the OTLP protocol over HTTP.

{% alert info %}
To gather full and correct metrics from OpenTelemetry traces, make sure that you are sampling all of the spans from your application. Otherwise, the information in KamonAPM
might not be fully correct or complete.
{% endalert %}

Exporting traces from your application
---------------------------------------

To export your traces from within a certain application, you can set it up to export traces in the OTLP format, over HTTP. The implementation will depend on your platform and
language of choice - for examples, see the [NodeJS], [Python], and [Go] guides. In general terms, however, [Kamon APM] will ingest the format as-is if:

* Traces are exported to `https://otel.apm.kamon.io/v1/traces`
* A header `x-kamon-apikey` is included, with the value matching your [api key]

{% alert warning %}
Make sure to send the data over the HTTP protocol, as gRPC is currently not supported by Kamon APM!
{% endalert %}

Export traces with the OpenTelemetry Collector
----------------------------------------------

The [OpenTelemetry Collector] is a vendor-agnostic way of collecting data from any number of your instrumented services and applications. It can ingest data from any applications
instrumented with [OpenTelemetry] SDKs, but also the Jaeger, Zipkin, or OpenCensus formats.

To export data, you must configure an OTLP exporter to send data to [Kamon APM], with the appropriate header:

{% code_block yaml %}
exporters:
  otlphttp:
    endpoint: "https://otel.apm.kamon.io"
    headers:
      "x-kamon-apikey": "YOUR_API_KEY"
{% endcode_block %}

The exporter will also need to be included in your pipeline as a trace exporter:

{% code_block yaml %}
service:
  pipelines:
    traces:
      exporters: [otlphttp]
{% endcode_block %}

A complete example that consumes OTLP via gRPC and HTTP and exports it using OTLP via HTTP, with batching:

{% code_block yaml %}
receivers:
  otlp:
    protocols:
      grpc: # on port 55680
      http: # on port 55681

processors:
  batch:

exporters:
  otlphttp:
    endpoint: "https://otel.apm.kamon.io"
    headers:
      "x-kamon-apikey": "YOUR_API_KEY"

service:
  pipelines:
    traces:
      receivers: [otlp]
      processors: [batch]
      exporters: [otlphttp]
{% endcode_block %}

You can then start the [OpenTelemetry] Collector with the following command:

{% code_example %}
{%   language shell guides/install/otel-node/run-collector.sh tag:run-collector version:latest label:"shell" %}
{% endcode_example %}

[OpenTelemetry]: https://opentelemetry.io/
[NodeJS]: ../node-js/
[Python]: ../python/
[Go]: ../go/
[Kamon APM]: ../../../apm/general/overview/
[traces]: https://github.com/open-telemetry/opentelemetry-specification/blob/main/specification/overview.md#trace
[api key]: ../../../apm/general/environments/#environment-information
[OpenTelemetry Collector]: https://opentelemetry.io/docs/collector/
