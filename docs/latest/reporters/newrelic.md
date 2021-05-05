---
title: 'Sending Metrics to New Relic with Kamon | Kamon Documentation'
description: 'How to set up sending metrics collected with Kamon Telemetry to New Relic'
layout: docs
---

{% include toc.html %}

New Relic Reporter
================

New Relic is a cloud-based platform that gives developers, engineers, operations, and management a clear view of what’s happening in software environments. 

## Installation

{% include dependency-info.html module="kamon-newrelic" version=site.data.versions.latest.newrelic %}

Once the reporter is on your classpath it will be automatically picked up by Kamon, just make sure that you add the [New Relic Insert API key] for your environment in the configuration:

{% code_block hcl %}
kamon.newrelic {
    # A New Relic Insights API Insert Key is required to send trace data to New Relic
    # https://docs.newrelic.com/docs/apis/get-started/intro-apis/types-new-relic-api-keys#insert-key-create
    nr-insights-insert-key = ${?INSIGHTS_INSERT_KEY}

    # Change the metric endpoint to the New Relic EU region
    # metric-ingest-uri = https://metric-api.eu.newrelic.com/metric/v1

    # Change the trace endpoint to the New Relic EU region
    # span-ingest-uri = https://trace-api.eu.newrelic.com/trace/v1
}
{% endcode_block %}

If you can't use an environment variable, you can replace the above with the actual key like
`nr-insights-insert-key = abc123secretvalue123`.

Note: This is less secure and additional precautions must be taken (like considering file permissions and excluding from source control).

## Configuration
The following configuration options are available to add to the `kamon.newrelic` configuration block.

### Override Endpoints
- `metric-ingest-uri`: By default, metrics are sent to the New Relic US region endpoint. You can change the endpoint to send metrics to New Relic's EU region. See [API endpoints for EU region accounts].
- `span-ingest-uri`: By default, spans are sent to the New Relic US region endpoint. You can change the endpoint to send spans to an Infinite Tracing trace observer endpoint or to New Relic's EU region. See [Infinite Tracing] and [API endpoints for EU region accounts].  

### Disable tracing

To disable tracing, simply comment out `span-metrics` for the appropriate module.

## Teasers

{% lightbox /assets/img/newrelic-overview.png %}
New Relic Overview
{% endlightbox %}

[New Relic One distributed tracing] provides a tool for visualizing all of your traces so that you can quickly diagnose performance issues:

{% lightbox /assets/img/newrelic-tracing.png %}
New Relic Tracing
{% endlightbox %}

[New Relic One dashboards] allow you to combine data from anywhere in the New Relic platform, including dimensional metrics and trace data sent by the Kamon New Relic Reporter, to build interactive visualizations. You can explore your data and correlate connected sources with tailored and learn the state of your system and applications for efficient troubleshooting.

{% lightbox /assets/img/newrelic-dashboard.png %}
New Relic Dashboard
{% endlightbox %}

[New Relic]: https://newrelic.com/

[New Relic Insert API key]: https://docs.newrelic.com/docs/apis/get-started/intro-apis/types-new-relic-api-keys#insert-key-create

[New Relic One distributed tracing]: https://docs.newrelic.com/docs/understand-dependencies/distributed-tracing/ui-data/additional-distributed-tracing-features-new-relic-one

[New Relic One dashboards]: https://docs.newrelic.com/docs/dashboards/new-relic-one-dashboards/get-started/introduction-new-relic-one-dashboards

[API endpoints for EU region accounts]: https://docs.newrelic.com/docs/using-new-relic/welcome-new-relic/get-started/our-eu-us-region-data-centers#endpoints

[Infinite Tracing]: https://docs.newrelic.com/docs/understand-dependencies/distributed-tracing/enable-configure/integrations-enable-distributed-tracing
