package io.kamon.examples

import kamon.Kamon
import kamon.trace.Tracer

import scala.concurrent.Future

object TraceContextManipulation extends App {
  implicit val ec = scala.concurrent.ExecutionContext.global
  Kamon.start()

  // tag:creating-a-trace-context:start
  val newContext = Kamon.tracer.newContext("test-trace")

  Thread.sleep(3000)
  newContext.finish()
  // tag:creating-a-trace-context:end

  // tag:new-context:start
  val testTrace = Kamon.tracer.newContext("test-trace")
  val testTraceWithToken = Kamon.tracer.newContext("trace-with-token", Some("token-42"))

  // You can rename a trace before it is finished.
  testTrace.rename("cool-functinality-X")
  testTrace.finish()

  // And you can also access it's token if you want to.
  println("Test Trace Token: " + testTrace.token)
  // tag:new-context:end


  // tag:new-context-block:start
  Tracer.withNewContext("GetUserDetails", autoFinish = true) {
    // While this block of codes executes a new TraceContext
    // is set as the current context and finished after the
    // block returns.
    println("Current Trace Token: " + Tracer.currentContext.token)

    "Some awesome result";
  }

  // No TraceContext is present when you reach this point.
  // tag:new-context-block:end


  // tag:storing-the-trace-context:start
  val context = Kamon.tracer.newContext("example-trace")

  Tracer.withContext(context) {
    // While this code executes, `context` is the current
    // TraceContext.
    println("Current Trace Token: " + Tracer.currentContext.token)
  }
  // tag:storing-the-trace-context:end


  // tag:creating-segments:start
  Tracer.withNewContext("trace-with-segments", autoFinish = true) {
    val segment = Tracer.currentContext.startSegment("some-cool-section", "business-logic", "kamon")
    // Some application code here.
    segment.finish()
  }
  // tag:creating-segments:end


  // tag:managed-segments:start
  Tracer.withNewContext("trace-with-segments", autoFinish = true) {

    Tracer.currentContext.withNewSegment("some-cool-section", "business-logic", "kamon") {
      // Here is where the segment does it's job.

    }
  }
  // tag:managed-segments:end


  // tag:async-segment:start
  Tracer.withNewContext("trace-with-segments", autoFinish = true) {

    Tracer.currentContext.withNewAsyncSegment("some-cool-section", "business-logic", "kamon") {
      Future {
        // Some code that will be executed asynchronously.
      }
    }
  }
  // tag:async-segment:end
}