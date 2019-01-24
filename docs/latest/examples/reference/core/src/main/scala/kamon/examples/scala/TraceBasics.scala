package kamon.examples.scala

import java.time.Instant

import kamon.Kamon
import kamon.metric.MeasurementUnit.information
import kamon.trace.Span

object TraceBasics extends App {

  // tag:creating-spans:start
  // Minimal Span start/finish cycle
  val span = Kamon.buildSpan("find-users").start()
  // Do your operation here.

  span.finish()
  // tag:creating-spans:end

  {
    // tag:adding-tags:start
    // Adding tags to the SpanBuilder
    val span = Kamon.buildSpan("find-users")
      .withTag("string-tag", "hello")
      .withTag("number-tag", 42)
      .withTag("boolean-tag", true)
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
    val span = Kamon.buildSpan("span-with-marks").start()

    // Adding marks to the Span.
    span
      .mark("message.dequeued")
      .mark(at = Instant.now(), "This could be free text")

    // After this point no marks can be added.
    span.finish()
    // tag:adding-marks:end
  }

  {
    // tag:span-metrics:start
    // Starting a Span and disabling metrics
    val span = Kamon.buildSpan("span-metrics")
      .withMetricTag("component", "netty.server")
      .disableMetrics()
      .start()

    // Enabling metrics for a Span
    span
      .enableMetrics()
      .tagMetric("algorithm", "expensive_algorithm")


    // After this point no further changes can be made.
    span.finish()
    // tag:span-metrics:end
  }

  {
    // tag:current-span:start
    // Both of these expressions return the same Span
    val spanFromContext = Kamon.currentContext().get(Span.ContextKey)
    val spanFromHelper = Kamon.currentSpan()
    // tag:current-span:end
  }

  {
    // tag:with-span-block:start
    val span = Kamon.buildSpan("operation").start()

    // Set as current Span
    Kamon.withSpan(span) {
      // Our span is the current Span here
    }

    // Set as current Span and finish.
    Kamon.withSpan(Kamon.buildSpan("one-off").start(), finishSpan = true) {
      // Our one-off Span is the current Span here
    }
    // tag:with-span-block:end
  }
}
