---
layout: post
title: Experimental support for Akka Remoting and Cluster is now available!
date: 2014-08-31
categories: teamblog, posts
tags: announcement
---

Dear users, we are really happy to announce that our efforts on bringing support for Akka remoting and cluster to Kamon
have finally arrived to a point where we can share a working feature with you!

Initially our plans were to deliver support for remoting and cluster in two releases from now 0.2.6/0.3.6 but during the
last few months we received requests for this feature from many of our users, so we decided to deliver a experimental
version earlier than what we initially expected and, if possible, ship this at least as experimental in 0.2.5/0.3.5.
Here is a brief summary of what is included in the snapshots detailed bellow:



* __First, it doesn't blow up in your face anymore__, previous to this snapshot users of remoting and cluster might experiment
errors because some parts of Kamon were not `Serializable`, making it imposible for them to use Kamon in such projects.
Now we did what was necessary to ensure that all messages flow without problems, keep reading for more on that.

* __TraceContext propagation for regular messages__: TraceContexts are propagated when sending a message to remote actors
in the same way it works with local actors. This also covers messages sent via ActorSelection and routers with remote
routees.

* __TraceContex propagation for system messages__: If a actor with a remote supervisor fails, the TraceContext is also
propagated through system messages to remote actors, meaning that if one of your actors fails in node A and the supervisor
lives in node B, the TraceContext will still be there. This also covers the create actor scenario: if you deploy a new
remote Actor while a TraceContext is available (e.g. using a actor per request, deploying in a remote node), the
TraceContext available in the local node when deploying the remote actor will be available in the remote node when
creating the actor instance.

* __A Trace can start in one node and finish in another__: As simple as it sounds, start a TraceContext in one node, let
it propagate thorugh messages to remote nodes and finish it over there! Sounds simple, but comes with a couple things
you need to know about:
    * There is little protection from closing a TraceContext more than once. One of the biggest challenges of providing
      this support comes from the fact that we use the TraceContext to measure how long did a request/feature execution
      take to complete, which is an relatively easy task when eveything is local but challenging when working with
      distributed systems. For now we decided not to provide full protection against finishing a remote TraceContext more
      than once because the efforts of keeping a consistent view on the state of all TraceContexts across the cluster
      seems prohibitive performance wise. We might reconsider this in the future based on feedback. If you understand
      how the TraceContext works and how it is propagated you will be just fine, and if in doubt just drop us a line on
      the mailing list.
    * Even while all of our timing measurements are taken and reported in nanoseconds for local TraceContexts, when
      finishing a remote TraceContext the we can't rely on the relative `System.nanoTime` and have to resort back to
      `System.currentTimeMillis`. Please make sure that you keep your clocks synchronized across the cluter.

We would like to receive as much feedback as possible from our users in order to take this in the right direction and
provide a solid support on which you can rely for production monitoring. The snapshots are available in our snapshots
[repo] and the published versions are:

* 0.2.5-4b999c39b6bd09d891de718fad10b795264755c6, compatible with Akka 2.2, Spray 1.2, Play 2.2 and Scala 2.10.x
* 0.3.5-23785a41dc3a0e9651ba87bc9dc255932ea64bd6, compatible with Akka 2.3, Spray 1.3, Play 2.3 and Scala 2.10.x
* 0.3.5-fde062f7e700925e30b60f366ddcd66a04f7c2c5, compatible with Akka 2.3, Spray 1.3, Play 2.3 and Scala 2.11.x

Have fun with it!


[repo]: http://snapshots.kamon.io
