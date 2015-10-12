---
layout: post
title: 'Reactive Docker Monitoring'
date: 2015-10-11
categories: teamblog
---

In this post we’ll show how to collect system metrics from Docker containers, making use of a very simple application
application that relies on [Akka Streams/HTTP] and all the benefits of Kamon's machinery.



Unless you were living under a rock and that rock is under the Titanic, then you must have heard about Docker. Nowadays
Docker is everywhere and [all sorts of things] can run in a container. You might already be using Docker for CI purposes
or even in production and when you do so, well, monitoring needs to be there too! We did a bit of exploration on monitoring
the very basic metrics we would like to get out of a container:

* **CPU usage**
* **Memory usage**
* **IO usage**
* **Network usage**

### How do we get these Metrics? ###

If you feel like complicating your life with this, you can use the container ID to fetch CPU, Memory and IO metrics from
`/sys/fs/cgroup/...` but then there is one missing thing: Network usage metrics. As it turns out, the Docker network
interfaces only exist under a network `namespace`, which we didn't want to investigate further as recent versions of
Docker provide a much better and cool alternative for this: the [Docker Remote API].

The [Docker Remote API]  is very easy to use, you just send a `GET` request to `/container/${container-id}/stats` and
you will get back a live stream of events with all the desired metrics for such container. A live __Stream__, by now you
should clearly see where this is going :).


### Preparing your Docker Host ###

You will need to do a little change to your `/etc/default/docker` [file], setting the `DOCKER_OPTS` variable to the
following:

{% code_block text %}
  DOCKER_OPTS='-H tcp://0.0.0.0:4243 -H unix:///var/run/docker.sock'
{% endcode_block %}

That configuration change will make Docker bind to port 4243 and there is where the Remote API will be available. Save
your changes, restart the docker service via `service docker restart` and then we are good to go. When you hit the
Remote API endpoint for a given container you will get one event per second containing the container stats.


### Setting up the Project ###

We need to include in our [Build.scala] some dependencies. It should look like this:

{% code_block scala %}

val dependencies = Seq(
    "io.kamon"    	      %% "kamon-core"           	        % "0.5.1",
    "io.kamon"            %% "kamon-statsd"                     % "0.5.1",
    "com.typesafe.akka"   %% "akka-stream-experimental"         % "0.1.0",
    "com.typesafe.akka"   %% "akka-http-core-experimental"      % "0.1.0",
    "com.typesafe.akka"   %% "akka-http-experimental"           % "0.1.0"
     // ... and so on with all the others dependencies you need.
    )
{% endcode_block %}

### Into the Streams ###

As we know, the [Docker Remote API] returns a live stream  and for consume and transform this stream we will use akka streams.

Two bullets to consider:

*  [Akka Streams/HTTP] provide a higher-level abstraction over Akka’s existing actor model.
*  The Actor model provides an excellent primitive for writing concurrent, scalable software, but it still is a primitive.

Let’s start with a code snippet

{% code_block scala %}
implicit val system = ActorSystem() //(1)
implicit val materializer = ActorMaterializer()
...
val network = Flow[ContainerStats].map(stats => (stats \ "network").extract[NetworkStats]) //(2)
val memory = Flow[ContainerStats].map(stats => (stats \ "memory_stats").extract[MemoryStats])
val cpu = Flow[ContainerStats].map(stats => (stats \ "cpu_stats").extract[CpuStats])
...
{% endcode_block %}

* (1) The `ActorSystem`to use and `ActorMaterializer` which will be responsible for materializing and running the streams.
* (2) A Flow is something with exactly one input and one output. In our code above consume a `ContainerStats` (the complete stream of stats) message and outputs an entity with *Network*, *Memory* or *Cpu* stats.

### Stats Flow ###

{% code_block scala %}
...
def flowWriter(...) = {
  Sink[ContainerStats]() { implicit builder =>
    import FlowGraph.Implicits._

    val stats = builder.add(Broadcast[ContainerStats](3)) //(1)

    stats ~> network ~> networkSink //(2)
    stats ~> memory ~> memorySink
    stats ~> cpu ~> cpuSink
    stats.in
  }
}
...
{% endcode_block %}

* (1) We want to record the stats in differents Kamon recorders, for that reason we have to split the source stream into
* 3 streams which will handle the record to these different recorders. The `Broadcast` simply emits elements from its
* input port to all of its output ports. (2) A `Sink` is something with exactly one input.

The most important part are the last couple of lines in the above code. Here we draw a graph that defines how a message
is handled when it is processed. In this case we first broadcast the docker api response to three parallel streams. In
each stream we next record the metrics with Kamon.

### Stats Sinks ###

A `Sink` is the endpoint for the stream. The data from the stream will eventually find it's way to the `Sink`.

{% code_block scala %}
def chunkConsumer(...) = Sink.foreach[HttpResponse] { response => //(1)
    ...
    val networkSink = Sink.foreach(NetworkMetrics(...))
    val memorySink = Sink.foreach(MemoryMetrics(...))
    val cpuSink = Sink.foreach(CpuMetrics(...))
    ...
}
{% endcode_block %}

* (1) Define a `Sink` that will process the chunked response.

The data flows into the input channel and collects in the `Sink`, in our case we have a `Sink` for each metric type
[NetworkMetrics], [MemoryMetrics] and [CpuMetrics]

### Select the Containers ###

We will select the containers to fetch the metrics.

{% code_block markup %}
...
docker {
  # The Docker Host.
  host = "127.0.0.1"

  # The Docker TCP port.
  port = 2375

  # List of images they need to be monitored.
  # For convenience must provide an alias in order to facilitate the visualization
  # [{"container-id","container-alias"}]
  containers = [{"container-1":"awesome-container-1"},{"container-2":"awesome-container-2"}]
}
...
{% endcode_block %}

Also we can replace the configuration through `System properties` in this way:

{% code_block scala %}
java -Dconfig.file=prod.conf -jar docker-monitor-assembly.jar
{% endcode_block %}

### Visualization and Fun!! ###

By default the application is configured for use with [StatsD] through `Kamon`. For this reason, the only thing what we need is our [Docker-Grafana-Graphite] image that have a built-in Docker dashboard.


To start a container with this image you just need to run the following command:


{% code_block scala %}
docker run -d -p 80:80 -p 8125:8125/udp -p 8126:8126 --name kamon-grafana-dashboard kamon/grafana_graphite
{% endcode_block %}


![Docker Dashboard](/assets/img/docker-dashboard.png)

###Future work ###

Possible future work for this library includes:

* Include `Disk Utilization` metric
* Monitoring of `Docker Events`
* Docker package

### Enjoy! ###

Streams can be applied in a variety of contexts. It’s also not hard to find out what’s been done with [scalaz-streams] or other tooling already available in other languages.

We also encourage you to review the full source code of our [Docker-Monitor].

[all sorts of things]: https://www.youtube.com/watch?v=GsLZz8cZCzc

[Build.scala]:https://github.com/kamon-io/docker-monitor/blob/master/project/Build.scala
[control groups]: https://en.wikipedia.org/wiki/Cgroups
[Docker Remote API]: https://docs.docker.com/reference/api/docker_remote_api_v1.18/#get-container-stats-based-on-resource-usage
[Akka Streams/HTTP]:http://doc.akka.io/docs/akka-stream-and-http-experimental/current/scala.html
[NetworkMetrics]:https://github.com/kamon-io/docker-monitor/blob/master/src/main/scala/kamon/docker/metrics/NetworkMetrics.scala
[MemoryMetrics]:https://github.com/kamon-io/docker-monitor/blob/master/src/main/scala/kamon/docker/metrics/MemoryMetrics.scala
[CpuMetrics]:https://github.com/kamon-io/docker-monitor/blob/master/src/main/scala/kamon/docker/metrics/CpuMetrics.scala
[Docker-Monitor]:https://github.com/kamon-io/docker-monitor
[wherever]:https://docs.docker.com/articles/configuring/#configuring-docker
[scalaz-streams]:https://github.com/scalaz/scalaz-stream
[StatsD]: /backends/statsd/
[Docker-Grafana-Graphite]:https://github.com/kamon-io/docker-grafana-graphite
