---
title: Kamon | Get Started
layout: documentation-1.x
---

What is Kamon?
==============

Kamon is a monitoring toolkit for applications running on the JVM. It gives you Metrics, Tracing and Context Propagation
APIs without locking you to any specific vendor. All Kamon APIs are completely decoupled from the services that can
receive the data, be it StatsD, Prometheus, Kamino, Datadog, Zipkin, Jaeger or any other supported reporter, with Kamon
you instrument your application once and report anywhere you want.



Start using Kamon
-----------------

There are a number of Recipes that can help you get the exact results you are looking for but, if you just want a quick
start then follow these steps:

#### Add the Library

All Kamon modules are available through Maven Central and you just need to add them as a dependency to your project. The
details on how to do this will differ depending on your dependency management tool of choice, but usually just by knowing
that our group id is `io.kamon` and our artifacts are named after the module name you are good to go. To get `kamon-core`
in your project, try one of these:

{% code_example %}
{%   language scala kamon-1.x/kamon-core-sbt/build.sbt tag:base-kamon-dependencies label:"SBT" %}
{%   language markup kamon-1.x/kamon-core-maven/pom.xml tag:base-kamon-dependencies label:"Maven" %}
{%   language markup kamon-1.x/kamon-core-gradle/build.gradle tag:base-kamon-dependencies label:"Gradle" %}
{% endcode_example %}

Starting with Kamon 1.0.0 we only support Java 8+. All modules are published for Scala 2.10, 2.11 and 2.12. If you are
not familiar with the Scala version suffix then just pick the greatest Scala version available, currently 2.12, as shown
in the Maven/Gradle examples above.

Also, we publish snapshots to our [Bintray Snapshots Repository][1] when trying out new concepts or releasing test versions
of our modules, keep that in mind if you want to be on the bleeding edge. There you can find instructions for Maven and
Gradle; for SBT add `resolvers += Resolver.bintrayRepo("kamon-io", "snapshots")` to your `build.sbt` file and your done.



#### Record Metrics

The `kamon.Kamon` companion object gives you everything you need to create metrics Instruments and start measuring your
application's behavior:

{% code_example %}
{%   language scala kamon-1.x/kamon-core-sbt/src/main/scala/kamon/examples/scala/GetStarted.scala tag:get-started-metrics %}
{%   language java kamon-1.x/kamon-core-sbt/src/main/java/kamon/examples/java/GetStarted.java tag:get-started-metrics %}
{% endcode_example %}

You can simply use the instruments returned by Kamon or you can `.refine(...)` them to get specialized instruments with
the specified tags.


#### Record Spans

Again, all you need is in the `kamon.Kamon` companion object, although, you are most likely never going to start a Span
by yourself but rather use the provided instrumention for toolkits and frameworks.

{% code_example %}
{%   language scala kamon-1.x/kamon-core-sbt/src/main/scala/kamon/examples/scala/GetStarted.scala tag:get-started-spans %}
{%   language java kamon-1.x/kamon-core-sbt/src/main/java/kamon/examples/java/GetStarted.java tag:get-started-spans %}
{% endcode_example %}

Spans can be tagged with Strings, Longs and Booleans. Also, Kamon will automatically track metrics for all your Spans and
you can customize that behavior by using `span.tagMetric(...)` on any Span.


#### Start Reporting your Data

Reporters take care of sending the Metrics and Tracing data to your backend of choice. Check the Reporters section for
all available reporters.

{% code_example %}
{%   language scala kamon-1.x/kamon-core-sbt/src/main/scala/kamon/examples/scala/GetStarted.scala tag:get-started-reporters %}
{%   language java kamon-1.x/kamon-core-sbt/src/main/java/kamon/examples/java/GetStarted.java tag:get-started-reporters %}
{% endcode_example %}


#### What's Next?

That's a basic setup that can get you reporting metrics and trace data, but there is so much more that you can do with
Kamon. Follow the Recipes and have fun!



[1]: https://bintray.com/kamon-io/snapshots
[Akka]: http://akka.io/
[configuration]: /documentation/kamon-core/0.6.6/configuration/
[sbt-aspectj]: https://github.com/sbt/sbt-aspectj/
[load-time weaving example]: https://github.com/sbt/sbt-aspectj/tree/master/src/sbt-test/weave/load-time/
[tracing]: /documentation/kamon-core/0.6.6/tracing/core-concepts/
[metrics]: /documentation/kamon-core/0.6.6/metrics/core-concepts/
[mailing list]: https://groups.google.com/forum/#!forum/kamon-user
