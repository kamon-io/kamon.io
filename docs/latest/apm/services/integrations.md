---
title: 'Kamon APM | Service Integrations | Kamon Documentation'
layout: docs
---

{% include toc.html %}

Service Integrations
=====================

{% lightbox /assets/img/pages/apm/service-integrations.png %}
Service Integrations
{% endlightbox %}

Kamon APM is built with the idea of understanding the metrics sent by Kamon Telemetry and automatically providing some insights for you. If you use it with [JVM](#jvm), [JDBC](#jdbc) drivers or [Akka](#akka), various integrations will be available to you out of the box. On top of that, any service that has server spans will have pre-rolled dashboards to track your general service performance and a per-operation breakdown of calls to your server.

Service Overview
-----------------


{% lightbox /assets/img/pages/apm/service-overview.png %}
Service Overview
{% endlightbox %}

Every service instrumented with either Kamon Telemetry or OpenTelemetry will have a Service Overview page. This page presents you with a quick overview of the state of your service as a _server_. It can be split up into several main areas: the charts, the server operations table, and the sidebar.

#### Service Overview Charts

Each Service Overview page has three charts: a service latency heatmap, the service throughput, and the service error throughput. Using these charts, you can see the general state of your service at a glance, as well as starting a deep dive into issues using the [analyze] button. Note that these charts are only showing **server** spans (i.e., where span kind is `server`), which correspond to processed requests from the outside (e.g., via HTTP or gRPC).

#### Server Operations Table

{% lightbox /assets/img/pages/apm/service-overview-operations.png %}
Service Overview - Server Operations
{% endlightbox %}

The server operations table is a breakdown of all server operations (i.e., where span kind is `server`) for this service. Two separate visualizations are present - a chart and a tabular breakdown. The table can show four different operations, with the default being throughput, chosen by selecting the appropriate operation in the top right corner: throughput, error count, 90th percentile latency, and the 99th percentile latency. The values in the chart will be grouped by operation. Unlike other [charts], this chart does not feature a [legend]. Instead, the table beneath it acts as an extended version of the same. The table features the operation name, and a numeric representation of all four of the values that can be chosen between as chart visualization, as well as a visual indicator that shows where the particular operation's value is in the overall distribution. Each column can be sorted by, and sorting is toggled by clicking on the appropriate table header. Much like in the regular chart legend, hovering a table row will highlight the appropriate value in the chart above.

#### Service Operations Sidebar

{% lightbox /assets/img/pages/apm/service-operations-sidebar.png %}
Service Overview - Sidebar
{% endlightbox %}

The overview sidebar includes two useful pieces of information - the status of [alerts] defined for the service, and the discovered links between this service and other services in your architecture.

The alerts portion of the sidebar will list all of the alerts you have defined for the service in question, as well as their overall status, and the time of the last (or current) [incident]. When an [availability target] has been defined, an additional indicator shaped like a progress bar will be displayed in the top right corner of each alert, showing the service availability status according to that alert. If it is above the set target, the indicator will be green, and otherwise it will be red. Clicking on an alert representation will open the [alert sidebar]. Additionally, above it a small indicator will be shown that states the overall health of the system, according to all of the defined alerts.

The linked services portion of the sidebar is split into two portions: upstream and downstream services. Upstream services are those that are being **called by** the current service, while downstream services are those that **call** the current service. Toggling between them will show a list of services, including their name and a description of the connection type, with that link directionality. These connections map one-on-one to the links visualized in the [service map].

[analyze]: ../../deep-dive/analyze/
[charts]: ../../general/charts/
[legend]: ../../general/charts/#chart-legend
[alerts]: ../../general/alerts/
[incident]: ../../general/alerts/
[availability target]: ../../general/alerts/
[alert sidebar]: ../../general/alerts/
[service map]: ../service-map/#service-links
