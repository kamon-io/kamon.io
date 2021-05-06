---
title: 'Kamon APM | Alert List | Kamon Documentation'
description: 'Manage alerting in Kamon APM and discover the status of your system at a glance with the Kamon APM Alert List'
layout: docs
---

{% include toc.html %}

Alert List
==========

{% lightbox /assets/img/pages/apm/alert-list.png %}
Alert List
{% endlightbox %}

The Alert List is a complete listing of all alerts configured in our system. It displays a summary of each alert and all of the relevant details, and allows you to create, edit, remove, or manage alerts from a central location. This view can be accessed from the sidebar, by clicking on the last icon in sequence.

Each alert entry includes the following information about it:

* The [alert status](#alert-status)
* The alert name and [description](#alert-description)
* The [service] or other data source the alert is tied to (e.g., host metrics)
* The [severity] of the alert
* The time the alert was last triggered on and how long ago it was
* The [availability target] status
* Notification [channels] configured to be used with this alert

Clicking on any of the alerts will open the [alert drawer] with further details.

Alert Status
------------

Each alert can be in one of three statuses:

| Status  | Indicator   | Description      |
|:--------|:-----------:|------------------|
| Healthy | <img src="/assets/img/pages/apm/status/healthy.svg"> | The alert is currently not triggered |
| Warning | <img src="/assets/img/pages/apm/status/warning.svg"> | The alert is currently active, and has the **warning** [severity] |
| Critical | <img src="/assets/img/pages/apm/status/critical.svg"> |  The alert is currently active, and has the **critical** [severity] |

If an alert is currently healthy, but has triggered in the past, a small clock icon will be attached to the bottom right corner of the indicator.

Alert Description
-----------------

When an alert is created, it will have an automatically generated description attached to it. The description is merely a verbalization of the conditions that need to be satisfied for the alert to be triggered. It will always have the format of `Triggers when the <measure> of <metric> goes <comparison> <threshold> for <evaluation period> consecutive minutes`.

For example, an alert looking at the **maximum** of metric **my.metric**, with the comparison of **above**, the threshold of **100 bytes** and an [evaluation period] of 3 minutes will have
a message stating:

> Triggers when the maximum of my.metric goes above 100B for 3 consecutive minutes

Notification Channels
----------------------

{% lightbox /assets/img/pages/apm/alert-list-channels.png %}
Alert List Notification Channels
{% endlightbox %}

If the alert list has been configured to notify one or more configured channels. If so, all the notification channels will be listed in the Alert List, as icons. If the notification channel is not
active, the icon will be crossed out.

Alert Actions
-------------

{% lightbox /assets/img/pages/apm/alert-list-menu.png %}
Alert List Menu
{% endlightbox %}

Each alert entry in the list will have a context menu to its extreme right. This menu will allow you to [create new alerts], [clone] existing ones, delete the alert, or [mute](#muted-alerts) them
for a certain amount of time. If the alert is already in a muted state, the menu will instead include an action to unmute it instead.

{% lightbox /assets/img/pages/apm/alert-list-muted.png %}
Muted Alert
{% endlightbox %}

Muted alerts are temporarily (or permanently) prevented from sending out notifications. This feature might be useful if you are expecting a period of downtime, or are already dealing with a
problem and do not need to be notified about it for the time being.

{% lightbox /assets/img/pages/apm/delete-alert-prompt.png %}
Delete Alert Prompt
{% endlightbox %}

When deleting an alert, you will first be prompted to confirm this action. This is to prevent accidental deletions, as this action is permanent and *cannot be reverted*.

[service]: ../../services/service-list/
[severity]: ../overview/#alert-severity
[evaluation period]: ../overview/#alert-evaluation-period
[availability target]: ../overview/#availability-target
[channels]: ../channels/
[create new alerts]: ../create-edit/
[clone]: ../create-edit/#clone-alert
[alert drawer]: ../alert-drawer/
