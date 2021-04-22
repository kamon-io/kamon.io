---
title: 'Kamon APM | Charts | Kamon Documentation'
layout: docs
---

{% include toc.html %}

Kamon APM Charts
================

Kamon APM allows you to work with several types of charts, each meant to visualize different types of data. In this document, we will go through each chart type, as well as other chart features, such as legends, summaries, selection, and chart operations. We offer you many pre-built charts, but you can always add new charts of your own through [alerts] and [dashboards].

Chart Types
------------

You will encounter several types of charts in Kamon APM, each with its use cases, features, and limitations. A quick overview of charts is as follows:

| Type        | Instruments                             | Selection          |
|-------------|-----------------------------------------|--------------------|
| Line        | All                                     | Time axis          |
| Bar         | All                                     | Time axis          |
| Histogram   | Gauge, Histogram, Range Sampler, Timer  | Value axis         |
| Heatmap     | Gauge, Histogram, Range Sampler, Timer  | Both axes          |
| Percentiles | Gauge, Histogram, Range Sampler, Timer  | N/A                |

### Line Charts

{% lightbox /assets/img/pages/apm/line-chart.png %}
Line Chart
{% endlightbox %}

Line charts show values as one or more lines, with the x axis corresponding to time, and the y axis corresponding to the measured value. They are useful for visualizing counts, throughput, or various aggregations of values. The exact values that can be visualized depend on the metric instrument type.

| Instrument    | Possible Visualizations                                    |
----------------|------------------------------------------------------------|
| Counter       | Count, Throughput, Sum                                     |
| Gauge         | Sum, Min, Max, Mean, Median, Percentile                    |
| Histogram     | Count, Throughput, Sum, Min, Max, Mean, Median, Percentile |
| Range Sampler | Count, Throughput, Sum, Min, Max, Mean, Median, Percentile |
| Timer         | Count, Throughput, Sum, Min, Max, Mean, Median, Percentile |

If values are grouped, multiple lines will be shown, one for each of the groups and with its own color. You can expand the [legend](#chart-legend) to see the details. For more details on how to group values, read about [dashboards], [alerts], or how to use the [analyze] view.

### Bar Charts

{% lightbox /assets/img/pages/apm/bar-chart.png %}
Bar Chart
{% endlightbox %}

Bar charts show values as stacked bars, with the x axis corresponding to time buckets, and the y axis corresponding to the measured value. They are useful for visualizing counts, throughput, or sums of values, all of which can be visualized for any of the metric instrument types.

If values are grouped, multiple stacked blocks will be shown, one for each of the groups, and with its own color. You can expand the [legend](#chart-legend) to see the details. For more details on how to group values, read about [dashboards], [alerts], or how to use the [analyze] view.

### Histogram Charts

{% lightbox /assets/img/pages/apm/histogram-chart.png %}
Histogram Chart
{% endlightbox %}

Histograms show the distribution of values grouped into value range buckets, with the height of the bar showing the number of entries that fall into each range. The x axis corresponds to the measured values in the distribution, and the y axis corresponds to the number of entries for that bucket. They are one of the traditional ways of visualizing distribuutions, and are vailable for gauge, histogram, range sampler and timer metric instrument types.

As with the bar chart, if values are grouped, multiple stacked blocks will be shown, one for each of the groups, and with its own color. You can expand the [legend](#chart-legend) to see the details. For more details on how to group values, read about [dashboards], [alerts], or how to use the [analyze] view.

Chart Legend
-------------

Chart Summaries
----------------

Selection and Zoom
-------------------

Chart Operations
------------------


[alerts]: ../alerts/
[dashboards]: ../dashboards/
[analyze]: ../deep-dive/analyze
[counters]: ../../core/metrics/#counters
[gauges]: ../../core/metrics/#gauges
[histograms]: ../../core/metrics/#histograms
[timers]: ../../core/metrics/#timers
[range samplers]: ../../core/metrics/#range-samplers
