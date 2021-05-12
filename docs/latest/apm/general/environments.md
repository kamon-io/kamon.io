---
title: 'Kamon APM | Environments | Kamon Documentation'
description: 'Learn how Kamon APM manages environments, separating your critical production services from staging deployments, and save money by leveraging this separation'
layout: docs
---

{% include toc.html %}

Environments
============

In Kamon APM, nearly everything is grouped in terms of _environments_. Services running in production and those running in development, maybe even on your local machine, do not have the same monitoring needs and you should not be forced to pay the same for them. For this reason, Kamon APM offers non-production environments **free of charge**. You can run any number of services in these environments without incurring any extra costs.

{% alert info %}
Non-production environments are a premium feature. You can use them without paying a cent extra, but they are only available on one of the [paid plans].
{% endalert %}

See this table for a quick overview of the default environments:

| Environment | With Starter Plan         | Metrics Retention | Traces Retention | Service Limit |
|-------------|---------------------------|-------------------|------------------|---------------|
| Production  | Yes <br> (max 5 services) | 14 days (Teams) <br> 6h (Starter) | 7 days (Teams) <br> 6h (Starter) | Unlimited (Paid) <br> 5 (Starter) |
| Staging     | _No_                      | 1 day             | 1 day            | Unlimited     |
| Development | _No_                      | 1 day             | 1 day            | Unlimited     |

Environment Picker
-------------------

With the environment picker, you can see the currently used environment, as well as switch between all available environments. It is located in the header, next to the [time picker], on every page in the application where it is applicable. When expanded via clicking on it, the dropdown will also show a quick summary of all of your environments. If you need additional environments, contact us using the [help] menu.

{% lightbox /assets/img/pages/apm/environment-picker.png %}
Environment Picker
{% endlightbox %}
### Environment Information


{% lightbox /assets/img/pages/apm/environment-info.png %}
Environment Info Dialog
{% endlightbox %}


You can access information about the current environment - its API key and retention - by clicking the key icon on the Environment Picker to open a modal window.

Production Environments
------------------------

Production environments offer higher retention rates, allowing you to investigate and debug errors and slowdowns in production even after they've happened. Each organization starts with an active production environment by default. On the Starter plan, it allows you to monitor up to 5 services and retains both metric and trace data for up to 6 hours. On the Teams plan, the number of services is _unlimited_, and the retention rates go up to 14 days for metrics and 7 days for traces.

If these limitations are not a match for your business needs, or you have a need for multiple production environments, you can [talk to us][help] to make adjustments!


Non-Production Environments
----------------------------

Not every environment consists of services which are in use all day, every day, and require careful monitoring at all times. When you're developing or testing new updates or microservices, it's more important that you detect problems quickly - and solve them as they happen - before rolling them out into production. For this purpose, all Kamon APM paid plans include two free non-production environments, named _Staging_ and _Development_. They are different from the production environment in two ways:

1. They will always have metric and trace retention of up to 1 day
2. You can monitor an unlimited number of services _for free_

[paid plans]: /apm/pricing/
[time picker]: ../time-picker/
[help]: ../help/
