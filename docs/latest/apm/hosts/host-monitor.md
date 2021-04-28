---
title: 'Kamon APM | Host Monitor | Kamon Documentation'
layout: docs
---

{% include toc.html %}

Host Monitor
=============

Gathering host information should be easy, but often it's not. Most services are running in containers, hosts (be they physical or virtual) are shared, and many complications ensue. To make it simple for you, we've created the **Host Monitor**, a host metric collector service pre-bundled in a Docker container. You can run one of these in each one of your nodes with ease, and get actual metrics for your physical host machines, by running the following command (replace `<API_KEY>` with your [environment API key]).

{% code_block bash %}
docker run --rm \
    --network=host \
    -e KAMON_API_KEY="&lt;API_KEY&gt;" \
    kamon/apm-host-monitor
{% endcode_block %}

You can make this process easier by opening the [connect host] dialog from inside Kamon APM, which will have the same command with the API key already included. After starting the Host Monitor, information should appear in the [host list] within a minute!

You can read more about the Host Monitor in its [Open Source GitHub project][GitHub].

[environment API key]: ../../general/environments/#environment-picker
[host list]: ../host-list/
[connect host]: ../host-list/#connect-host
[GitHub]: https://github.com/kamon-io/kamon-apm-host-monitor
