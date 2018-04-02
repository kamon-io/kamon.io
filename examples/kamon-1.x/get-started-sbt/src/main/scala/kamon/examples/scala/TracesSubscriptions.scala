package kamon.examples.scala

import akka.actor.{ActorSystem, Props, Actor}
import kamon.Kamon
import kamon.metric.SubscriptionsDispatcher.TickMetricSnapshot
import kamon.metric.TraceMetrics
import kamon.metric.instrument.Histogram
import kamon.trace.{TraceInfo, Tracer}


object TracesSubscriptions extends App {
  Kamon.start()
  val mySystem = ActorSystem("my-system")
  val subscriber = mySystem.actorOf(Props[TracePrinter], "subscriber")

  // tag:subscribe-to-traces:start
  Kamon.tracer.subscribe(subscriber)
  // tag:subscribe-to-traces:end

  // tag:create-some-traces-and-segments:start
  Tracer.withNewContext("example-trace", autoFinish = true) {
    Tracer.currentContext.withNewSegment("quick-segment", "code", "kamon") {
      Thread.sleep(100)
    }

    Tracer.currentContext.withNewSegment("slow-segment", "code", "kamon") {
      Thread.sleep(3000)
    }
  }
  // tag:create-some-traces-and-segments:end

  // Wait a bit for the snapshot to be flushed.
  Thread.sleep(15000)
  Kamon.shutdown()
}

// tag:subscriber:start
class TracePrinter extends Actor {
  def receive = {
    case traceInfo: TraceInfo =>

    println("#################################################");
    println("Trace Name: " + traceInfo.name)
    println("Timestamp: " + traceInfo.timestamp)
    println("Elapsed Time: " + traceInfo.elapsedTime)
    println("Segments: ");

    traceInfo.segments.foreach { segmentInfo =>
      println("    ------------------------------------------");
      println("    Name: " + segmentInfo.name)
      println("    Category: " + segmentInfo.category)
      println("    Library: " + segmentInfo.library)
      println("    Timestamp: " + segmentInfo.timestamp)
      println("    Elapsed Time: " + segmentInfo.elapsedTime)
    }
  }
}
// tag:subscriber:end

/*
// tag:example-output:start
#################################################
Trace Name: example-trace
Timestamp: 1429311913406000000.nanos
Elapsed Time: 3103544112.nanos
Segments:
    ------------------------------------------
    Name: quick-segment
    Category: code
    Library: kamon
    Timestamp: 1429311913408589398.nanos
    Elapsed Time: 100184837.nanos
    ------------------------------------------
    Name: slow-segment
    Category: code
    Library: kamon
    Timestamp: 1429311913509367747.nanos
    Elapsed Time: 3000135844.nanos
// tag:example-output:end
*/