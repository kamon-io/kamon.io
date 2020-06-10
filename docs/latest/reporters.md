---
layout: docs
title: 'Kamon Reporters | Kamon Documentation'
---

Reporters
=========

Reporters are a special kind of module that receive periodic snapshots of the telemetry data collected by Kamon and send
this data (or expose it) to external systems. Reporters are organized in three main sections:


## Combined Reporters

These reporters can handle both metrics and spans data collected by Kamon:
  - **[Kamon APM][apm]**. Sends data to Kamon APM over the HTTP API.
  - **[Datadog][datadog]**. Bundles three separate reporters that can send metrics and spans to the Datadog Agent or the Datadog API.
  - **[New Relic][newrelic]**. Sends metrics and spans to New Relic.

## Metrics Only

These reporters can send metrics data to external systems:
  - **[Prometheus][prometheus]**. Exposes a scrape endpoint with the Prometheus exposition format.
  - **[InfluxDB][influxdb]**. Sends data to InfluxDB using the line protocol.

## Spans Only
Span reporters send Spans to external systems:
  - **[Zipkin][zipkin]**. Sends spans to Zipkin using the V2 API.
  - **[Jaeger][jaeger]**. Sends spans to Jaeger's Collector via HTTP or Agent via UDP.


[apm]: ./apm/
[datadog]: ./datadog/
[influxdb]: ./influxdb/
[jaeger]: ./jaeger/
[newrelic]: ./newrelic/
[prometheus]: ./prometheus/
[sematext]: ./sematext-spm/
[statsd]: ./statsd/
[zipkin]: ./zipkin/