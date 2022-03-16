---
title: 'Spring WebClient Documentation'
description: 'Automatically extract metrics, traces and perform context propagation on Spring applications'
layout: docs
---

{% include toc.html %}

Spring WebClient Instrumentation
=======================
Since __2.1.13__

{% alert info %}
This is an experimental feature that is currently enabled by default.
You can disable it by adding `kanela.modules.spring.enabled = no`
to your configuration.
{% endalert %}

Overview
--------

The Spring WebClient instrumentation will automatically create spans for requests sent using Spring WebClient.
All requests will have a name that corresponds to the request type sent by the client, and add HTTP trace and span headers
to the request.

Example span and tags: 

{% lightbox /assets/img/spring-webclient-example-trace.png %}
WebClient Example Span
{% endlightbox %}

Manual Installation
-------------------

In case you are not using the Kamon Bundle, add the dependency below to your build.

{% include dependency-info.html module="kamon-spring" version=site.data.versions.latest.core %}
{% include instrumentation-agent-notice.html %}
