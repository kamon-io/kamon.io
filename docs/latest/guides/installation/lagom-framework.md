---
title: 'Setting up Kamon with Lagom Framework Applications'
description: 'Learn how to collect metrics and traces from Lagom Framework applications using Kamon'
layout: docs
---

{% include toc.html %}

Setting up Kamon with Lagom Framework Applications
==================================================

This guide walks you through setting up Kamon with a Lagom Framework application and sending your first metrics and
traces to Kamon APM. We are using Kamon APM as the observability platform in this example but the same steps will work if 
you choose any other [reporter][reporter].

Before we start, make sure you are using **Lagom Framework 1.5 or 1.6**. This guide might work with Lagom Framework 1.4 
but we have not tested it.

{% alert warning %}
  Beware that Kamon will **not work with Lagom applications on Development mode**. The instructions below will enable
  the Kamon instrumentation for Production Mode only.
{% endalert %}

With that out of the way, let's get to it!


## Step 1: Add the Kamon Dependencies

Add the `kamon-bundle` and `kamon-apm-reporter` dependencies to the implementation projects (usually the ones ending with 
`-impl`) in your `build.sbt` file:

{% code_example %}
{%   language scala guides/install/lagom-scala-sbt/build.sbt tag:kamon-dependencies version:latest label:"SBT / build.sbt" %}
{% endcode_example %}

The Kamon Bundle dependency contains all the Kamon [automatic instrumentation][automatic-instrumentation] modules in a
single jar, so that you don't need any additional dependencies. The APM reporter dependency is in charge of sending all
your metrics and traces to Kamon APM.


## Step 2: Setup the SBT Javaagent Plugin

Add the sbt-javaagent Plugin to your `project/build.sbt` file:

{% code_block scala %}
addSbtPlugin("com.lightbend.sbt" % "sbt-javaagent" % "0.1.6")
{% endcode_block scala %}

Then, enable the `JavaAgent` plugin and include the Kanela agent on your `build.sbt` file:
  1. Adding the `JavaAgent` plugin to the `.enablePlugins` call will enable the plugin
  2. Then the `javaAgents += ...` settings adds the Kanela agent so it will be included when your application is built
     for production

{% code_example %}
{%   language scala guides/install/lagom-scala-sbt/build.sbt tag:enable-javaagent version:latest label:"SBT / build.sbt" %}
{% endcode_example %}



## Step 3: Initialize Kamon

Add the calls to `Kamon.initWithoutAttaching` and `Kamon.stop` to the `load` method in your application loader:

{% code_example %}
{%   language scala guides/install/lagom-scala-sbt/hello-stream-impl/src/main/scala/io/kamon/hellostream/impl/HelloStreamLoader.scala tag:initialize-kamon version:latest label:"ApplicationLoader" %}
{% endcode_example %}

These two calls ensures that all reporters and internal Kamon components are initialized and stopped along with your 
application.


## Step 4: Configure the Kamon APM Reporter

Add your service name and API key to the `application.conf` file:

{% code_block hcl %}
kamon {
  environment.service = "Lagom Application"
  apm.api-key = "Your API Key"
}
{% endcode_block %}

You can copy your API key directly from [Kamon APM](https://apm.kamon.io/?envinfo=show){:target="_blank" rel="noopener"}.
If you are using other [reporters][reporter] please take a look at their documentation for additional settings.



## Step 5: Verify the Installation

Next time your application starts on production, Kamon should be up and running as well! Open [http://localhost:5266/](http://localhost:5266/){:target="_blank" rel="noopener"}
in your browser and you'll find the Kamon Status Page. It should look like this:

{% lightbox /assets/img/kamon-status-page.png %}
Kamon Status Page
{% endlightbox %}

The important bits to check in the Status Page are that the modules have a green check mark and instrumentation is shown
as active on the top-left corner. If your installation didn't go well, please stop by our [Github Discussions](https://github.com/kamon-io/Kamon/discussions){:target="_blank" rel="noopener"}
and post a question. We will do our best to help!

That is it, your installation is done! You might want to check out the [How To Guides][how-to-guides] for common 
post-installation steps to improve your instrumentation.



## Experimental: Using Kamon in Development Mode

The guide above sets up Kamon for production mode only, but there is a hack we developed (yes, it is a hack) to enable 
Kamon's instrumentation in Development mode, with two caveats:
  1. It only works with **Lagom 1.6.x**
  2. It only works with one Lagom project at a time

If you have several Lagom projects in the same build you can run them all, but only one of them will run with 
Kamon's instrumentation in Development mode.

Here is how you set it up:

### Add the SBT Kanela Runner Plugin

Include the `sbt-kanela-runner-lagom-1.6` plugin in your `project/plugins.sbt` file:

{% code_example %}
{%   language scala guides/install/lagom-scala-sbt/project/plugins.sbt tag:kanela-runner-plugin version:latest label:"project/plugins.sbt" %}
{% endcode_example %}

### Initialize Kamon

Add the calls to `Kamon.initWithoutAttaching` and `Kamon.stop` to the `loadDev` method in your application loader:

{% code_example %}
{%   language scala guides/install/lagom-scala-sbt/hello-impl/src/main/scala/io/kamon/hello/impl/HelloLoader.scala tag:initialize-kamon-dev version:latest label:"ApplicationLoader" %}
{% endcode_example %}

### Enable the SbtKanelaRunnerLagom Plugin

Finally, pick one (and only one) of your implementation services and enable the `SbtKanelaRunnerLagom` plugin on it:

{% code_example %}
{%   language scala guides/install/lagom-scala-sbt/build.sbt tag:enable-sbt-kanela-runner version:latest label:"SBT / build.sbt" %}
{% endcode_example %}

And you are ready to go. The next time you execute `run` or `runAll` on your SBT terminal you will see the Kamon 
instrumentation kick off for the project that has the `SbtKanelaRunnerLagom` plugin enabled.

[reporter]: ../../../reporters/
[automatic-instrumentation]: ../../../instrumentation/
[how-to-guides]: ../../../guides/#how-to-guides
