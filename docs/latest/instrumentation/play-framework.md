---
title: 'Play Framework Instrumentation | Kamon Documentation'
description: 'Automatically extract metrics, distributed traces and perform context propagation on Play Framework applications'
layout: docs
---

{% include toc.html %}

Play Framework Instrumentation
==============================

The Play Framework instrumentation automatically enables Context propagation and distributed tracing for both incoming and
outgoing requests processed by Play, as well as lower level HTTP server metrics. The gist of the features provided
by the instrumentation is:
  1. Context will be automatically propagated using HTTP headers.
  2. Client and Server HTTP request Spans will be automatically created and propagated, meaning that besides tracing you
     will also get HTTP endpoint metrics via the `span.processing-time` metric.
  3. Lower level HTTP server metrics will be collected for the HTTP server side.

The instrumentation will work out of the box with both the default Akka HTTP server or the Netty HTTP server, as well
as instrument HTTP client requests made with the WSClient for Play Framework 2.6 and 2.7.

{% alert warning %}
Just as a reminder, you will need to use the SBT Kanela Runner for Play Framework
applications if you plan to use Kamon while running on development mode. Please refer to the <a href="/get-started/">Get
Started</a> guide if you need help with the setup.
{% endalert %}

Bellow, you will find a more detailed descriptions of each feature and relevant configuration settings in case you want
to customize the behavior, but you don't need to learn any of it start using the instrumentation! Just start to your
application with the instrumentation agent and you are good to go.


Context Propagation
-------------------

The client and server instrumentations build on top of lower level building blocks provided by Kamon, such as the HTTP
Propagation Channels and the common HTTP Client and Server instrumentation, you don't need to do anything special to get
them to work other than starting your application with the instrumentation agent. In the following sections we will put
together all bits and pieces that can be configured for the Akka HTTP instrumentation in case you want to know what is
happening under the hood and how to modify the instrumentation behavior.


### Controlling Propagation

The instrumentation will automatically read/write Kamon's Context from/tp HTTP headers in all HTTP requests and set that
Context as current while requests are being processed, enabling higher level features like distributed tracing. If you
want to change the propagation channel or completely disable Context propagation you can use the `propagation` settings
bellow:

```hcl
kamon.instrumentation.play.http {
  server {
    propagation {
      enabled = yes
      channel = default
    }
  }

  client {
    propagation {
      enabled = yes
      channel = default
    }
  }
}

```

{% include warning.html message="Please note that without Context propagation Kamon will not be able to properly join
distributed traces!" %}


### Custom Tag Mappings

By default, Kamon will use the `context-tags` HTTP header to transport all Context tags, but if you want to configure a
specific header to transport one context tag, you can do so by providing a mapping on the HTTP propagation channel:

```hcl
kamon.propagation.http.default.tags {
  mappings {
    requestID = "X-Request-ID"
  }
}
```

With the settings above, Kamon will automatically try to turn the contents of the `X-Request-ID` HTTP header on incoming
requests into the `requestID` Context tag and also write the value of the `requestID` Context tag (if present) on the
`X-Request-ID` HTTP header for outgoing requests.



Request Tracing
---------------

HTTP Server and Client requests processed by the application will be automatically traced, which in turn means that
metrics can (and will) be recorded for the HTTP operations. You can control whether tracing is enabled or not under the
`tracking` section bellow, as well as controlling whether Span Metrics will be recorded when tracing is enabled:

```hcl
kamon.instrumentation.play.http {
  server {
    tracing {
      enabled = yes
      span-metrics = on
    }
  }

  client {
    tracing {
      enabled = yes
      span-metrics = on
    }
  }
}

```

### Customizing the Span Tags

Kamon will automatically add the HTTP URL, method and status code (from the response) as tags to all Spans generated via
instrumentation and you can configure how these tags will be added on the client and server sides of the instrumentation
by adding one of the following modes to each setting:

- **off** will prevent Kamon from adding the tag to Spans.
- **span** will add the tag as a Span tag.
- **metric** will add the tag as a Span metric tag. Please note that you should not use high cardinality values as metric
  tags, that's one of the reasons the URL is only set as a span tag.

Also, it is possible to make Kamon copy tags from the current Context into the HTTP operation Spans by using the
`from-context` section. In the example bellow we are showing the default settings for the Akka HTTP instrumentation and
additionally, we are instructing Kamon to copy the `requestID` tag as a Span tag for both the client and server side
instrumentation.

```hcl
kamon.instrumentation.play.http {
  server.tracing.tags {
    url = span
    method = metric
    status-code = metric

    from-context {
      requestID = span
    }
  }

  client.tracing.tags {
    url = span
    method = metric
    status-code = metric

    from-context {
      requestID = span
    }
  }
}

```


HTTP Server Metrics
-------------------

As a lower level part of the instrumentation, Kamon will track the performance of the HTTP Server. You can control
whether HTTP Server metrics will be recorded or not by using the `` setting:

```hcl
kamon.instrumentation.play.http {
  server.metrics {
    enabled = yes
  }
}

```

This feature is enabled by default and will collect the following metrics:

{%  include metric-detail.md name="http.server.requests" %}
{%  include metric-detail.md name="http.server.request.active" %}
{%  include metric-detail.md name="http.server.request.size" %}
{%  include metric-detail.md name="http.server.response.size" %}
{%  include metric-detail.md name="http.server.connection.lifetime" %}
{%  include metric-detail.md name="http.server.connection.usage" %}
{%  include metric-detail.md name="http.server.connection.open" %}


Manual Installation
-------------------

In case you are not using the Kamon Bundle, add the dependency below to your build.

{% include dependency-info.html module="kamon-play" version=site.data.versions.latest.play %}
{% include instrumentation-agent-notice.html %}


[get-started]: /get-started/
