---
layout: post
title: Bugfix Release - Kamon 0.3.4/0.2.4 is out!
date: 2014-08-05
categories: teamblog
tags: announcement
redirect_from:
  - /teamblog/2014/08/05/bugfix-release-kamon-0.3.4-and-0.2.4-released/
---

Dear users, the 0.3.3/0.2.3 release that fixed a few issues last week still contain a couple problems reported by some
of you, the most important one being a IndexOutOfBoundsException being thrown in some cases when recording values with a
MinMaxCounter. We finally solved the issue and pushed a new release to make sure a stable version is available. The list
of changes for this release is:



* kamon-core
   * Fix IndexOutOfBoundsException being thrown when recording values from a MinMaxCounter (see [issue 71]).
   * Use the inline variant of TraceRecorder.withTraceContext.
   * Avoid having any other copies of the AspectJ weaver around in runtime by marking the weaver dependency as "provided".

* kamon-spray
   * Use the inline variant of TraceRecorder.withTraceContext.

* kamon-play
   * Use the inline variant of TraceRecorder.withTraceContext.

* kamon-log-reporter
   * Provide the ability to report system metrics.

* kamon-system-metrics (Experimental)
   * Minor changes in the banner displayed when starting the system metrics module.

As usual, the compatibility information for this release:

   * 0.3.4 is compatible with Akka 2.3, Spray 1.3, Play 2.3 and Scala 2.10.x/2.11.x
   * 0.2.4 is compatible with Akka 2.2, Spray 1.2, Play 2.2 and Scala 2.10.x

Have fun with Kamon and let us know if you have any problems!

[issue 71]: https://github.com/kamon-io/Kamon/issues/69
