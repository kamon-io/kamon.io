---
title: 'Elasticsearch Client Instrumentation | Kamon Documentation'
description: 'Automatically measure request times and extract metrics from Elasticsearch client communication'
layout: docs
---

{% include toc.html %}

Elasticsearch Client Instrumentation 
================================
Since __2.1.7__

Overview
--------

The Elasticsearch client instrumentation will automatically create spans for requests sent using the 
official Java REST client.

When using the high level client, all Spans created by the instrumentation will have a name 
that corresponds to the request type (e.g. ListTasksRequest will be translated to "elasticsearch.list.tasks").
When using the low level client, all Spans will have a name representing the operation 
(e.g. "elasticsearch.sync" or "elasticsearch.async").

The Spans will also have the following tags:

  - **elasticsearch.http.endpoint**: endpoint (e.g "/_cluster/health") that you're calling.
  - **elasticsearch.http.method**: http request method of the request.
  

<p class="alert alert-info">
The instrumentation only supports the 7.x series of the Elasticsearch Java Rest client. If you are interested in support for the
6.x series, please open an issue on Github.
</p> 

Manual Installation
-------------------

In case you are not using the Kamon Bundle, add the dependency bellow to your build.

{% include dependency-info.html module="kamon-elasticsearch" version=site.data.versions.latest.core %}
{% include instrumentation-agent-notice.html %}
