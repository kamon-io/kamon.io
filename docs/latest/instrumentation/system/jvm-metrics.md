---
title: 'JVM Metrics | Kamon Documentation'
layout: docs
---

{% include toc.html %}

JVM Metrics
===========

Available Metrics
-----------------

Kamon will automatically detect and start the JVM metrics module when it is in your classpath and the following metrics
will become available:

{%  include metric-detail.md name="jvm.gc" %}
{%  include metric-detail.md name="jvm.gc.promotion" %}
{%  include metric-detail.md name="jvm.memory.used" %}
{%  include metric-detail.md name="jvm.memory.free" %}
{%  include metric-detail.md name="jvm.memory.committed" %}
{%  include metric-detail.md name="jvm.memory.max" %}
{%  include metric-detail.md name="jvm.memory.pool.used" %}
{%  include metric-detail.md name="jvm.memory.pool.free" %}
{%  include metric-detail.md name="jvm.memory.pool.committed" %}
{%  include metric-detail.md name="jvm.memory.pool.max" %}
{%  include metric-detail.md name="jvm.memory.allocation" %}


Disabling the Module
--------------------

This module is enabled by default, but you can explicitly enable/disable it by changing the `enabled` setting in your
application configuration:

```text
kamon.modules {
  jvm-metrics {
    enabled = no
  }
}
```

Kamon will still recognize that the module is available, but will not automatically start it when `Kamon.init()` or
`Kamon.loadModules()` are called.


Manual Installation
-------------------

In case you are not using the Kamon Bundle, add the dependency below to your build (the JVM metrics module is
distributed as part of the system metrics module).

{% include dependency-info.html module="kamon-system-metrics" version=site.data.versions.latest.system %}
