---
title: 'Cassandra Driver Instrumentation | Kamon Documentation'
description: 'Automatically extract metrics, distributed traces and perform context propagation on Akka applications'
layout: docs
---

{% include toc.html %}

Cassandra Driver Instrumentation 
================================
Since __2.1.2__

Overview
--------

The Cassandra Driver instrumentation automatically traces queries sent to Cassandra, and collects performance metrics
for the driver sessions and connection pools. 

<p class="alert alert-info">
The instrumentation only supports the 3.x series of the Cassandra Java Driver. If you are interested in support for the
4.x series, please comment and subscribe to <a href="https://github.com/kamon-io/Kamon/issues/798">this issue</a> on
Github.
</p>


Query Tracing
-------------

With the default configuration, the driver instrumentation will only trace calls to `Session.execute(...)`. For example,
if you prepare an statement and execute seven different queries on a session, your trace will look similar to this:

<img class="img-fluid rounded" src="/assets/img/cassandra-without-roundtrip-spans.png">

Each executed query generates a single Span, regardless of whether the query had to be retried or speculated on.

All query spans will contain these tags:
  * __component__: With the value `cassandra.driver`.
  * __db.type__: With the value `cassandra`.
  * __db.statement__: With the CQL statement sent to Cassandra.
  * __cassandra.query.kind__: Message class. With either select, update, delete or insert.

Additionally, for select queries there are three additional tags with information about the `ResultSet` retured for the
query:
  * __cassandra.driver.rs.fetch-size__: With the configured fetch size for the query.
  * __cassandra.driver.rs.fetched__: With the count of CQL rows that were fetched with the query.
  * __cassandra.driver.rs.has-more__: With true or false, indicating whether the Cassandra server has more results
    waiting to be fetched for the query.


### Round Trip Tracing
In addition to the query tracing, the instrumentation can produce Spans for every round trip between the Cassandra 
driver and the server. Round trip tracing is disabled by default because it significally increases the number of Spans
generated for every executed query. You can enabled by chaging this setting in your `application.conf` file:

{% code_block typesafeconfig %}
kamon.instrumentation.cassandra {
  tracing.create-round-trip-spans = no
}
{% endcode_block %}

Every interaction with Cassandra servers is traced when round trip tracing is enabled, including preparation of 
statements, retries and speculative executions. For example, the same application used in the query tracing above 
generates all these Spans in a normal request processing:

<img class="img-fluid rounded" src="/assets/img/cassandra-with-roundtrip-spans.png">

Notice the `cassandra.query.prepare` span, and all of the `cassandra.query.execution` spans that were automatically
created for each query sent to Cassandra.

All the round trip Spans are tagged with:
  * __cassandra.node__: With the execution's target node.
  * __cassandra.rack__: With the rack of the execution's target node.
  * __cassandra.dc__: With the datacenter of the execution's target node.

Use with caution. The number of Spans generated when round trip tracing is enabled can overwhelm your tracing system.


### Disabling Tracing
If you want to completely disable tracing of Cassandra queries and round trips, set the 
`kamon.instrumentation.cassandra.tracing.enabled` setting to `no`, as shown below:

{% code_block typesafeconfig %}
kamon.instrumentation.cassandra {
  tracing.enabled = no
}
{% endcode_block %}


Session Metrics
---------------

All the following metrics are automatically tracked for all Cassandra Driver sssions:

{%  include metric-detail.md name="cassandra.driver.session.borrow-time" %}
{%  include metric-detail.md name="cassandra.driver.session.connections.open" %}
{%  include metric-detail.md name="cassandra.driver.session.connections.trashed" %}
{%  include metric-detail.md name="cassandra.driver.session.in-flight" %}
{%  include metric-detail.md name="cassandra.driver.session.speculative-executions" %}
{%  include metric-detail.md name="cassandra.driver.session.retries" %}
{%  include metric-detail.md name="cassandra.driver.session.errors" %}
{%  include metric-detail.md name="cassandra.driver.session.timeouts" %}
{%  include metric-detail.md name="cassandra.driver.session.cancelled" %}


Connection Pool Metrics
-----------------------

The instrumentation can automatically collect metrics for all connection pools created by the Cassandra Driver. This
feature is disabled by default, because the Casssandra Driver creates one connection pool for each node it connects to,
which could mean tracking tens to hundreds of connection pools.

If you wish to enable connection pool metrics, set the `kamon.instrumentation.cassandra.metrics.track-node-connection-pools` 
setting to `yes`, as shown below:

{% code_block typesafeconfig %}
kamon.instrumentation.cassandra {
  metrics.track-node-connection-pools = yes
}
{% endcode_block %}

When connection pool tracking is enabled, you will start seeing these metrics for all connection pools:

{%  include metric-detail.md name="cassandra.driver.node.pool.borrow-time" %}
{%  include metric-detail.md name="cassandra.driver.node.pool.connections.open" %}
{%  include metric-detail.md name="cassandra.driver.node.pool.connections.trashed" %}
{%  include metric-detail.md name="cassandra.driver.node.pool.in-flight" %}
{%  include metric-detail.md name="cassandra.driver.node.pool.retries" %}
{%  include metric-detail.md name="cassandra.driver.node.pool.errors" %}
{%  include metric-detail.md name="cassandra.driver.node.pool.timeouts" %}
{%  include metric-detail.md name="cassandra.driver.node.pool.cancelled" %}



Manual Installation
-------------------

In case you are not using the Kamon Bundle, add the dependency bellow to your build.

{% include dependency-info.html module="kamon-cassandra" version=site.data.versions.latest.core %}
{% include instrumentation-agent-notice.html %}