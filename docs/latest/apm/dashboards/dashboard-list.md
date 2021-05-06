---
title: 'Kamon APM | Dashboard List | Kamon Documentation'
description: 'Manage and explore custom dashboards in Kamon APM'
layout: docs
---

{% include toc.html %}

Dashboard List
===============

{% lightbox /assets/img/pages/apm/dashboard-list.png %}
Dashboard List
{% endlightbox %}

The Dashboard List is a complete list of all dashboards defined for your organization, with a search bar, general information about each dashboard, and the ability to create new dashboards or manage existing ones. When the page is first opened, the search input will automatically be focused. The search is full-text and case-insensitive, and will search through dashboards names, as well as the names of all data sources included in the dashboard. You can read more about data sources in the [dashboard chart creation guide].

The table lists the following details about each dashboard:

* The name of the dashboard
* All services or other data sources that are visualized in the dashboard
* The number of charts defined for the dashboard

Clicking on any of the dashboard rows will take you to the [dashboard details], allowing you to see and manipulate the charts contained therein.

{% alert info %}
Dashboards are tied to your entire organization, not just the current environment! You can read more about how dashboards behave across environments in the [dashboard details] and the [dashboard chart creation][dashboard chart creation guide] guides.
{% endalert %}

Creating Dashboards
--------------------

{% lightbox /assets/img/pages/apm/create-dashboard.png %}
Dashboard Creation Dialog
{% endlightbox %}

New dashboards can be created using the **Create Dashboard** button in the top right corner. This will open a dialog, allowing you to name the dashboard. The name must be unique across dashboards and contain at least three letters, but otherwise it's up to you! Name it in a way that makes sense. Upon creating the dashboard, you will immediately be taken to the [empty dashboard view].

Editing Dashboards
-------------------

{% lightbox /assets/img/pages/apm/rename-dashboard.png %}
Rename Dashboard Dialog
{% endlightbox %}

Though dashboards offer plenty of editing capabilities, the only way to change them from the Dashboard List view is to rename them. You can access this action from the context menu on the right-hand-side of each table row. This will open a dialog, pre-filled with the current dashboard name, and will allow you to rename it. The dashboard creation validations still apply: the name must have three letters and be unique.

Cloning Dashboards
-------------------

{% lightbox /assets/img/pages/apm/clone-dashboard.png %}
Clone Dashboard Dialog
{% endlightbox %}

For those cases where you need to make a very similar dashboard to an existing one, or to back up an existing dashboard, Kamon APM offers a Clone Dashboard functionality. You can create a dashboard which has the exact same charts and chart layout as an already existing dashboard in just two steps. The action can be accessed from the context menu on each dashboard row, and will open a dialog. It will look the same as the Create Dashboard dialog, requiring you to name the dashboard. A default name of `Clone of <original_name>` will be filled in. The name needs to be at least three characters long and unique. Upon completing the flow, you will be taken to the [dashboard details] for he new dashboard, and may continue to edit it.

Deleting Dashboards
--------------------

{% lightbox /assets/img/pages/apm/delete-dashboard.png %}
Delete Dashboard Confirmation Dialog
{% endlightbox %}

If a dashboard is no longer necessary, you can delete it from the context menu on the right. Note that anybody can delete any dashboard, independently of who the dashboard author is. You will be prompted to confirm the action.

{% alert warning %}
Deleting dashboards is irreversible. If you delete a dashboard by accident, you will need to re-create it by hand.
{% endalert %}


[dashboard details]: ../dashboard/
[dashboard chart creation guide]: ../create-edit-dashboard/
[empty dashboard view]: ../dashboard/#mpty-dashboard

