package kamon.examples.scala

import akka.actor.{ActorSystem, Props, Actor}
import kamon.Kamon
import kamon.metric.SubscriptionsDispatcher.TickMetricSnapshot
import kamon.metric.TraceMetrics
import kamon.metric.instrument.Histogram


object MetricsSubscriptions {
  Kamon.start()
  val mySystem = ActorSystem("my-system")
  val subscriber = mySystem.actorOf(Props[SimplePrinter], "subscriber")

  val traceRecorder = Kamon.metrics.entity(TraceMetrics, "test-trace")
  traceRecorder.elapsedTime.record(500)
  traceRecorder.elapsedTime.record(600)

  // tag:metrics-subscriptions:start
  Kamon.metrics.subscribe("trace", "test-trace", subscriber)
  Kamon.metrics.subscribe("trace", "test-*", subscriber)
  Kamon.metrics.subscribe("trace", "**", subscriber)
  // tag:metrics-subscriptions:end


  // Register a couple histograms
  val testHistogram: Histogram = Kamon.metrics.histogram("test-histogram")
  val otherHistogram: Histogram = Kamon.metrics.histogram("other-histogram")

  for(i <- 1 to 100) {
    testHistogram.record(i)
    otherHistogram.record(i)
  }

  // Register a couple counters as well
  Kamon.metrics.counter("first-counter").increment
  Kamon.metrics.counter("second-counter").increment(42)

  // tag:custom-subscriptions:start
  Kamon.metrics.subscribe("counter", "**", subscriber)
  Kamon.metrics.subscribe("histogram", "test-histogram", subscriber)
  // tag:custom-subscriptions:end

  // Wait a bit for the snapshot to be flushed.
  Thread.sleep(15000)

  Kamon.shutdown()
}

// tag:subscriber:start
class SimplePrinter extends Actor {
  def receive = {
    case tickSnapshot: TickMetricSnapshot =>
      val counters = tickSnapshot.metrics.filterKeys(_.category == "counter")
      val histograms = tickSnapshot.metrics.filterKeys(_.category == "histogram")

      println("#################################################")
      println("From: " + tickSnapshot.from)
      println("To: " + tickSnapshot.to)

      counters.foreach { case (e, s) =>
        val counterSnapshot = s.counter("counter").get
        println("Counter [%s] was incremented [%d] times.".format(e.name, counterSnapshot.count))
      }

      histograms.foreach { case (e, s) =>
        val histogramSnapshot = s.histogram("histogram").get
        println("Histogram [%s] has [%d] recordings.".format(e.name, histogramSnapshot.numberOfMeasurements))
      }
  }
}
// tag:subscriber:end