---
title: 'Kamon APM | JDBC Integration | Kamon Documentation'
describe: "Learn about Kamon APM's integrations for JDBC operation tracing and Hikari connection pool metrics"
layout: docs
---

{% include toc.html %}

JDBC Integration
================

{% lightbox /assets/img/apm/jdbc-page.png %}
JDBC Integration
{% endlightbox %}

If your service is running one of the [supported database drivers][jdbc-metrics], Kamon APM will automatically make available to you a JDBC integration page, which will show a list of all sampled spans related to database queries. It can be used to inspect, sort, and dive into [trace details]. Read about the [trace list] for more information.

Additionally, if using one or more [Hikari connection pools][connection-pool-metrics], each pool will get its dedicated charts displaying the following metrics for the connection pool: the distribution of the number of open connections, the distribution of concurrently borrowed collection counts, the 99th percentile of borrow times for those connections, and durations of any borrow timeouts occurring in your application.

The JDBC integration can be access from the [service overview] page, where a JDBC tab will appear on the top of the page if the integration is detected. 

{% alert info %}
JDBC integration dashboards are a feature exclusive to Kamon Telemetry. If you use OpenTelemetry with a service using JDBC, you will not have access to this dashboard.
{% endalert %}

[service overview]: ../service-details/
[jdbc-metrics]: ../../../instrumentation/jdbc/statement-tracing/
[connection-pool-metrics]: ../../../instrumentation/jdbc/hikari/
[trace list]: ../../traces/trace-list/
[trace details]: ../../traces/trace-details/
