---
title: Kamon | Documentation | Reporting Data to New Relic
layout: documentation
---

Reporting Data to New Relic
===========================

If you are a [New Relic] user and tried to start your Spray/Akka application using the New Relic's agent you probably
noticed a crude reality: nothing is shown in your dashboard, no web transactions are recognised and errors are not
reported for your Spray/Akka applications. Don't even think about detailed traces for the slowest transactions.

We love Spray and Akka, and we love New Relic, we couldn't leave this happening anymore!

<p class="alert alert-warning">This is not an official New Relic product, and is not endorsed by New Relic.</p>

You can use our New Relic integration module to report Trace metrics to New Relic, the data being currently reported is:

- Time spent for Web Transactions: Also known as `HttpDispatcher` time, represents the total time taken to process a web
  transaction, from the moment the `HttpRequest` is received by spray-can, to the moment the answer is sent to the IO
  layer.
- Apdex
- Errors

Differentiation between JVM and External Services is coming soon, as well as actor metrics and detailed traces.


Installation
-------------

To report data to New Relic just make sure you put the `kamon-newrelic` library in your classpath and start your
application with both, the Aspectj Weaver and New Relic agents. Please refer to our [get started] page for more info on
how to add the AspectJ Weaver and the [New Relic Agent Installations Instructions].


Configuration
-------------

Currently you will need to add a few settings to your `application.conf` file for the module to work:

```scala
akka {
  // Custom logger for New Relic that takes all the `Error` events from the event stream and publish them to New Relic
  loggers = ["akka.event.slf4j.Slf4jLogger", "kamon.newrelic.NewRelicErrorLogger"]

  // Make sure the New Relic extension is loaded with the ActorSystem
  extensions = ["kamon.newrelic.NewRelic"]
}

kamon {
  newrelic {
    // These values must match the values present in your newrelic.yml file.
    app-name = "KamonNewRelicExample[Development]"
    license-key = 0123456789012345678901234567890123456789
  }
}
```


Let's see it in Action!
-----------------------

Let's create a very simple Spray application to show what you should expect from this module. The entire application code
is in our [kamon-newrelic-example] at Github.

```scala
import akka.actor.ActorSystem
import spray.routing.SimpleRoutingApp

object NewRelicExample extends App with SimpleRoutingApp {

 implicit val system = ActorSystem("kamon-system")

 startServer(interface = "localhost", port = 8080) {
   path("helloKamon") {
     get {
       complete {
         <h1>Say hello to Kamon</h1>
       }
     }
   } ~
   path("helloNewRelic") {
     get {
       complete {
         <h1>Say hello to New Relic</h1>
       }
     }
   }
 }
}
```

As you can see, this is a dead simple application: two paths, different responses for each of them. Now let's hit it hard
with Apache Bench:

```bash
ab -k -n 200000 http://localhost:8080/helloKamon
ab -k -n 200000 http://localhost:8080/helloNewRelic
```

After a couple minutes running you should start seeing something similar to this in your dashboard:

<img class="img-responsive" src="/assets/img/newrelic.png">

<div class="alert alert-info">
Note: Don't think that those numbers are wrong, Spray is that fast!
</div>


Limitations
-----------
* The first implementation only supports a subset of New Relic metrics.


Licensing
---------
New Relic has [its own, separate licensing].



[New Relic]: http://newrelic.com
[get started]: /get-started
[New Relic Agent Installations Instructions]: https://docs.newrelic.com/docs/java/new-relic-for-java#h2-installation
[kamon-newrelic-example]: https://github.com/kamon-io/Kamon/tree/master/kamon-examples/kamon-newrelic-example
[its own, separate licensing]: http://newrelic.com/terms
