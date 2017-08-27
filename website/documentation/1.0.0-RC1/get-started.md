---
title: Kamon | Get Started
layout: documentation-1.0.0-RC1
---

Get Started with Kamon
======================

Kamon is a monitoring toolkit for applications running on the JVM. At its core, Kamon gives you a simple but powerful
APIs for recording metrics, enable distributed tracing and perform in-process and cross-process context propagation.
All these APIs are completely decoupled from the services that can receive the data, be it StatsD, Prometheus, Kamino,
Datadog, Zipkin, Jaeger or any other supported backend, with Kamon you instrument your application once and report to
everywhere you want.

Kamon is distributed as a core module with all the metric recording and trace manipulation APIs and optional modules
that provide bytecode instrumentation and/or reporting capabilities to your application. All the modules are added to
your application as simple library dependencies and additionally, if you are using instrumentation modules then the
AspectJ Weaver agent needs to be included as a JVM startup parameter when running your application.

Make sure you follow this steps:



Include the modules you want in your project
--------------------------------------------

All Kamon modules are available through Maven Central and you just need to add them as a compile dependency to your
project. The details on how to do this will differ depending on your dependency management tool of choice, but usually
just by knowing that our group id is `io.kamon` and our artifacts are named after the module name you are good to go.
Still, here are some examples with common build tools:

{% code_example %}
{%   language scala kamon-1.0.x/kamon-core-examples/build.sbt tag:base-kamon-dependencies label:"SBT" %}
{%   language markup kamon-1.0.x/maven-basic-example/pom.xml tag:base-kamon-dependencies label:"Maven" %}
{% endcode_example %}

Our latest version is published for both Scala 2.10 and Scala 2.11 using SBT's cross version feature, meaning that our
artifacts are suffixed by either `_2.10` or `_2.11` to denote the target Scala version. If you are not in the Scala
world and you are not familiar with this conventions just pick the greatest Scala version available, currently 2.11, as
shown in the Maven example above. You can find a complete list of Kamon modules in the left panel of this page.



Start Kamon
-----------

To access the metrics and tracing APIs, and to ensure that all Kamon modules are loaded you will need to start Kamon by
using the `Kamon.start(..)` method. This is a one time operation that you should perform as early as possible in your
application code to ensure that all instrumentation and uses of the Kamon APIs will work properly. Having it as the
first line in your application's main wont hurt:


{% code_example %}
{%   language scala kamon-1.0.x/kamon-core-examples/src/main/scala/kamon/examples/scala/GetStarted.scala tag:get-started %}
{%   language java kamon-1.0.x/kamon-core-examples/src/main/java/kamon/examples/java/GetStarted.java tag:get-started %}
{% endcode_example %}

Optionally, you can provide a custom configuration object when starting Kamon. See the [configuration] section for a
quick overview of how to configure Kamon. When you are done with Kamon, remember to shut it down using the
`Kamon.shutdown()` method.



### Optional: Prepare your application to start with the AspectJ Weaver ###

This step is only required if any of the modules that you included in your application requires AspectJ, if that is not
your case you can jump directly to the enjoy section!

Starting your application with the AspectJ weaver is dead simple, just add the `-javaagent` JVM startup parameter
pointing to the weaver's file location and you are done. The details on how to do this vary depending on your preferred
way of deployment, here are some quick notes for the most common deployment scenarios:

{% code_example %}
{%   language text manually-adding-aspectj-weaver-agent/readme.md tag:manually-add-aspectj-weaver label:"Manually" %}
{%   language text manually-adding-aspectj-weaver-agent/readme.md tag:using-aspectj-runner label:"sbt-aspectj-runner" %}
{%   language scala using-sbt-aspectj-plugin/build.sbt tag:using-sbt-aspectj label:"sbt-aspectj" %}
{% endcode_example %}

In case you need to use the AspectJ Weaver but you didn't set it up correctly, Kamon will log a big and noticeable error
message in your log that will hardly pass unnoticed, but Kamon will not kill the application for this error.

If your deployment method is not listed here and you can't figure out how to add the weaver, please ask for help in our
[mailing list].



Enjoy!
------

At this point you already have what is necessary to use Kamon. After the Kamon initialization, all available modules
will be automatically started, you don't need to explicitly activate/start them.

But, Kamon is not just about having a couple histograms and counters hanging around in your code to measure stuff, we
can do much more than that! Please, refer to the [metrics] and [tracing] module's documentation to learn how to use
these core features and to each additional module's documentation for usage details and recommendations.


[Akka]: http://akka.io/
[configuration]: /documentation/kamon-core/0.6.6/configuration/
[sbt-aspectj]: https://github.com/sbt/sbt-aspectj/
[load-time weaving example]: https://github.com/sbt/sbt-aspectj/tree/master/src/sbt-test/weave/load-time/
[tracing]: /documentation/kamon-core/0.6.6/tracing/core-concepts/
[metrics]: /documentation/kamon-core/0.6.6/metrics/core-concepts/
[mailing list]: https://groups.google.com/forum/#!forum/kamon-user
