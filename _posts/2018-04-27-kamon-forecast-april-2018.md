---
layout: post
title: 'Kamon Forecast - April 2018'
date: 2018-04-27
author: the Kamon Team
categories: posts
tags: featured
redirect_from:
  - /teamblog/2018/04/27/kamon-forecast-april-2018/
---


Dear community, yesterday Diego and Ivan had a call to discuss a bit of what we think the future of Kamon should look
like and we wanted to summarize the ideas and share with the community.



We think these ideas could be better split in two different groups:

#### Work on Kamon 1.x

The main goal on the 1.x series is going to be improving the APIs and quality of Kamon in general. We are very happy with
how things ended up in Kamon 1.0, but there are many details that can and should be improved. The ideas:
  - Standardizing the way HTTP services are instrumented and make sure that all frameworks will provide a consistent
    behavior and metric/span tags. We are basically copy/pasting some behavior between modules and we think we have
    done it enough times to recognize good patterns and come up with a good abstraction for this.
  - Improve the way reporters are added and configured, some functionality like controlling the flush interval for a
    particular reporter could be pulled into core and make all reporting modules simpler.
  - Formalize Context tags. Currently we have the so called "Broadcast String Keys" which essentially are just tags in
    the context. These keys are slightly different to other keys in the sense that we automatically provide codecs for
    them and they are using so commonly that we should probably provide a simpler access to them.
  - Have microbenchmarks for Kamon core and keep track of changes over time.
  - Upgrade more reporting modules. The StatsD module is about to be released and we should have an official JMX module or
    help with a community version that is available already. Also we want to have a Google Stackdriver and Amazon
    Cloudwatch + Amazon X-Ray reporters.
  - Focus on having problem-free default behavior in all modules. This means, for example, having low cardinality operation
    names on all HTTP-related spans and making sure that in the cases where we can't git it by default, all the operation
    names would be rather conservative to ensure low cardinality. Other example would be making sure that Akka actors are
    not wildly captured by default, specially streams-related, annonymous and system actors.

We are sure that we will be breaking the configuration format to achieve some of these goals, but as long as we are not
breaking binary compatibility we will try to apply all these changes as part of the 1.x series.

#### Work on Kamon 2.x

This release is going to be mostly about usability and switching to our own instrumentation agent. In case you didn't
hear about it before, Cristian and Diego have been working a lot in Kanela, our own instrumentation agent that is meant
to replace AspectJ. Several modules are already available to use with Kanela and starting with Kamon 2.0 we will no
longer have AspectJ at all.

The main goal on here is to provide an alternative and simpler way of adding instrumentation to services. Currently there
is a bit of a process to get Kamon working, specially if you are new to the ecosystem. It might not be obvious sometimes
that users need to manually add all libraries including instrumentation and it can be a bit tedious to go through the
process of understanding what needs to be added.

We want to provide some sort of `kamon-bundle` agent that packs Kanela and all available instrumentation for all modules
so that users can simply start their apps with `java -javaagent:kamon-bundle.jar ...` and get metrics, tracing and
context propagation working without the hassle. The only intervention required by users is going to be changing configuration
settings when needed and maybe adding library dependencies for the reporting modules.

Still, all modules will continue to be released as they are now, in case people prefer the current way of doing things.


#### Beyond 2.0

We don't want to invest much time on writing new instrumentation until we move to Kanela because we know we are going in
that direction, writing more AspectJ instrumentation would be a waste of time. Once we get to 2.0 then we can start looking
at expanding the ecosystem. For sooo long we wanted to instrument Cassandra and Kafka, there are so many client libraries
that could be instrumented, there are many frameworks out there that don't have Kamon support just yet. All of this will
come once we get to 2.0!

If you want to give some feedback about the above please stop by our [Gitter Channel][1] and drop us a line :P

[1]: https://gitter.im/kamon-io/Kamon
