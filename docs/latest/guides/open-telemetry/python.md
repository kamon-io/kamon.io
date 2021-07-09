---
title: Python with Kamon APM | Installation Guides
description: >-
  Send traces from your Python services to Kamon APM using OpenTelemetry to get trace preview, trace metrics, and more
layout: docs
---

Python and OpenTelemetry with Kamon APM
========================================

To export [OpenTelemetry] traces to [Kamon APM], you can send them using OTLP via HTTP, by installing and configuring the `opentelemetry-exporter-otlp-proto-http` exporter.
The following example assumes that `flask` is being used, and the `requests` library is used for external call. If this is not the case, remove or add the appropriate instrumentation
libraries.

## 1. Add the Dependencies

Add the following dependencies, here given using `pip`:

{% code_example %}
{%   language shell guides/install/otel-python-flask/pip.sh tag:otel-deps version:latest %}
{% endcode_example %}

## 2. Add Instrumentation Code

To set up instrumentation, you will need to configure the tracer, add an exporter, and register any instrumentation libraries you may need.
In the following example, Flask and `requests` library instrumentation will be added, as described above. Make sure to change the service name to the appropriate name,
as well as enter the correct [api key] for your environment.

In this example application, we include our instrumentation code, before setting up a simple flask application. The application has three endpoints with very simple
logic. When calling the `/status` endpoint, it will call the other two endpoints.

{% code_example %}
{%   language python guides/install/otel-python-flask/app.py tag:instrumented-flask-app version:latest %}
{% endcode_example %}

## 3. Kamon APM Preview

After starting the server, we can call the `/status` endpoint and start seeing traces, metrics, and more, in Kamon APM!

```bash
curl http://localhost:5000/status
```

{% lightbox /assets/img/apm/otel/otel-flask-trace-details.png %}
Example Application Trace
{% endlightbox %}

{% lightbox /assets/img/apm/otel/otel-flask-operations.png %}
Example Application Operation Metrics
{% endlightbox %}

[OpenTelemetry]: https://opentelemetry.io/
[Kamon APM]: ../../../apm/general/overview/
[api key]: ../../../apm/general/environments/#environment-information