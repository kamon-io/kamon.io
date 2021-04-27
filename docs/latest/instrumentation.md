---
layout: docs
title: 'Instrumentation Modules | Kamon Documentation'
---

Instrumentation Modules
=======================

All the instrumentation modules are included in the Kamon Bundle so, out of the box, you get instrumentation for
everything below! If you are not using the Kamon Bundle please refer to each module's Manual Installation section.

- **[Akka](./akka/)** instrumentation provides context propagation, metrics and tracing for Akka actors, routers,
  dispatchers, actor systems, cluster sharding and remoting components.
- **[Akka HTTP](./akka-http/)** instrumentation provides context propagation, metrics and tracing for both the Client
  and Server sides of an Akka HTTP application.
- **[Executors](./executors/)** instrumentation provides metrics collection for executor services and wrappers that
  enable context propagation to tasks scheduled on them.
- **[Futures](./futures/)** instrumentation provide automatic context propagation through Scala, Twitter and Scalaz futures.
- The JDBC instrumentation enables **[JDBC Statement Tracing](./jdbc/statement-tracing/)** and automatic metrics collection
  for the **[HikariCP](./jdbc/hikari/)** connection pool.
- **[Logback](./logback/)** provides converters that can be used to put Context information in your log patterns and
  bytecode instrumentation that propagates the context as expected with using Logback's AsyncAppender.
- The System Metrics module provides **[JVM](./system/jvm-metrics/)**, **[Process](./system/process-metrics/)** and
  **[Host](./system/host-metrics/)** metrics like garbage collection time, CPU and memory usage and many more!