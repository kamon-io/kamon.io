---
title: 'Akka Ask Pattern Timeout Warning | Kamon Documentation'
layout: docs
redirect_from:
  - /documentation/0.6.x/kamon-akka/ask-pattern-timeout-warning/
  - /documentation/1.x/instrumentation/akka/ask-pattern-timeout-warning/
---

{% include toc.html %}

Ask Pattern Timeout Warning
===========================

Akka's ask pattern provides an easy way to send a message to an actor and expect to receive a response from it. For this
to work properly the ask pattern needs to take care of several details, but to keep it short in this section we will
mention only two:

- sending a message to an actor doesn't mean it will reply, it does not even mean that it will process it.
- Scala Futures do not timeout, you need to fulfill your promises.

In order to ensure that the `Future` you get when using the ask pattern is always fulfilled an action that completes the
future with a `AskTimeoutException` is scheduled using the provided timeout, and that action is canceled if the target
actor replies in a timely fashion to the message. By doing this, it doesn't matter if the actor you are _asking_ for
something replies, fails or is unavailable, your future will always be completed with something. To get practical, picture
a piece of code like the following making use of the ask pattern:

{% code_example %}
{%   language scala instrumentation/akka/src/main/scala/kamon/examples/akka/scala/AskPatternTimeoutWarning.scala tag:simple-ask %}
{% endcode_example %}

What happens when the timeout is reached and there is no reply from the actor? well, your future is completed with an exception that looks
like this one:

{% code_example %}
{%   language text instrumentation/akka/src/main/scala/kamon/examples/akka/scala/AskPatternTimeoutWarning.scala tag:typical-ask-timeout-exception label:"Typical Exception Stacktrace" %}
{% endcode_example %}

If your application code is making use of the ask pattern then you should be familiar (and probably not friends) with
that message. It is nice to know that a message timed out, but where was that message sent from? it might be simple to
find that out if you are sending messages from a single location in your code, but once you start composing
ask-generated futures it can get much more complicated to know exactly what timed out, as one failure will be propagated
through the whole composition and you won't know for sure which one failed.

One way to deal with this problem would be to add a transformation to each ask-generated future that you get, so that if
a future fails with a `Failure(AskTimeoutException)` you can turn it into a `Failure(SomeRecognizibleException)` and you
can know for sure what failed. This implies a bit more of coding, but certainly seems to be the safest way to proceed.

Nevertheless, sometimes you can't introduce these code changes but you still need to know what is going on. If you get
to this point, Kamon offers you the possibility to gather information about the call sites where the ask pattern was
used and log a warning message with this info in case the ask times out. You can enable this timeout warning by setting
the `kamon.akka.ask-pattern-timeout-warning` configuration key. This warning comes in two flavors as described bellow:


### Lightweight Warning ###

When the warning setting is set to `lightweight`, Kamon will log a simple warning telling you the file and line number
(if available) from which the ask that timed out was called. The message for the example above looks like this:

{% code_example %}
{%   language text instrumentation/akka/src/main/scala/kamon/examples/akka/scala/AskPatternTimeoutWarning.scala tag:lightweight-warning label:"Lightweight Warning" %}
{% endcode_example %}

This approach works nicely when you are using the ask pattern directly in your codebase and thanks to some AspectJ features
we can capture the source code location of the ask usage practically without overhead.


### Heavyweight Warning ###

Sometimes you are not using the ask pattern directly, but you use some library that uses it internally and just having
the source call location of the ask isn't enough, but you rather need the full stack trace to know what piece of your
code is triggering the ask. In these cases, you can set the warning setting to `heavyweight` and Kamon will capture the
full stack trace when the ask is triggered and include it in the warning message if the ask times out. That's why we
call it "heavyweight", it will __allways__ create an exception to capture the stack trace, but only log it if the ask
times out, and, taking the exception consumes a bit of resources. Depending on how heavily your application uses the ask
pattern, the extra overhead might be tolerable, but that is up to you to decide.

<p class="alert alert-warning">
Beware that taking stack traces on every use of the ask pattern doesn't come for free. When using the heavyweight mode
Kamon creates a new Exception and stores its stack trace during the lifetime of the ask, and that will consume some
additional CPU and memory.
</p>

When using the heavyweight mode, the logged warning message looks like this:

{% code_example %}
{%   language text instrumentation/akka/src/main/scala/kamon/examples/akka/scala/AskPatternTimeoutWarning.scala tag:heavyweight-warning label:"Heavyweight Warning" %}
{% endcode_example %}


[ask pattern]: http://doc.akka.io/docs/akka/snapshot/scala/actors.html
