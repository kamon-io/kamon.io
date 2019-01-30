---
title: Kamon > Documentation > Reporters > Sematext
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

To add the `kamon-spm` dependency to your build take into account:
  - Group ID: `io.kamon`
  - Package ID: `kamon-spm`
  - Scala Versions: 2.10 / 2.11 / 2.12
  - Latest Version: [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.kamon/kamon-spm_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.kamon/kamon-spm_2.11).

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

<img class="img-fluid" src="/assets/img/spm-module-overview.png">

Actors:

<img class="img-fluid" src="/assets/img/spm-module-actors.png">

Dispatchers:

<img class="img-fluid" src="/assets/img/spm-module-dispatchers.png">

Routers:

<img class="img-fluid" src="/assets/img/spm-module-routers.png">

System:

<img class="img-fluid" src="/assets/img/spm-module-system.png">

Custom:

<img class="img-fluid" src="/assets/img/spm-module-custom.png">

Traces:

<img class="img-fluid" src="/assets/img/spm-module-traces.png">


[SPM]: http://sematext.com/spm/index.html
[Sign up]: https://apps.sematext.com/users-web/register.do
[Create 'Akka' app]: https://apps.sematext.com/spm-reports/registerApplication.do
[sample Akka/Play app]: https://github.com/sematext/kamon-spm-example
[application.conf]: https://github.com/sematext/kamon-spm-example/blob/master/src/main/resources/application.conf
