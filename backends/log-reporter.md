---
title: Kamon | LogReporter | Documentation
layout: documentation
---

Reporting Metrics to LogReporter
================================
<hr>

LogReporter is a backend that simply periodically dumps metrics to [Akka Logging].

It is not very useful for production, but a convenient way to test Kamon without having to install a full-fledged backend.


Installation
------------

To use the LogReporter module just add the `kamon-log-reporter` dependency to your project and start your application using the
Aspectj Weaver agent. Please refer to our [get started] page for more info on how to add dependencies to
your project and starting your application with the AspectJ Weaver.


Configuration
-------------

First, include the Kamon(LogReporter) extension under the `akka.extensions` key of your configuration files as shown here:

```scala
akka {
  extensions = ["kamon.logreporter.LogReporter"]
}
```

[Akka Logging]: http://doc.akka.io/docs/akka/snapshot/scala/logging.html
[get started]: /introduction/get-started/
