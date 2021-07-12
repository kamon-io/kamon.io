---
title: 'Caffeine Documentation'
description: 'Extract traces and metrics from Caffeine based caches'
layout: docs
---
{% include toc.html %}

Caffeine Instrumentation
=======================
Since __2.2.2__

Overview
--------

The Caffeine instrumentation adds tracing for Caffeine and Caffeine backed [synchronous clients][caffeine-project]. 
Metrics are gathered using the `KamonStatsCounter`, which needs to be added manually.
When building a cache, add it by calling the `recordStats` method:

{% code_block scala %}
Caffeine.newBuilder()
        .recordStats(() -> new KamonStatsCounter("cache_name"))
        .build();
{% endcode_block bash %}


Manual Installation
-------------------

In case you are not using the Kamon Bundle, add the dependency below to your build.

{% include dependency-info.html module="kamon-caffeine" version=site.data.versions.latest.core %}
{% include instrumentation-agent-notice.html %}

[caffeine-project]: https://github.com/ben-manes/caffeine
