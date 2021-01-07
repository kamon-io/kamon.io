package kamon.examples.scala

import java.time.Instant

import kamon.Kamon
import kamon.trace.Span

object TraceBasics extends App {

  // tag:creating-spans:start
  // Minimal Span start/finish cycle
  val span = Kamon.spanBuilder("find-users").start()
  // Do your operation here.

  span.finish()
  // tag:creating-spans:end

  {
    // tag:adding-tags:start
    // Adding tags to the SpanBuilder
    val span = Kamon.spanBuilder("find-users")
      .tag("string-tag", "hello")
      .tag("number-tag", 42)
      .tag("boolean-tag", true)
      .start()

    // Adding tags to the Span.
    span
      .tag("other-string-tag", "bye")
      .tag("other-number-tag", 24)
      .tag("other-boolean-tag", false)

    // After this point no tags can be added.
    span.finish()
    // tag:adding-tags:end
  }

  {
    // tag:adding-marks:start
    val span = Kamon.spanBuilder("span-with-marks").start()

    // Adding marks to the Span.
    span
      .mark("message.dequeued")
      .mark("This could be free text", at = Instant.now())

    // After this point no marks can be added.
    span.finish()
    // tag:adding-marks:end
  }

  {
    // tag:span-metrics:start
    // Starting a Span and disabling metrics
    val span = Kamon.spanBuilder("span-metrics")
      .tagMetrics("component", "netty.server")
      .doNotTrackMetrics()
      .start()

    // Enabling metrics for a Span
    span.trackMetrics()
      .tagMetrics("algorithm", "expensive_algorithm")

    // After this point no further changes can be made.
    span.finish()
    // tag:span-metrics:end
  }

  {
    // tag:current-span:start
    // Both of these expressions return the same Span

    val spanFromContext = Kamon.currentContext().get(Span.Key)
    val spanFromHelper = Kamon.currentSpan()
    // tag:current-span:end
  }

  {
    // tag:with-span-block:start
    val span = Kamon.spanBuilder("operation").start()

    // Set as current Span
    Kamon.runWithSpan(span) {
      // Our span is the current Span here
    }

    // Set as current Span and finish.
    Kamon.runWithSpan(Kamon.spanBuilder("one-off").start(), finishSpan = true) {
      // Our one-off Span is the current Span here
    }
    // tag:with-span-block:end
  }
}
