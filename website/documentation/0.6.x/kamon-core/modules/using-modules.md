---
title: Kamon | Core | Documentation
layout: documentation-0.6.x
---

Using Modules
=============

The components included in the `kamon-core` module are meant to be the basic building blocks on which all other Kamon
modules will build functionality. You choose, by adding the complimentary module dependencies, which functionality you
want to include in your application and which ones you want to leave out.

Even while there is no rule about what should be included into a single module, we tend to ensure that each module
includes only one of the following items:

* __Support for a Code Library__: By means of bytecode instrumentation and library-specific entity recorders that adapt
to the concepts exposed by the library and measure it's performance characteristics. Even while several libraries may
work in tandem to achieve a goal in some use cases, it is usually advisable to create modules for each library on its
own, to ensure that other users remain free to include only the bits that they consider necessary. Our Akka, Scala, Spray,
Play! and Annotation modules fall into this category.
* __Support for a Metrics Backend__: A metrics backend module usually will subscribe to certain set of metrics and traces
and push that information to a backend in a backend-specific protocol. Usually some additional configuration is required
like providing the target host IP address, port and even credentials. Our StatsD and Datadog modules are examples of this
category.



Starting Modules
----------------

Modules will be automatically picked up and started just by being in the classpath when your application runs. Some
modules may require additional configuration to work properly, but all available modules will be started once you call
`Kamon.start(...)` in your application startup process.

If you have a module in your classpath but you don't want it to be auto-started, you can use the
`kamon.modules.$module-name.auto-start` key to control if the module should be started or not, as shown bellow:

{% code_example %}
{%   language typesafeconfig kamon-core-examples/src/main/resources/application.conf tag:disabling-modules label:"application.conf" %}
{% endcode_example %}

Please note that this only works for modules that actually start some sort of process or actors, like the
`kamon-system-metrics` module and all reporting modules. For other modules such as the `kamon-spray` or `kamon-play`
modules setting the auto-start to false wont prevent the instrumentation from kicking in.



AspectJ Weaver Sanity Check
---------------------------

Since some Kamon modules require you to start your application using the AspectJ Weaver agent, we included a small
sanity check that will ensure that if Kamon detects a module that requires AspectJ, the application was properly started
with the AspectJ Weaver in place. If you fail to do so, Kamon will log a large and noticeable error message as the
example shown bellow, listing the modules requiring the weaver to work properly.

{% code_example %}
{%   language text kamon-core-examples/src/main/resources/weaver-missing-output.txt tag:weaver-missing-example-message label:"Log Error Message" %}
{% endcode_example %}

As explained in the message, if for some reason it is fine for you to have the modules in your classpath and not
starting your application with the AspectJ Weaver agent, you can use the `kamon.show-aspectj-missing-warning` config
setting to disable the message.
