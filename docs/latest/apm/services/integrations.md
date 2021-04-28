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

The server operations table is a breakdown of all server operations (i.e., where span kind is `server`) for this service. Two separate visualizations are present - a chart and a tabular breakdown. The table can show four different operations, with the default being throughput, chosen by selecting the appropriate operation in the top right corner: throughput, error count, 90th percentile latency, and the 99th percentile latency. The values in the chart will be grouped by operation. Unlike other [charts], this chart does not feature a [legend]. Instead, the table beneath it acts as an extended version of the same. The table features the operation name, and a numeric representation of all four of the values that can be chosen between as chart visualization, as well as a visual indicator that shows where the particular operation's value is in the overall distribution. Each column can be sorted by, and sorting is toggled by clicking on the appropriate table header. Much like in the regular chart legend, hovering a table row will highlight the appropriate value in the chart above. Clicking on any table row will take you to the [operation details](#operation-details) page for that particular operation.

#### Service Operations Sidebar

{% lightbox /assets/img/pages/apm/service-operations-sidebar.png %}
Service Overview - Sidebar
{% endlightbox %}

The overview sidebar includes two useful pieces of information - the status of [alerts] defined for the service, and the discovered links between this service and other services in your architecture.

The alerts portion of the sidebar will list all of the alerts you have defined for the service in question, as well as their overall status, and the time of the last (or current) [incident]. When an [availability target] has been defined, an additional indicator shaped like a progress bar will be displayed in the top right corner of each alert, showing the service availability status according to that alert. If it is above the set target, the indicator will be green, and otherwise it will be red. Clicking on an alert representation will open the [alert sidebar]. Additionally, above it a small indicator will be shown that states the overall health of the system, according to all of the defined alerts.

The linked services portion of the sidebar is split into two portions: upstream and downstream services. Upstream services are those that are being **called by** the current service, while downstream services are those that **call** the current service. Toggling between them will show a list of services, including their name and a description of the connection type, with that link directionality. These connections map one-on-one to the links visualized in the [service map].

Service Operations
--------------------

{% lightbox /assets/img/pages/apm/service-operations-page.png %}
Service Operations
{% endlightbox %}

The Service Operations page will be defined for any service sending telemetry data to Kamon APM, including OpenTelemetry-instrumented services. The service is very similar to the [overview operations table](#service-operations-table), with the same chart with four operations (throughput, error count, 90th and 99th percentiles of latency), grouping per service, and table legend behaviour. However, there are two crucial differences: Firstly, _all_ operations are represented here, not just server span operations. Secondly, in the first column, underneath the service name, two more entries will be present, if applicable. The first is the span kind, and the second is the instrumentation component that is the source of this information, with the two values separated by a slash (`/`) character. Clicking on any table row will take you to the [operation details](#operation-details) page for that particular operation.

Operation Details
-------------------

{% lightbox /assets/img/pages/apm/operation-details.png %}
Service Operation Details
{% endlightbox %}

The Service Operation Details page offers a drill-down into information gathered about one particular operation. Like the Overview, it contains the Latency histogram and the throughput and error charts. However, unlike the overview page, only trace metric information gathered for this particular operation is included in the charts.

Underneath the charts is located a trace list, with all sampled traces belonging to this operation. It can be used to inspect, sort, and dive into trace details. Read about the [trace list] for more information.

JVM
-----

{% lightbox /assets/img/pages/apm/jvm-page.png %}
JVM Integration
{% endlightbox %}

Services running on the JVM and collecting the [JVM metrics][jvm-metrics] will have a JVM integration tab, displaying charts with information collected about the Java virtual machine your service is running on. They can be split into three main categories:

The top half of the page displays garbage collection metrics - the duration and number of garbage collection intervals, with an additional split between young and old GC generations. The second section is dedicated to heap and memory usage. Here you can see the memory usage, both in heap and outside of the heap, as well as additional memory allocations in your code and promotions of memory chunks to the old generation. Finally, the third section shows durations of [JVM hiccups], as well as their distribution in a [percentiles chart].

One particular correlation that can be of interest is that between hiccups and garbage collection time. If you have many, or very long, hiccups that do not correlate very well to the garbage collection times, it might mean that there are issues in your host setup, or that other processes are taking up resources that your service running on JVM might make better use of instead.

JDBC
-----

{% lightbox /assets/img/pages/apm/jdbc-page.png %}
JDBC Integration
{% endlightbox %}

If your service is running one of the [supported database drivers][jdbc-metrics], Kamon APM will automatically make available to you a JDBC integration page, which will show a list of all traces related to database queries. It can be used to inspect, sort, and dive into trace details. Read about the [trace list] for more information.

Additionally, if using one or more [Hikari connection pools][connection-pool-metrics], each pool will get its dedicated charts displaying the following metrics for the connection pool: the distribution of the number of open connections, the distribution of concurrently borrowed collection counts, the 99th percentile of borrow times for those connections, and durations of any borrow timeouts occurring in your application.

Akka
------

Kamon APM offers a number of built-in integrations for services running Akka, allowing you to easily deep-dive into your actor systems. The particular services will depend on the features your application is using and, in the case of per-actor metrics, configuration.

#### Akka Actor System Dashboards

{% lightbox /assets/img/pages/apm/akka-actor-systems.png %}
Akka Actor Systems Integration
{% endlightbox %}

Kamon will collect the [actor system metrics] from inside of your Akka application, and display them in a handy chart and list. You can see your message throughput or count, grouped per actor system, as well as the number of dead letters (messages that were not delivered) and the maximum number of active actors in the time period. Actor systems will be automatically identified by the Kamon instrumentation. Clicking on any of the table rows will take you the per-system details page.

{% lightbox /assets/img/pages/apm/akka-actor-system-details.png %}
Akka Actor System Details Integration
{% endlightbox %}

This page will show the same general metrics, but in a chart format which can then be used as a starting point for a deeper analysis and investigation. Namely, throughput of messages, the number of tracked versus untracked messages, the number of dead letters, the distribution of active actors through time, and the number of unhandled messages will be shown.

#### Akka Actor Dashboards

{% lightbox /assets/img/pages/apm/akka-actors.png %}
Akka Actors Integration
{% endlightbox %}

If [actor metrics] are [enabled][enable_actor_metrics], Kamon APM will keep display information about them in the form of a chart and a list with a numerical breakdown. Message throughput, the number of processed messages, and the number of errors are tracked, with the first two being available in a chart visualization. All of them can be sorted in the table, and hovering on any table row will highlight the corresponding entry in the chart. Clicking on any of the table rows will take you to the details page for that specific actor. In the first cell, the actor path will be shown in the first row, while the second row shows the actor's Akka actor system, as well as the class which implements the actor.

{% lightbox /assets/img/pages/apm/akka-actor-details.png %}
Akka Actor Details Integration
{% endlightbox %}

The details page will focus on a particular actor, and will show the following metrics in a chart:

* Actor message throughput
* Actor throughput, per service instance
* Actor errors, as throughput, per service instance
* Actor message processing time distribution
* Actor message processing time [percentiles chart]
* Messages' time in mailbox distribution
* Actor mailbox size distribution

{% alert warning %}
Note that actor metrics require [configuration][enable_actor_metrics] and are not collected by default. This is due to the fact that you might hundreds or even thousands of actors in your system, and may not wish to instrument them by default.
{% endalert %}

#### Akka Actor Group Dashboards

{% lightbox /assets/img/pages/apm/actor-groups.png %}
Akka Actor Groups Integration
{% endlightbox %}

When using Kamon Telemetry and Kamon APM together, you will also have access to [actor group metrics]. As mentioned in the previous section, it is not always plausible to keep track of every actor, but with Kamon Telemetry, automatic or configuration-based grouping of actors into actor groups allows us to intelligently group and display useful metrics about your actors while still keeping the number of groups to a manageable level.

{% lightbox /assets/img/pages/apm/actor-group-details.png %}
Akka Actor Group Details Integration
{% endlightbox %}

#### Akka Dispatcher Dashboards

{% lightbox /assets/img/pages/apm/akka-dispatchers.png %}
Akka Dispatchers Integration
{% endlightbox %}

{% lightbox /assets/img/pages/apm/akka-dispatcher-details.png %}
Akka Dispatcher Details Integration
{% endlightbox %}

#### Akka Router Dashboards

{% lightbox /assets/img/pages/apm/akka-routers.png %}
Akka Routers Integration
{% endlightbox %}

{% lightbox /assets/img/pages/apm/akka-router-details.png %}
Akka Router Details Integration
{% endlightbox %}

#### Akka Cluster Shard Dashboards

#### Akka Remote Dashboard

[analyze]: ../../deep-dive/analyze/
[charts]: ../../general/charts/
[legend]: ../../general/charts/#chart-legend
[alerts]: ../../general/alerts/
[incident]: ../../general/alerts/
[availability target]: ../../general/alerts/
[alert sidebar]: ../../general/alerts/
[service map]: ../service-map/#service-links
[trace list]: ../../traces/trace-list/
[jvm-metrics]: ../../../instrumentation/system/jvm-metrics/
[JVM hiccups]: https://www.azul.com/giltene-how-java-got-the-hiccups/
[percentiles chart]: ../../general/charts/#percentiles-chart
[jdbc-metrics]: ../../../instrumentation/jdbc/statement-tracing/
[connection-pool-metrics]: ../../../instrumentation/jdbc/hikari/
[actor system metrics]: ../../../instrumentation/akka/metrics/#actor-system-metrics
[actor metrics]: ../../../instrumentation/akka/metrics/#actor-metrics
[enable_actor_metrics]: ../../../instrumentation/akka/metrics/#filtered-metrics
[actor group metrics]: ../../../instrumentation/akka/metrics/#actor-group-metrics
