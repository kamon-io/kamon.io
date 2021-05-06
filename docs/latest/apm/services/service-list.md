---
title: 'Kamon APM | Service List | Kamon Documentation'
description: 'Learn how to leverage the Kamon APM Service List to monitor your overall microservice architecture and discover issues immediately.'
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

Clicking on any particular service will take you to the [detailed overview][integrations] of that service.

{% lightbox /assets/img/pages/apm/service-list.png %}
Service List
{% endlightbox %}

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

Adding New Services
--------------------

To add a new service to Kamon APM, all you need to do is have a service start sending data to Kamon APM with the correct API key. You can click the **Add Service** button in the top right corner to open the Add Service modal dialog. In this dialog, you can select which framework or library you are using to get instructions tailored to your particular use case. Note that the **Service Name** field is editable, and it will update the configuration block below it for easy of copy-pasting the configuration changes into the appropriate file. Clicking on the _copy_ icon next to any of the inputs or code display boxes will copy the code into your clipboard.

{% lightbox /assets/img/pages/apm/add-service.png %}
Add Service Dialog
{% endlightbox %}

Upon completing the process by clicking the Done button, the new service should appear in the application within a minute. If the service does not appear, make sure that you have (re)started your service with the correct configuration and code changes. In case that does not help, contact us using the [help menu].

Removing Services
------------------

If you no longer wish to see a service in Kamon APM, or in this particular [environment], you can delete it using the **delete** action in the context menu, to the right-hand-side of the row in the service list. You will be prompted to confirm the removal of the service. Note that this action is _not_ permanent - if the service sends data to this Kamon APM environment again, it will re-appear in the application. If you wish to make sure that the service is permanently removed from the list, make sure that you have **removed or changed the Kamon Telemetry code changes and configuration** and restart your service.

{% lightbox /assets/img/pages/apm/delete-service-prompt.png %}
Delete Service Prompt
{% endlightbox %}

[Service Overview]: ../integrations/
[integrations]: ../integrations/
[alerts]: ../../alerts/
[help menu]: ../../general/help/
[environment]: ../../general/environments/
