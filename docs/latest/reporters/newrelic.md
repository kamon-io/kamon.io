---
title: 'Sending Metrics to New Relic with Kamon | Kamon Documentation'
layout: docs
---

{% include toc.html %}

New Relic Reporter
================

[New Relic] is a cloud-based platform that gives developers, engineers, operations, and management a clear view of what’s happening in today’s complex software environments. So you can find and fix problems faster, build high-performing DevOps teams, and deliver delightful experiences for your customers.

## Installation

{% include dependency-info.html module="kamon-newrelic" version=site.data.versions.latest.newrelic %}

Once the reporter is on your classpath it will be automatically picked up by Kamon, just make sure that you add the [New Relic Insert API key] for your environment in the configuration:

{% code_block hcl %}
kamon.newrelic {
    # A New Relic Insights API Insert Key is required to send trace data to New Relic
    # https://docs.newrelic.com/docs/apis/get-started/intro-apis/types-new-relic-api-keys#insert-key-create
    nr-insights-insert-key = ${?INSIGHTS_INSERT_KEY}
}
{% endcode_block %}

If you can't use an environment variable, you can replace the above with the actual key like
`nr-insights-insert-key = abc123secretvalue123`.

Note: This is less secure and additional precautions must be taken (like considering file permissions and excluding from source control).

## Configuration

### Disable tracing

To disable tracing, simply comment out `span-metrics` for the appropriate module.

## Teasers

<img class="img-fluid my-4" src="/assets/img/newrelic-overview.png">

[New Relic One distributed tracing] provides a powerful tool for visualizing all of your traces in a meaningful and convenient manner so that you can quickly diagnose any performance issues:

<img class="img-fluid my-4" src="/assets/img/newrelic-tracing.png">

[New Relic One dashboards] allow you to combine data from anywhere in the New Relic platform, including dimensional metrics and trace data sent by the Kamon New Relic Reporter, to build flexible, interactive visualizations. You can easily explore your data and correlate connected sources with tailored, user-friendly charts, and quickly learn the state of your system and applications for faster, more efficient troubleshooting.

<img class="img-fluid my-4" src="/assets/img/newrelic-dashboard.png">

[New Relic]: https://newrelic.com/

[New Relic Insert API key]: https://docs.newrelic.com/docs/apis/get-started/intro-apis/types-new-relic-api-keys#insert-key-create

[New Relic One distributed tracing]: https://docs.newrelic.com/docs/understand-dependencies/distributed-tracing/ui-data/additional-distributed-tracing-features-new-relic-one

[New Relic One dashboards]: https://docs.newrelic.com/docs/dashboards/new-relic-one-dashboards/get-started/introduction-new-relic-one-dashboards