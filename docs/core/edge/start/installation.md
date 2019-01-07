---
title: Install Kamon
layout: default
---

{% include toc.html %}

Getting Started with Kamon
==========================

Welcome to the Kamon Community! This guide will help you install the Kamon libraries and the instrumentation agent in
your application. Long story short, you need to go through 3 steps:

  1. [Add the Kamon libraries](#adding-libraries).
  2. [Start the available modules](#starting-modules).
  2. [Enable the bytecode instrumentation](#enabling-instrumentation).


Adding Libraries
-----------------

Add the `kamon-bundle` dependency using your build system of choice. This dependency contains the Kamon core library as
well as all the available instrumentation.

{% code_example %}
{%   language scala start/install/sbt/build.sbt tag:base-kamon-dependencies label:"SBT" %}
{%   language groovy start/install/gradle/build.gradle tag:base-kamon-dependencies label:"Gradle" %}
{%   language markup start/install/maven/pom.xml tag:base-kamon-dependencies label:"Maven" %}
{% endcode_example %}

Kamon is only available for Java 8+. All modules are published for Scala 2.10, 2.11 and 2.12 (when possible). If you are
not familiar with the Scala version suffix then just pick the greatest Scala version available, currently 2.12, as shown
in the Maven/Gradle examples above.

### Reporting Modules

Besides the core libraries and instrumentation, you will probably want to add reporting modules to send (or expose) data
to external systems like Prometheus, Zipkin, Kamon APM and so on. You can find a list of all reporting modules in our
[reporters section][reporters].


Starting Modules
----------------

During your application startup procedure you must call `Kamon.loadModules()`. This will make Kamon detect all modules
available in the classpath and start them appropriately.

{% code_example %}
{%   language scala start/install/sbt/src/main/scala/kamon/example/Start.scala tag:load-modules %}
{%   language kotlin start/install/gradle/src/main/kotlin/kamon/example/Start.kt tag:load-modules %}
{%   language java start/install/maven/src/main/java/kamon/example/Start.java tag:load-modules %}
{% endcode_example %}

Take a look at the [configuration section][configuration] if you need finer control overwhat modules are loaded during
startup.


Enabling Instrumentation
------------------------

The bytecode instrumentation is powered by the [Kanela Agent][kanela] and it can be enabled in two different ways:


### Attaching on Runtime

When attaching on runtime you must call `Kamon.enableInstrumentation()` as the very first line of code in your
application launcher, even before loading modules, as show bellow:

{% code_example %}
{%   language scala start/install/sbt/src/main/scala/kamon/example/Instrument.scala tag:enable-instrumentation %}
{%   language kotlin start/install/gradle/src/main/kotlin/kamon/example/Instrument.kt tag:enable-instrumentation %}
{%   language java start/install/maven/src/main/java/kamon/example/Instrument.java tag:enable-instrumentation %}
{% endcode_example %}

We recommend attaching on runtime because it is simple and makes the instrumentation work from your build tool launcher,
and your IDE without any changes butif you can't get access to your application launcher, try the javaagent option.

### Using -javaagent

Download the [latest version][kanela-latest] of the `kanela-agent` an ensure your application is started with the
-javaagent startup parameter:

{% code_block %}
java -javaagent:/path/to/kanela-agent.jar ...
{% endcode_block %}






[reporters]: /todo
[configuration]: /todo
[kanela]: /todo
[kanela-latest]: /kanela.jar