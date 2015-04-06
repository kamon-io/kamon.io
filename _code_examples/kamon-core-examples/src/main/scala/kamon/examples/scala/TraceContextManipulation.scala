package io.kamon.examples

import kamon.Kamon

object TraceContextManipulation extends App {
  Kamon.start()

  // tag:creating-a-trace-context:start
  val newContext = Kamon.tracer.newContext("test-trace")
  val contextWithCustomToken = Kamon.tracer.newContext("test-trace", "token-1234")
  // tag:creating-a-trace-context:end

}