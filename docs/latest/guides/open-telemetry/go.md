---
title: Go with Kamon APM | Installation Guides
description: >-
  Send traces from your Go services to Kamon APM using OpenTelemetry to get trace preview, trace metrics, and more
layout: docs
---

Go and OpenTelemetry with Kamon APM
====================================


To export [OpenTelemetry] traces to [Kamon APM], you can send them using OTLP via HTTP, by installing and configuring the `otlptracehttp` exporter.

## 1. Add the Dependencies

Firstly, we need to install the [OpenTelemetry] dependencies, by running the following in the directory where your `go.mod` file is located:

{% code_example %}
{%   language shell guides/install/otel-go/deps.sh tag:otel-deps version:latest %}
{% endcode_example %}

## 2. Add Instrumentation Code

To set up instrumentation, you will need to configure the tracer and add an exporter.Make sure to change the service name to the appropriate name,
as well as enter the correct [api key] for your environment.

You can define a simple function that will set up the basic building blocks, as well as provide a teardown, as follows:

{% code_example %}
{%   language go guides/install/otel-go/setup.go tag:setup version:latest %}
{% endcode_example %}

## 3. Add App Code

Following the setup, you can then invoke (and defer teardown) of this function in your application, as in the following example:

{% code_example %}
{%   language go guides/install/otel-go/main.go tag:app version:latest %}
{% endcode_example %}

A complete example with both [OpenTelemetry] setup and an example of using it:

{% code_example %}
{%   language go guides/install/otel-go/main.go tag:go-example version:latest %}
{% endcode_example %}

## 4. Kamon APM Preview

You can now run the app from the directory and start seeing traces in Kamon APM!

```bash
go run .
```

{% lightbox /assets/img/apm/otel/otel-go-trace-details.png %}
Example Application Trace
{% endlightbox %}

{% lightbox /assets/img/apm/otel/otel-go-operations.png %}
Example Application Operation Metrics
{% endlightbox %}


[OpenTelemetry]: https://opentelemetry.io/
[Kamon APM]: ../../../apm/general/overview/
[api key]: ../../../apm/general/environments/#environment-information