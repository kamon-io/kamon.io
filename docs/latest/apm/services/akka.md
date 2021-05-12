---
title: 'Kamon APM | JVM Integration | Kamon Documentation'
describe: "Monitor Akka actors, dispatchers, routers, time in mailbox, and more with pre-made dashboard starting for free with Kamon APM"
layout: docs
---

{% include toc.html %}

Akka Integrations
=================


Kamon APM offers a number of built-in integrations for services running Akka, allowing you to easily deep-dive into your actor systems. The particular services will depend on the features your application is using and, in the case of per-actor metrics, configuration. All available Akka integrations can be accessed from the [service overview], using the Akka dropdown tab in the top of the page.

{% alert info %}
All Akka integration dashboards are a feature exclusive to Kamon Telemetry. If you use OpenTelemetry with a service using Akka, you will not have access to these dashboards.
{% endalert %}

#### Akka Actor System Dashboards

{% lightbox /assets/img/pages/apm/akka-actor-systems.png %}
Akka Actor Systems Integration
{% endlightbox %}

Kamon will collect the [actor system metrics] from inside of your Akka application, and display them in a handy [aggregation summary chart]. You can see your message throughput or count, grouped per actor system, as well as the number of dead letters (messages that were not delivered) and the maximum number of active actors in the time period. Actor systems will be automatically identified by the Kamon instrumentation. Clicking on any of the table rows will take you the per-system details page.

{% lightbox /assets/img/pages/apm/akka-actor-system-details.png %}
Akka Actor System Details Integration
{% endlightbox %}

This page will show the same general metrics, but in a chart format which can then be used as a starting point for a deeper analysis and investigation. Namely, throughput of messages, the number of tracked versus untracked messages, the number of dead letters, the distribution of active actors through time, and the number of unhandled messages will be shown.

#### Akka Actor Dashboards

{% lightbox /assets/img/pages/apm/akka-actors.png %}
Akka Actors Integration
{% endlightbox %}

If [actor metrics] are [enabled][enable_actor_metrics], Kamon APM will keep display information about them in the form of an [aggregation summary chart]. Message throughput, the number of processed messages, and the number of errors are tracked, with the first two being available in a chart visualization. All of them can be sorted in the table, and hovering on any table row will highlight the corresponding entry in the chart. Clicking on any of the table rows will take you to the details page for that specific actor. In the first cell, the actor path will be shown in the first row, while the second row shows the actor's Akka actor system, as well as the class which implements the actor.

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

[service overview]: ../service-details/
[aggregation summary chart]: ../../general/aggregation-summary-chart/
[percentiles chart]: ../../general/charts/#percentile-charts
[actor system metrics]: ../../../instrumentation/akka/metrics/#actor-system-metrics
[actor metrics]: ../../../instrumentation/akka/metrics/#actor-metrics
[enable_actor_metrics]: ../../../instrumentation/akka/metrics/#filtered-metrics
[actor group metrics]: ../../../instrumentation/akka/metrics/#actor-group-metrics