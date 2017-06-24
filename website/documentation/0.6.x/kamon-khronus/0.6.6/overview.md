---
title: Kamon | Khronus | Documentation
tree_title: Sending Metrics to Khronus
layout: module-documentation
---

Reporting Metrics to Khronus
============================

[Khronus] is an open source, distributed and reactive time series database designed to store, retrieve, analyze and process a large amount of custom metrics.

Installation
------------

Add the `kamon-khronus` dependency to your project and ensure that it is in your classpath at runtime, that's it. Kamon's module loader will detect the module and automatically start it.

Configuration
-------------

Set `kamon.khronus.host` to point to your Khronus instance using a `host:port` value like `11.22.33.44:1173`. Most likely, you will also want to set `kamon.khronus.app-name` to the application name you wish to report metric as.

These are the configuration keys available and their default values (taken from the `reference.conf` file supplied with this module):

{% code_block typesafeconfig %}
kamon {
  khronus {
    host = "127.0.0.1:1173"
    app-name = "kamon-khronus"
    # Time interval in milliseconds to flush the buffer and send the accumulated metrics.
    # It must be less than the smallest time window configured in Khronus.
    interval = 3000
    # Maximum number of measures to hold in memory within intervals.
    # Past this threshold, metrics will be discarded.
    max-measures = 500000
  }
}
{% endcode_block %}

Reported Metrics
----------------

This reporter will automatically subscribe itself to the following categories:

* __counter__.
* __histogram__.
* __gauge__.
* __trace__.
* __executor-service__.

The other important categories like __akka-*__, __system-metrics__, and __http-server__ were not included in this initial version due to time constraints. Also, there are no facilities for filtering categories. Please feel free to jump in!

[Khronus]: https://github.com/Searchlight/khronus
