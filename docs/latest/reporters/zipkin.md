---
title: 'Sending Spans to Zipkin with Kamon | Kamon Documentation'
layout: docs
redirect_from:
  - /documentation/1.x/reporters/zipkin/
---

{% include toc.html %}

Sending Spans to Zipkin
=======================

[Zipkin][1] is distributed tracing system. It was originally created at Twitter but currently is run as a completely
independent open source project. Most people would recognize Zipkin as _the_ distributed tracing platform since it has
a really strong and active community and it is integrated with most major libraries and toolkits on the JVM.

The `kamon-zipkin` module translates Kamon's representation of Spans and sends them to Zipkin's v2 API.


## Installation and Startup

Add the `kamon-zipkin` dependency to your build:
  - Group ID: `io.kamon`
  - Package ID: `kamon-zipkin`
  - Scala Versions: 2.10 / 2.11 / 2.12
  - Latest Version: [![kamon-zipkin](https://maven-badges.herokuapp.com/maven-central/io.kamon/kamon-zipkin_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.kamon/kamon-zipkin_2.12)

Adding the dependency to SBT would look like this:

{% code_block scala %}
libraryDependencies += "io.kamon" %% "kamon-zipkin" % "1.0.0"
{% endcode_block scala %}

And then start the reporter:

{% code_block scala %}
import kamon.zipkin.ZipkinReporter

Kamon.addReporter(new ZipkinReporter())
{% endcode_block scala %}

That's it. Go to the Zipkin UI and start browsing your traces.

## Configuration

It couldn't be simpler. All you need to provide is the host and port where Zipkin is listening.

{% code_block typesafeconfig %}
kamon.zipkin {
  host = "localhost"
  port = 9411
}
{% endcode_block scala %}


## Visualization and Fun

These are extracted from our [Monitoring Akka Quickstart][2] recipe, head over there to learn more about how to get
started with Monitoring Akka with Kamon!

Tracer view in Zipkin:

<img class="img-fluid my-4" src="/assets/img/recipes/quickstart-zipkin-trace.png">

Span details:

<img class="img-fluid my-4" src="/assets/img/recipes/quickstart-zipkin-span-detail.png">

[1]: https://zipkin.io/
[2]: /documentation/1.x/recipes/monitoring-akka-quickstart/
