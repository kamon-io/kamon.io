---
title: NodeJS with Kamon APM | Installation Guides
description: >-
  Send traces from your NodeJS services, in JavaScript or Typescript, to Kamon APM using OpenTelemetry to get trace preview, trace metrics, and more
layout: docs
---

NodeJS and OpenTelemetry with Kamon APM
========================================

To export [OpenTelemetry] traces to [Kamon APM], you can send them using OTLP via HTTP, by installing and configuring the `@opentelemetry/exporter-collector-proto` exporter.
The following example assumes that `express` is being used, and HTTP(S) is used as the communication channel. If this is not the case, remove or add the appropriate instrumentation
libraries.

## 1. Add the Dependencies

Add the following dependencies using `npm` or `yarn`:

{% code_example %}
{%   language shell guides/install/otel-node/npm.sh tag:otel-deps version:latest label:"npm" %}
{%   language shell guides/install/otel-node/yarn.sh tag:otel-deps version:latest label:"yarn" %}
{% endcode_example %}

## 2. Add Instrumentation Code

To set up instrumentation, you will need to configure node tracing, add an exporter, and register any instrumentation libraries you may need.
In the following example, HTTP and Express instrumentation will be added, as described above. Make sure to change the `serviceName` to the appropriate name,
as well as enter the correct [api key] for your environment. When deploying the application to production, make sure to also adjust the `DiagLogLevel` to
avoid having your logs flooded with debug logs.

We would suggest to place this code into a separate file (in this example we use `instrumentation.js`/`instrumentation.ts`), but the same code can also be placed
at the top of your entry file (e.g., `index.js`).

{% code_example %}
{%   language javascript guides/install/otel-node/instrumentation.js version:latest tag:instrumentation label:"JavaScript" %}
{%   language typescript guides/install/otel-node/instrumentation.ts version:latest tag:instrumentation label:"TypeScript" %}
{% endcode_example %}

## 3. Example Application

In this example application, we include our instrumentation code, before setting up a simple express application. The application has three endpoints with very simple
logic. When calling the `/status` endpoint, it will call the other two endpoints.

{% alert warning %}
Make sure to include or import the instrumentation code before importing any other libraries. Otherwise, the appropriate libraries might not be instrumented correctly!
{% endalert %}

{% code_example %}
{%   language javascript guides/install/otel-node/app.js version:latest tag:app label:"JavaScript" %}
{%   language typescript guides/install/otel-node/app.ts version:latest tag:app label:"TypeScript" %}
{% endcode_example %}

## 4. Kamon APM Preview

After starting the server, we can call the `/status` endpoint and start seeing traces, metrics, and more, in Kamon APM!

```bash
curl http://localhost:8080/status
```

{% lightbox /assets/img/apm/otel/otel-trace-details.png %}
Example Application Trace
{% endlightbox %}

{% lightbox /assets/img/apm/otel/otel-operations.png %}
Example Application Operation Metrics
{% endlightbox %}

[OpenTelemetry]: https://opentelemetry.io/
[Kamon APM]: ../../../apm/general/overview/
[api key]: ../../../apm/general/environments/#environment-information
