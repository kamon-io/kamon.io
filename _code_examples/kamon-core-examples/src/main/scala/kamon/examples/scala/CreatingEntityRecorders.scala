package kamon.examples.scala

import kamon.Kamon
import kamon.metric.{Entity, EntityRecorderFactory, GenericEntityRecorder}
import kamon.metric.instrument.{Time, InstrumentFactory}

object CreatingEntityRecorders extends App {
  val kamon = Kamon()

  //
  // Managed registration.
  //

  kamon.metrics.register(ActorMetrics, "my-managed-actor").map { registration =>
    val managedRecorder = registration.recorder
    managedRecorder.processingTime.record(42)
  }

  //
  // Manual registration.
  //
  val myManualActor = Entity("my-manual-actor", ActorMetrics.category)
  val instrumentFactory = kamon.metrics.instrumentFactory(ActorMetrics.category)
  val manualRecorder = kamon.metrics.register(myManualActor,
    new ActorMetrics(instrumentFactory)).recorder

  manualRecorder.processingTime.record(42)

  kamon.shutdown()
}

class ActorMetrics(instrumentFactory: InstrumentFactory) extends GenericEntityRecorder(instrumentFactory) {
  val timeInMailbox = histogram("time-in-mailbox", Time.Nanoseconds)
  val processingTime = histogram("processing-time", Time.Nanoseconds)
  val mailboxSize = minMaxCounter("mailbox-size")
  val errors = counter("errors")
}

object ActorMetrics extends EntityRecorderFactory[ActorMetrics] {
  def category: String = "actor"
  def createRecorder(instrumentFactory: InstrumentFactory): ActorMetrics = new ActorMetrics(instrumentFactory)
}
