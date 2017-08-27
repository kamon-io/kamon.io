---
title: Kamon | Play | Documentation
layout: documentation-0.6.x
---

Play Framework Integration
==========================


The `kamon-play` module ships with bytecode instrumentation that brings automatic traces and segments management and
automatic trace token propagation to your Play! applications. The details on what those features are about can be found
in the [base functionality] section. Here we will dig into the specific aspects of bringing support for them when using
Play!.

<p class="alert alert-info">
The <b>kamon-play</b> module requires you to start your application using the AspectJ Weaver Agent. Kamon will warn you
at startup if you failed to do so.
</p>

<p class="alert alert-warning">
Since Kamon 0.5.0 we support both Play Framework 2.3 and 2.4, but bringing support for Play! 2.4 required us
to ship different modules for each Play! version. Please make sure you add either <b>kamon-play-23</b> or
<b>kamon-play-24</b> to your project's classpath.
</p>



Server Side Tools
-----------------

As you might expect, our bytecode instrumentation will automatically start a trace when a request is received by your
application and finish it when the response is sent back. Additionally, the `kamon-play` module will try to
generate a readable trace name based on your routing information, by concatenating the full path pattern of your request
with the HTTP verb being used in the request. Let's see a quick example:

{% code_example %}
{%   language scala kamon-play-examples/app/controllers/KamonPlayExample.scala tag:trace-name-action %}
{%   language text kamon-play-examples/conf/routes tag:trace-name-routes label:"Routes" %}
{% endcode_example %}

With the routes provided above, all request sent to `/automatically-named-trace` will receive a name based on the path
and the HTTP verb. If you send a GET request to that path, then you will get a trace named
`automatically-named-trace.get`.

The second option we have is to use the `TraceName` action as demonstrated in the `namedTrace` case above, where we are
using the `TraceName` action to change the automatically generated name to `my-trace-name`. When you use this action to
rename a trace, the path or the HTTP verb are no longer important, as the trace will be renamed to whatever string you
gave to the `TraceName` action.

Finally, you can use the `kamon.play.automatic-trace-token-propagation` configuration key to decide whether to include
the current trace's token in the HTTP response messages.

<p class="alert alert-warning">
By default the <b>kamon-play</b> module utilizes the tags of the request in order to create the trace name, but in the case that the requests doesn't contains tags, we need name the trace as <b>UntaggedTrace</b> in order to avoid the creation of undesired traces
</p>

Client Side
-----------

For the client side, the `kamon-play` module brings instrumentation that will automatically start a segment when you
issue a HTTP request using the `WS` facilities and finish it once the response is back. By default, the name of the
generated segment will have a `http-client` category, `WS-client` as the library name and the segment name will be
generated after the request url. This approach can be problematic though, as sending request to many different urls can
yield an undesirable number of generated segments. To avoid that issue, you can provide your own implementation of
`kamon.play.NameGenerator` and use whatever criteria you useful from the HTTP request to generate a suitable segment
name.



Using the AspectJ Weaver
------------------------

Adding the AspectJ Weaver agent to your Play! application can be a little tricky, as it depends on whether you are
launching Play! in development mode or in production mode. The requirement stays simple, add the
`-javaagent:/path/to/aspectj-weaver.jar` to your JVM startup options, but here are the specifics for each case:


### Running in Development Mode ###

When running on development mode, Play! will not allow forking the JVM where your application runs, thus rendering
useless the most common approaches such as using the `sbt-aspectj` plugin to include the `-javaagent:...` option. In
this case what you really need to do is use our [sbt-aspectj-runner] plugin or launch the activator command with an additional `-J` option specifying the agent location. These options shown below::

{% code_example %}
{% language text kamon-play-examples/app/controllers/KamonPlayExample.scala tag:launch-play-dev-mode-plugin label:"Launching in Dev Mode with aspectj-play-runner" %}
{% language text kamon-play-examples/app/controllers/KamonPlayExample.scala tag:launch-play-dev-mode label:"Launching in Dev Mode with Activator" %}
{% endcode_example %}


### Running in Production Mode ###

Running in production mode is a bit different, assuming that you already packaged your application then you will need to
add the weaver option as described bellow:

{% code_example %}
{%   language text kamon-play-examples/app/controllers/KamonPlayExample.scala tag:launch-play-prod-mode label:"Launching in Production Mode" %}
{% endcode_example %}


[base functionality]: /integrations/web-and-http-toolkits/base-functionality/
[sbt-aspectj-runner]: https://github.com/kamon-io/sbt-aspectj-runner
