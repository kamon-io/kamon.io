---
title: 'Kafka Producer and Consumer Instrumentation | Kamon Documentation'
description: 'Automatically extract metrics, distributed traces and perform context propagation on Akka applications'
layout: docs
---

{% include toc.html %}

Kafka Producer and Consumer Instrumentation 
===========================================
Since __2.1.4__

Overview
--------

The Kafka Producer and Consumer instrumentation creates automatic Spans for all send operations on the Producer 
side, and provides a small API to create Spans for message processing on the consumer side.

In the example trace below, the `producer.send` Span is automatically created on the producer side, and the 
`consumer.process` Span is manually created on the consumer side, using the `runWithConsumerSpan` helper function.

<img class="img-fluid rounded" src="/assets/img/kafka-producer-consumer-spans.png">


Producer Spans
--------------

A `producer.send` Span is automatically created for every producer record sent to Kafka, without any manual 
intervention. All producer Spans are tagged with:
  * __component__: With the value `kafka.producer`.
  * __kafka.client-id__: With the producer client id assigned by Kafka.
  * __kafka.key__: With the record key, if any.
  * __kafka.topic__: With the name of the destination topic for the record.
  * __kafka.partition__: With the partition number assigned to the record.

In addition to creating the producer Span, the instrumentation will also include a binary representation of the current
context on the `kctx` (short for Kamon Context) header. The `kctx` header is required for context propagation and
distributed tracing to work, so, don't drop it!


Consumer Spans
--------------

It is necessary to make a small code change on the consumer side if you want to trace how long it takes to process
incoming records and get access to the context propagated from the consumer side. The helper functions for creating
consumer Spans are part of the `KafkaInstrumentation` class. 

All consumer spans created by the helper functions are tagged with:
  * __component__: With the value `kafka.consumer`.
  * __kafka.client-id__: With the consumer client id assigned by Kafka.
  * __kafka.group-id__: With the consumer group id, if any.
  * __kafka.key__: With the record key, if any.
  * __kafka.topic__: With the name of the destination topic for the record.
  * __kafka.partition__: With the partition number assigned to the record.
  * __kafka.offset__: With the consumed record's offset.
  * __kafka.poll-time__: With the time it took for the poll operation that fetched the record to complete.
  * __kafka.timestamp__: With the timestamp associated with the consumer record.
  * __kafka.timestamp-type__: With the type of timestamp associated with the consumer record.


### Continuing or Starting Traces

Depending on your use case, you might want the consumer Spans to be part of the same trace as the producer Spans, or to
start a new trace of their own and get a link to the producer trace. You can switch between these two behaviors using
the `continue-trace-on-consumer` setting:


{% code_block typesafeconfig %}
kamon.instrumentation.kafka {
  client.tracing {
    continue-trace-on-consumer = yes
  }
}
{% endcode_block %}

As rule of thumb, when your producer and consumer applications are part of a real time processing pipeline, you will 
want to keep the producer and consumer Spans in the same trace. Otherwise, letting the consumer create its own trace 
with a link to the producer trace is the way to go.


### Creating Consumer Spans

__Using the runWithConsumerSpan Function__

You can wrap your record processing logic with the `KafkaInstrumentation.runWithConsumerSpan` function to create a Span
for each processed consumer record:

{% code_block scala %}
records.iterator().forEachRemaining(record => runWithConsumerSpan(record) {
  // Your record processing logic goes here...  
})
{% endcode_block %}

By default, the consumer Span is named `consumer.process` and is finished as soon as the provided code block finishes
executing. There are different versions of the `runWithConsumerSpan` function that let you change the operation name and
decide whether the Span should be finished after executing the processing logic or not.


__Using the consumerSpan Function__

If the `runWithConsumerSpan` function doesn't match your execution model, you can use the `consumerSpan` function to 
create a new consumer Span with all the tags and info described above, but you will be in charge of managing that Span
programmatically. 

You probably will want to use the `Kamon.runWithSpan` function (or similar) when creating spans like this.


__Accessing the Incoming Context__

When you are only interested in getting access to the incoming context, you can use the `extractContext` function or
import the `KafkaInstrumentation.Syntax` implicit class:


{% code_block scala %}
// Extracting the incoming Context from a consumer record:
val incomingContext = KafkaInstrumentation.extractContext(record)

// Or, importing KafkaInstrumentation.Syntax
val incomingContext = record.context
{% endcode_block %}


Manual Installation
-------------------

In case you are not using the Kamon Bundle, add the dependency below to your build.

{% include dependency-info.html module="kamon-kafka" version=site.data.versions.latest.core %}
{% include instrumentation-agent-notice.html %}
