---
title: Kamon | Documentation | Reporting Data to JMX MBeans
layout: documentation
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

<img class="img-responsive" src="/assets/img/jmx-module-percentile.png">

Visualization and Fun
---------------------

Here is a very simple example of a JMX values in VisualVM MBeans plugin with metrics reported by Kamon:

<img class="img-responsive" src="/assets/img/jmx-module-overiew.png">


[JMX]: https://en.wikipedia.org/wiki/Java_Management_Extensions
[VisualVM]: http://visualvm.java.net/download.html
[add some JVM params]: https://theholyjava.wordpress.com/2012/09/21/visualvm-monitoring-remote-jvm-over-ssh-jmx-or-not/