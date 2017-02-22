---
title: Kamon | Core | Documentation
tree_title: Trace Metrics
layout: documentation
---

Trace Metrics
=============

Once your application is tracking traces and segments across components you have what is needed to enjoy the goodness of
trace and segment metrics. There is nothing particularly special in traces and segment metrics, both are simple entities
as described in the core metrics section, but there are a couple considerations that might be of your interest when
working with these metrics:

* The metric entity categories are `trace` and `trace-segment`, no surprise there.
* The entity recorders for traces and segments have a single metric inside: a histogram named `elapsed-time`, which
uses the `Time.Nanoseconds` unit of measurement.
* Even while the segment's elapsed time is measured from the moment they are created until the moment they are finished, the
segment metrics are not recorded until the correspondent trace has finished. Once the trace is finished, all segments
still open will get their metrics recorded immediately after finishing. The reason for delaying the recording is that a
trace can be renamed at any point while it is still open, so we can only be sure of which entity recorder to use once the
trace has finished and we know for sure that the trace name can't be changed again.
* Although the `trace` and `trace-segment` recorders are completely separate entities, all `trace-segment` entity
recorders will always have a tag named `trace` that contains the name of the trace for which the segment was recorded.
If you are using certain segment from different traces you will see that their metrics are "scoped" to a trace using the
tag.

Beyond the considerations above, trace and segment metrics are like any other metric in Kamon and as such, you can
obviously subscribe to them using the metrics subscription protocol or report them to external metric backends as all of
our reporting modules currently do.
