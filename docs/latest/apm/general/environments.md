---
title: 'Kamon APM | Environments | Kamon Documentation'
description: 'Learn how Kamon APM manages environments, separating your critical production services from staging deployments, and save money by leveraging this separation'
layout: docs
---

{% include toc.html %}

Environments
============

In Kamon APM, nearly everything is grouped in terms of _environments_. An environment is a collection of services and hosts, grouped for some audience. A testing or staging environment might be used internally for making sure everything works, while a production or live environment will be customer-facing. Kamon APM offers three environments by default: a production environment, staging environment, and a development environment. Services running on the staging or development environment, collectively called non-production environments, have lower retention, but will _never_ contribute to your monthly bill.

{% alert info %}
Non-production environments are a premium feature. You can use them without paying a cent extra, but they are only available on one of the [paid plans].
{% endalert %}

See this table for a quick overview of the default environments:

| Environment | On Developer Plan     | Data Retention | Service Limit |
|-------------|:---------------------:|-------------------|------------------|---------------|
| Production  | &#x2714; | 14 days (Teams) <br> 14 days (Starter) <br> 6 hours (Developer) <br> | Unlimited (Teams) <br> Up to **10** (Starter) <br> Up to **5** (Developer) |
| Staging     | &#x274C;                  | 1 day | Unlimited     |
| Development | &#x274C;                  | 1 day | Unlimited     |

Environment Picker
-------------------

{% lightbox /assets/img/apm/environment-picker.png %}
Environment Picker
{% endlightbox %}

With the environment picker, you can see the currently used environment, as well as switch between all available environments. It is located in the header, next to the [time picker], on every page in the application where it is applicable. When expanded via clicking on it, the dropdown will also show a quick summary of all of your environments. If you need additional environments, contact us using the [help] menu.

### Environment Information


{% lightbox /assets/img/apm/environment-info.png %}
Environment Info Dialog
{% endlightbox %}


You can access information about the current environment - its API key and retention - by clicking the key icon on the Environment Picker to open a modal window.

Production Environments
------------------------

Production environments offer higher retention rates, allowing you to investigate and debug errors and slowdowns in production even after they've happened. Each organization starts with an active production environment by default. On the Developer plan, it allows you to monitor up to 5 services and retains both metric and trace data for up to 6 hours. With the Starter plan, up to 10 services can be monitored, and the retention is 7 days. On the Teams plan, the number of services is _unlimited_, and the retention rates go up to 14 days for both metrics and traces.

If these limitations are not a match for your business needs, or you have a need for multiple production environments, you can [talk to us][help] to make adjustments!


Non-Production Environments
----------------------------

Not every environment consists of services which are in use all day, every day, and require careful monitoring at all times. When you're developing or testing new updates or APIs, it's more important that you detect problems quickly - and solve them as they happen - before rolling them out into production. For this purpose, all Kamon APM paid plans include two free non-production environments, named _Staging_ and _Development_. They are different from the production environment in two ways:

1. They will always have metric and trace retention of up to 1 day
2. You can monitor an unlimited number of services _for free_

[paid plans]: /apm/pricing/
[time picker]: ../time-picker/
[help]: ../help/
