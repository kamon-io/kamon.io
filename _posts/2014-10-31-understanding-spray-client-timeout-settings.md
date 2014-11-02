---
layout: post
title: Understanding Spray Client Timeout Settings
date: 2014-11-02
categories: teamblog
---

If you have ever used spray-client in a real world application you certainly must have come to the point of setting
reasonable timeouts for HTTP requests, and you might already be familiar with these guys (extracted from Spray's
reference.conf file):

```
spray.can {
  client {
    # The max time period that a client connection will be waiting for a response
    # before triggering a request timeout. The timer for this logic is not started
    # until the connection is actually in a state to receive the response, which
    # may be quite some time after the request has been received from the
    # application!
    # There are two main reasons to delay the start of the request timeout timer:
    # 1. On the host-level API with pipelining disabled:
    #    If the request cannot be sent immediately because all connections are
    #    currently busy with earlier requests it has to be queued until a
    #    connection becomes available.
    # 2. With pipelining enabled:
    #    The request timeout timer starts only once the response for the
    #    preceding request on the connection has arrived.
    # Set to `infinite` to completely disable request timeouts.
    request-timeout = 20 s

    # The time period within which the TCP connecting process must be completed.
    # Set to `infinite` to disable.
    connecting-timeout = 10s  
  }

  host-connector {
    # The maximum number of parallel connections that an `HttpHostConnector`
    # is allowed to establish to a host. Must be greater than zero.
    max-connections = 4

    # The maximum number of times an `HttpHostConnector` attempts to repeat
    # failed requests (if the request can be safely retried) before
    # giving up and returning an error.
    max-retries = 5

    # If this setting is enabled, the `HttpHostConnector` pipelines requests
    # across connections, otherwise only one single request can be "open"
    # on a particular HTTP connection.
    pipelining = off  

  }
}
```

Their meanings are very clearly described in the comments, but do people really understand what they mean? quite often
the answer is no, so we decided to throw a bit of light over these configuration settings and uncover what is going on
under the hood, hoping to develop a decent understanding on what happens when send you a HTTP request using spray-client
with the Request Level API as well as the Host Level API.

Please note that this post is not intended to be a comprehensive guide on how to configure spray-client timeouts but
rather a short explanation of the underpinnings of it that should allow you to understand the settings and how they
affect the behaviour of your application. Hopefully that will be enough for you to find the right values!

### Using the Request Level API ###

Let's start with a simple code example:

```scala
import akka.actor.ActorSystem
import akka.util.Timeout
import spray.client.pipelining.sendReceive
import spray.httpx.RequestBuilding
import scala.concurrent.duration._

object SprayClientExample extends App with RequestBuilding {
  implicit val requestTimeout = Timeout(60 seconds)
  implicit val system = ActorSystem("spray-client-example")
  implicit val executionContext = system.dispatcher

  val clientPipeline = sendReceive

  val startTimestamp = System.currentTimeMillis()
  val response = clientPipeline {
    Get("http://127.0.0.1:9090/users/kamonteam/timeline")
  }
  response.onComplete(_ => println(s"Request completed in ${System.currentTimeMillis() - startTimestamp} millis."))
}
```

We have a simple service running on `localhost:9090` that will respond to these requests with a 15 seconds delay. Running
the above example code, after the short wait, yields the following output:

```
Request completed in 16020 millis.
```

Nothing special, it does exactly what we think it should do. Note that we are setting a high (60 seconds) timeout for
the Request Level API, primarily to avoid it getting in our way for this tests and we will leter revisit it. Now let's
start with the surprises, what happens if we send six requests at once with the default configration?, this modified
version of the code:

```scala
  for(r <- 1 to 6) {
    val startTimestamp = System.currentTimeMillis()
    val response = clientPipeline {
      Get("http://127.0.0.1:9090/users/kamonteam/timeline")
    }
    response.onComplete(_ => println(s"Request [$r] completed in ${System.currentTimeMillis() - startTimestamp} millis."))
  }
```

Yields this output (ordering might be different when you try it, but the important bits will remain):

```
Request [3] completed in 15887 millis.
Request [4] completed in 15887 millis.
Request [1] completed in 16033 millis.
Request [2] completed in 15887 millis.
Request [6] completed in 30891 millis.
Request [5] completed in 30891 millis.
```

At this point you might have two questions in mind: first, why the first four requests complete in ~15 seconds but the
last two requests take ~30 seconds to complete? That's the easy one, Spray Client creates a `HttpHostConnector` for each
host to which you intent to send requests to (roughly speaking, more details apply), you can think of the Host Connector
as a connection pool that has `spray.can.host-connector.max-connections` connections to the given host and with the
default value of four, the first four requests get dispatched immediatly and the other two have to wait until a
connection is available to proceed. If you set `max-connections` to a higher value, there will be enough connections for
all requests to be dispatched immediatly and all of them will finish in ~15 seconds, as expected.

The second question is, why a request can take ~30 seconds to complete if `spray.can.client.request-timeout` is set to
20 seconds by default?. The documentation is very clear stating that _"The timer for this logic is not started until the
connection is actually in a state to receive the response, which may be quite some time after the request has been
received from the application!"_, but, how big can that _"quite some time"_ be? well, it depends on a few factors
related to how Spray Client works under the hood, this little diagram becomes handy to understand what is going on
there.

<img class="img-responsive" src="/assets/img/diagrams/spray-client-actors.png">

Following the flow in the diagram:

1. Your application code uses a `sendReceive` to send a HTTP request. By default, the `sendReceive` will use the
HttpManager as the transport for the HTTP request (that's what you get when you evaluate `IO(Http)` for a actor system).
The HttpManager is the root of all the Spray-related actors, both on the server and client side, but for simplicity we
only included the ones related to spray-client in the diagram.

2. The HttpManager will use target host (among other details) in the HTTP request to determine the HttpHostConnector to
which the request should be sent. If no HttpHostConnector is available for the given host, a new one is created and the
request is dispatched to it. Additionaly, based on the client connection settings (user-agent header, idle timeout,
request timeout and some others) the HttpManager will locate or create a HttpClientSettingsGroup that is required by the
HttpHostConnector to work.

3. The HttpHostConnector does it's magic. As we said before, the HttpHostConnector acts as a connection pool and as
such, it has to find or create a suitable HttpHostConnectionSlot for dispatching the incoming HTTP request and if it
can't find one then it will queue the request until a suitable one becomes available. Depending on whether HTTP
pipelining is on or off the strategy for dispatching the requests will vary, we will assume the default setting of
`spray.can.host-connector.pipelining = off` and you can read more these strategies in the [Spray documentation][spray-host-connector-documentation].
When the host connector creates a new HttpHostConnectionSlot, the new child is provided with the HttpClientSettingsGroup
that was passed by the HttpManager when creating the HttpHostConnector. Here is where the last two request from our
earlier example got queued waiting for a connection to be available.

4. The HttpHostConnectionSlot will ask the HttpClientSettingsGroup to create a new HttpClientConnection and once it is
connected it will dispatch the request to it. The process of setting up the HttpClientConnection only happens when the
first request arrives and then it will be reused as long as possible. 

5. The HttpClientConnection talks to the Akka-IO layer to establish the TCP connection and sends the HTTP request data
to it. This is a very simplyfied version of what happens here, but in order to keep it short we should just be aware
that it might take some time to establish the TCP connection and it has to happen within the timeout specified by
`spray.can.client.connecting-timeout` and then, after the connection is established and the request is dispatched the
`spray.can.client.request-timeout` starts running. 

Looking back at the "how big can that _"quite some time"_ be?", much more comes to mind. If there are no available
connections the request will be queued until one becomes available. If no connection is established then all the
relevant actors need to be created. Finally, at least, four actor messages involved since the moment where the client
application code sends the HTTP request until the point where the request timeout starts counting, and all of these
actors, by default, are scheduled in the Actor System's default dispatcher which might be busy with some other work and
potentially blocked by some naively uncompartmentalized expensive operations.


### What about the Host Level API? ###

The Host Level API is just about talking directly to the HttpHostConnector rather than going through the HttpManager to
get to it. After that, everything is the same as what we described above. The gist of it is that you send a
`Http.HostConnectorSetup` message to the HttpManager and it will reply with a `Http.HostConnectorInfo` containing the
ActorRef of the HttpHostConnector, then you can send the requests directly to it. 

If you decide to setup and use a HttpHostConnector directly, keep in mind that it has an idle timout after which it will
be stopped and the previous ActorRef will no longer be valid, so you either set the idle timeout to infinte or watch the
ActorRef and handle the situation gracefully somehow. We tend to recommend people to avoid the Host Level API since 
using it correctly adds a bit of complexity that is already solved by the HttpManager and in most cases saving one actor
message there is not going to be significant compared to all the work that has to be done down the road.


### The Meaning(less) of the sendReceive Timeout ###

Take a look at the signatures of `sendReceive`:

```scala
  type SendReceive = HttpRequest ⇒ Future[HttpResponse]

  def sendReceive(implicit refFactory: ActorRefFactory, executionContext: ExecutionContext, 
    futureTimeout: Timeout = 60.seconds): SendReceive

  def sendReceive(transport: ActorRef)(implicit ec: ExecutionContext, futureTimeout: Timeout): SendReceive
```

The implicit timeout provided in our example as `requestTimeout` satisfies the `futureTimeout` implicit parameter of the
`sendReceive` function, but this timeout doesn't have anything to do with all the timeouts we have seen before, it is
just used to satisfy the timeout required by the [ask] made to the transport actor (the HttpManager by default) and give
you back a `Future[HttpResponse]` that will be fulfilled when the response arrives. If the future timeout is reached and
no response was received, the future will be completed with a AskTimeoutException **but the request processing will
continue down the road, until a result (succesful or not) can be returned**.


### Continues Processing? Tell Me More ###

Spray Client doesn't have the notion of a deadline for processing a HttpRequest, it just knows that the request has to
go through a series of steps to complete (succesfuly or not) and only after all the steps are completed it will cease
processing the request and give you a response. If we modify the `requestTimeout` variable in our previous example to 10
seconds, all response futures will be completed with a AskTimeoutException after 10 seconds, but still, Spray Client
will send all requests to our service anyway, even the last two requests that have to wait ~15 seconds until a
connection becomes available. The generated by that modified version looks like this:

```
Request [1] completed in 10161 milliseconds.
Request [2] completed in 10020 milliseconds.
Request [3] completed in 10020 milliseconds.
Request [4] completed in 10020 milliseconds.
Request [5] completed in 10020 milliseconds.
Request [6] completed in 10020 milliseconds.
```

but ~5 seconds after that, the first four responses arrive and go to dead letters because nobody is waiting for them and
~15 seconds after it the last two responses arrive, also going to dead letters. The log looks like this (shortened to
keep it readable):

```
[06:00:12.153] [akka://spray-client-example/deadLetters] Message [spray.http.HttpResponse] from ...
[06:00:12.153] [akka://spray-client-example/deadLetters] Message [spray.http.HttpResponse] from ...
[06:00:12.154] [akka://spray-client-example/deadLetters] Message [spray.http.HttpResponse] from ...
[06:00:12.154] [akka://spray-client-example/deadLetters] Message [spray.http.HttpResponse] from ...
[06:00:27.161] [akka://spray-client-example/deadLetters] Message [spray.http.HttpResponse] from ...
[06:00:27.161] [akka://spray-client-example/deadLetters] Message [spray.http.HttpResponse] from ...
```


### One More Thing: Retries ###

The last thing that you need to keep in mind is that Spray Client can (and will, by default) retry idempotent requests
for the number of times specified in `spray.can.host-connector.max-retries` and retrying is part of the request
processing steps mentioned before, meaning that it will retry 5 times by default before giving up, wheter you like it or
not. If you have a server taking too long to respond on the other side it might take at least ~100 seconds (5 retries of
20 seconds each, without counting connection setup time) for Spray Client to give up on a given request. Not setting the
retries limits accordingly to what your application needs might cause your system to spend to many time processing
requests that you no longer are interested in.

As a final advice, always try to simulate the various conditions mentioned here (more requests than connections, slow
connection times, slow server, etc.) in a development environment and tune your settings accordingly, many CPU cycles
might be saved by doing do!


[spray-host-connector-documentation]: http://spray.io/documentation/1.3.2/spray-can/http-client/host-level/#using-an-httphostconnector
[ask]: https://github.com/spray/spray/blob/release/1.3/spray-client/src/main/scala/spray/client/pipelining.scala#L38
