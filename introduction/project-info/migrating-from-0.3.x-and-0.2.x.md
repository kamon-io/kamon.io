---
title: Kamon | Documentation | Migrating from 0.3.x/0.2.x
layout: documentation
---

Migrating from 0.3.x/0.2.x
==========================

Our 0.4.0 is finally here! That's cool, but sadly you wont be able to simply bump the dependency version and start using
the new version as some changes are required for your application to work properly now. Please read through and apply
the changes that make sense for your application and please let us know if you had any other issue not listed in here.


### Watch your Dependencies ###

All the Scala and Akka instrumentation that was initially provided with the `kamon-core` module has been moved to the
`kamon-scala` and `kamon-akka` modules respectively, make sure that you add those modules if you are expecting to use
the features available there.

If you are still using Akka 2.2.x please make sure that you use the artifacts that have the `_akka-2.2` suffix.


### Start Kamon ###

You will need to start Kamon by calling `Kamon.start(...)` during your application startup and before any component
makes use of Kamon API's, otherwise you will get some "Kamon has already been started." error messages.


### Update the Kamon API Usages ###

In previous releases all Kamon components were exposed as Akka extensions that you could simply grab from anywhere you
had an actor system in implicit scope. That was useful for Akka users, but painful for everyone else who don't even know
what an actor system is. Now, all the user-facing APIs are exposed via the `Kamon.metrics` and `Kamon.tracer` members.
If you were using something like `Kamon(UserMetrics).registerCounter(...)` to register a counter, you must change
your code to use `Kamon.metrics.counter(...)` instead.


### Remove all Kamon Akka Extensions ###

Now the Kamon extensions are automatically initialized in a Kamon-internal actor system that is not accesible to users
and they should only be started in that actor system. Please remove all the Kamon-specific extensions that you might
have listed before in the `akka.extensions` configuration key.


### Upgrade your Metrics Settings ###

The `kamon.metrics` section has been renamed to `kamon.metric`. Also the entity filters have been changed from the previous
full list of entity filters to a simpler and merge-friendly format. Filters that looked like this:

{% code_block typesafeconfig %}
kamon.metrics {
  filters = [
    {
      actor {
        includes = [ "user/simple-service-actor" ]
        excludes = [ "system/*", "user/IO-*" ]
      }
    },
    {
      trace {
        includes = [ "*" ]
        excludes = []
      }
    },
    {
      dispatcher {
        includes = [ "akka.actor.default-dispatcher" ]
        excludes = []
      }
    }
  ]
}
{% endcode_block%}

Should now look like this:

{% code_block typesafeconfig %}
kamon.metrics {
  filters {
    akka-actor {
      includes = [ "system-name/user/simple-service-actor" ]
      excludes = [ "system-name/system/**", "system-name/user/IO-**" ]
    }
    trace {
      includes = [ "**" ]
      excludes = []
    }
    akka-dispatcher {
      includes = [ "system-name/akka.actor.default-dispatcher" ]
      excludes = []
    }
  }
}
{% endcode_block%}


### Prefixes for Akka Entities ###

Now all Akka-specific entities will have the `akka-` prefix in their category name. Also, now all Akka entities will have
their name prefixed with the name of the actor system to which they belong.
