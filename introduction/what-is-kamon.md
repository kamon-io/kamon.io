---
title: Kamon | Get Started
layout: documentation
---

What is Kamon?
==============

Kamon is a reactive-friendly toolkit for monitoring applications that run on top of the Java Virtual Machine. We are
specially keen to applications built with the Typesafe Reactive Stack (using Scala, Akka, Spray and/or Play!) but as
long as you are on the JVM, Kamon can help get the monitoring information you need from your application.

You can integrate Kamon into your application by either:

* Using the provided APIs to manually create metric recording [instruments] and manipulate traces to measure whatever
you find interesting in your application, or

* Letting Kamon do the heavy work for you, using the provided bytecode instrumentation (via AspectJ) to introduce
metrics measurement and tracing code in several popular libraries and toolkits, allowing you to get immediate
visibility into your application's behavior without having to touch a single line of code, or

* Using a mix of both approaches, using the provided instrumentation to get some general information and the provided
APIs to enhance that information with domain specific metrics and traces.

Check our [getting started] guide for directions on how to start integrating Kamon with your application right away!

<br>

What do I get Out of the Box?
-----------------------------

With every Kamon release, the following modules are shipped:

#### kamon-core ####
Base APIs and stuff

#### kamon-akka ####
Base APIs and stuff




[instruments]: /core/metrics/instruments/
[getting started]: /introduction/get-started/
