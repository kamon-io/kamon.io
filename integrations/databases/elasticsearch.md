---
title: kamon | Elasticsearch | Documentation
layout: documentation
---

Elasticsearch Integration
=================


The `kamon-elasticsearch` module brings bytecode instrumentation to trace elasticsearch requests

<p class="alert alert-info">
The <b>kamon-elasticsearch</b> module requires you to start your application using the AspectJ Weaver Agent. Kamon will warn you
at startup if you failed to do so.
</p>

The bytecode instrumentation provided by the `kamon-elasticsearch` module hooks into Elasticsearch's internals to automatically
start and finish segments for requests that are issued within a trace. This translates into you having metrics about how
the requests you are doing are behaving.

### Metrics ###

The following metrics will be recorded:

* __reads__: a histogram that tracks the reads requests latency (Get and Search).
* __writes__: a histogram that tracks the writes requests latency (Index, Update, and Delete).
* __slows__: a simple counter with the number of measured slow requests.
* __errors__: a simple counter with the number of failures.

### Naming Segments ###

By default, the name generator bundled with the `kamon-elasticsearch` module will use the `org.elasticsearch.action.ActionRequest` class name
to assign a name to the automatically generated segment (i.e GetRequest, SearchRequest, IndexRequest, etc). Currently, the only way to override
that name would be to provide your own implementation of `kamon.elasticsearch.ElasticsearchNameGenerator` which is used to assign the segment name

### Slow Requests ###

Requests that take longer to execute than the configured `kamon.elasticsearch.slow-query-threshold` can be processed by user-defined
`kamon.elasticsearch.SlowRequestProcessor`. The default processor logs a warning message

### Error Processor ###
Requests that error can be processed by user-defined `kamon.elasticsearch.ElasticsearchErrorProcessor`. The default processor logs an error message

### Configuration ###

{% code_block typesafeconfig %}
kamon {
  elasticsearch {
    slow-query-threshold = 2 seconds

    # Fully qualified name of the implementation of kamon.elasticsearch.SlowRequestProcessor.
    slow-query-processor = kamon.elasticsearch.DefaultSlowRequestProcessor

    # Fully qualified name of the implementation of kamon.elasticsearch.ElasticsearchErrorProcessor.
    elasticsearch-error-processor = kamon.elasticsearch.DefaultElasticsearchErrorProcessor

    # Fully qualified name of the implementation of kamon.elasticsearch.ElasticsearchNameGenerator that will be used for assigning names to segments.
    name-generator = kamon.elasticsearch.DefaultElasticsearchNameGenerator
  }
}
{% endcode_block %}
