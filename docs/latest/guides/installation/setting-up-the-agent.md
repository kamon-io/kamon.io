---
title: 'Setting up the Instrumentation Agent'
description: 'Learn how to setup the instrumentation agent for Kamon'
layout: docs
tags: core, docs
redirect_from:
  - /docs/latest/guides/setting-up-the-agent/
  - /documentation/1.x/recipes/adding-the-aspectj-weaver/
---

{% include toc.html %}

Instrumentation Agent Setup
===========================

In most situations, you wont need to do anything special to setup the agent other than following the directions on the
[Get Started][get-started] guide, since either the `Kamon.init()` call or the SBT plugins will take care of attaching
Kanela at runtime, but we are packing all the different options together in this guide for reference in case you need
them.


In a Nutshell
-------------

All you need to do is download the latest release from our [Kanela releases][kanela-releases] repository and start your
JVM with the `-javaagent:path-to-kanela.jar` JVM option. For example, if you are running your application from
IntelliJ IDEA you will need to add the `-javaagent` option to the "VM options" section as shown bellow:

<img class="img-fluid rounded" src="/assets/img/agent/intellij-javaagent.png">

And that is pretty much it. Even though it is a simple task, it can be challenging in different environments so please,
follow the instructions bellow when:
  1. [Running applications from SBT](#running-from-sbt)
  2. [Running a Play Framework application on development mode](#play-framework)
  3. [Packaging applications with sbt-native-packager](#using-sbt-native-packager)


Running from SBT
----------------

The simplest solution is to use the [sbt-kanela-runner][sbt-kanela-runner] plugin. This plugin will ensure that the
instrumentation is applied to your classes regardless of whether you are forking the process (via `fork in run := true`)
or not. To get it working add these lines to your `project/plugins.sbt` file:

{% code_block scala %}
addSbtPlugin("io.kamon" % "sbt-kanela-runner" % "{{ site.data.versions.latest.kanela_runner }}")
{% endcode_block %}

That's it! Next time your application starts, instrumentation will be enabled.



Play Framework
--------------

Once again, the [sbt-kanela-runner][sbt-kanela-runner] plugin is the way to go. The plugin has special variants for Play
applications that will have special treatment of Play's infrastructure for running on Development mode, to include it
you must add the right plugin depending on your Play version:

### Play 2.7

{% code_block scala %}
addSbtPlugin("io.kamon" % "sbt-kanela-runner-play-2.7" % "{{ site.data.versions.latest.kanela_runner }}")
{% endcode_block scala %}

### Play 2.6

{% code_block scala %}
addSbtPlugin("io.kamon" % "sbt-kanela-runner-play-2.6" % "{{ site.data.versions.latest.kanela_runner }}")
{% endcode_block scala %}

The runner brings the [sbt-javaagent][sbt-javaagent] dependency with it, but it requires you to active it explicily by
calling `.enablePlugin(JavaAgent)` on the right project instance in your `build.sbt` file, it should look like this:

{% include kamon-play-enable-javaagent.md version="latest" %}



Using sbt-native-packager
-------------------------

You can use the [sbt-javaagent plugin][sbt-javaagent] together with sbt-native-packager to get the Kanela agent
option automatically added to the startup scripts. To achieve this, first add the plugin to your `project/plugins.sbt`
file:

{% code_block scala %}
addSbtPlugin("com.lightbend.sbt" % "sbt-javaagent" % "0.1.5")
{% endcode_block scala %}

And enable the plugin in your `build.sbt` file:

{% code_block scala %}
lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging, JavaAgent)

javaAgents += "io.kamon" % "kanela-agent" % "{{ site.data.versions.latest.kanela }}"
{% endcode_block scala %}

You can find additional details on the [sbt-javaagent GitHub repo][sbt-javaagent].

[kanela-releases]: https://bintray.com/kamon-io/releases/kanela
[sbt-kanela-runner]: https://github.com/kamon-io/sbt-kanela-runner
[sbt-javaagent]: https://github.com/sbt/sbt-javaagent/
[get-started]: /get-started/