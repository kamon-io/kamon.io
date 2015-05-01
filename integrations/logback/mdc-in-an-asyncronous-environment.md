---
title: Kamon | Core | Documentation
layout: documentation
---

MDC in an Asynchronous Environment
==================================

### What is MDC? ###

__Mapped Diagnostic Context__ is essentially a map maintained by the logging framework where the application code provides key-value pairs which can then be inserted by the logging framework in log messages. [MDC] data can also be highly helpful in filtering messages, store useful data to diagnose errors or triggering certain actions.

This context can be used to store values that can be displayed in every Logging statement. For example:	


{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/MDCSupport.scala tag:what-is-mdc %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/MDCSupport.java tag:what-is-mdc %}
{% endcode_example %}


`logback.xml` must be configured to display the __X-ApplicationId__ value as show below:

{% code_example %}
{%   language markup kamon-core-examples/src/main/resources/logback.xml tag:logback-what-is-mdc-config label:"logback.xml" %}
{% endcode_example %}

In the Console, the [MDC] value for __X-ApplicationId__ is now printed out:

{% code_example %}
{%   language text kamon-core-examples/src/main/scala/kamon/examples/scala/MDCSupport.scala tag:what-is-mdc-output label:"Log Output" %}
{% endcode_example %}


### Limitation of the default implementation of the MDC ###

To record the values in the [MDC], Logback uses a `ThreadLocal` variable in order to manages contextual information on a per thread basis. A child thread automatically inherits a copy of the mapped diagnostic context of its parent.

This strategy works when one thread is used for one request, like in servlet container before the [3.0] specification. In a asynchronous server, the processing of a request could be composed of several function calls, and each call can be run on a different thread for that reason the implementation of the [MDC] with a `ThreadLocal` cannot work with this non-blocking asynchronous threading model.

### Known Alternatives ###

Depending of the utilized framework there is some known workaround:

* a custom [Akka Dispatcher]. This solution needs minimal change to a current application.
* a custom [ExecutionContext] that propagates the [MDC] from the caller’s thread to the callee’s one. 
* a custom [ExecutorService] in order to propagate the [MDC] from the caller’s thread to the callee’s one.


### The Kamon Way ###

bla bla bla

{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/MDCSupport.scala tag:kamon-way-mdc %}
{% endcode_example %}


{% code_example %}
{%   language text kamon-core-examples/src/main/scala/kamon/examples/scala/MDCSupport.scala tag:kamon-way-mdc-output label:"Log Output" %}
{% endcode_example %}


[MDC]: http://logback.qos.ch/manual/mdc.html
[3.0]: https://www.jcp.org/en/jsr/detail?id=315
[Akka Dispatcher]: http://doc.akka.io/docs/akka/snapshot/scala/dispatchers.html
[ExecutionContext]: http://www.scala-lang.org/files/archive/nightly/docs/library/index.html#scala.concurrent.ExecutionContext
[ExecutorService]: https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorService.html