package kamon.examples.scala

import kamon.Kamon
import kamon.metric.{Entity, EntityRecorderFactory, GenericEntityRecorder}
import kamon.metric.instrument.{Time, InstrumentFactory}

object CreatingEntityRecorders extends App {
  Kamon.start()

  // tag:entity-registration:start
  val myManagedActorMetrics = Kamon.metrics.entity(ActorMetrics, "my-managed-actor")

  myManagedActorMetrics.mailboxSize.increment()
  myManagedActorMetrics.processingTime.record(42)
  myManagedActorMetrics.mailboxSize.decrement()
  //tag:entity-registration:end

  Kamon.shutdown()
}

// tag:creating-entity-recorders:start
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
// tag:creating-entity-recorders:end
