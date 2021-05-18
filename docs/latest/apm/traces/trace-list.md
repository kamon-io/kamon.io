---
title: 'Kamon APM | Trace List | Kamon Documentation'
description: 'Find spans and traces that match your criteria using the Span List widget and identify issues in your system with ease'
layout: docs
---

{% include toc.html %}

Span List
==========

{% lightbox /assets/img/apm/span-list.png %}
Span List
{% endlightbox %}

Span lists are one of the core components in Kamon APM. You will often find them integrated into pages such as the [JDBC] dashboard, the [trace search], or the [analyze view].
They will list spans that match certain criteria, as determined by the context they appear in, but all will share a few common features. Every span list will show the following:

| Field          | Type       | Description                          |
|:---------------|:-----------|:-------------------------------------|
| Span Result    | Icon       | Completed successfully or with error |
| Operation Name | String     | `operation` as per the [span tag]    |
| Time           | Datetime   | When the span concluded              |
| Duration       | Duration   | Duration of span and marker showing position in overall distribution of span durations in the list |

The span might also include one of two markers next to the duration, that indicate presence of errors:


{% lightbox /assets/img/apm/span-error-markers.png %}
Span Error Markers
{% endlightbox %}

The red **X** indicator shows the the span itself has an error (or, rather, an error occurred during the operation). The yellow exclamation mark indicates that while the operation
itself did not experience an error, once of the operations it called (its descendant spans) did.

Every trace list is *time sensitive*, and will only show spans within the time period selected in the [time picker]. If you are browsing the application in live mode, the list will
auto-update every minute. You can force it to update manually by pressing the refresh button in the top right corner of the span list.

{% lightbox /assets/img/apm/span-list-errors.png %}
Span List - Errors Only
{% endlightbox %}

The span list also includes an error toggle. If turned on, only spans with errors (an error occurred during the execution of the operation) will be shown in the list.

[JDBC]: ../../services/jdbc/
[trace search]: ../trace-search/
[analyze view]: ../../deep-dive/analyze/#trace-list
[span tag]: ../overview/#span-tags
[time picker]: ../../general/time-picker/
