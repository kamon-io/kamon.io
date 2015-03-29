package kamon.examples.scala

import kamon.Kamon
import scala.concurrent.duration._

object RecordingMetrics extends App {
  Kamon.start()

  // tag:simple-metrics:start
  val myHistogram = Kamon.metrics.histogram("my-histogram")
  myHistogram.record(42)
  myHistogram.record(43)
  myHistogram.record(44)

  val myCounter = Kamon.metrics.counter("my-counter")
  myCounter.increment()
  myCounter.increment(17)

  val myMMCounter = Kamon.metrics.minMaxCounter("my-mm-counter", refreshInterval = 500 milliseconds)
  myMMCounter.increment()
  myMMCounter.decrement()

  val myTaggedHistogram = Kamon.metrics.histogram("my-tagged-histogram", tags = Map("algorithm" -> "X"))
  myTaggedHistogram.record(700L)
  myTaggedHistogram.record(800L)
  // tag:simple-metrics:end

  Kamon.shutdown()
}
