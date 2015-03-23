package kamon.examples.scala

import kamon.Kamon

// tag:simple-metrics:start
object SimpleMetrics extends App {
  val kamon = Kamon()

  val myHistogram = kamon.userMetrics.histogram("my-histogram")
  myHistogram.record(42)
  myHistogram.record(43)
  myHistogram.record(44)

  val myCounter = kamon.userMetrics.counter("my-counter")
  myCounter.increment()
  myCounter.increment(17)

  kamon.shutdown()
}
// tag:simple-metrics:end
