---
title: 'Kamon APM | Charts | Kamon Documentation'
description: 'Learn how Kamon APM visualized any metric in your system using line charts, bar charts, heatmaps, histograms, percentile distribution visualizations, and more'
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

Line chart support selection along the time axis (x axis) and, in [analyze] view, zooming into the x axis.

### Bar Charts

{% lightbox /assets/img/pages/apm/bar-chart.png %}
Bar Chart
{% endlightbox %}

Bar charts show values as stacked bars, with the x axis corresponding to time buckets, and the y axis corresponding to the measured value. They are useful for visualizing counts, throughput, or sums of values, all of which can be visualized for any of the metric instrument types.

If values are grouped, multiple stacked blocks will be shown, one for each of the groups, and with its own color. You can expand the [legend](#chart-legend) to see the details. For more details on how to group values, read about [dashboards], [alerts], or how to use the [analyze] view.

Bar chart support selection along the time axis (x axis) and, in [analyze] view, zooming into the x axis.

### Histogram Charts

{% lightbox /assets/img/pages/apm/histogram-chart.png %}
Histogram Chart
{% endlightbox %}

Histograms show the distribution of values grouped into value range buckets, with the height of the bar showing the number of entries that fall into each range. The x axis corresponds to the measured values in the distribution, and the y axis corresponds to the number of entries for that bucket. They are one of the traditional ways of visualizing distribuutions, and are available for gauge, histogram, range sampler and timer metric instrument types. Values in the chart will match the time selected in the [time picker], but will do not display the time axis.

As with the bar chart, if values are grouped, multiple stacked blocks will be shown, one for each of the groups, and with its own color. You can expand the [legend](#chart-legend) to see the details. For more details on how to group values, read about [dashboards], [alerts], or how to use the [analyze] view.

Histogram chart support selection and zooming into the value axis (x axis) in [analyze] view.

The [summary](#chart-summaries) of the histogram chart will always show the count of recorded values.
If the chart is hovered, the count for the selected bucket, and the range of values the bucket covers, will be shown. Otherwise, when the chart is not interacted with, the entire count of recorded values will be shown.

The [legend](#chart-legend) will show the same values, but will respect grouping, if any is specified.

### Heatmap Charts

{% lightbox /assets/img/pages/apm/heatmap-chart.png %}
Heatmap Chart
{% endlightbox %}

Heatmaps show the distribution of values in a given time period, as specified by the [time picker]. The x axis corresponds to time, while the y axis corresponds to the recorded values. Each entry inside of it is represented as a square and its colour will represent how often this value occurred in a given time period. Blue and green colors represent rare events, yellow and orange more common events, while red values represent that most values recorded belong to the given bucket.

Unlike other charts, grouping does not have an effect on heatmaps, since the color is determined by the frequency of recorded values.

Histogram support selection on the time axis (x axis), while in the [analyze] view they support selection and zooming on both axes.

### Percentile Charts

{% lightbox /assets/img/pages/apm/percentiles-chart.png %}
Percentiles Chart
{% endlightbox %}

The percentiles chart shows at what percentile of recordings assume a certain value. On the x axis, the percentile at which a value first appears is shown, while on the y axis the values themselves are displayed. It is split into 45 buckets, with gradually reduced granularity and eventual precision of up to 3 decimals (up to the 99.999th percentile). They are available for gauge, histogram, range sampler and timer metric instrument types. Values in the chart will match the time selected in the [time picker], but will do not display the time axis.

Like for line charts, if values are grouped, multiple lines will be shown, one for each of the groups and with its own color. In addition to that, a *dashed* line will be shown, with the overall distribution for *all groups*. You can expand the [legend](#chart-legend) to see the details per group. For more details on how to group values, read about [dashboards], [alerts], or how to use the [analyze] view.

Percentiles charts do not support selection or zooming at all, as their purpose is to show the entire distribution.

The [summary](#chart-summaries) of the percentiles chart will always show two things:

1. The value at a given percentile
2. The number of recorded values at a percentile higher than this one

If the chart is hovered, the appropriate percentile will be shown. Otherwise, when the chart is not interacted with, values for the 99th percentile are shown. When the chart shows multiple groups, hovering it will still show the overall distribution across all groups.

The [legend](#chart-legend) will show the same values, but will respect grouping, if any is specified.

Chart Legend
-------------

<div data-video-src="/assets/video/chart-legend-toggle.mp4" data-caption="Chart Legend" />


Every standard chart in Kamon APM has a legend that can be expanded. When expanded, it will show the legend in tabular format, with one row per group. Each row will have at least two columns, and can be sorted by any of the columns by clicking on the header. The first column will always be the name, which can be one of the following:

* Tags which make up the grouping **(most common)**
* Metrics, if multiple metrics are shown in the same chart **(rare)**

The name will also include a color indicator which matches the color the values assume in the chart above it (with the exception of the [heatmap](#heatmap-charts), which has different semantics).

The other column(s) depend on the chart in question, and can be the number of values, throughput, value at a certain percentile, or any other value that can be visualized in Kamon APM. These values will largely depend on the chart in question, and is arbitrary. The two exceptions are [histograms](#histogram-charts) and [percentile charts](#percentile-charts), which will always have a pre-defined legend, as stated in sections dedicated to them.

When hovering on a certain row, the values which correspond to the that group will be highlighted in the chart. This can be useful when many different groups appear in the same chart and it becomes difficult to differentiate them. The one exception is the [heatmap](#heatmap-chart), for which this has no effect.

For any chart with a time axis (i.e., all but percentiles and histogram charts), hovering on the chart will update the values in the legend to match the time range being hovered.

Chart Summaries
----------------

{% lightbox /assets/img/pages/apm/chart-summary.png %}
Chart Summary
{% endlightbox %}

Every chart in Kamon APM has one or more summaries in the top right corner. These summaries will typically show quick insights into the chart, in a numeric format. The summaries can be the count of recorded values, throughput, the value at a certain percentile, or any other value that can be visualized in Kamon APM. These values will largely depend on the chart in question, and are arbitrary, but fine-tuned to be of use. The two exceptions are [histograms](#histogram-charts) and [percentile charts](#percentile-charts), which will always have a pre-defined summary, as stated in sections dedicated to them.

For any chart, hovering on areas of it will update the values in the summaries to match the time or value range being hovered.

Hover, Selection and Zoom
--------------------------

Every chart in Kamon APM will respond to hovering over it. When doing so, a certain area of the chart will be highlighted and summaries and legends will change to match only that chunk of the recorded values. These updates will also be **synchronized** between all charts that share a time axis (i.e., all but [percentile](#percentile-charts) and [histogram](#histogram-charts) charts). This means that hovering one chart on the time axis will highlight all charts on the page and update their legends and summaries.

<div data-video-src="/assets/video/chart-hover.mp4" data-caption="Chart Hover Behavior" />

With a few exceptions, which we will get into later, any chart with a time axis can also be zoomed into by clicking and dragging a desired time period. The selected area will be aligned to the nearest time period granularity, and will update the [time picker], and with it every other chart in the application. Doing this will transfer the application into [fixed time mode], independently of the mode you are currently in. If you wish to return to live update mode, you will need to interact with the [time picker].

The exceptions to this rule are as follows:

1. Any chart without a time axis cannot be selected in this way ([percentiles] and [histograms])
2. Chart previews when creating [alerts] and [dashboards] cannot be selected
3. The [alert drawer] cannot be selected
4. The [analyze] view has special behaviour discussed in its own chapter

<div data-video-src="/assets/video/chart-selection.mp4" data-caption="Chart Selection Behavior" />

Chart Operations
------------------

{% lightbox /assets/img/pages/apm/chart-operations.png %}
Chart Operations
{% endlightbox %}

Every chart in Kamon APM has a set of operations that can be performed with it. They can be accessed by clicking the toggle on the chart name to reveal a dropdown menu. At the very least, you will be able to:

1. Create an [alert][alerts] based on the data in this chart
2. Create a new [dashboard][dashboards] chart based on this chart

In certain contexts, such as [dashboards], additional operations might be available, and will be outlined in their respective sections.

Additionally, upon hovering, each chart will show a green Analyze button. Upon clicking on this button, you will be taken to the [analyze] view, and will be able to drill down into the metric details and correlate traces.

[alerts]: ../../alerts/
[alert drawer]: ../../alerts/#alert-drawer
[dashboards]: ../../dashboards/introduction/
[analyze]: ../../deep-dive/analyze/
[counters]: ../../../core/metrics/#counters
[gauges]: ../../../core/metrics/#gauges
[histograms]: ../../../core/metrics/#histograms
[timers]: ../../../core/metrics/#timers
[range samplers]: ../../../core/metrics/#range-samplers
[time picker]: ../time-picker/
[fixed time mode]: ../time-picker/#fixed-time-mode
