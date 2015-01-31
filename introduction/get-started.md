---
title: Kamon | Get Started
layout: documentation
---

Get Started with Kamon
======================

Kamon is distributed as a core module with all the metric recording and trace manipulation APIs and optional modules
that provide bytecode instrumentation and/or reporting capabilities to your application. All the modules are added to
your  application as simple library dependencies and additionally, if you are using instrumentation modules then the
AspectJ Weaver agent needs to be included as a JVM startup parameter when running your application.

Let's dig into these steps with more detail:



Include the modules you want in your project
--------------------------------------------

All Kamon modules are available through Maven Central and you just need to add them as a compile dependency to your
project. The details on how to do this will differ depending on your dependency management tool of choice, but usually
just by knowing that our group id is `io.kamon` and our artifacts are named after the module name you are good to go.
Still, here are some examples with common build tools:

{% code_example %}
{%   language scala kamon-core-examples/build.sbt start:3 end:12 label:"SBT" %}
{%   language markup maven-basic-example/pom.xml start:11 end:17 label:"Maven" %}
{% endcode_example %}

You can find a complete list of Kamon modules in the [overview] section.



Create a Kamon Instance
-----------------------

To access the metrics and tracing APIs, and to ensure that all Kamon modules are loaded you will need to create a Kamon
instance. Kamon uses [Akka] internally for several purposes and thus, requires an `ActorSystem` to work. If you don't
have an `ActorSystem` already on your application or you don't even know what it is, don't worry, Kamon can create one
for you, but if you have one we advise you to share the same ActorSystem instance with Kamon. To create a Kamon instance
just call the most convenient factory method four you in the `kamon.Kamon` companion object:


{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/GetStarted.scala start:6 end:12 %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/GetStarted.java start:9 end:15 %}
{% endcode_example %}



### Optional: Prepare your application to start with the AspectJ Weaver ###

This step is only required if any of the modules that you included in your application requires AspectJ, if that is not
your case you can jump directly to the enjoy section!

Starting your application with the AspectJ weaver is dead simple, just add the `-javaagent` JVM startup parameter
pointing to the weaver's file location and you are done. The details on how to do this vary depending on your preferred
way of deployment, here are some quick notes for the most common deployment scenarios:

{% code_example %}
{%   language text manually-adding-aspectj-weaver-agent/readme.md start:4 end:7 label:"Manually" %}
{%   language scala using-sbt-aspectj-plugin/build.sbt start:7 label:"sbt-aspectj" %}
{% endcode_example %}

In case you need to use the AspectJ Weaver but you didn't set it up correctly, Kamon will log a big and noticeable error
message in your log so that it wont pass unnoticed, but Kamon will not kill the application for this error. If your
deployment method is not listed here and you can't figure out how to add the weaver, please ask for help in our
[mailing list].



Enjoy!
------

At this point you already have what is necessary to start using Kamon. After the Kamon initialization, all available
modules will be automatically started, you don't need to explicitly activate them.

But, Kamon is not just about having a couple histograms and counters hanging around in your code to measure stuff, we
can do much more than that! Please, refer to the [metrics] and [tracing] module's documentation to learn how to use and
configure the core features and to each additional module's documentation for usage details and recommendations.


[Akka]: http://akka.io/
[overview]: /introduction/overview/
[sbt-aspectj]: https://github.com/sbt/sbt-aspectj/
[load-time weaving example]: https://github.com/sbt/sbt-aspectj/tree/master/src/sbt-test/weave/load-time/
[tracing]: /core/tracing/core-concepts/
[metrics]: /core/metrics/core-concepts/
[logging]: /core/tracing/logging/
[mailing list]: https://groups.google.com/forum/#!forum/kamon-user
