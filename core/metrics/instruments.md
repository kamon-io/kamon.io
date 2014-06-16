---
title: Kamon | Core | Documentation
layout: documentation
---

Metric Recording Instruments
============================

When recording metrics you generally want to have three kinds of tools: histograms, counters and gauges. Here is how
Kamon provides you with these tools:


Histograms
----------

When measuring latency it is hard to know upfront what information is important for you, is it the median? the average?
(hell no!), the maximum? the standard deviation? percentiles? what percentiles? 95%? 98%? 99%? 99.99%?. Kamon doesn't
take that decision for you, instead we store **all latency measurements** taken from your application and let you decide
what to do with the data.

Some applications process millions of events per second, meaning that millions of measurements will be taken every
second. You might be asking yourself: is it possible to store millions of measurements efficiently without incurring in
significant memory and CPU overhead? Yes, it is possible, thanks to the [HdrHistogram] developed by [Gil Tene].

The HdrHistogram mixes linear and exponential bucket systems to produce a unique data structure capable of recording
measurements with configurable precision and with fixed memory and cpu costs, regardless of the number of measurements
recorded. Under the hood, the HdrHistogram stores all the data in a single array of longs (in our case, more options are
available) as occurrences of a given value, adjusted with the precision configuration provided when creating the
HdrHistogram. For example, if we were to store a recording of 10 units in a HdrHistogram with a underlying array similar
to the one shown in the diagram bellow, all that's needed is to add one to the value in the ninth bucket.

<img class="img-responsive" src="/assets/img/diagrams/hdr-layout.png">

If you need to store a recording of 19 units, then the highest closest bucket is used, in this case the thirteenth
bucket corresponding to the value 20 is used. The actual number of buckets necessary for a HdrHistogram is calculated
based on two values passed upon creation:

* __number of significant value digits__: Gives you control over the precision with which values are recorded in the
HdrHistogtram. If you create a HdrHistogram tracking values with three significant value digits of precision, then the
required number of buckets is calculated to ensure that all recordings fall into buckets that are no longer than 0.1%
away from the original recording value. Two significant value digits give you 1% precision and one significant value
digit gives you 10% precision. Kamon always uses two significant value digits by default.

* __maximum trackable value__: Gives you control over the range of values that can be covered by the HdrHistogram. Kamon
uses 3.600.000.000.000 (one hour in nanoseconds) by default.

The underlying data structure (the longs array) is allocated only once when the HdrHistogram is created and it is reused
during the lifetime of the monitored entity.

This is just a high level overview, we highly encourage you to read the documentation available in the [HdrHistogram]
site to get a better understanding of how it works.


Counters
--------

Explain the default counter and the minmaxcounter.


Gauges
------

Explain how gauges are backed by histograms.

[HdrHistogram]: https://github.com/HdrHistogram/HdrHistogram
[Gil Tene]: https://twitter.com/giltene
