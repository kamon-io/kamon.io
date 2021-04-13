---
title: 'Kamon APM | Overview | Kamon Documentation'
layout: docs
---

{% include toc.html %}

Kamon APM
====================

Overview
--------

__[Kamon APM][apm]__ is a hosted monitoring and debugging platform for microservices, designed from the ground up with one goal in mind: accept all the metrics and tracing data exactly as Kamon records it, no averages, no summaries, no downsampling, no data quality loss. Having the entire data that Kamon captures means proper aggregation of data across instances and proper percentiles calculation, which translates in better, accurate and relevant alerts and insight on your application’s behavior.

<img class="img-fluid my-4" src="/assets/img/apm-preview.png">

With Kamon APM, you can __[deep dive][analyze]__ into the metrics your services send, use __[traces][traces]__ to analyze performance bottlenecks and errors, see the layout of your microservice architecture using the __[Service Map][service-map]__. When your system is behaving unusually, you can set up __[alerts][alerts]__ to receive reports to your e-mail, Slack, or elsewhere. If the pre-defined integrations are not enough, you can set up a __[dashboard][dashboards]__ to visualize any metric received by the system, including __[host metrics][hosts]__ and span processing metrics.

Getting Started
----------------

You can get started with Kamon APM **for free**, in just 3 minutes. To sign up via e-mail or Google, simply press

<div class="w-100 text-center">
  <a
    href="#"
    class="onboarding-start-button btn-primary btn"
    data-url="signup?external=yes">
    Start Monitoring
  </a>
</div>

Upon signing up, you can receive your API key and start sending data to Kamon APM from one or more of your services.

Environments
-------------

Time Picker
------------

Getting Help
-------------

[apm]: https://apm.kamon.io
[analyze]: ./analyze/
[traces]: ./traces/
[service-map]: ./services/service-map/
[alerts]: ./alerts/
[dashboards]: ./dashboards/
[hosts]: ./hosts/
