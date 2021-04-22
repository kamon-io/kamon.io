---
title: 'Setting up Kamon with Lagom Framework'
description: 'Learn how to collect metrics and traces from Lagom Framework apps using Kamon'
layout: docs
---

Setting up Kamon with Lagom Framework
=====================================

This guide walks you through setting up Kamon with a Lagom Framework application and sending your first metrics and
traces to Kamon APM. The same steps will work if you choose any other [reporter][reporter].

Before we start, make sure you are using **Lagom Framework 1.5 or 1.6**. This guide might work with Lagom Framework 1.4 
but we have not tested it.

{% alert warning %}
  Beware that Kamon will **not work with Lagom applications on Development mode**. The instructions below will enable
  the Kamon instrumentation for Production Mode only.
{% endalert %}

With that out of the way, let's get to it!


## 1. Add the Kamon Dependencies

Add the `kamon-bundle` and `kamon-apm-reporter` dependencies to the implementation projects (usually ending with `-impl`) 
in your `build.sbt` file:

{% code_example %}
{%   language scala guides/install/lagom-scala-sbt/build.sbt tag:kamon-dependencies version:latest label:"SBT / build.sbt" %}
{% endcode_example %}

The Kamon Bundle dependency contains all the Kamon [automatic instrumentation][automatic-instrumentation] modules in a
single jar, so that you don't need any additional dependencies. The APM reporter dependency is in charge of sending all
your metrics and traces to Kamon APM.


## 2. Add the SBT Javaagent Plugin Dependency

Add the SBT Javaagaent Plugin to your `project/build.sbt` file:

{% code_block scala %}
addSbtPlugin("com.lightbend.sbt" % "sbt-javaagent" % "0.1.6")
{% endcode_block scala %}


## 3. Enable the JavaAgent Plugin

Include the `JavaAgent` plugin in the call to `.enablePlugins` and add a new value to the `javaAgents` setting with the 
Kanela agent in your `build.sbt` file:

{% code_example %}
{%   language scala guides/install/lagom-scala-sbt/build.sbt tag:enable-javaagent version:latest label:"SBT / build.sbt" %}
{% endcode_example %}

Enabling the SBT Java Agent Plugin ensures that the Kamon instrumentation will be initialized properly when running in 
Production Mode.

## 4. Configure the APM Reporter

Add your service name and API key to the `application.conf` file:

{% code_block hcl %}
kamon {
  environment.service = "Lagom Application"
  apm.api-key = "Your API Key"
}
{% endcode_block %}

You can copy your API key directly from [Kamon APM](https://apm.kamon.io/?envinfo=show){:target="_blank" rel="noopener"}.



Verifying the Installation
--------------------------

Next time your application starts, Kamon should be up and running as well! Open [http://localhost:5266/](http://localhost:5266/){:target="_blank" rel="noopener"}
in your browser and you'll find the Kamon Status Page. It should look like this:

{% lightbox /assets/img/kamon-status-page.png %}
Kamon Status Page
{% endlightbox %}

The important bits to check in the Status Page are that the modules have a green check mark and instrumentation is shown
as active on the top-left corner. If your installation didn't go well, please stop by our [Github Discussions](https://github.com/kamon-io/Kamon/discussions){:target="_blank" rel="noopener"}
and post a question. We will do our best to help!

That is it, your installation is done! You might want to check out the [How To Guides][how-to-guides] for common 
post-installation steps to improve your instrumentation.



[reporter]: ../../../reporters/
[automatic-instrumentation]: ../../../instrumentation/
[how-to-guides]: ../../../guides/#how-to-guides
