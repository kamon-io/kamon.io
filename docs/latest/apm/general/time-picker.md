---
title: 'Kamon APM | Time Picker | Kamon Documentation'
description: 'Learn how to use the time picker to constrain metrics and traces. Examine your services in real time, or drill down into incident periods'
layout: docs
---

{% include toc.html %}

Time Picker
============

When looking at metrics and traces coming out of your monitored microservices, one of the important questions you need to ask is _when_. With the Time Picker, you can drill down into the exact time period you are interested in, or keep an eye on data inside a live sliding window as new information arrives. The time picker is located in the header, in the top right corner of the application. It has two modes of operations - **Live** and **Fixed** - and allows for switching between them.

In Live mode, all charts and traces in the application will be showing a sliding window, which will update with new information _every minute_. When you are in Live mode, the status indicator will be green, and will display a spinning icon. The text on the time picker will indicate which sliding window duration has been set.

{% lightbox /assets/img/pages/apm/live-time-picker.png %}
Live Time Picker
{% endlightbox %}

<a id="fixed-time-mode" /> In Fixed mode, the time window will not update, and all metrics and traces will be constrained to that time window. In Fixed mode, you can debug past incidents and performance bottlenecks without risking your data updating while you're analyzing it. When in Fixed mode, the time picker will show a pause icon in dark gray, and the selected time window boundaries will be shown.

{% lightbox /assets/img/pages/apm/fixed-time-picker.png %}
Fixed Time Picker
{% endlightbox %}

When clicking on the time picker, it will expand to offer a number of useful pre-selected time periods. Selecting any of these periods will switch to using Live mode for the given period. It can be expanded further by clicking on the Custom Range button and selecting an exact range of dates to view in Fixed mode.

{% lightbox /assets/img/pages/apm/expanded-time-picker.png %}
Expanded Time Picker
{% endlightbox %}

You can easily switch between Live and Fixed mode by clicking the Liveness indicator on the right of the Time Picker. When switching modes, the currently selected duration will be retained. For example, when switching from Last 1 day to Fixed mode, it will still show the last day, but will freeze the time frame. When switching from a Fixed period of one day to Live, it will now show the last 1 day, regardless of which range of a day was selected previously.

{% alert info %}
Available time periods in the Time Picker are constrained by your retention, which depend on your [environment] type and your plan. If you need longer retention,
you can upgrade to one of the [paid plans].
{% endalert %}

Time &amp; Charts
-----------------

As noted, all data shown in the application will correspond to the selected time period. The relationship between charts is two-directional. If a specific chart has a time axis, it can be zoomed into by dragging and making a selection. This will update the time picker, moving it into Fixed mode for the selected period, and will update all other charts. You can read about the types of [charts] to learn more.

{% lightbox /assets/img/pages/apm/chart-selection.png %}
Chart Time Selection
{% endlightbox %}

Analyze Time Picker
-------------------

When going into [Analyze][analyze] mode, a separate instance of the Time Picker is present. It will automatically be in Fixed mode and correspond to whichever interval you were viewing when you clicked the Analyze button, be it Live or Fixed. The analyze will be connected to the chart in the same way that charts regularly are, but it will not affect the global time picker when you exit the Analyze view. This allows you to deep-dive into issues and diagnose problems without losing the overall context in your application.

{% lightbox /assets/img/pages/apm/analyze-time-picker.png %}
Analyze Time Picker
{% endlightbox %}

[charts]: ../charts/
[analyze]: ../../deep-dive/analyze.md
[paid plans]: /apm/pricing/
[environment]: ../environments/
