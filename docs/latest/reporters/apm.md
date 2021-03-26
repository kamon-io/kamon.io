---
title: 'Sending Metrics and Spans to Kamon APM | Kamon Documentation'
layout: docs
redirect_from:
  - /documentation/1.x/reporters/kamino/
---

{% include toc.html %}

Kamon APM Reporter
==================

[Kamon APM][apm] is a hosted monitoring and debugging platform for microservices, designed from the ground up with one
goal in mind: accept all the metrics and tracing data exactly as Kamon records it, no averages, no summaries,
no downsampling, no data quality loss. Having the entire data that Kamon captures means proper aggregation of data
across instances and proper percentiles calculation, which translates in better, accurate and relevant alerts and insight
on your application's behavior.


## Installation

{% include dependency-info.html module="kamon-apm-reporter" version=site.data.versions.latest.apm %}

Once the reporter is on your classpath it will be automatically picked up by Kamon, just make sure that you add the API
key for your environment in the configuration:

{% code_block hcl %}
kamon {
  environment {
    service = "my-service-name"
  }

  apm {
    api-key = "abcdefghijklmnopqrstuvwxyz"
  }
}
{% endcode_block %}

Also, as seen above, you might want to change the default service name to reflect the name that you want to see
displayed in Kamon APM.


## Teasers

Kamon APM has a deep understanding on all metrics reported by Kamon and provides ready to use dashboards that make it
super easy to start looking at service, JVM, Hosts, Akka-related metrics, Traces and more. Here are some examples from a
demo application:

<img class="img-fluid my-4" src="/assets/img/apm-service-map.png">
<img class="img-fluid my-4" src="/assets/img/apm-services-list.png">
<img class="img-fluid my-4" src="/assets/img/apm-analyze.png">
<img class="img-fluid my-4" src="/assets/img/apm-analyze-trace-detail.png">
<img class="img-fluid my-4" src="/assets/img/apm-jvm-metrics.png">

[apm]: /apm/
