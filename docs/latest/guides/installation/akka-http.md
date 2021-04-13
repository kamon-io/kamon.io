---
title: 'Setting up Kamon with Akka HTTP'
description: 'Learn how to collect metrics and traces from Akka HTTP apps using Kamon'
layout: docs
redirect_from:
- /docs/latest/guides/frameworks/elementary-akka-setup/
- /documentation/1.x/recipes/monitoring-akka-quickstart/
---

Setting up Kamon with Akka HTTP
====================================

This guide walks you through setting up Kamon with an Akka HTTP application and sending your first metrics and traces to
Kamon APM. The same steps will work if you choose any other [reporter][reporter].

Before we start, make sure you are using **Akka HTTP 10.1 or 10.2**. Earlier Akka HTTP versions are not supported.

Let's get to it!


## 1. Add the Kamon Dependencies

Add the `kamon-bundle` and `kamon-apm-reporter` dependencies to your `build.sbt` file (or equivalent for other build 
tools):

{% code_example %}
{%   language scala guides/install/akka-http-scala/build.sbt tag:kamon-dependencies version:latest label:"SBT / build.sbt" %}
{%   language markup guides/install/akka-http-java/pom.xml tag:kamon-dependencies version:latest label:"Maven / pom.xml" %}
{%   language groovy guides/install/akka-http-java/build.gradle tag:kamon-dependencies version:latest label:"Gradle / build.gradle" %}
{% endcode_example %}

The Kamon Bundle dependency contains all the Kamon [automatic instrumentation][automatic-instrumentation] modules in a
single jar, so that you don't need any additional dependencies. The APM reporter dependency is in charge of sending all
your metrics and traces to Kamon APM.


## 2. Initialize Kamon

Call `Kamon.init()` as the first thing in your main method:

{% code_example %}
{%   language scala guides/install/akka-http-scala/src/main/scala/com/example/QuickstartApp.scala tag:kamon-init version:latest %}
{%   language java guides/install/akka-http-java/src/main/java/com/example/QuickstartApp.java tag:kamon-init version:latest %}
{% endcode_example %}

The call to `Kamon.init()` installs the automatic instrumentation on the JVM and starts sending data to Kamon APM (and
any other reporters you might have). Is is very important that the call to `Kamon.init()` happens before creating your 
`ActorSystem` instance, otherwise the automatic instrumentation will not be able to hook into all the Akka-related classes.

{% alert warning %}
  <p>
    Beware that some Scala projects apply mixins to their "main" companion object, and those mixins might cause
    Akka-related classes to load before Kamon initializes.
  </p>

  <p>
    Start your applications with the [-javaagent JVM option](../../how-to/start-with-the-kanela-agent/) if you can't work
    around the mixins' initialization order.
  </p>
{% endalert %}


## 3. Configure the APM Reporter

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

<img class="img-fluid" src="/assets/img/kamon-status-page.png" alt="Kamon Status Page">

The important bits to check in the Status Page are that the modules have a green check mark and instrumentation is shown
as active on the top-left corner. If your installation didn't go well, please stop by our [Github Discussions](https://github.com/kamon-io/Kamon/discussions){:target="_blank" rel="noopener"}
and post a question. We will do our best to help!

That is it, your installation is done! You might want to check out the [How To Guides][how-to-guides] for common 
post-installation steps to improve your instrumentation.

[reporter]: ../../../reporters/
[how-to-guides]: ../../../guides/#how-to-guides