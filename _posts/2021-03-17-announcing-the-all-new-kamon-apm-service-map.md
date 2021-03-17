---
layout: post
title: 'Announcing the All-new Kamon APM Service Map'
date: 2021-03-17
author: the Kamon Team
categories: releases
tags: featured
permalink: /blog/announcing-the-all-new-kamon-apm-service-map/
summary_image: '/assets/img/posts/kamon-apm-service-map-cover-image.png'
excerpt: >-
    Monitoring the health of your entire system and jumping to the root cause of problems has just become easier with
    the all new Kamon APM Service Map!
---

Kamon APM wakes up today with a new home page for all your observability data: the all-new **Kamon APM Service Map**!

The Service Map shows a real-time representation of **all your microservices and the dependencies between them**,
combined with health status and easy access to the most important bits of data from your entire infrastructure.

<div class="text-center my-5">
  <img class="img-fluid" src="/assets/img/posts/kamon-apm-service-map-example.png" alt="Kamon APM Service Map">
</div>


## Connecting Services, Infrastructure and Everything in-between

We learned over the last few years running microservices ourselves that the three most common root causes of production
issues with microservices are:
  - A bug/regression was introduced in a recent deployment, or
  - An upstream service is experiencing issues, or
  - Infrastructure has gone crazy for completely unrelated reasons

Sure, there is a lot more crazy stuff that can happen, but more often than not we see the symptoms of a performance issue
in one service and find the root cause a couple of upstream services away.

In these situations **it is vital to understand how services communicate with each other and what infrastructure is involved
on running each one of the services**, so that you can direct the investigation straight into the most probable cause instead
of playing guessing games across your entire infrastructure. That is exactly what we are bringing to the table today with
the all-new Service Map:

<video loop muted autoplay width="100%" class="my-4">
  <source src="/assets/video/kamon-apm-service-map-dependencies.mp4" type="video/mp4">
</video>

The Service Map puts together:
  - Health indicators for each service.
  - Dependencies between services.
  - Latency, throughput and error summaries for all services.
  - Live status of all configured alerts.
  - Quick access to your favourite dashboards.
  - CPU and memory usage summaries for all hosts.

All in one screen, and you don't need to do anything for the Service Map to work! Kamon APM will automatically detect 
dependencies across services and infrastructure components by analyzing all your metrics and traces.


#### Analyze Anomalies from the Service Map

When you find a good candidate for root cause of an issue you can hit the Analyze button right from the the Service
details card and start slicig and dicing:

<video loop muted autoplay width="100%" class="my-4">
  <source src="/assets/video/kamon-apm-service-map-analyze.mp4" type="video/mp4">
</video>


## Ready to Give it a Try?

The all-new Kamon APM Service Map is available in all plans and you can start right away for free: [Start Monitoring Now](#){: .onboarding-start-button}.