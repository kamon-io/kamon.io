---
title: 'Logging with Context | Kamon Documentation'
description: 'Learn how to setup Kamon from scratch'
layout: docs
---

{% include toc.html %}

Logging with Context
====================

This guide helps you use the Logback instrumentation to include additional Context information in your log patterns. We
assume that you already have a working Kamon setup before starting here.


Installing the Converters
-------------------------

There are four built-in converters in the Logback module, which should be added to your `logback.xml` file:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="false" debug="false">
  <conversionRule conversionWord="traceID" converterClass="kamon.instrumentation.logback.tools.TraceIDConverter" />
  <conversionRule conversionWord="spanID" converterClass="kamon.instrumentation.logback.tools.SpanIDConverter" />
  <conversionRule conversionWord="contextTag" converterClass="kamon.instrumentation.logback.tools.ContextTagConverter" />
  <conversionRule conversionWord="contextEntry" converterClass="kamon.instrumentation.logback.tools.ContextEntryConverter" />

  <!-- the rest of your config... -->

</configuration>
```

Once they are there, use the conversion words to include pieces of the Context in your log patters as shown bellow.


### Trace and Span Identifiers

The Trace and Span identifiers are the simplest ones to get around, the only requirement is to place the exact
conversion word configured above in the desired position of the log pattern:


```xml
<configuration scan="false" debug="false">
  <!-- all conversion rules from above -->

  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>%d | %traceID %spanID | %m%n</pattern>
    </encoder>
  </appender>

  <root level="DEBUG">
    <appender-ref ref="STDOUT" />
  </root>
</configuration>
```

### Context Tags and Entries

Including Context Tags and Entries is very similar to including the trace and span identifiers, but the conversion words
must be provided with a parameter that specifies the name of the tag or entry that you want to include in the log. For
example, the configuration bellow will write the value of the `user.id` tag and the `someKey` entry in the logs:

```xml
<configuration scan="false" debug="false">
  <!-- all conversion rules from above -->

  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>%d | %contextTag{user.id} %contextEntry{someKey} | %m%n</pattern>
    </encoder>
  </appender>

  <root level="DEBUG">
    <appender-ref ref="STDOUT" />
  </root>
</configuration>
```

That's all you need. Have fun with Kamon!