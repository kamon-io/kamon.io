---
title: 'Sending Spans to Jaeger with Kamon | Kamon Documentation'
layout: docs
redirect_from:
  - /documentation/1.x/reporters/jaeger/
---

{% include toc.html %}

Sending Spans to Jaeger
=======================

[Jaeger][1] is a distributed tracing system. It was originally created at Uber.

The `kamon-jaeger` module translates Kamon's representation of Spans and sends them to Jaeger's API.


## Installation and Startup

Add the `kamon-jaeger` dependency to your build:
  - Group ID: `io.kamon`
  - Package ID: `kamon-jaeger`
  - Scala Versions: 2.10 / 2.11 / 2.12
  - Latest Version: [![kamon-jaeger](https://maven-badges.herokuapp.com/maven-central/io.kamon/kamon-jaeger_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.kamon/kamon-jaeger_2.12)

Adding the dependency to SBT would look like this:

{% code_block scala %}
libraryDependencies += "io.kamon" %% "kamon-jaeger" % "1.0.1"
{% endcode_block scala %}

And then start the reporter:

{% code_block scala %}
import kamon.jaeger.JaegerReporter

Kamon.addReporter(new JaegerReporter())
{% endcode_block scala %}

That's it. Go to the Jaeger UI and start browsing your traces.


## Configuration

It couldn't be simpler. All you need to provide is the host and port where Zipkin is listening.

{% code_block typesafeconfig %}
kamon.jaeger {
  host = "localhost"
  port = 14268
  tls = false
}
{% endcode_block scala %}


## Visualization and Fun

These screenshots were taken by running the [Monitoring Akka Quickstart][2] recipe with the Jaeger reporter, head over
there to learn more about how to get started with Monitoring Akka with Kamon!

Trace view in Jaeger:

<img class="img-fluid my-4" src="/assets/img/jaeger-trace-search.png">

Trace Details:

<img class="img-fluid my-4" src="/assets/img/jaeger-trace-detail.png">

[1]: https://github.com/jaegertracing/jaeger
[2]: /documentation/1.x/recipes/monitoring-akka-quickstart/
