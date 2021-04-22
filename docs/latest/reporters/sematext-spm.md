---
title: 'Sending Metrics to Sematext SPM with Kamon | Kamon Documentation'
layout: docs
redirect_from:
  - /documentation/0.6.x/kamon-sematext-spm/overview/
  - /documentation/1.x/reporters/sematext/
---

{% include toc.html %}


Reporting Metrics to SPM
=======================

[SPM] is a proactive performance monitoring solution that provides anomaly detection, alerting, transaction tracing, network topology discovery and log correlation available in the Cloud and On Premises.

Installation
------------

[Sign up]. [Create 'Akka' app] in SPM. Get your SPM app token. Add `kamon-spm` dependency to your project according to application creation instructions.

{% include dependency-info.html module="kamon-spm" version=site.data.versions.latest.apm %}

Configuration
-------------

SPM backend extension requires the property `kamon.spm.token` to be defined. SPM provides reports for `akka-actor`, `akka-router`, `akka-dispatcher`, `system-metrics`, `trace` and other categories. You should define all required categories under `kamon.util.filters`.

{% code_block typesafeconfig %}
  kamon {
    util.filters{
      ...
    }
    spm {
      token = "[place-token-here]"
    }
    reporters = ["kamon.spm.SPMReporter"]
  }
{% endcode_block %}

To see a full example of Kamon SPM Backend configuration look at [application.conf] in [sample Akka/Play app] with Kamon and SPM.

**Note:** By default this extension uses hostname resolved using `InetAddress.getLocalHost.getHostName`. However, hostname can be redefined using `kamon.spm.hostname-alias` property.

Visualisation
-------------

Overview:

{% lightbox /assets/img/spm-module-overview %}
SPM Module Overview
{% endlightbox %}


Actors:

{% lightbox /assets/img/spm-module-actors.png %}
SPM Actors
{% endlightbox %}


Dispatchers:

{% lightbox /assets/img/spm-module-dispatchers.png %}
SPM Dispatchers
{% endlightbox %}

Routers:

{% lightbox /assets/img/spm-module-routers.png %}
SPM Routers
{% endlightbox %}

System:

{% lightbox /assets/img/spm-module-system.png %}
SPM System
{% endlightbox %}

Custom:

{% lightbox /assets/img/spm-module-custom.png %}
SPM Custom
{% endlightbox %}

Traces:

{% lightbox /assets/img/spm-module-traces.png %}
SPM Traces
{% endlightbox %}


[SPM]: http://sematext.com/spm/index.html
[Sign up]: https://apps.sematext.com/users-web/register.do
[Create 'Akka' app]: https://apps.sematext.com/spm-reports/registerApplication.do
[sample Akka/Play app]: https://github.com/sematext/kamon-spm-example
[application.conf]: https://github.com/sematext/kamon-spm-example/blob/master/src/main/resources/application.conf
