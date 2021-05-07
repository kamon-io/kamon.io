---
title: 'Kamon APM | Trace Search | Kamon Documentation'
description: 'Quickly find distributed traces that are causing problems with the Kamon APM Trace Search'
layout: docs
---

{% include toc.html %}

Trace Search
============

{% lightbox /assets/img/pages/apm/trace-search.png %}
Trace Search
{% endlightbox %}

With the Trace Search, you can quickly and easily filter through your ingested traces, breaking them down by service, operation and [span kind], as well as duration or presence of error.

You need to select one or more services for which to search through traces. As you select services, the available operations and span kinds will be populated in the multiple select
inputs that follow it. By default, the resulting spans will not be constrained by duration. You may do so by changing the values in latency (duration) inputs. This filter is optional,
but if either the minimum or the maximum is entered, both will have to be. Finally, if you are only interested in spans that have had errors occur during their recording, they can be
selected by toggling the error spans only switch.

Every time the filters are updated, a [span list] will be displayed, and will be populated with spans that match the filters specified (if any).

{% lightbox /assets/img/pages/apm/find-trace-error.png %}
Find Trace - Wrong Trace ID
{% endlightbox %}

Additionally, if you already know the trace ID of a trace you are searching for, you can enter it in the trace ID search in the top right corner. If the trace ID is valid,
a drawer will be opened with [trace details] for that trace. Otherwise, the input will briefly transition into an error state to indicate that no trace could be found for that
ID. Note that you can only find and view spans that are included in your [retention period].

[span list]: ../trace-list/
[trace details]: ../trace-details/
[span kind]: ../../../core/tracing/#spans
[retention period]: ../../general/environments/
