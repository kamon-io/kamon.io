---
layout: post
title: Scalatra Kamon Integragion
date: 2015-07-30
categories: teamblog
---

Build Setup
----------------------------------------------

you need include in your `Build.scala` some dependencies. It should look like this.

```scala
val resolutionRepos = Seq(
      "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases",
      "Kamon Repository Snapshots" at "http://snapshots.kamon.io"
  )

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

```

### Configuring Scalatra ###
in order to `Scalatra` listen web requests, it necessary register a listener in `web.xml` inside of  `../WEB-INF` folder.

```xml
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0">
    <listener>
        <listener-class>org.scalatra.servlet.ScalatraListener</listener-class>
    </listener>
</web-app>
```
### Create a Simple Servlet ###

Let's start creating a convenient trait in order to use the Kamon instruments:

```scala
trait KamonSupport {
  def counter(name: String) = Kamon.metrics.counter(name)
  def minMaxCounter(name: String) = Kamon.metrics.minMaxCounter(name)
  def time[A](name: String)(thunk: => A) = Latency.measure(Kamon.metrics.histogram(name))(thunk)
}
```

Now let’s create a simple servlet that will record some metrics. Create a file called `KamonServlet.scala` and introduce following code

```scala
class KamonServlet extends ScalatraServlet with KamonSupport {

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
}
```
also we will need `bootstrap` Scalatra, creating `ScalatraBootstrap.scala` in `src/main/scala` folder and adding the following code.

```scala

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext):Unit = {
    Kamon.start()
    context.mount(new KamonServlet(), "/kamon")
  }

  override def destroy(context: ServletContext): Unit = {
    Kamon.shutdown()
  }
}
```
at this point we have 4 URL we can hit:
  * **GET** /kamon/time
  * **GET** /kamon/counter
  * **GET** /kamon/minMaxCounter

### Configure Kamon modules ###

```json
kamon {
  modules {
    kamon-log-reporter.auto-start = yes
  }
}

```

### Start the Server ###
Scalatra uses `Jetty` internally, and is itself is a simple java servlet. So what we can do is just run an embedded `Jetty` instance that points to the servlet and configure it.

```scala
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
```
Before run the our server, we need create a `logback.xml` file to control the logging and only shows messages at `info` level or greater.

```xml
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%date{HH:mm:ss.SSS} %-5level [%thread] %logger{55} - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="info">
        <appender-ref ref="STDOUT" />
    </root>
</configuration>
```
We can run this application directly doing in the console ```sbt run```. The output that we will see will be something like this if we hit some of the endpoints we've set up.

* curl http://localhost:8080/kamon/counter
* curl http://localhost:8080/kamon/time
* curl http://localhost:8080/kamon/minMaxCounter

```
[info] +--------------------------------------------------------------------------------------------------+
[info] |                                                                                                  |
[info] |                                         Counters                                                 |
[info] |                                       -------------                                              |
[info] |                                    counter  =>  1                                                |
[info] |                                                                                                  |
[info] |                                        Histograms                                                |
[info] |                                      --------------                                              |
[info] |  time                                                                                            |
[info] |    Min: 57671680     50th Perc: 57671680       90th Perc: 57671680       95th Perc: 57671680     |
[info] |                      99th Perc: 57671680     99.9th Perc: 57671680             Max: 57671680     |
[info] |                                                                                                  |
[info] |                                      MinMaxCounters                                              |
[info] |                                    -----------------                                             |
[info] |  minMaxCounter                                                                                   |
[info] |          Min: 0                      Average: 0.0                         Max: 1                 |
[info] |                                                                                                  |
[info] +--------------------------------------------------------------------------------------------------+
```

### Ok, but show me the money ###
So far so good. But Kamon ...

```scala
trait KamonSupport {
  ...
  def traceFuture[A](name:String)(future: => Future[A]):Future[A] = Tracer.withContext(Kamon.tracer.newContext(name)) {
    future.onComplete(_ => Tracer.currentContext.finish())(SameThreadExecutionContext)
    future
  }
}
```

```scala
class KamonServlet extends ScalatraServlet with KamonSupport with FutureSupport {

  implicit val executor: ExecutionContext = ExecutionContext.Implicits.global

  get("/async") {
    traceFuture("retrievePage") {
      Future {
        HttpClient.retrievePage()
      }
    }
  }
}

object HttpClient {
  def retrievePage()(implicit ctx: ExecutionContext): Future[String] = {
    val prom = Promise[String]()
    dispatch.Http(url("http://slashdot.org/") OK as.String) onComplete {
      case Success(content) => prom.complete(Try(content))
      case Failure(exception) => println(exception)
    }
    prom.future
  }
}

```

```scala
object AspectJ {
  lazy val aspectjSettings = Seq(
    // fork the run so that javaagent option can be added
    fork in run := true,
    // add the aspectj weaver javaagent option
    javaOptions in run <++= weaverOptions in Aspectj
  )
}
```

```scala
we need modify your `Build.scala` and add the following.

val main = Project(appName, file(".")).settings(libraryDependencies ++= dependencies)
                                      .settings(defaultSettings: _*)
                                      .settings(aspectjSettings ++ AspectJ.aspectjSettings) // we need register the AspectJ weaver
```
```scala
trait KamonSupport {
  ...
  def traceFuture[A](name:String)(future: => Future[A]):Future[A] = Tracer.withContext(Kamon.tracer.newContext(name)) {
    future.onComplete(_ => Tracer.currentContext.finish())(SameThreadExecutionContext)
    future
  }
}
```

```scala
trait KamonSupport {
  ...
  def traceFuture[A](name:String)(future: => Future[A]):Future[A] = Tracer.withContext(Kamon.tracer.newContext(name)) {
    future.onComplete(_ => Tracer.currentContext.finish())(SameThreadExecutionContext)
    future
  }
}
```

well, let run the application with ``sbt run``` and we measure the async operation.

* curl http://localhost:8080/kamon/async

```
[info] +--------------------------------------------------------------------------------------------------+
[info] |                                                                                                  |
[info] |    Trace: retrievePage                                                                           |
[info] |    Count: 1                                                                                      |
[info] |                                                                                                  |
[info] |  Elapsed Time (nanoseconds):                                                                     |
[info] |    Min: 2164260864   50th Perc: 2164260864     90th Perc: 2164260864     95th Perc: 2164260864   |
[info] |                      99th Perc: 2164260864   99.9th Perc: 2164260864           Max: 2164260864   |
[info] |                                                                                                  |
[info] +--------------------------------------------------------------------------------------------------+
```
