---
layout: post
title: Scalatra Kamon Integragion
date: 2015-05-01
categories: teamblog
---

Build Setup
----------------------------------------------

you need include in your `Build.scala` some dependencies. It should look like this.

{% code_example %}
{%   language scala posts/kamon-scalatra-integration/Build.scala tag:dependencies label:"Build.scala" %}
{% endcode_example %}


### Configuring Scalatra ###
in order to `scalatra` listen web requests, it necessary register a listener in `web.xml` inside of  `../WEB-INF` folder.


{% code_example %}
{%   language markup posts/kamon-scalatra-integration/web.xml tag:web-xml label:"web.xml"%}
{% endcode_example %}

also we will need `bootstrap` Scalatra, creating `ScalatraBootstrap.scala` in `src/main/scala` folder and adding the following code.


{% code_example %}
{%   language scala posts/kamon-scalatra-integration/ScalatraBootstrap.scala tag:bootstrap label:"ScalatraBootstrap.scala"%}
{% endcode_example %}


### Create a Simple Servlet ###

Let's start creating a convenient trait in order to use the Kamon instruments:


{% code_example %}
{%   language scala posts/kamon-scalatra-integration/KamonSupport.scala tag:instruments label:"KamonSupport.scala"%}
{% endcode_example %}


Now let’s create a simple servlet that will record some metrics. Create a file called `KamonServlet.scala` and introduce following code


{% code_example %}
{%   language scala posts/kamon-scalatra-integration/KamonServlet.scala tag:servlet label:"KamonServlet.scala"%}
{% endcode_example %}


### Start the Server ###

{% code_example %}
{%   language scala posts/kamon-scalatra-integration/EmbeddedServer.scala tag:server label:"EmbeddedServer.scala"%}
{% endcode_example %}
