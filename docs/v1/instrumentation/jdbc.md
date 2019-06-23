---
title: 'JDBC Operations Tracing | Kamon Documentation'
layout: docs
redirect_from:
  - /documentation/0.6.x/kamon-jdbc/overview/
---

{% include toc.html %}

JDBC
====

The bytecode instrumentation provided by the `kamon-jdbc` module hooks into the JDBC API to automatically
start and finish segments for requests that are issued within a trace. This translates into you having metrics about how
the requests you are doing are behaving.

## Dependency Installation
{% include dependency-info.html module="kamon-jdbc" version="1.0.2" %}
{% include instrumentation-agent-notice.html %}



### Metrics ###

The following metrics will be recorded:

* __reads__: a histogram that tracks the reads requests latency (SELECT statement).
* __writes__: a histogram that tracks the writes requests latency (INSERT, UPDATE, and DELETE statements).
* __slows__: a simple counter with the number of measured slow requests.
* __errors__: a simple counter with the number of failures.

### Naming Segments ###

By default, the name generator bundled with the `kamon-jdbc` module will use the statement name as the name to the automatically generated segment (i.e SELECT, INSERT, etc). Currently, the only way to override that name would be to provide your own implementation of `kamon.jdbc.JdbcNameGenerator` which is used to assign the segment name

### Slow Requests ###

Requests that take longer to execute than the configured `kamon.jdbc.slow-query-threshold` can be processed by user-defined
`kamon.jdbc.DefaultSlowQueryProcessor`. The default processor logs a warning message

### Error Processor ###
Requests that error can be processed by user-defined `kamon.jdbc.SqlErrorProcessor`. The default processor logs an error message

### Configuration ###

{% code_block typesafeconfig %}
kamon {
  jdbc {
    slow-query-threshold = 2 seconds

    # Fully qualified name of the implementation of kamon.jdbc.SlowQueryProcessor.
    slow-query-processor = kamon.jdbc.DefaultSlowQueryProcessor

    # Fully qualified name of the implementation of kamon.jdbc.SqlErrorProcessor.
    sql-error-processor = kamon.jdbc.DefaultSqlErrorProcessor

    # Fully qualified name of the implementation of kamon.jdbc.JdbcNameGenerator that will be used for assigning names to segments.
    name-generator = kamon.jdbc.DefaultJdbcNameGenerator
  }
}
{% endcode_block %}