---
title: 'Host Metrics | Kamon Documentation'
layout: docs
---

{% include toc.html %}

Host Metrics
============


Available Metrics
-----------------

Kamon will automatically detect and start the Process metrics module when it is in your classpath and the following
metrics will become available:

{%  include metric-detail.md name="host.cpu.usage" %}
{%  include metric-detail.md name="host.memory.used" %}
{%  include metric-detail.md name="host.memory.free" %}
{%  include metric-detail.md name="host.memory.total" %}
{%  include metric-detail.md name="host.swap.free" %}
{%  include metric-detail.md name="host.swap.used" %}
{%  include metric-detail.md name="host.swap.total" %}
{%  include metric-detail.md name="host.load.average" %}
{%  include metric-detail.md name="host.storage.mount.space.used" %}
{%  include metric-detail.md name="host.storage.mount.space.free" %}
{%  include metric-detail.md name="host.storage.mount.space.total" %}
{%  include metric-detail.md name="host.storage.device.data.read" %}
{%  include metric-detail.md name="host.storage.device.data.write" %}
{%  include metric-detail.md name="host.storage.device.ops.read" %}
{%  include metric-detail.md name="host.storage.device.ops.write" %}
{%  include metric-detail.md name="host.network.packets.read.total" %}
{%  include metric-detail.md name="host.network.packets.read.failed" %}
{%  include metric-detail.md name="host.network.packets.write.total" %}
{%  include metric-detail.md name="host.network.packets.write.failed" %}
{%  include metric-detail.md name="host.network.data.read" %}
{%  include metric-detail.md name="host.network.data.write" %}


Disabling the Module
--------------------

This module is enabled by default, but you can explicitly enable/disable it by changing the `enabled` setting in your
application configuration:

```text
kamon.modules {
  host-metrics {
    enabled = no
  }
}
```

Kamon will still recognize that the module is available, but will not automatically start it when `Kamon.init()` or
`Kamon.loadModules()` are called.


Manual Installation
-------------------

In case you are not using the Kamon Bundle, add the dependency bellow to yor build (the Process metrics module is
distributed as part of the system metrics module).

{% include dependency-info.html module="kamon-system-metrics" version=site.data.versions.latest.system %}
