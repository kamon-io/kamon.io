---
title: 'Process Metrics | Kamon Documentation'
layout: docs
---

{% include toc.html %}

Process Metrics
===============


Available Metrics
-----------------

Kamon will automatically detect and start the Process metrics module when it is in your classpath and the following
metrics will become available:

{%  include metric-detail.md name="process.cpu.usage" %}
{%  include metric-detail.md name="process.ulimit.file-descriptors.max" %}
{%  include metric-detail.md name="process.ulimit.file-descriptors.used" %}
{%  include metric-detail.md name="process.hiccups" %}


Disabling the Module
--------------------

This module is enabled by default, but you can explicitly enable/disable it by changing the `enabled` setting in your
application configuration:

```text
kamon.modules {
  process-metrics {
    enabled = no
  }
}
```

Kamon will still recognize that the module is available, but will not automatically start it when `Kamon.init()` or
`Kamon.loadModules()` are called.


Manual Installation
-------------------

In case you are not using the Kamon Bundle, add the dependency below to your build (the Process metrics module is
distributed as part of the system metrics module).

{% include dependency-info.html module="kamon-system-metrics" version=site.data.versions.latest.system %}
