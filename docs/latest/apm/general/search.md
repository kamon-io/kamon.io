---
title: 'Kamon APM | Search | Kamon Documentation'
description: 'Navigate relevant topics in Kamon APM with ease by using the global search functionality'
layout: docs
---

{% include toc.html %}

Search
=======

{% lightbox /assets/img/apm/search.png %}
Search Bar
{% endlightbox %}

When you cannot find what you are looking for something in Kamon APM, you can find it more quickly and easily by using the search bar. It is a global search over the entire application, and can find [services], [hosts], [dashboards], and [specific integrations]. Additionally, if an _entire_ [trace] ID is entered, it will search for such a trace. Only full matching is done on the trace ID, and partial trace ID searches cannot be done. Alerts cannot be searched for.

{% lightbox /assets/img/apm/search-toggle.png %}
Search Toggle
{% endlightbox %}

You can open the search bar at any point by either clicking on the search icon in the left application sidebar, or by pressing the `S` key. The latter will not work when you are focusing on an input or when a dialog is open.

[services]: ../../services/service-list/
[hosts]: ../../hosts/host-details/
[dashboards]: ../../dashboards/introduction/
[specific integrations]: ../../services/service-details/#integrations
[trace]: ../../traces/overview/
