---
layout: docs
title: 'Logging with Context | Kamon Documentation'
redirect_from:
  - /integrations/logback/mdc-in-an-asyncronous-environment/
---

{% include toc.html %}

Logging with Context
====================

The `kamon-logback` module provides converters that can be used to put Context information in your log patterns and
bytecode instrumentation that propagates the context as expected with using Logback's `AsyncAppender`.

## Dependency Installation
{% include dependency-info.html module="kamon-logback" version="1.0.5" %}
{% include instrumentation-agent-notice.html %}






Logging TraceID
---------------

Inserting a `conversionRule` allows you to incorporate the trace ID for a request into your [Logback Layout][logback layout].
Here is a simple example `logback.xml` configuration that does this:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="false" debug="false">
  <conversionRule conversionWord="traceID" converterClass="kamon.logback.LogbackTraceIDConverter" />

  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>%d{yyyy-MM-dd HH:mm:ss} | %-5level | %traceID | %c{0} -> %m%n</pattern>
    </encoder>
  </appender>

  <root level="DEBUG">
    <appender-ref ref="STDOUT" />
  </root>
</configuration>
```


Propagating TraceID to AsyncAppender
------------------------------------

If you choose to use [`AsyncAppender`](https://logback.qos.ch/manual/appenders.html#AsyncAppender), your trace ID will
automatically be propagated to the thread where the log is actually published. No configuration needed. The same applies
for the span ID. You can use them in the logback pattern like this:

{% code_block xml %}
 <pattern>%d{yyyy-MM-dd HH:mm:ss} | %-5level | %X{kamonTraceID} | %X{kamonSpanID} | %c{0} -> %m%n</pattern>
{% endcode_block %}

You can also add custom values to MDC. To do this, simply add the key value in the library configuration:

{% code_block typesafeconfig %}
kamon.logback.mdc-traced-local-keys = [ userID ].
kamon.logback.mdc-traced-broadcast-keys = [ requestID ]
{% endcode_block %}

Then, add the value to the kamon context:

{% code_block scala %}
Context
  .create(Span.ContextKey, span)
  .withKey(Key.broadcastString("userID"), Some("user-1"))
  .withKey(Key.local[Option[String]("requestID", None), Some("request-id") {
  // loggers called in this context will have access to the userID, requestID
}
{% endcode_block %}

Note: While in Kamon you can have one local key and one broadcast key with the same name, in MDC this is not possible.
In this case only the broadcast key will be stored in MDC (will be present in the logs)


[logback layout]: https://logback.qos.ch/manual/layouts.html