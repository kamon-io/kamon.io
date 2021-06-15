---
layout: docs
noindex: true
title: 'Instrumentation Modules | Kamon Documentation'
---

Instrumentation Modules
=======================

The available modules are:

- **[Akka](./akka/)** provides metrics and tracing information from Akka actors, routers, dispatchers, actor systems,
  cluster sharding and remoting components.
- **[Executors](./executors/)** provides metrics collection for executor services and wrappers that enable context
  propagation to tasks scheduled on them.
- **[Futures](./futures/)** provides automatic context propagation through Scala, Twitter and Scalaz futures.
- **[JDBC](./jdbc/)** instruments JDBC drivers and the Hikari connection pool for tracing JDBC operations and extracting
  metrics on the connection pool.
- **[Logback](./logback/)** provides converters that can be used to put Context information in your log patterns and
  bytecode instrumentation that propagates the context as expected with using Logback's AsyncAppender.
- **[System Metrics](./system-metrics/)** provides host metrics like CPU, memory, network and file system usage.