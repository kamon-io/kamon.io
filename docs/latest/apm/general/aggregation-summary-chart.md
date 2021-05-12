---
title: 'Kamon APM | Charts | Kamon Documentation'
description: 'Investigate aggregated statistics with the aggregation summary chart'
layout: docs
---

{% include toc.html %}

Aggregation Summary Chart
=========================

{% lightbox /assets/img/pages/apm/aggregation-summary-chart.png %}
Aggregation Summary Chart
{% endlightbox %}

Kamon APM can show quick insights into various metrics using the Aggregation Summary Chart, which can group several related metrics and metric aspects into a single component.
These charts are not much different than the typical [line] or [bar chart] - they respond to time selection, hovering, and selection, and they offer the same [chart actions] - but do have several key differences. These charts will always show two or more different aggregations, which _do not have to be on the same metric_. You can switch between them using an operation toggle in the top right corner, which will change the visualization in the chart.

{% alert info %}
Though different metrics may be used in the aggregation summary chart, they must always share the same metric tags and will always be grouped by the same tags.
{% endalert %}

In addition to the ability to switch between different visualizations and metrics, the aggregation summary chart also has a richer legend. Instead of the simple [chart legend], these charts will show a rich table, which shows the label (or multiple labels) by which aggregations were grouped, values for each entry across all aggregations, as well as a progress bar which indicates the relative value, in linear scale, of this grouping compared to the largest value recorded. The values and relative value visualizations will update as the chart is hovered or selections are made, allowing for easier analysis of relative performance of the metric, without having to switch visualizations as you explore.

Both the chart itself and each row can be explored further by clicking on the [analyze] button, which will appear when hovering the element in question.

Each entry in the table can also be clicked to explore a more detailed breakdown of the particular grouping on a separate dashboard. The dashboard in question will depend on the specific chart implementation.

[line]: ../charts/#line-charts
[bar chart]: ../charts/#bar-charts
[chart legend]: ../charts/#chart-legend
[analyze]: ../../deep-dive/analyze/
[chart actions]: ../charts/#chart-operations
