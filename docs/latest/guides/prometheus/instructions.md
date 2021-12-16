---
title: Prometheus with Kamon APM | Installation Guides
description: >-
  Send metrics to Kamon APM using Prometheus and create dashboards, alerts, and more
layout: docs
---

Using Prometheus with Kamon APM
==================================

This guide describes how [Prometheus] can be used with [Kamon APM] in general terms, without going into the specifics of the languages and tools used, outside of
[Prometheus] itself. See the [Prometheus Getting Started Guide](https://prometheus.io/docs/prometheus/latest/getting_started/) for more general information.

[Prometheus] can be configured to send data to [Kamon APM] via the [remote write] feature. You will need to configure it as follows, replacing `YOUR_API_KEY` with
the [api key] from [Kamon APM].

{% code_block yaml %}
remote_write:
  - url: "https://prometheus.apm.kamon.io/v1/metrics"
    headers:
      "x-kamon-apikey": "YOUR_API_KEY"
{% endcode_block %}

You **must not** set the following configuration for [Prometheus] to inter-operate with [Kamon APM].

{% code_block yaml %}
  metadata_config:
    send: false # DO NOT DO THIS!
{% endcode_block %}

The default value, `true`, will ensure that data is correctly sent.

When configuring [Prometheus] scrape jobs, [Kamon APM] will interpret the `job_name` as the [service] name, and the `static_config` target URL as the `instance` name.
Data will be grouped by these tags in [Kamon APM], and can be used for breakdown and analysis. Both of these can be overridden by specifying custom labels in your
configuration file.

{% code_block yaml %}
scrape_configs:
  - job_name: "my-app"

    static_configs:
    - targets: ["localhost:8080"]
      labels:
        host: "my-host"
        instance: "my-app@my-host"

        # uncomment to override the service name
        # service: "better-service-name"
{% endcode_block %}

Alternatively, labels can be set up inside of the app being scraped. However, to do that, you will need to make the following change to the [Prometheus] configuration:

{% code_block yaml %}
scrape_configs:
  - job_name: "my-app"

    static_configs:
    - targets: ["localhost:8080"]
      honor_labels: true # When set to true, labels will not be re-mapped
{% endcode_block %}

Viewing Prometheus Metrics
---------------------------

{% lightbox /assets/img/apm/prometheus-dashboard.png %}
Custom Dashboard with Prometheus Metrics
{% endlightbox %}

[Prometheus] metrics will map into a [counter], [gauge], or [histogram] in [Kamon APM], and can be visualized in the [available chart types] for that metric type.
These charts can be then assembled into [custom dashboards].

[Prometheus]: https://prometheus.io/
[Kamon APM]: ../../../apm/general/overview/
[remote write]: https://prometheus.io/docs/prometheus/latest/configuration/configuration/#remote_write
[api key]: ../../../apm/general/environments/#environment-information
[custom dashboards]: ../../../apm/dashboards/introduction/
[available chart types]: ../../../apm/general/charts/
[counter]: ../../../core/metrics/#counters
[gauge]: ../../../core/metrics/#gauges
[histogram]: ../../../core/metrics/#histograms