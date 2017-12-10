---
title: Kamon > Documentation > Instrumentation > Executor Service Metrics
layout: documentation-1.x
---

Executor Service Instrumentation
================================

This module lets you collect metrics from executor service: Thread Pool Executors and Fork Join Pools. To start tracking
a executor service you must register it with the executors module by calling `kamon.executors.Executors.register(...)`
as shown bellow:

{% code_example %}
{%   language scala kamon-1.x/recipes/basic-futures-and-executors/src/main/scala/kamon/recipe/futuresandexecutors/FuturesAndExecutors.scala tag:registering-a-executor label:"Registering a Executor Service" %}
{% endcode_example %}

You will get back a `Registration` that you can cancel at any moment if you want to stop tracking the executor service.
That is something you should definitely do before shutting down the executor service.


### Exposed Metrics

Regardless of the type, all executor services will get the following metrics:

* __executor.threads__ (Histogram). Samples the number of threads in the executor service. Tags:
  * __state__: Active (state=active) and total (state=total).

* __executor.tasks__ (Counter). Tracks the number of tasks processed by the executor service. Tags:
  * __state__: Submitted (state=submitted) and completed (state=completed).

* __executor.queue__ (Histogram). Samples the queue size for the executor service.


Additionally, all executor service metrics will also have the following tags:
* __name__: With the name provided during registration.
* __type__: Fork Join Pool (type=fjp) or Thread Pool Executor (type=tpe).


#### Additional Metrics for Thread Pool Executors

* __executor.pool__ (Gauge). Tracks several configuration settings for the executor service . Tags:
  * __setting=min__: Minimum pool size.
  * __setting=max__: Maximum pool size.
  * __setting=corePoolSize__: Core pool size.

#### Additional Metrics for Fork Join Pools

* __executor.pool__ (Gauge). Tracks several configuration settings for the executor service . Tags:
  * __setting=min__: Minimum pool size.
  * __setting=max__: Maximum pool size.
  * __setting=parallelism__: Parallelism.

