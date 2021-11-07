---
layout: post
archived: true
title: Bugfix Release - Kamon 0.3.3/0.2.3 is out!
date: 2014-07-29
author: the Kamon Team
categories: archive
tags: announcement
redirect_from:
  - /teamblog/2014/07/29/bugfix-release-kamon-0.3.3-and-0.2.3-released/
---

Dear users, the Kamon 0.3.2/0.2.2 release that went out last week turned out to have two problems:

  * A NullPointerException was thrown when an actor is stopped (see [issue 69]).
  * User metrics were not being reported to StatsD and Datadog.
  * The `kamon-system-metrics` module artifacts didn't include all the Sigar related files.



As usual, the compatibility information for this release:

   * 0.3.3 is compatible with Akka 2.3, Spray 1.3, Play 2.3 and Scala 2.10.x/2.11.x
   * 0.2.3 is compatible with Akka 2.2, Spray 1.2, Play 2.2 and Scala 2.10.x

Have fun with Kamon and let us know if you have any problems!

[issue 69]: https://github.com/kamon-io/Kamon/issues/69
