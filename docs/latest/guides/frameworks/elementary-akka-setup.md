---
title: 'Elementary Akka HTTP Setup Guide | Kamon Documentation'
description: 'Learn how to setup Kamon to collect metrics and distributed traces from an Akka HTTP application'
layout: docs
redirect_from:
  - /documentation/1.x/recipes/monitoring-akka-quickstart/
---

{% include toc.html %}

Elementary Akka HTTP Setup
==========================

Welcome! Here, we'll get you started with monitoring Akka HTTP applications. 
We'll use the official [Akka HTTP Quickstart][1] example, one of the most basic and simple examples available.
There is a [guide][2] explaining exactly what the app does, so it would be nice if you take a look there first!

We will take this application, add Kamon to it, publish metrics that Prometheus can scrape and send trace data to Zipkin. 
Let's get started!

## Add the Kamon Libraries

First, we will add the Bundle dependency, as well as the
Prometheus and Zipkin reporters to this project. Add this to your `build.sbt`:

{% code_example %}
{%   language scala guides/frameworks/elementary-akka-setup/build.sbt tag:base-dependencies label:"Adding the Dependencies" %}
{% endcode_example %}

## Initializing Kamon

Next, we ensure that Kamon will be initialized as the very first thing during the application startup process. 
Edit the main method in `QuickstartApp.scala`:

{% code_example %}
{%   language scala guides/frameworks/elementary-akka-setup/src/main/scala/com/lightbend/akka/sample/AkkaQuickstart.scala tag:init-kamon label:"Initializing Kamon" %}
{% endcode_example %}

## Configure the sampler

Kamon is careful about not tracing _everything_ that comes through your app, so it doesn't impact the runtime of your program.
For this tutorial though, we want to capture all traces, since we'll be sending a small amount of requests.

You can to that by adding the following line to `application.conf`:

{% code_example %}
{%   language hcl guides/frameworks/elementary-akka-setup/src/main/resources/application.conf tag:sample-always label:"application.conf" %}
{% endcode_example %}


{% alert warning %}
Make sure to remove that line before deploying to production!
Tracing _everything_ has a high performance penalty!
{% endalert %}


That's it! Now you can simply `sbt run` the application and after a few seconds you will get the Prometheus metrics
exposed on <http://localhost:9095/metrics> and message traces sent to Zipkin!

The default configuration publishes the Prometheus endpoint on port 9095 and assumes that you have a Zipkin instance
running locally on port 9411 but you can change these values under the `kamon.prometheus` and `kamon.zipkin`
configuration paths, respectively.


#### Metrics

Once you got it running and got to <http://localhost:9095/metrics/>, something like this will show up:

<img class="img-fluid" src="/assets/img/recipes/quickstart-prometheus-metrics.png">

That means that your data is ready to be scraped by Prometheus! Once you configure this target in Prometheus you are
ready to run some queries like this:

<img class="img-fluid" src="/assets/img/recipes/quickstart-prometheus-query.png">

See how easy it is to start collecting useful data about your app? 
Let's move on to tracing!


#### Traces

First, let's give Kamon something to trace. Not much we can show if no one's using the app.
Follow the instructions under the [Exercising the example][3] portion of the guide to give the app some traffic.

Second, assuming that you have a Zipkin instance running locally with the default ports, you can go to <http://localhost:9411>
and start investigating traces for this application. Once you find a trace you are interested in you will see something
like this:

<img class="img-fluid" src="/assets/img/recipes/quickstart-zipkin-trace.png">


### You made it!

That's it, you are now collecting metrics and tracing information from an Akka HTTP application. Next step: try it on your own
application, have fun and let us know if anything goes wrong!

[1]: https://developer.lightbend.com/start/?group=akka&project=akka-http-quickstart-scala
[2]: https://developer.lightbend.com/guides/akka-http-quickstart-scala/
[3]: https://developer.lightbend.com/guides/akka-http-quickstart-scala/#exercising-the-example
[get-started]: /get-started/
