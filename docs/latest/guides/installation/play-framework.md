---
title: Monitoring Play Framework Applications with Kamon | Installation Guides
description: >-
  This guide walks you through setting up Kamon with a Play Framework application and sending your first metrics and 
  traces to Kamon APM
layout: docs
---

{% include toc.html %}

Monitoring Play Framework Applications with Kamon
=================================================

This guide walks you through setting up Kamon with a Play Framework application and sending your first metrics and
traces to Kamon APM. The same steps will work if you choose any other [reporter][reporter].

Before we start, make sure you are using **Play Framework 2.6, 2.7 or 2.8**. This guide assumes that you have a Play
Framework project using SBT as the build tool, and that you are not using a custom Application Loader (see the [special
cases](#special-cases) section below).

Let's get to it!


## 1. Add the Kamon Dependencies

Add the `kamon-bundle` and `kamon-apm-reporter` dependencies to your `build.sbt` file:

{% include kamon-play-dependency.md version="latest" %}

The Kamon Bundle dependency contains all the Kamon [automatic instrumentation][automatic-instrumentation] modules in a
single jar, so that you don't need any additional dependencies. The APM reporter dependency is in charge of sending all
your metrics and traces to Kamon APM.


## 2. Add the SBT Kanela Runner Plugin

Add the SBT Kanela Runner Plugin to your `project/build.sbt` file:

{% include kamon-play-plugin.md version="latest" %}

Make sure that the `play-2.x` suffix matches your Play Framework version.

The SBT Kanela Runner Plugin ensures that Kamon's instrumentation is applied when you hit `run` from the SBT console on
Play Framework projects (a.k.a. Development Mode). See the special cases below if you want to enable instrumentation
in [Production Mode](#using-instrumentation-in-production-mode-only) only.


## 3. Enable the JavaAgent Plugin

Include the `JavaAgent` plugin in the call to `.enablePlugins` in your `build.sbt` file:

{% include kamon-play-enable-javaagent.md version="latest" %}

The SBT Java Agent Plugin is downloaded as a depency of the SBT Kanela Runner Plugin, you only need to enable it. Enabling
the SBT Java Agent Plugin ensures that the Kamon instrumentation will be initialized properly when running in Production
Mode.

## 4. Configure the APM Reporter

Add your service name and API key to the `conf/application.conf` file:

{% code_block hcl %}
kamon {
  environment.service = "Play Application"
  apm.api-key = "Your API Key"
}
{% endcode_block %}

You can copy your API key directly from [Kamon APM](https://apm.kamon.io?envinfo=show){:target="_blank" rel="noopener"}.



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

Special Cases
=============

## Custom Application Loaders
If your Play Framework application has a custom `ApplicationLoader`, you will need to add a few lines of code to ensure 
that Kamon is initialized and stopped properly. Add these lines to the `load` method in your `ApplicationLoader` 
implementation:

{% code_block scala %}
import kamon.Kamon

class MyApplicationLoader extends ApplicationLoader {
  def load(context: ApplicationLoader.Context): Application = {

    // For Kamon 2.3.1+
    Kamon.initWithoutAttaching(context.initialConfiguration.underlying)

    // For Kamon 2.3.0 and earlier versions
    //   - The reconfigure call ensures that Kamon is configured with 
    //     Play's configuration file from `conf/application.conf`
    //   - The second line starts reporters and system metrics collection
    Kamon.reconfigure(context.initialConfiguration.underlying)
    Kamon.loadModules()


    // Ensures that Kamon stops after every run. Specially important
    // when running on Development mode.
    context.lifecycle.addStopHook { () =>
      Kamon.stop()
    }

    new MyComponents(context).application
  }
}
{% endcode_block %}


## Using Instrumentation in Production Mode Only
If you want to use Kamon's instrumentation in Production Mode only, you can skip the SBT Kanela Runner Plugin and use the 
`sbt-javaagent` plugin directly. 

To swap the plugins, remove the SBT Kanela Runner Plugin from [Step #2](#2-add-the-sbt-kanela-runner-plugin) and add this 
line instead:

{% code_block scala %}
addSbtPlugin("com.lightbend.sbt" % "sbt-javaagent" % "0.1.6")
{% endcode_block scala %}

Then, add the `javaAgents` option to your Play project, pointing to the Kanela agent:

{% code_block scala %}
javaAgents += "io.kamon" % "kanela-agent" % "{{ site.data.versions.latest.kanela }}"
{% endcode_block scala %}

That's it. The rest of the installation steps remain the same.



[reporter]: ../../../reporters/
[automatic-instrumentation]: ../../../instrumentation/
[how-to-guides]: ../../../guides/#how-to-guides
