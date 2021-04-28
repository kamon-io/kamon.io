---
title: 'Kamon APM | Span Ingestion | Kamon Documentation'
layout: docs
---

{% include toc.html %}

Span Ingestion
==============

{% lightbox /assets/img/pages/apm/span-ingestion-page.png %}
Span Ingestion Management
{% endlightbox %}

Kamon APM will only ingest a limited number of spans per day, depending on your plan and the number of services. The span ingestion management page allows you to observe span ingestion, discover when span ingestion is being limited, and manage span ingestion if you need more than the default plan limits. The page includes an overview of your span ingestion in the last 24 hours, as well as the last month, both of which are sliding windows.

On the right-hand-side, ingestion numbers are shown for both periods, as numbers of spans your services sent, and the number that was ingested by Kamon APM. The numbers are shown in millions. To the left, you can compare the number of ingested spans with the the limit afforded by your plan and span ingestion management settings. If you are under your limit, the number of ingested spans will be shown in green, otherwise it will be shown in red. If the limit is crossed, *most* future spans will be dropped. However, Kamon APM will still ingest **around 1% of spans** even in that case, so that the application is still usable and useful, though to a lesser degree. A further breakdown of your span limit and how it is calculated can be seen next to the statistics.

If you are on the free Starter plan, the hard limit for span ingestion is **30k spans** for all environments. On the **Teams** plan, the limit is computed from the number of services registered for that environment, on the Production environment. Each service on the Production environment allows for **200k spans** to be ingested in a 24 hour period. Note that this amount is a total - if you have two services, you can ingest **400k spans** daily, and it does not matter which of the services is sending them - only the sum is relevant. For **non-production** environments, the limit will always be **100k spans** in a 24 hour window.

{% lightbox /assets/img/pages/apm/manage-extra-spans.png %}
Manage Extra Spans
{% endlightbox %}

Any user with the **Admin** role can manage span ingestion by adding additional spans to the daily limit. This sets the amount of additional spans that can be ingested **daily** before Kamon APM will begin dropping spans. This number will always be a multiple of million, and can go up to 100 million spans a day. Each one million spans ingested past the limit afforded by your services will cost <strong>&euro; 1</strong>. Note that if you set a higher limit than what you ingest that day, only **ingested** spans are charged.

An example:

> Organization Test has 2 services on a production environment - service **Web** and service **Scheduler**
>
> The **Web** service sends 300k spans a day, while **Scheduler** sends 150k spans a day, all of them after the **Web** service was done running for the day.
>
> All of the spans from the **Web** service will be ingested, because it sent them first, while only around 100k spans from the **Scheduler** service will be ingested.
>
> After those first 100k spans, only around 500 will be ingested in that 24 hour window (out of 50k), as Kamon APM always preserves around 1% of sent spans.
>
> The Administrator of Organization Test sets the limit of daily span ingestion to 2M per day for the next day.
> On the next day, all 450k spans are ingested. The organization will only be billed an additional &euro; 1, as they started using 1 million additional spans,
> even though up to 2 million spans are permitted by the settings.

{% alert info %}
Extra Span Management is a premium feature available exclusively on [paid plans]! Upgrade your account to fine-tune your span ingestion settings.
{% endalert %}

You can access the span management page from the user menu in the bottom left of the screen, or from any other Administration page using the tabs at the top of the screen.

{% lightbox /assets/img/pages/apm/menu-profile.png %}
Menu
{% endlightbox %}

[paid plans]: /apm/pricing/
