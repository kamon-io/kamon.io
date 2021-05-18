---
title: 'Kamon APM | Notification Channels | Kamon Documentation'
description: 'Manage how you get notified when incidents happen in your system, using Slack, PagerDuty, and more'
layout: docs
---

{% include toc.html %}

Notification Channels
=====================

{% lightbox /assets/img/apm/notification-channels.png %}
Notification Channels
{% endlightbox %}

You can think of Notification channels as a way to notify the outside world of issues detected in Kamon APM. Using this feature, you can configure an integration with Slack, PageDuty, or e-mail to
easily set up notifications when something goes wrong an an alert triggers. Each notification channel will have an icon to indicate its type, a channel time determined by the creator, a type,
and can be turned on and off, as well as deleted.

A communication channel may be in an enabled or disabled state. If they are disabled, notifications that would usually go to this channel will instead be ignored. The state can be toggled using
the switch in the notification channel list.

Note that creating a notification channel does *not* mean that you will automatically start getting messages there - they need to be configured when [creating an alert].

{% lightbox /assets/img/apm/create-channel.png %}
Create Notification Channels
{% endlightbox %}

You can create a channel by clicking on the button in the top right corner, which will open a create notification channel dialog. Here, you can pick from the available integrations and
enter the configuration parameters needed to set it up.

For e-mail, you will need to enter a valid e-mail address to which to send notifications, as well as a custom name for this channel.

For PagerDuty, you will need to enter a custom name and a valid API key for PagerDuty.

Slack will only present you with a button which will allow you to easily configure an integration with Slack in just one or two minutes, from the Slack online integration administration tool.

{% lightbox /assets/img/apm/delete-channel.png %}
Delete Notification Channel
{% endlightbox %}

You can delete any channel at any time by clicking the delete button in the extreme right of each entry in the table. You will be prompted to confirm this action. Note that this action
is irreversible!

{% lightbox /assets/img/apm/slack-notification.png %}
Slack Alert Notification
{% endlightbox %}


[creating an alert]: ../create-edit/
