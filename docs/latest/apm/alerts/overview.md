---
title: 'Kamon APM | Alerts | Kamon Documentation'
description: 'Learn about alerting in Kamon APM, managing availability targets, and setting up notifications so you never miss a production incident again'
layout: docs
---

{% include toc.html %}

Alerts - Overview
=================

{% lightbox /assets/img/pages/apm/alert-drawer.png %}
Alert Drawer
{% endlightbox %}

When something goes wrong in your system, you want to know! This is where Kamon APM alerts come in. The core concept of alerts is simple - you set up a metric and the aspect of it you want to keep track of, and you specify a threshold. If the threshold is crossed for a specified amount of time, the alert is *triggered*. From that point until the alert goes below the threshold again is considered a single [incident]. Incidents are recorded even if they have not been resolved, and as marked as *ongoing*. An alert with an active incident is considered *on fire*.

Alerts are always linked to a *data source*. This data source is a metric, and it can be from either a service that sends data to Kamon APM, one of your [hosts], or an internal metric of Kamon APM itself. You can read more about them in the chapter about [creating alerts]. These alerts determine the state of the data source they are connected to - if any of the alerts connected to a service has an ongoing incident (also known as *on fire*), the service has the severity state of that alert. If multiple alerts are *on fire*, the most severe alert is considered. If no alerts are specified, or none are *on fire*, the service is considered *healthy*.

Alert Severity
---------------

Alerts can have two different states of severity: **Warning** and **Critical**. Other than their relative importance, there is one more crucial difference. Critical alerts can trigger [notifications to outside channels]. Warnings will simply be visible inside of Kamon APM, and are not considered severe enough to wake you up in the middle of the night.

Alert Availability Target
--------------------------

Alerts may have a certain availability target set up. An availability target is simple - it's merely saying that the alert must be healhty for a certain percentage of time, in a certain time window. For example, you may demand that an alert be healthy 99% of the time in every one week period. You can learn more about setting up availability targets in the chapter on [creating alerts], and more about where you can see them in chapters about the [alert drawer] and [alert lists].

[incident]: ../incidents/
[hosts]: ../../hosts/host-monitor/
[creating alerts]: ../create-edit/
[notifications to outside channels]: ../channels/
[alert drawer]: ../alert-drawer/
[alert lists]: ../alert-list/
