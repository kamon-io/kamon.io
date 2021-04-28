---
layout: post
title: 'Monitoring Queues and Resources with Kamon Range Samplers'
date: 2021-04-29
author: the Kamon Team
categories: releases
tags: featured
permalink: /blog/monitoring-queues-and-resources-with-kamon-range-samplers/
summary_image: '/assets/img/posts/kamon-apm-service-map-cover-image.png'
excerpt: >-
    In this article we discuss the origin story of Kamon's Range Samplers: what they are, how they work and how we use 
    them to monitor queues, connection pools and more.
description: >-
    In this article we discuss the origin story of Kamon's Range Samplers: what they are, how they work and how we use 
    them to monitor queues, connection pools and more.
---

In this article we discuss the origin story of Kamon's Range Samplers: what they are, how they work and how we use them
to monitor queues, connection pools and more.

## The Origins

The first use case wanted to tackle was monitoring an Akka Actor's Mailbox, even though later on we realized that the
same technicques we applied to mailboxes could be used for several other use cases. We will stick to the general
idea of monitoring queues for now and visit some of those other use cases later in this post.

Let's start with an imaginary queue with the following behavior over time:

<div class="text-center my-5">
  <img class="img-fluid" src="/assets/img/posts/range-sampler-example-data.png" alt="Kamon APM Pricing Plans">
</div>

The queue size goes up and down as elements flow through it, and every now and then there are periods of inactivity
signaled by those flat lines.

So, how do we monitor this thing?

The basic monitoring instinct says: create a gauge tracking the queue size and update it every time an element is added
or removed from the queue. Implementing this initial idea and recording the gauge's value every 10 seconds yields
something like this:

<div class="text-center my-5">
  <img class="img-fluid" src="/assets/img/posts/range-sampler-example-data-with-gauge.png" alt="Kamon APM Pricing Plans">
</div>

All the activitiy in those 40 seconds got reduced to four numbers: **8, 12, 7 and 10**. That's _some_ data, but far from
enough to describe what really happened in thoe 40 seconds.

We are missing out on two important behaviors on this queue:
  - There was a spike of more than twice the highest value we got from the gauge (see that 25 on the second interval).
  - There was a brief moment when the queue was able to catch up with all the work that was thrown at it (see that 0 on
    the third interval).

This queue might have grown to hunders of elements in a small period of time and we wouldn't have noticed. Let's try to
fix that.



#### Challenge #1: Monitoring Peaks and Lows

Now that we know peaks and lows are interesting data points, we could create two more gauges tracking the lowest and
highest queue size values seen on each time interval.

These two new guages must be updated every time the queue size changes and set a new high or a new low. Also, these
gauges must reset to the current value at the end of every reporting interval, so that they "forget" behavior from
previous time periods and focus only on the highs and lows from the current period.

Drawing those two extra gauges lead to a chart like this:

<div class="text-center my-5">
  <img class="img-fluid" src="/assets/img/posts/range-sampler-example-data-with-peaks-and-lows.png" alt="Kamon APM Pricing Plans">
</div>

The highs and lows can tell us a little bit more about the queue's behavior:
  - We can see that the queue had spikes of up to 25 elements on the second interval.
  - We can also see that the queue was able to catch up with all the work when the lowest went down to zero on the third
    interval.

These two extra gauges give us a better idea of what **range** the queue operates on and that's much better than the
initial example, but we can do better than that.



#### Challenge #2: Improving Granularity

With the solution we created so far it is possible to know for sure that the queue size went all the way up to 25 during
the second time interval, but a question remains: for how long was the queue size at 25 elements? was it most of the
time? was it a quick blip? There is no way to know.

You can get around this issue by capturing and visualizing data more frequently, like every second. That's possible in
several monitoring tools, but not a viable solution.

If only there was a way to record many values in a 10-second interval and report all of them together. Oh wait, that's
what Kamon's histograms do! We could throw all of these measurements into a histogram and get a distribution of values
at the end of each reporting interval, and that's exactly what we did.


#### Welcome the Range Sampler

A Range Sampler is a special kind of metric instrument in Kamon that does three special characteristics:
  - It keeps track of three variables internally: the lowest, highest and current queue size values.
  - Every 200ms it records the lowest, highest and current queue size values in a histogram and reset the internal
    variables.
  - At the end of every 10-second period it generates a histogram with 150 data points: the lowest, highest and current
    values recorded 5 times per second for 10 seconds.

It might not click on you just yet, but wait until you get to the visualization section below.


#### Similar Use Cases

With time we learned that any variable that moves up and down and should ideally return to a baseline (zero most of the
time) can be monitored we a range sampler. For example:
  - A database connection pool borrows and get back connections all the time. You can monitor the number of borrowed
    connections with a range sampler and see how the pool behaves between zero (no connections being used) and the max
    number of connections available (all connections being used). Kamon's HikariCP automatic instrumentation does
    exactly that.
  - We can count the number of requests a HTTP Server is currently handling with a range sampler and get an idea of how
    concurrency can affect the server's perfoance. Kamon does this automatically for Play, Akka HTTP and other supported
    HTTP frameworks.

Now that you understand how range samplers work, matching these principles with other use cases should be piece of cake.
Now let's leave the theory behind and get practical.



## Visualizing and Interpreting the Data

Here is where most people get lost. Reasoning about why a queue size metric is producing a histogram distribution
instead of a single value does not come easy at first. In fact, it sounds crazy! But once you understand the challenges
mentioned above and how data gets collected, then you can start to see how to get interesting insights out of the data.


#### Understanding Operating Range

Instead of focusing on absolute figures like "the queue size is 50", what you really want to do is understand what is
the range in which a queue operates and what's the most common area in which it operates, if any. Heatmaps are excellent
for visualizing exactly that:

<div class="text-center my-5">
  <img class="img-fluid" src="/assets/img/posts/range-sampler-pretty-range-samplers-data.png" alt="Kamon APM Pricing Plans">
</div>

There are several insights on the **behavior** of a queue from looking at this heatmap:
  - It rarely goes below 10 elements. This means that almost always there is some work waiting to be done in the queue.
  - It never goes above 70. Signaling that even though there are spikes in the amount of work to be done, it is never
    getting out of hand.
  - Most of the time the queue size stays around 20 elements. That's where the "heat" is concentrated.

If you ask me, the queue above is a well behaved one. It has spikes from time to time but its size is always within a
consistent range from 10 to 70 elements. Nothing to worry about there.

Let's look at a very different example:

<div class="text-center my-5">
  <img class="img-fluid" src="/assets/img/posts/range-sampler-overflow-sample-data.png" alt="Kamon APM Pricing Plans">
</div>

That's not the kind of heatmap you want to see for a queue. After 11:00 PM the queue size started growing and the lowest
value never made it back to zero. Spikes are not necessarily a bad thing, but when a queue size doesn't make it back to
a baseline value it signals two important facts:
  - There is a lot more work than what the queue can handle, and you should probably add more processing resources or
    improve the processing performance.
  - The queue is on its way to an overflow. It might take a few minutes or hours, but work is piling up in that queue
    and it will eventually lead to out of memory and/or gargage collection issues.

The important bits to keep in mind is that the lowest, highest and most common values are the only signals that can
really tell you something about a queue's behavior and help you draw conclusions on whether you should act or not.


#### Understanding Resource Utilization

Let's say we are using a Range Sampler to monitor borrowed connections in a connection pool and we get a heatmap like
this:

<div class="text-center my-5">
  <img class="img-fluid" src="/assets/img/posts/range-sampler-borrowed-connections-example.png" alt="Kamon APM Pricing Plans">
</div>

Those spikes might require some attention. Are they something we should worry about? 

We can take advantage of range samplers having a fixed number of recordings for each time interval and use percentiles
as a measure of how much time a queue was at certain size or bigger. For example, we can draw the p99 over time instead
of a heatmap:

<div class="text-center my-5">
  <img class="img-fluid" src="/assets/img/posts/range-sampler-borrowed-connections-p99.png" alt="Kamon APM Pricing Plans">
</div>

In most time periods, 99% of the time there are no more than one borrowed connections. This also means that most of the
noise we see on the heatmap must have happened in the reamining 1% of the time, so probably nothing to worry about.

We can go further and analyze all percentiles across the entire time period and get a better idea of the overall
behavior for this connection pool (notice that the horizontal axis represents percentiles, not time):

<div class="text-center my-5">
  <img class="img-fluid" src="/assets/img/posts/range-sampler-borrowed-connections-all-percentiles.png" alt="Kamon APM Pricing Plans">
</div>

Now we know that:
  - **70%** of the time there are 0 connections being used. That's a lot of downtime.
  - **99.6%** of the time we use up to 3 connections.
  - All spikes we see must have happened on the remaining **0.4%** of the time.

Depending on your use case it might be fine to have these very brief spikes in activity. Maybe it is not fine, and you
need to spread the load across time to avoid spikes from happening. That's up to you, though. What matters is that range
samplers collect the data that helps you decide whether to do something or not.


#### Overcoming Monitoring Tool Limitations

Not everybody has access to pretty heatmaps and percentile charts. So what then?

If your monitoring tool can't produce the same visualizations we showed in this post you can always stick to the basics
when working with range sampler data: plot the lowest, highest and maybe the average of all data points.

There are many reasons to stay away from averages, but given the way range samplers work it might be better than no data
at all.


## The Big Takeaways:

Here are the three things you should remember from this post:
  - Range Samplers help you understand the range in which a variable operates, and they are typically used for
    monitoring queues, connection pools and active requests.
  - Heatmaps are the most useful way to visualize a range sampler's data.
  - You can supplement heatmaps with percentile charts to get deeper insights into the range sampler's behavior.



## Ready to Monitor with Range Samplers?

Range samplers are part of [Kamon Telemetry](/telemetry/) and you can use them regardless of where you send data to. The
heatmaps and percentile charts in this post were taken from [Kamon APM](/apm/).

If you want to use the same tools to monitor your queues, actor mailboxes and a lot more, [Sign Up for Kamon APM](#){: .onboarding-start-button} and start monitoring up to 5 services for Free!