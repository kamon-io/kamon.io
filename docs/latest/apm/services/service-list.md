---
title: 'Kamon APM | Service List | Kamon Documentation'
layout: docs
---

{% include toc.html %}

Service List
============

Overview
------------

The Service List page offers a quick overview of the services that are sending data to Kamon APM. It displays all of the services in this environment in a tabular format, allowing you to view, sort, and manage them.

Each service entry will show the following information:

* [Service status](#service-status) 
* The name of the service
* A sparkline showing the service latency, service throughput, and service errors
* 99th percentile of service latency
* Service operation throughput
* Service errors
* Detected [integrations]

Specific lines in the sparkline chart can be highlighted by hovering the appropriate column (latency, throughput, or errors) for easier management. Services can also be sorted by name, latency, throughput or number of errors by clicking on the header.

**NOTE:** The numbers shown are only for _server_ spans. This means that database call spans and other client-side and internal spans will not be included.

<div class="w-100 text-center">
  <img class="img-fluid my-4" src="/assets/img/pages/apm/service-list.png" alt="Service List">
</div>

Service Status
---------------

Each service can, depending on its liveness, your subscription plan, and the alerts defined for it, be in one of five states:

| State   | Status      | Description      |
|:--------|:-----------:|------------------|
| Healthy | <img src="/assets/img/pages/apm/status/healthy.svg"> | A service which is sending data and has no active alert incidents |
| Warning | <img src="/assets/img/pages/apm/status/warning.svg"> | A service which is sending data, but has an active alert with the **warning** severity |
| Critical | <img src="/assets/img/pages/apm/status/critical.svg"> | A service which is sending data, but has an active alert with the **critical** severity |
| Dead | <img src="/assets/img/pages/apm/status/dead.svg"> | A service which has stopped sending data |
| Restricted | <img src="/assets/img/pages/apm/status/restricted.svg"> | A service which surpasses the count allowed by the free plan and is obscured |

When multiple alerts have active incidents for the same service, the most severe one will determine its state. You can see the [alerts] documentation for more details.

Dead services are those services which are no longer sending data to the system. This could be due to them being stopped, or due to an issue in your configuration. Historical data for these services will still be shown, as long as some data was sent inside of the currently selected period.

Restricted services are those services which go beyond the maximum number of services allowed by your subscription plan. On the Free Plan, you can have **up to 5** services at the same time, but any additional services will be restricted. While data sent by them will be ingested, you will not be able to see or use the data in any way, unless you delete another existing service to reduce to number to 5 or below, or upgrade to a paid plan. On **all** paid plans, the number of services is **unrestricted**. If clicked, these services will show the Upgrade Prompt, allowing you to switch to a paid plan directly from the Service List.

[Service Overview]: ./integrations/
[integrations]: [./integrations/]
[alerts]: ../alerts/
