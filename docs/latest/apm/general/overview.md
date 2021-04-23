---
title: 'Kamon APM | Overview | Kamon Documentation'
layout: docs
---

{% include toc.html %}

Kamon APM
===========

Overview
--------

[Kamon APM][apm] is a hosted monitoring and debugging platform for microservices, designed from the ground up with one goal in mind: accept all the metrics and tracing data exactly as Kamon records it, no averages, no summaries, no downsampling, no data quality loss. Having the entire data that Kamon captures means proper aggregation of data across instances and proper percentiles calculation, which translates in better, accurate and relevant alerts and insight on your application’s behavior.

{% lightbox /assets/img/apm-preview.png %}
Kamon APM
{% endlightbox %}

With Kamon APM, you can [deep dive][analyze] into the metrics your services send, use [traces] to analyze performance bottlenecks and errors, see the layout of your microservice architecture using the [Service Map]. When your system is behaving unusually, you can set up [alerts] to receive reports to your e-mail, Slack, or elsewhere. If the pre-defined integrations are not enough, you can set up a [dashboard] to visualize any metric received by the system, including [host metrics][hosts] and span processing metrics.

Getting Started
----------------

You can get started with Kamon APM **for free**, in just 3 minutes. You can [sign up] with your e-mail or via Google _without_ having to enter your credit card details. The free Starter plan can be used for free for any duration of time, but will not offer the full range of Kamon APM capabilities. See the list of [plans] for more details.

[apm]: https://apm.kamon.io
[analyze]: ./analyze/
[traces]: ./traces/
[Service Map]: ./services/service-map/
[alerts]: ./alerts/
[dashboard]: ./dashboards/
[hosts]: ./hosts/
[plans]: /apm/pricing/
[sign up]: https://apm.kamon.io/signup
