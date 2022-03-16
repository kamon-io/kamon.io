---
layout: docs
title: 'Start using Kamon'
description: 'Learn how to get metrics and traces out of your services with Kamon'
permalink: /docs/latest/guides/
redirect_from:
  - /docs/latest/guides/getting-started/
  - /documentation/0.6.x/get-started/
  - /documentation/1.x/get-started/
  - /documentation/get-started/
  - /introduction/get-started/
---

{% include toc.html %}

Installation
------------

These guides walk you through setting up Kamon on your application and getting your first metrics and traces out of it.
Long story short is that you need to add the Kamon dependencies and ensure that Kamon is initialized, but there are
little details that change for each framework:

- Follow these steps for [Play Framework][play-guide] applications. Kamon has support for Play Framework **2.6, 2.7 and 2.8**.
- There are slightly different steps for [Lagom Framework][lagom-guide]. Kamon has support for Lagom Framework **1.6**,
  although it will probably work with Lagom **1.4 and 1.5**.
- You can also get started with [Akka HTTP][akka-http-guide]. Kamon has support for Akka HTTP **10.1 and 10.2**.

How to Guides
-------------

These are typical tasks that you will want to get done after your initial Kamon installation is done:

- [Include Trace IDs and Context information][logging-with-context] in your log patterns.
- [Starting with the Kanela agent][start-with-the-kanela-agent] has tips and tricks that can help you setup the 
  instrumentation agent with your build tool and IDE.



Migrations
----------

Follow these guides if you are upgrading from early Kamon versions:
  - [Upgrading from Kamon 0.6 to 1.0](./migration/from-0.6-to-1.0/).
  - [Upgrading from Kamon 1.x to 2.0](./migration/from-1.x-to-2.0/).

The documentation for Kamon 1.x is still alive [here](/docs/v1/core/), in case you need it.


[akka-http-guide]: ./installation/akka-http/
[play-guide]: ./installation/play-framework/
[lagom-guide]: ./installation/lagom-framework/
[logging-with-context]: ./how-to/log-trace-id-and-context-info/
[start-with-the-kanela-agent]: ./how-to/start-with-the-kanela-agent/