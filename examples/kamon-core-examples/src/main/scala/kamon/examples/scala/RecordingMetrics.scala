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

  // tag:entity-metrics:start
  val myManagedActorMetrics = Kamon.metrics.entity(ActorMetrics, "my-managed-actor")

  myManagedActorMetrics.mailboxSize.increment()
  myManagedActorMetrics.processingTime.record(42)
  myManagedActorMetrics.mailboxSize.decrement()
  // tag:entity-metrics:end


  // tag:cleanup:start
  Kamon.metrics.removeHistogram("my-histogram")
  Kamon.metrics.removeCounter("my-counter")
  Kamon.metrics.removeMinMaxCounter("my-mm-counter")

  // The histogram was created with tags and the same tags need to be
  // provided to correctly remove it.
  Kamon.metrics.removeHistogram("my-tagged-histogram", tags = Map("algorithm" -> "X"))

  // For entities you need to provide the entity category as well.
  Kamon.metrics.removeEntity("my-managed-actor", "akka-actor")
  // tag:cleanup:end

  Kamon.shutdown()
}
