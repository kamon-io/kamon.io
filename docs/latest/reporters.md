---
layout: docs
title: 'Kamon Reporters | Kamon Documentation'
---

Reporters
=========

Reporters are a special kind of module that receive periodic snapshots of the telemetry data collected by Kamon and send
this data (or expose it) to external systems. Reporters are organized in three main sections:


## Combined Reporters

Combined reporters can handle both metrics and spans data collected by Kamon. At the moment, the [Kamon APM][apm]
reporter is the only combined reporter.

## Metrics Reporters

These reporters can send metrics data to external systems. The available reporters are:
  - [Prometheus][prometheus]. Exposes a scrape endpoint with the Prometheus exposition format.
  - [InfluxDB][influxdb]. Sends data to InfluxDB using the line protocol.
  - [StatsD][statsd]. Sends data to StatsD over UDP.
  - [Datadog][datadog]. Sends data to the Datadog Agent or the Datadog API.
  - [Sematext SPM][sematext]. Sends data to Sematext through their public API.

## Span Reporters
Span reporters send Spans to external systems.
  - [Zipkin][zipkin]. Sends data to zipkin using the V2 API.
  - [Jaeger][jaeger]. Sends data to Jaeger's HTTP API.


[apm]: ./apm/
[prometheus]: ./prometheus/
[influxdb]: ./influxdb/
[statsd]: ./statsd/
[datadog]: ./datadog/
[sematext]: ./sematext-spm/
[zipkin]: ./zipkin/
[jaeger]: ./jaeger/