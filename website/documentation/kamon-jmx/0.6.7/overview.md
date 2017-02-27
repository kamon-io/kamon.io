---
title: Kamon | Documentation | Reporting Data to JMX MBeans
tree_title: Exposing Metrics via JMX
layout: module-documentation
---

Reporting Metrics to JMX MBeans
==============================

[JMX] is a Java technology that supplies tools for managing and monitoring applications, system objects, devices and service-oriented networks. Those resources are represented by objects called MBeans (for Managed Bean). 


Installation
-------------

Add the `kamon-jmx` dependency to your project and ensure that it is in your classpath at runtime, that’s it. Kamon’s module loader will detect that the JMX module is in the classpath and automatically start it.


Configuration
-------------

The JMX module subscribes itself to the entities included in the `kamon.jmx.subscriptions` key. By default, the following subscriptions are included:

{% code_block typesafeconfig %}
kamon.jmx {
  subscriptions {
    histogram       = [ "**" ]
    min-max-counter = [ "**" ]
    gauge           = [ "**" ]
    counter         = [ "**" ]
    trace           = [ "**" ]
    trace-segment   = [ "**" ]
    akka-actor      = [ "**" ]
    akka-dispatcher = [ "**" ]
    akka-router     = [ "**" ]
    system-metric   = [ "**" ]
    http-server     = [ "**" ]
  }
}
{% endcode_block %}

If you are interested in reporting additional entities to JMX please ensure that you include the categories and name patterns accordingly.

Integration Notes
-----------------

For all single instrument entities (those tracking counters, histograms, gaugues and min-max-counters) the generated metric key will follow the `kamon.instrument-type.entity-name` pattern. Additionaly all tags supplied when creating the instrument will also be reported.

* __Connection__:  For connect to your application you need [add some JVM parameters] for start JMX agent and install a JMX client (for example [VisualVM])
* __Histogram percentile__:  You may calculate any percentile on hystogram values.

<img class="img-fluid" src="/assets/img/jmx-module-percentile.png">

Visualization and Fun
---------------------

Here is a very simple example of a JMX values in VisualVM MBeans plugin with metrics reported by Kamon:

<img class="img-fluid" src="/assets/img/jmx-module-overiew.png">


[JMX]: https://en.wikipedia.org/wiki/Java_Management_Extensions
[VisualVM]: http://visualvm.java.net/download.html
[add some JVM params]: https://theholyjava.wordpress.com/2012/09/21/visualvm-monitoring-remote-jvm-over-ssh-jmx-or-not/


Importing JMX Metrics into Kamon
================================

Installation
-------------

The kamon-jmx module now provides both moving metrics data into JMX and exporting metrics out of JMX.  So just like above, add the `kamon-jmx` dependency to your project and ensure that it is in your classpath at runtime, that’s it. Kamon’s module loader will detect that the JMX module is in the classpath and automatically start it.

Configuration
-------------

To use the JMX to Kamon functionality, you must do three things.  First, you add a subscription named kamon-mxbeans to the kamon.jmx.subscriptions key.  Here is an example:

{% code_block typesafeconfig %}
kamon.jmx {
  subscriptions {
    histogram       = [ "**" ]
    min-max-counter = [ "**" ]
    gauge           = [ "**" ]
    counter         = [ "**" ]
    trace           = [ "**" ]
    trace-segment   = [ "**" ]
    system-metric   = [ "**" ]
    http-server     = [ "**" ]
    kamon-mxbeans   = [ "**" ]
  }
}
{% endcode_block %}

Note: only the kamon-mxbeans line is important here.  You can add or remove other subscriptions here without effecting the behavior of the JMX metrics exporting.

---------------
Second, add the kamon-mxbeans module to the kamon.modules part of the configuration.  Here is an example:

{% code_block typesafeconfig %}
kamon.modules {
  kamon-mxbeans {
    auto-start = yes
    requires-aspectj = no
    extension-class = "kamon.jmx.extension.JMXMetricImporter"
  }
}
{% endcode_block %}


-----------------
Finally, add a kamon.kamon-mxbeans configuration object to your config.  Here is an example:

{% code_block typesafeconfig %}
kamon.kamon-mxbeans {
  mbeans = [
    { "name": "example-mbean", "jmxQuery": "example:type=myBean,name=*",
      "attributes": [
        { "name": "Counter1", "type": "counter" },
        { "name": "Counter2", "type": "counter" }
      ]
    }
  ],
  identify-delay-interval-ms = 1000,
  identify-interval-ms = 1000,
  value-check-interval-ms = 1000
}
{% endcode_block %}

The jmxQuery allows this Kamon module to find the mbean(s) that should be exported to Kamon.  The name is the Kamon name of this new metric.  The attributes are metrics to export to Kamon and the type of metric to export.  The supported types are counter, guage, min_max_counter and histogram.  The identify-delay-interval-ms is the number of milliseconds to wait before querying JMX the first time.  The identify-interval-ms is how often to query JMX for new mxbeans.  And value-check-interval-ms is the number of milliseconds between polling of known mxbeans for new metric values.


Full Example
------------

{% code_block typesafeconfig %}
kamon {
  jmx {
    subscriptions {
      histogram       = [ "**" ]
      min-max-counter = [ "**" ]
      gauge           = [ "**" ]
      counter         = [ "**" ]
      trace           = [ "**" ]
      trace-segment   = [ "**" ]
      akka-actor      = [ "**" ]
      akka-dispatcher = [ "**" ]
      akka-router     = [ "**" ]
      system-metric   = [ "**" ]
      http-server     = [ "**" ]
      # Here is the addition of the JMX exporting functionality to the subscriptions
      kamon-mxbeans   = [ "**" ]
    }
  }

  # adding the JMX to Kamon module
  modules {
    kamon-mxbeans {
      auto-start = yes
      requires-aspectj = no
      extension-class = "kamon.jmx.extension.JMXMetricImporter"
    }
  }

  # Configuring what JMX metrics to export to Kamon
  kamon-mxbeans {
    mbeans = [
      { "name": "my-mbean", "jmxQuery": "test:type=exampleBean,name=*",
        "attributes": [
      { "name": "Value1", "type": "counter" },
      { "name": "Value2", "type": "counter" } ] }
    ],
    identify-delay-interval-ms = 1000,
    identify-interval-ms = 1000,
    value-check-interval-ms = 1000
  }
}
{% endcode_block %}

