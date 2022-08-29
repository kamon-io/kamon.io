---
layout: post
title: Ignoring Health Check Endpoints from Metrics and Traces
date: 2022-08-29
author: Ivan Topolnjak
twitter: ivantopo
categories: blog
tags: featured
permalink: /blog/ignoring-health-check-endpoints-from-metrics-and-traces/
summary_image: '/assets/img/posts/ignore-spans-summary-image.png'
cover_image: '/assets/img/posts/ignore-spans-cover-image.png'
excerpt: >-
    Your liveness and readiness HTTP endpoints can generate a bunch of useless Spans and can mess with your latency metrics. 
    On this post we'll learn how to configure Kamon Telemetry to completely ignore these enpoints for good.
description: >-
    Your liveness and readiness HTTP endpoints can generate a bunch of useless Spans and can mess with your latency metrics. 
    On this post we'll learn how to configure Kamon Telemetry to completely ignore these enpoints for good.
---

Your liveness and readiness HTTP endpoints can generate a bunch of useless Spans and can mess with your latency metrics. 
On this post we'll learn how to configure Kamon Telemetry to completely ignore these enpoints for good.


## What's the problem?

If you are deploying your applications on Kubernetes, Nomad, or pretty much any other orchestration technology, you are
very likely exposing liveness and readiness HTTP endpoints to tell your orchestrator whether your application is alive 
or not, and ready to receive requests.

Depending on the frequency of these checks and the real-world throughput of your application, you might end up with 
something like this:

<div class="text-center my-5">
  <img class="img-fluid" src="/assets/img/posts/ignore-spans-healthcheck-skew.png" alt="Health checks skewing latency metrics">
</div>

This isn't a high-throughput application, and definitely not a fast one, but the latency heatmap is saying that most 
of the activity is happening around the 20 milliseconds area. Why? Liveness and readiness checks! There are three negative
side effects of including liveness and readiness checks on your latency metrics and traces:
  - **They inflate your throughput numbers**. These endpoints get called all the, making it look like there is a 
    constant rate of requests to your application. This is not necessariy bad on its own, but the extra request volume 
    can make your error rates look smaller than they really are.
  - **They pull down your latency profile**. Most liveness and readiness checks run fast (in fact, 20 milliseconds 
    is kind of slow for what we see in most applications) and these "fast" requests pull down your latency percentiles,
    making it look as if your service is faster than it really is.
  - **They eat away your spans quota**. Spans are more expensive to collect, transfer, and store than metrics. Why waste
    Spans on requests that almost always have a hardcoded response and provide no additional debugging value?

These side effects are less important as your application gets higher and higher throughput, though. You will not be 
sending more than one liveness check per second (you wont, [right](https://twitter.com/ivantopo/status/1562436301424537603){:target="_blank"}?)
and as your real throughput gets close and above the 100 requests/sec barrier, the effect of the health check endpoints
becomes just a tiny bit of noise in the data. Taking care of that noise is a lot easier than you think, though.


## Ignoring Endpoints with Kamon Telemetry

The question of "how can we disable metrics and traces for liveness endpoints?" came up so many times on our support 
chat that we had to create a configuration setting specific for it in [Kamon Telemetry 2.5.8](https://github.com/kamon-io/Kamon/releases/tag/v2.5.8){:target="_blank"}. 

The newly introduced `kamon.trace.ignored-operations` setting takes a list of operation names to be ignored:

{% code_block typesafeconfig %}
  kamon.trace.ignored-operations = [
    "/status", 
    "/health", 
    "/ready"
  ]
{% endcode_block %}

And just like that, your liveness and readiness endpoints are gone from metrics and traces!

### Migrating from Adaptive Sampling Groups
If you are a long-term Kamon Telemetry user, you are probably solving this issue with the Adaptive Sampler groups configuration. 
The group settings will continue to work as expected, but there are a couple of reasons to consider migrating to
ignored operations:
  - Ignored operations work with all samplers, including the `always` and `random` samplers
  - There is a new `track-metrics-on-ignored-operations` setting which allows to completely disable metrics for ignored 
    operations. This is something the adaptive sampler couldn't do, as it would only disable trace sampling, but metrics
    would be generated anyways


### Beware of Who Takes the Sampling Decision
Before wrapping up, just one more thing to take into account.

Ignored operations tell the Kamon Telemetry tracer to force a "Do not sample" decision on *new* traces, not on traces
where a sampling decision was already made. To put it visually (and borring from our short explanation of 
[how sampling works](/blog/how-to-keep-traces-for-slow-and-failed-requests/#sampling-101)), it won't be possible to 
ignore an endpoint on S2 that receives calls from S1, because the sampling decision for the trace is taken by S1 before
the call is sent to S2:

<div class="text-center my-5">
  <img class="img-fluid" src="/assets/img/posts/local-tail-sampler-sampling-101.png" alt="How sampling works in distributed tracing">
</div>

This won't be a problem when your endpoints get called directly by Kubernetes, Consul, or any other health-checking 
agent, but it is worh mentioning in case you want to get creative with the ignored operations setting.

Have fun!
