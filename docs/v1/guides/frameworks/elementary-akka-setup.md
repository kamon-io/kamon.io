---
title: 'Elementary Akka Setup Guide | Kamon Documentation'
description: 'Learn how to setup Kamon to collect metrics and distributed traces from an Akka application'
layout: docs
redirect_from:
  - /documentation/1.x/recipes/monitoring-akka-quickstart/
---

{% include toc.html %}

Elementary Akka Setup
==========================

This recipe will guide you through the process of monitoring the [Akka Quickstart][1] example application, one of the
most basic and common examples you can find on the internet when learning how to use Akka. There is a [guide][2]
explaining exactly what it does so it would be nice if you take a look there first to understand what the application
does. We had a small change to the application, though: in order to make things more interesting we added a loop that
randomly selects a greeter and sends a message to it, it looks like this:


{% code_example %}
{%   language scala guides/frameworks/elementary-akka-setup/src/main/scala/com/lightbend/akka/sample/AkkaQuickstart.scala tag:message-loop label:"Message Loop" %}
{% endcode_example %}

Other than that, the application is roughly the same. We will take this application, add Kamon to it, publish metrics to
that can be scraped with Prometheus and send trace data to Zipkin. Let's get started!

## Add the Kamon Libraries

This example is using Akka 2.5, we will need to add `kamon-akka-2.5` to our build as shown here:

{% code_example %}
{%   language scala guides/frameworks/elementary-akka-setup/build.sbt tag:base-dependencies label:"Adding the Dependencies" %}
{% endcode_example %}

## Setting up the Agent

There are several ways to setup the AspectJ Weaver, we even [wrote a guide][3] specifically dedicated to it. Here we
will be [running from SBT][4] and just adding the [`sbt-aspectj-runner`][5] plugin to the build is enough to get it
working.


{% code_example %}
{%   language scala guides/frameworks/elementary-akka-setup/project/plugins.sbt tag:add-aspectj label:"Setting up the AspectJ Weaver" %}
{% endcode_example %}

## Configure the Akka Filters

You will need to explicitly tell Kamon which actors, routers, dispatchers and groups you would like to monitor and trace.
This application will start 4 different actors: `helloGreeter`, `howdyGreeter`, `goodDayGreeter` and `printerActor`. Now
imagine that your monitoring requirements include:

  - Metrics on all actors, except for `howdyGreeter`.
  - Metrics on all dispatchers.
  - Message tracing for all actor messages.

All that can be achieved by simply providing the right filters under `kamon.util.filters` in your `application.conf` file
as shown below:

{% code_example %}
{%   language scala guides/frameworks/elementary-akka-setup/src/main/resources/application.conf tag:filters label:"application.conf" %}
{% endcode_example %}

## Start Reporting your Data

The last step in the process: start reporting your data! You can register as many reporters as you want by using the
`Kamon.addReporter(...)` function:

{% code_example %}
{%   language scala guides/frameworks/elementary-akka-setup/src/main/scala/com/lightbend/akka/sample/AkkaQuickstart.scala tag:start-reporting label:"Start Reporting" %}
{% endcode_example %}

That's it! Now you can simply `sbt run` the application and after a few seconds you will get the Prometheus metrics
exposed on <http://localhost:9095/> and message traces sent to Zipkin! The default configuration publishes the Prometheus
endpoint on port 9095 and assumes that you have a Zipkin instance running locally on port 9411 but you can change these
values under the `kamon.prometheus` and `kamon.zipkin` configuration keys, respectively.


#### Metrics

Once you got it running and got to <http://localhost:9095/>, something like this will show up:

<img class="img-fluid" src="/assets/img/recipes/quickstart-prometheus-metrics.png">

That means that your data is ready to be scraped by Prometheus! Once you configure this target in Prometheus you are
ready to run some queries like this:

<img class="img-fluid" src="/assets/img/recipes/quickstart-prometheus-query.png">

Those are the actor metrics that we configured Kamon to record. Notice how there are no metrics for the `howdyActor`.
Now you can go ahead, explore the available metrics and create your own dashboards!


#### Traces

Assuming that you have a Zipkin instance running locally with the default ports, you can go to <http://localhost:9411>
and start investigating traces for this application. Once you find a trace you are interested in you will see something
like this:

<img class="img-fluid" src="/assets/img/recipes/quickstart-zipkin-trace.png">

There are the `Greet` and `Greeting` messages going between actors, and a third message which is actually a log event
that gets executed asynchronously (you didn't see that coming, right?). Clicking on a span will bring up a details view
where you can see all tags for the span and also you can see the `akka.actor.dequeued` mark (annotation in Zipkin's
terms) which tells you at which point the message was dequeued from mailbox to start processing:

<img class="img-fluid" src="/assets/img/recipes/quickstart-zipkin-span-detail.png">


### You made it!

That's it, you are now collecting metrics and tracing information from an Akka application. Next step: try it on your own
application, have fun and let us know if anything goes wrong!

[1]: https://developer.lightbend.com/start/?group=akka&project=akka-quickstart-scala
[2]: https://developer.lightbend.com/guides/akka-quickstart-scala/
[3]: ../../setting-up-the-agent/
[4]: ../../setting-up-the-agent/#running-from-sbt
[5]: https://github.com/kamon-io/sbt-aspectj-runner
