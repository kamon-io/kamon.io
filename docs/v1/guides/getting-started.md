---
title: 'Getting Started with Kamon | Kamon Documentation'
noindex: true
description: 'Learn how to set up Kamon from scratch'
layout: docs
---

{% include toc.html %}

Getting Started with Kamon
==========================

Welcome to the Kamon Community! This guide will help you install the Kamon libraries and the instrumentation agent in
your application. Long story short, you need to go through 3 steps:

  1. [Add the Kamon libraries](#adding-libraries).
  2. [Start modules you want](#starting-modules).
  2. [Enable the bytecode instrumentation](#enabling-instrumentation).


Adding Libraries
-----------------

Add the `kamon-core` dependency using your build system of choice. This dependency contains all the context propagation,
metrics and distributed tracing APIs required to instrument any application running on the JVM.

{% code_example %}
{%   language scala guides/install/sbt/build.sbt tag:base-kamon-dependencies label:"SBT" %}
{%   language groovy guides/install/gradle/build.gradle tag:base-kamon-dependencies label:"Gradle" %}
{%   language markup guides/install/maven/pom.xml tag:base-kamon-dependencies label:"Maven" %}
{% endcode_example %}

Kamon is available for Java 8+. All modules are published for Scala 2.10, 2.11 and 2.12 (when possible). If you are not
familiar with the Scala version suffix then just pick the greatest Scala version available, currently 2.12, as shown in
the Maven/Gradle examples above.

### Additional Libraries

Besides the core library, you will need to bring two additional groups of dependencies: first, the instrumentation
modules that help you gather metrics and trace data from your application like the Akka and Play instrumentation or the
System Metrics module, and second, the reporting modules that let you export that data to external systems like
Prometheus, Zipkin and so on. You can find a list of all modules in our [instrumentation section][instrumentation].


Loading Modules
---------------

During your application startup procedure you must call `Kamon.loadModules()`. This will make Kamon detect all modules
available in the classpath and start them appropriately.

{% code_example %}
{%   language scala guides/install/sbt/src/main/scala/kamon/example/Start.scala tag:load-modules %}
{%   language kotlin guides/install/gradle/src/main/kotlin/kamon/example/Start.kt tag:load-modules %}
{%   language java guides/install/maven/src/main/java/kamon/example/Start.java tag:load-modules %}
{% endcode_example %}

Take a look at the [configuration section][configuration] if you need finer control over what modules are loaded during
startup.


Enabling Instrumentation
------------------------

The bytecode instrumentation is powered by [AspectJ][aspectj], all you need to do is add the `-javaagent` JVM
startup parameter pointing to the `aspectjweaver.jar` file from the latest [AspectJ distribution][aspectjweaver] as shown
below:

{% code_block %}
java -javaagent:/path/to/aspectjweaver.jar ...
{% endcode_block %}

Depending on your build system and tools there might be additional or special steps, please refer to the [agent setup][agent]
section for instructions specific to your environment.



[instrumentation]: ../../instrumentation/
[agent]: ../setting-up-the-agent/
[aspectj]: https://www.eclipse.org/aspectj/
[aspectjweaver]: https://www.eclipse.org/aspectj/downloads.php
