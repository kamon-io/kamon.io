---
layout: post
title: Welcome Akka 2.4, Goodbye Akka 2.2.
date: 2015-06-14
categories: teamblog, posts
---

In case you didn't notice yet, we are using Akka internally to handle certain aspects of Kamon's infrastructure, like
using actor messages to support the metrics and trace subscription protocols, relying on Akka IO for UDP communication
in the StatsD and Datadog modules and using Spray for our New Relic backend, which in turn builds on top of Akka.



Using Akka and Spray has been cool in many aspects, including the fact that by relying on them we bring a very small
number of dependencies into our user's classpath, but also comes with a little limitation: the Akka, Spray, Play! and
Kamon's Akka version need to be synchronized for all to work properly. It means, we need to publish artifacts targeting
specific Akka, Spray and Play! versions, as well as having special handling on certain [configuration settings] to keep
Kamon's actor system separated from our user's actor system, if any.

That doesn't seem to be hard, but the code base for supporting Akka 2.2 every day differs a bit more from what is
necessary to support Akka 2.3 and maintaining it turns out to be a little pain we don't want to suffer anymore. So, once
Akka 2.4 is released we will switch our main code base to Akka 2.4 and kill support for Akka 2.2. Of course, despite the
small number of downloads for the `_akka-2.2` artifacts, we will continue to publish artifacts for Akka 2.2 until Akka
2.4 is available, so you still have a few months to plan your upgrade :).

One more thing to note is that even while Akka 2.3 and Akka 2.4 are advised to be binary compatible, you can't use a
Kamon version built for Akka 2.3 with Akka 2.4, so even while the code base will apparently remain the same, the main
artifacts will target Akka 2.4 and there will be a secondary set of artifacts with the `_akka-2.3` suffix targeting Akka
2.3.

Thanks to some [experiments] done by [Kailuo Wang], we now know that the current code base is compatible with Akka
2.4-M1 so we decided to publish a Kamon snapshot as well targeting that version, you can now find Kamon
`0.4.1-35bb09838d1b0a2a1e36cd68c2db158b728a2981` in our [snapshots repository]. Just remember to change the module names
to include the `_akka-2.4` suffix, like in `kamon-core_akka-2.4` and so on.

We hope you have fun with this snapshot and of course, that you report back if there is any inconvenient while using it!


[configuration settings]: /introduction/configuration/
[experiments]: https://github.com/kamon-io/Kamon/issues/215
[Kailuo Wang]: https://github.com/kailuowang
[snapshots repository]: http://snapshots.kamon.io/
