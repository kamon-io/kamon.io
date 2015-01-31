package kamon.examples.scala

import kamon.Kamon

object GetStarted extends App {
  val kamon = Kamon()
  val someHistogram = kamon.userMetrics.histogram("some-histogram")
  val someCounter = kamon.userMetrics.counter("some-counter")

  someHistogram.record(42)
  someHistogram.record(50)
  someCounter.increment()

  // This application wont terminate unless you shutdown Kamon.
  kamon.shutdown()
}
