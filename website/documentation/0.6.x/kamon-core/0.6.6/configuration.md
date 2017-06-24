---
title: Kamon | Configuration
tree_title: Configuring Kamon
tree_index: 1
layout: module-documentation
---

Configuring Kamon
=================

Kamon uses the [Typesafe Config] library to manage all that can be configured inside Kamon, as many projects in the JVM
world are doing already. This library brings clear and predictable practices for defining application and library
settings that can be easily understood and extended. If you are already familiar with this library, you can just jump
into the next section.

The Typesafe Config's standard behavior is very straightforward and we will resume it for you: When you create a
configuration object using `ConfigFactory.load()`, it will read the available configurations from a few predefined
places and have them ready for you, so that whenever you want to read a configuration setting they will be looked up
in the following order:

- JVM System Properties.
- All `application.conf` files in your classpath.
- All `application.json` files in your classpath.
- All `application.properties` files in your classpath.
- All `reference.conf` files in your classpath.

Normally, each library that uses the Typesafe Config library for configuration purposes will ship with a
`reference.conf` file containing sensible default values for all the settings that can be configured and the user will
provide "overrides" to those values primarily via your own `application.conf` file or via system properties. Probably an
image of the common lookup priorities will settle quickly in your mind:

<img class="img-fluid" src="/assets/img/diagrams/typesafe-config-overview.png">

These configuration files are usually written using the [HOCON] notation. All Kamon modules that need configuration ship
with a reference.conf file where default settings are contained and you are free to override any of those values by
supplying your owns in your `application.conf` file. Whenever we refer to a configuration setting throughout the docs,
we are certainly referring to a setting available in a module's `reference.conf` file that you can override at your
wish.

This resume is just a very high level overview of what the Typesafe Config library can do for you, please refer to their
[documentation] to learn how to slice and dice with configuration settings and make the best possible profit out of it.



Providing a Custom Config Object
--------------------------------

If you need to prevent Kamon from using the standard behavior of calling `ConfigFactory.load()`, there is an additional
overload of the `Kamon.start(..)` method that accepts a configuration object to be used instead. This is how you would
provide such a custom configuration object:

{% code_example %}
{%   language scala kamon-core-examples/src/main/scala/kamon/examples/scala/CustomConfiguration.scala tag:custom-configuration %}
{%   language java kamon-core-examples/src/main/java/kamon/examples/java/CustomConfiguration.java tag:custom-configuration %}
{% endcode_example %}

Please note that when you provide your own `Config` object, Kamon will use that and only that object as a configuration
settings source. You must ensure that the `Config` object you are supplying contains both your custom settings and all
the reference settings available in the classpath. Most APIs in the typesafe-config's `ConfigFactory` class already take
care of that but if that isn't the case for you then simply adding a `.withFallback(ConfigFactory.defaultReference())`
to your configuration object you can ensure that everything Kamon needs is in place.


Special Considerations for Akka and Spray
-----------------------------------------

Kamon core components use Akka internally for several purposes and some modules make use of Spray as well, but all of
this happens in a internal actor system created by Kamon for it's own usage. If your application is using Akka or Spray,
you probably don't want all the settings applied to your application's actor system to be applied to Kamon's actor
system as well, that's why we have a special treatment of configuration settings for the `akka.*` and `spray.*`
patterns.

When Kamon's internal actor system is created we strip away all the `akka.*` and `spray.*` configuration settings
available (either from standard behavior or from a provided Config object) and we add all the configuration settings
available under the `kamon.internal-config` to the resulting Config object before supplying it to the internal actor
system. By doing this, any customizations you do to Akka or Spray for your application wont affect Kamon's behavior, but
still you can provide custom settings for Kamon's internal actor system if you want to.

For example, if you wish to change the log level and the parallelism factor for the default dispatcher in Kamon's internal
actor system, you would need to provide something like this in your configuration file:

{% code_example %}
{%   language typesafeconfig kamon-core-examples/src/main/resources/application.conf tag:internal-configuration label:"application.conf" %}
{% endcode_example %}

[Typesafe Config]: https://github.com/typesafehub/config
[documentation]: https://github.com/typesafehub/config
[HOCON]: https://github.com/typesafehub/config/blob/master/HOCON.md
