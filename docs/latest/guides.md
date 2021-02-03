---
layout: docs
title: 'Kamon Guides | Kamon Documentation'
description: 'Learn how to install Kamon in your services and start getting telemetry data out of it'
permalink: /docs/latest/guides/
redirect_from:
  - /docs/latest/guides/getting-started/
  - /documentation/0.6.x/get-started/
  - /documentation/1.x/get-started/
  - /documentation/get-started/
  - /introduction/get-started/
---

Installation
============

These guides are aimed towards getting your feet wet and start getting telemetry data from your services as quick as
possible. There is one for each type of service:

- The [**Plain Application Guide**][plain-app] is meant for any application where you are in control of the "main"
  method, like with most Akka, Akka HTTP and Spring Boot services.
- For [**Play Framework Applications**][play-app] follow these steps since there are a few differences in the process.
- You can head to the [**Manual Instrumentation**][manual-instrumentation] guide if you want to get your hands dirty
  from the start.
- [**Setting up the Agent**][setting-up-the-agent] has tips and tricks that can help you setup the instrumentation agent
  with your build tool and IDE.


Common Tweaks
=============

Here are some of the most common tweaks you might want to apply after your service is up and running with Kamon:

- On the [**Logging with Context**][logging-with-context] guide you will learn how to include things like the current
  trace ID and Context tags in your log events.


Frameworks
==========

Guides in this section are aimed to getting you from zero to telemetry with a particular framework or toolkit, including
setup, basic configuration and seeing example data in several metrics and tracing solutions.

- The [**Elementary Akka setup**][elementary-akka] guide takes one of the most common example Akka applications and
  enables metrics and distributed tracing on it using Kamon's automatic instrumentation.



[plain-app]: ./installation/plain-application/
[play-app]: ./installation/play-framework/
[manual-instrumentation]: ./installation/manual-instrumentation/
[logging-with-context]: ./common-tweaks/logging-with-context/

[getting-started]: ./getting-started/
[setting-up-the-agent]: ./installation/setting-up-the-agent/
[elementary-akka]: ./frameworks/elementary-akka-setup/