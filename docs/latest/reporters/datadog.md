---
title: 'Sending Metrics to Datadog with Kamon | Kamon Documentation'
description: 'How to send metrics and traces from Kamon to Datadog'
layout: docs
redirect_from:
  - /documentation/1.x/reporters/datadog/
  - /documentation/0.6.x/kamon-datadog/overview/
---

{% include toc.html %}

Datadog Reporter
================

Datadog is a monitoring service for IT, Operations and Development teams who write and run applications at scale.

## Installation

{% include dependency-info.html module="kamon-datadog" version=site.data.versions.latest.datadog %}

Once the reporter is on your classpath it will be automatically picked up by Kamon. This dependency ships with three
modules:
  - `datadog-agent` (enabled: true) Sends metrics data to the Datadog Agent via UDP.
  - `datadog-trace-agent` (enabled: true) Sends spans data to the Datadog Trace Agent via HTTP.
  - `datadog-api` (enabled: false) Sends metrics data directly to the Datadog public API.

If you want to control which modules are started by default just change the `enabled` setting for the appropriate module.

{% code_block hcl %}
kamon {
  modules {
    datadog-agent {
      enabled = true
    }

    datadog-trace-agent {
      enabled = true
    }

    datadog-api {
      enabled = false
    }
  }
}
{% endcode_block %}


## Agent Reporter

By default, the Agent reporter assumes that you have an instance of the Datadog Agent running in localhost and listening
on port 8125. You can configure specific details for the agent reporter with these configuration settings:

{% code_block hcl %}
kamon {
  datadog {

    #
    # Settings relevant to the DatadogAgentReporter
    #
    agent {

      # Hostname and port in which your dogstatsd is running (if not using
      # the API). Remember that Datadog packets are sent using UDP and
      # setting unreachable hosts and/or not open ports wont be warned
      # by the Kamon, your data wont go anywhere.
      hostname = "127.0.0.1"
      port = 8125

      # Max packet size for UDP metrics data sent to Datadog.
      max-packet-size = 1024 bytes
      measurement-formatter = "default"
      packetbuffer = "default"
    }
  }
}

{% endcode_block %}


## Trace Agent Reporter

The Trace Agent reporter is also started by default and assumes that it can reach the agent locally. These are the
settings relevant to the Trace Agent reporter:

{% code_block hcl %}
kamon {
  datadog {

    #
    # Settings relevant to the DatadogSpanReporter
    #
    trace {

      # Default to trace agent URL
      # See: (https://docs.datadoghq.com/api/?lang=python#tracing)
      api-url = "http://localhost:8126/v0.4/traces"

      # FQCN of the "kamon.datadog.KamonDataDogTranslator" implementation
      # that will convert Kamon Spans into Datadog Spans, or "defult" to
      # use the built-in translator.
      translator = "default"

      # HTTP client timeout settings:
      #   - connect-timeout: how long to wait for an HTTP connection
      #     to establish before failing the request.
      #   - read-timeout: how long to wait for a read IO operation
      #     to complete before failing the request.
      #   - write-timeout: how long to wait for a write IO operation
      #     to complete before failing the request.
      #
      connect-timeout = 5 seconds
      read-timeout = 5 seconds
      write-timeout = 5 seconds
    }
  }
}

{% endcode_block %}


## API Reporter

When using the API reporter you must configure your API key using the `kamon.datadog.http.api-key` configuration setting.
Since Kamon has access to the entire distribution of values for a given period, the API reporter can directly post the
data that would otherwise be summarized and sent by the Datadog Agent. Gauges andAll histogram-backed metrics will be reported as
follows:
  - metric.avg
  - metric.count
  - metric.median
  - metric.95percentile
  - metric.max
  - metric.min

You can refer to the [Datadog documentation](https://docs.datadoghq.com/developers/metrics/#histograms) for more details.
Here are the settings relevant to the API reporter:

{% code_block hcl %}
kamon {
  datadog {

    #
    # Settings relevant to the DatadogAPIReporter
    #
    api {

      # API endpoint to which metrics time series data will be posted.
      api-url = "https://app.datadoghq.com/api/v1/series"

      # Datadog API key to use to send metrics to Datadog directly
      # over HTTPS. The API key will be combined with the API URL
      # to get the complete endpoint use for posting time series
      # to Datadog.
      api-key = ""

      # HTTP client timeout settings:
      #   - connect-timeout: how long to wait for an HTTP connection
      #     to establish before failing the request.
      #   - read-timeout: how long to wait for a read IO operation
      #     to complete before failing the request.
      #   - write-timeout: how long to wait for a write IO operation
      #     to complete before failing the request.
      #
      connect-timeout = 5 seconds
      read-timeout = 5 seconds
      write-timeout = 5 seconds
    }
  }
}

{% endcode_block %}


## Metric Units

Kamon keeps all timing measurements in nanoseconds and information measurements in bytes. In order to scale those to other
units before sending to Datadog, set the `time-unit` and `information-unit` config keys to desired units. Supported units
are:

```typesafeconfig
n  - nanoseconds
µs - microseconds
ms - milliseconds
s  - seconds

b  - bytes
kb - kilobytes
mb - megabytes
gb - gigabytes
```

For example,

```hcl
kamon.datadog.time-units = "ms"
```

Will scale all timing measurements to milliseconds right before sending to Datadog.


Integration Notes
-----------------

* Contrary to other Datadog client implementations, we don't flush the metrics data as soon as the measurements are
  taken but instead, all metrics data is buffered by the `kamon-datadog` module and flushed periodically using the
  configured `kamon.metric.tick-interval`.
* It is advisable to experiment with the `kamon.metric.tick-interval` and `kamon.datadog.agent.max-packet-size` settings to
  find the right balance between network bandwidth utilisation and granularity on your metrics data.


Teasers
-------

Creating a dashboard in the Datadog user interface is really simple, all metric names will match the Kamon metric names
with the additional "qualifier" suffix. Here is a very simple example of a dashboard created with metrics reported by Kamon:

<img class="img-fluid my-4" src="/assets/img/datadog-dashboard.png">

[Datadog]: http://www.datadoghq.com/
[get started]: /docs/latest/guides/getting-started/
