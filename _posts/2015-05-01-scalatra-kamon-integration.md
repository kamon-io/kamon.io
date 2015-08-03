---
layout: post
title: Scalatra Kamon Integration
date: 2015-08-03
categories: teamblog
---
In this post we’ll show how set up a basic **Scalatra** project and instrument it to test the **Kamon** integration. This is a really simplified example and also we will avoid some issues related to the installation, because there are awesome tutorials about that, having said that, let's start.

### Build Setup ###

We need to include in our [Build.scala] some dependencies. It should look like this.

{% code_block scala %}

val dependencies = Seq(
    "io.kamon"    	          %% "kamon-core"           	  % "0.4.0",
     // [Optional]
    "io.kamon"    	          %% "kamon-scala"                % "0.4.0",
     // [Optional]
    "io.kamon"    	          %% "kamon-log-reporter"   	  % "0.4.0",
     // ... and so on with all the others dependencies you need.
    )

val main = Project(appName, file(".")).settings(libraryDependencies ++= dependencies)
                                      .settings(defaultSettings: _*)
                                      .settings(aspectjSettings ++ AspectJ.aspectjSettings) //(1)
{% endcode_block %}

1. We need to register the [AspectJ] weaver with the purpose of automatically propagating the `TraceContext` across the asynchronous operations that might be scheduled for a given Future.

### Create a Simple Servlet ###

Let's start by creating a convenient trait in order to use the Kamon [instruments]

{% code_block scala %}
trait KamonSupport {
  def counter(name: String) = Kamon.metrics.counter(name)
  def minMaxCounter(name: String) = Kamon.metrics.minMaxCounter(name)
  def time[A](name: String)(thunk: => A) = Latency.measure(Kamon.metrics.histogram(name))(thunk)
  def traceFuture[A](name:String)(future: => Future[A]):Future[A] =
    Tracer.withContext(Kamon.tracer.newContext(name)) {
     future.andThen { case completed ⇒ Tracer.currentContext.finish() }(SameThreadExecutionContext)
   }
}
{% endcode_block %}

Then we create a **Servlet** that will record some metrics. in order to achieve this we mix our `KamonSupport` to call the provided methods.

{% code_block scala %}
class KamonServlet extends ScalatraServlet with KamonSupport with FutureSupport {
  ...  
  get("/time") {
    time("time") {
      Thread.sleep(Random.nextInt(100))
    }
  }

  get("/minMaxCounter") {
    minMaxCounter("minMaxCounter").increment()
  }

  get("/counter") {
    counter("counter").increment()
  }

  get("/async") {
    traceFuture("retrievePage") {
      Future {
        HttpClient.retrievePage()
      }
    }
  }
  ...
}
{% endcode_block %}

now we have 5 **URL** that we can hit:

* **GET** */kamon/time*
* **GET** */kamon/counter*
* **GET** */kamon/minMaxCounter*
* **GET** */kamon/async*

### Bootstrap Scalatra and Kamon ###

We will need to bootstrap `Scalatra` and hook `Kamon` into it's lifecycle and the best place for this is  `ScalatraBootstrap`.

{% code_block scala %}

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext):Unit = {
    Kamon.start() //(1)
    context.mount(new KamonServlet(), "/kamon")
  }

  override def destroy(context: ServletContext):Unit = {
    Kamon.shutdown() //(2)
  }
}
{% endcode_block %}

1. To access the metrics and tracing APIs, and to ensure that all Kamon modules are loaded you will need to start Kamon by using the `Kamon.start(..)` method.
2. When you are done with Kamon, remember to shut it down using the `Kamon.shutdown()` method.

### Select a Kamon Backend ###

This time will we use the [kamon-log-reporter]. This module is not meant to be used in production environments, but it certainly is a convenient way to test Kamon and know in a quick manner what's going on with our application in development, moreover like all Kamon modules it will be picked up from the classpath and started at runtime.

{% code_block typesafeconfig %}
kamon {
  modules {
    kamon-log-reporter.auto-start = yes
  }
}
{% endcode_block %}

Additionally we can find more info about how to configure [modules] and supported backends([StatsD], [Datadog], [New Relic], [Your Own]) in the docs.

### Start the Server ###
**Scalatra** uses `Jetty` internally, and is in itself a simple java servlet. So what we can do is just run an embedded `Jetty` instance that mount the servlet and configure it.

{% code_block scala %}
object EmbeddedServer extends App {
  val server = new Server(8080)
  val context: WebAppContext = new WebAppContext()

  context.setServer(server)
  context.setContextPath("/")
  context.setWar("src/webapp")
  server.setHandler(context)

  try {
    server.start()
    server.join()
  } catch {
    case e: Exception =>
      e.printStackTrace()
      System.exit(1)
  }
}
{% endcode_block %}

We can run this application directly running froms the console ```sbt run```. The output we will see will be something like this if we hit some of the endpoints we've set up.

* **curl** *http://localhost:8080/kamon/time*
* **curl** *http://localhost:8080/kamon/counter*
* **curl** *http://localhost:8080/kamon/minMaxCounter*

{% code_block text %}
+------------------------------------------------------------------------------------------------+
|                                                                                                |
|                                         Counters                                               |
|                                       -------------                                            |
|                                    counter  =>  1                                              |
|                                                                                                |
|                                        Histograms                                              |
|                                      --------------                                            |
|  time                                                                                          |
|    Min: 57671680     50th Perc: 57671680       90th Perc: 57671680       95th Perc: 57671680   |
|                      99th Perc: 57671680     99.9th Perc: 57671680             Max: 57671680   |
|                                                                                                |
|                                      MinMaxCounters                                            |
|                                    -----------------                                           |
|  minMaxCounter                                                                                 |
|          Min: 0                      Average: 0.0                         Max: 1               |
|                                                                                                |
+------------------------------------------------------------------------------------------------+
{% endcode_block %}

### Ok, but show me the money ###
So far so good. **But what about if my route is running in a Future?**. The answer is **Nothing**, the body of the future will be executed asynchronously on some other thread in a provided `ExecutionContext`, but Kamon through bytecode instrumentation, will capture the `TraceContext` available when the future was created and make it available while executing the future’s body.

Well, let's run the application with ```sbt run``` and we measure the **async** operation.

* **curl** *http://localhost:8080/kamon/async*

{% code_block %}
+------------------------------------------------------------------------------------------------+
|                                                                                                |
|    Trace: retrievePage                                                                         |
|    Count: 1                                                                                    |
|                                                                                                |
|  Elapsed Time (nanoseconds):                                                                   |
|    Min: 2164260864   50th Perc: 2164260864     90th Perc: 2164260864     95th Perc: 2164260864 |
|                      99th Perc: 2164260864   99.9th Perc: 2164260864           Max: 2164260864 |
|                                                                                                |
+------------------------------------------------------------------------------------------------+
{% endcode_block %}

For a more detailed explanation about the Kamon **Trace module** and **Automatic TraceContext Propagation with Futures** please start [here].

### Enjoy! ###
There it is, all your data metrics available to import into whatever tool you like. From here, you should be able to instrument your applications as needed.

We also encourage you to review the full source code of [Scalatra Kamon Example] used in this tutorial.

Thanks to [Carlos Ferreyra] for the review.

[Carlos Ferreyra]:https://twitter.com/cryptic_marlbo
[modules]: /core/modules/using-modules/
[instruments]: /core/metrics/instruments/
[kamon-log-reporter]: /backends/log-reporter/
[AspectJ]: https://github.com/kamon-io/Kamon/blob/master/kamon-examples/kamon-scalatra-example/project/AspectJ.scala
[Build.scala]:https://github.com/kamon-io/Kamon/blob/master/kamon-examples/kamon-scalatra-example/project/Build.scala
[StatsD]: /backends/statsd/
[Datadog]: /backends/datadog/
[New Relic]: /backends/newrelic/
[Your Own]: /core/metrics/subscription-protocol/
[here]: /core/tracing/core-concepts/
[Scalatra Kamon Example]: https://github.com/kamon-io/Kamon/tree/master/kamon-examples/kamon-scalatra-example
