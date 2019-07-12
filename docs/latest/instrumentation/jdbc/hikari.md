---
title: 'JDBC Operations Tracing | Kamon Documentation'
layout: docs
redirect_from:
  - /documentation/0.6.x/kamon-jdbc/overview/
---

{% include toc.html %}

Hikari CP Instrumentation
=========================

As part of the JDBC instrumentation, Kamon can automatically gather performance metrics out of Hikari connection pools
and use the pool information when creating Spans from Statements exection.

Metrics
-------

All Hikari connection pools will get the following metrics:

{%  include metric-detail.md name="jdbc.pool.connections.open" %}
{%  include metric-detail.md name="jdbc.pool.connections.borrowed" %}
{%  include metric-detail.md name="jdbc.pool.borrow-time" %}
{%  include metric-detail.md name="jdbc.pool.borrow-timeouts" %}


Manual Installation
-------------------

In case you are not using the Kamon Bundle, add the dependency bellow to your build:

{% include dependency-info.html module="kamon-jdbc" version=site.data.versions.latest.jdbc %}
{% include instrumentation-agent-notice.html %}