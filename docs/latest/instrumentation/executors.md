---
title: 'Executor Service Metrics | Kamon Documentation'
layout: docs
redirect_from:
  - /documentation/1.x/instrumentation/executor-service/
---

{% include toc.html %}

Executor Service Instrumentation
================================

This module lets you collect metrics from an Executor Service, be it a Thread Pool Executor or a Fork Join Pool. To
start tracking an Executor Service you will need to register it with the executors module by calling
`ExecutorsInstrumentation.instrument(...)` as shown below:

{% code_example %}
{%   language scala instrumentation/executors/src/main/scala/kamon/examples/executors/FuturesAndExecutors.scala tag:registering-a-executor label:"Registering a Executor Service" %}
{% endcode_example %}

You will get back an instrumented Executor Service that will be recording metrics until it is shut down. It is important
to ensure that the instrumented executor is being used and not the original one, otherwise some metrics and Context
propagation features will not work as expected.


Options
-------

When instrumenting an Executor Service you have the possibility to tweak two different options:
- Tracking time in queue (default: yes) will track setup a timer that measures the time between submitting a task to the
  executor and when it starts executing.
- Propagate Context on submit (default: no) will capture the current Context at the moment a task is submitted the
  executor and restore that Context when the tasks executors. In most cases you will not need to enable this option
  because the bytecode instrumentation shipping on this module will take care of performing Context propagation, but if
  you are doing manual instrumentation this will definitely be useful for you.


Collected Metrics
-----------------

The following metrics are collected for both `ThreadPoolExecutor` and `ForkJoinPool`:

{%  include metric-detail.md name="executor.threads.min" %}
{%  include metric-detail.md name="executor.threads.max" %}
{%  include metric-detail.md name="executor.threads.active" %}
{%  include metric-detail.md name="executor.threads.total" %}
{%  include metric-detail.md name="executor.tasks.completed" %}
{%  include metric-detail.md name="executor.tasks.submitted" %}
{%  include metric-detail.md name="executor.time-in-queue" %}
{%  include metric-detail.md name="executor.queue-size" %}

Additionally, the parallelism setting is also reported for `ForkJoinPool` executors:

{%  include metric-detail.md name="executor.parallelism" %}


Manual Installation
-------------------

In case you are not using the Kamon Bundle, add the dependency below to your build to instrument Akka 2.4 and Akka 2.5
applications.

{% include dependency-info.html module="kamon-executors" version=site.data.versions.latest.executors %}
{% include instrumentation-agent-notice.html %}
