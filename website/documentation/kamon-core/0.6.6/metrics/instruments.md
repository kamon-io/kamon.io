---
title: Kamon | Core | Documentation
tree_title: Instruments
layout: module-documentation
---

Metric Recording Instruments
============================

Kamon provides four metric recording instruments that are used internally in our integrations as well as offered for you
to use with the user metrics module. Even while their names might sound familiar and self-describing, their semantics
differ from what similarly named instruments in other metrics library offer, please read their descriptions to make sure
that you select the right tool for the job when using these instruments in your apps.


Counters
--------

The Counter is the simplest one, it just counts and resets to zero upon each flush. Some other libraries allow counters
to go up and down but we only allow them to go up, they are ideal for counting errors or occurrences of specifics events
in your app but they fall short for things like mailbox sizes, see the MinMaxCounter section bellow to understand why.


Histograms
----------

When measuring value distributions (usually latency) it is hard to know upfront what information is important for you,
is it the median? the average? (hell no!), the maximum? the standard deviation? percentiles? what percentiles? 95%? 98%?
99%? 99.99%?. Kamon doesn't take that decision for you, instead we store **all measurements** taken from your
application and let you decide what to do with the data.

Some applications process millions of events per second, meaning that millions of measurements will be taken every
second. You might be asking yourself: is it possible to store millions of measurements efficiently without incurring in
significant memory and CPU overhead? Yes, it is possible, thanks to the [HdrHistogram] developed by [Gil Tene], and it
is backing our Histogram implementation.

The HdrHistogram mixes linear and exponential bucket systems to produce a unique data structure capable of recording
measurements with configurable precision and with fixed memory and cpu costs, regardless of the number of measurements
recorded. Under the hood, the HdrHistogram stores all the data in a single array of longs (in our case, more options are
available) as occurrences of a given value, adjusted with the precision configuration provided when creating the
HdrHistogram. For example, if we were to store a recording of 10 units in a HdrHistogram with an underlying array similar
to the one shown in the diagram bellow, all that's needed is to add one to the value in the ninth bucket.

<img class="img-fluid" src="/assets/img/diagrams/hdr-layout.png">

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


MinMaxCounters
--------------

Now we get to one created by us, the MinMaxCounter. When monitoring queues, like we do for actor mailboxes, just having
a number going up and down (like a traditional counter) that is collected every X time or recording the queue size every
X time (like a traditional gauge) isn't enough. Many things can happen to a queue between flushes, you could get a
million messages in a queue and process them all before the next flush, recording a value of 0 as the queue size if
using a traditional gauge. 

When monitoring a queue size it is not just about knowing where it is at a given moment because that number is probably
incorrect right after reading it, but knowing where it was is of great help. The MinMaxCounter internally has 3
variables, one tracking the current value, one tracking the minimum and one tracking the maximum. These three values are
read and stored in a histogram every 100 milliseconds by default and upon each flush all the measurements of where the
queue size was, containing the lowest and highest values are reported. Knowing the boundaries between which a queue size
usually moves is a incredibly valuable information to detect trends that might lead to failures as well as when trying
to move from unbounded to bounded queues. After each flush, the max and min values are reset to the current value,
meaning that if no changes occur then the three of them will have the same value in the next flush.


Gauges
------

Our Gauge is a mix between the Histogram and the MinMaxCounter, taking measurements of a given value every 100
milliseconds by default and storing the observed values in a Histogram. This can be seen as a traditional gauge that
happens to report many values upon every flush rather than a single one (the latest). Since it uses a Histogram to store
all recordings, if you flush every 10 seconds and configure the gauge to refresh every 100 milliseconds, you will get
100 measurements for the gaugue on every flush.

[HdrHistogram]: https://github.com/HdrHistogram/HdrHistogram
[Gil Tene]: https://twitter.com/giltene
