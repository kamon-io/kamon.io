---
layout: post
title: Quick look at Kamon 0.4.0
date: 2015-03-13
categories: teamblog, posts
---

We have been working on Kamon for almost two years now and during that time we have seen it evolve from "the thing for
AkKA MONitoring" that was only able to produce a couple actor metrics into a robust set of tools being used by
hundreds of engineers around the globe; from guys doing a simple POC with Akka and Spray to big companies with several
production servers being monitored with Kamon.



At the very beginning we tried to use [Dropwizard Metrics] (formerly Codahale Metrics) under the hood; that would allow
us to reuse many reporters already available and present people with something familiar, something they probably already
used and was now being enhanced by Kamon to play nicely with reactive applications. But, as we went through this path we
realized that we wanted something more, something that was tailor made to our goals, something that could match our
ideals of what monitoring tools should look like, something that we could consider a better tool for the job at hand, so
we armed ourselves with courage and decided to create yet another metrics library for our own usage, contained in
kamon-core. That big leap was by far one of the most profitable decisions we made in Kamon's early stages and we
attribute a fair share of Kamon's success to the consequences of that decision, most notably the fact that we adopted
the [HdrHistogram] as our histogram implementation and that allowed us to offer something nobody else was offering:
report millions of precise latency measurements with fixed time and memory requirements, not some statistical
representation (or lie) of what is really going on.

During this couple years we have seen people using Kamon outside the reactive world and achieving successful results,
but typically with several caveats and shortcomings, most of them related to how closely tight our APIs were to some
Akka APIs and to the fact that our Scala APIs were not necessarily friendly to folks in the Java world. Definitely this
is the time to make the next big leap and turn Kamon into something better, something that targets more than just
reactive applications. Don't get me wrong here, we are still using Akka for several purposes in Kamon and definitely
reactive applications are awesome, but we want the whole JVM and that requires some of the changes we are introducing
now and some that will come in the near future.

We are taking the leap this time with a very strong and clear goal in mind: We will make Kamon __the__ monitoring tool
for applications running on the JVM, or we will go crazy trying. It wont be easy, it wont be quick, but we will do our
best to make it a reality.

There has been quite some time since our 0.3.5/0.2.5 release, mostly because we knew that the upcoming release was going
to break compatibility with previous versions and we didn't want to take the leap without feeling comfortable with the
changes to be introduced. The time has come and the release is almost here, so we wanted to share a few details of what
is coming, please read through and share your thoughts and ideas, we are always happy to hear what you have to say :D.


### No More Parallel Tracks Releases ####

Up to our latest release we had version 0.3.5 for Akka 2.3.x and friends and version 0.2.5 for Akka 2.2.x and friends.
This can be a bit confusing, both releases have the exact same features and just differ in dependency versions and some
minor code changes but the 0.2.x series sound outdated and confuse people that are not familiar with the Akka releases
and compatibility notes. Starting with Kamon 0.4.0 there will be a single version number on each release and several
artifacts will be published for the various Akka/Spray/Play versions. The main artifacts will always depend on the
latest versions of Akka, Spray and Play and the artifacts targeting previous dependency versions will include a
`_akka-2.x` suffix on their names, but all artifacts will have the same version number.


### Independant Kamon API ###

Kamon's core components have been developed as [Akka extensions] that we use throughout our instrumentation points and
can easily be accessed by any Akka user. That's nice, until you need to use Kamon outside an ActorSystem or you don't
even have one because you are, let's say, developing a Scalatra application without any reactive libraries. Starting
with Kamon 0.4.0 we have our own isolated ActorSystem that shares nothing with any user-created ActorSystem and all
the APIs exposed by Kamon are now placed at the `kamon.Kamon` companion object. One side effect of this is that now
Kamon needs to be started by calling `Kamon.start(..)`, but that shouldn't be a major problem for everyone.

You will still need know a bit about Akka if you are creating new Kamon reporting modules, but if you are just a Kamon
user you wont need to know anything beyond the APIs provided by Kamon.


### Simplified Metrics Registration ###

In previous Kamon releases there were two kinds of metrics: metric groups for things like actors, routers, traces,
dispatchers and so, and "user metrics" that allowed you could request a single instrument (histogram, counter, min max
counter or gauge) and use it at your own will. Both APIs were separate and apparently dedicated for different purposes
but the reality is that under the hood both were doing pretty much the same thing, so, now you simply use
`Kamon.metrics.histogram(..)` or `Kamon.metrics.entity(..)` and will get back what you asked for.


### Java Friendly API ###

We want all of our users to feel at home when using Kamon and while that might be easy to achieve for our Scala users it
requires a bit more of effort to ensure everything is both Java and Scala friendly, we will do that effort. Starting
with Kamon 0.4.0 we will make sure that all of our APIs can be easily and naturally used from both Scala and Java.


### Professional Documentation ###

Yes, we know you were waiting for this item. Today, the weakest area in Kamon is definitely our documentation and we are
ready to fix that. It is in our plans to dedicate next few days to write complete documentation on all Kamon features,
accompanied by examples written both in Scala and Java. Kamon 0.4.0 will be released as soon as this documentation is
ready and from now on, no feature is complete if it is not properly documented.


### A New Annotations Module ###

What can be easier than just annotating a method to automatically get call count metrics or a histogram of execution
times for that method? Our new annotations module gives you that and more! Make sure you explore the docs and give it
a try.

Probably there is much more to say about this new release but we will leave some content for the actual announcement. We
have just published the snapshot version `0.3.6-04677941e2253717510566cc288eab2166f7379e` of our master branch in our
[snapshots repository], just in case you feel curious and want to give this Kamon version a try. Those artifacts were
built with Scala 2.11, please let us know if you are interested in a build for Scala 2.10 and we will happily assist.
Enjoy!




[Dropwizard Metrics]: https://github.com/dropwizard/metrics
[HdrHistogram]: http://hdrhistogram.org/
[Akka extensions]: http://doc.akka.io/docs/akka/2.3.8/scala/extending-akka.html
[snapshots repository]: http://snapshots.kamon.io/
