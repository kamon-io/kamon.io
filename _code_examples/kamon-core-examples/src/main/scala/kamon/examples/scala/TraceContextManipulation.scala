package io.kamon.examples

import kamon.Kamon

object TraceContextManipulation extends App {
  Kamon.start()

  // tag:creating-a-trace-context:start
  val newContext = Kamon.tracer.newContext("test-trace")

  Thread.sleep(3000)
  newContext.finish()
  // tag:creating-a-trace-context:end

}