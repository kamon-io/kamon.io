---
title: 'Kamon APM | Host List | Kamon Documentation'
description: 'Keep track of your service hosts with Kamon APM, and discover CPU or memory bottlenecks at a glance'
layout: docs
---

{% include toc.html %}

Host List
=============

{% lightbox /assets/img/apm/host-list.png %}
Host List
{% endlightbox %}

With the Host List, you can get a quick overview of the status of your service hosts, in a tabular format. Each row of the table represents one host, listing its name, the services hosted on it, and its CPU usage (p90) and memory usage percentage. Both of these metrics are also represented in a sparkline. When hovering on the appropriate numerical representation of the metric, the sparkline in question will be highlighted. The values in the sparkline correspond to the time period selected in the [time picker]. Clicking on any of the rows will take you to the [host details] page for that host.

Connect Host
-------------

{% lightbox /assets/img/apm/connect-host.png %}
Connect Host
{% endlightbox %}


You can connect new hosts to Kamon APM by initializing the [Host Monitor] for that host machine. It's a simple matter of clicking on the Connect Host button, in the top right, and following the instructions in the dialog.

[Host Monitor]: ../host-monitor/
[host details]: ../host-details/
[time picker]: ../../general/time-picker/