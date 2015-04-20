package kamon.examples.scala

import akka.actor.{ActorSystem, Props, Actor, ActorLogging}
import ch.qos.logback.classic.pattern.ClassicConverter
import ch.qos.logback.classic.spi.ILoggingEvent
import kamon.trace.Tracer


object TraceTokenLogging extends App {
  implicit val system = ActorSystem("trace-token-logging")
  val upperCaser = system.actorOf(Props[UpperCaser], "upper-caser")


  // Send five messages without a TraceContext
  for(_ <- 1 to 5) {
    upperCaser ! "Hello without context"
  }


  // Wait a bit to avoid spaghetti logs.
  Thread.sleep(1000)

  // tag:sending-async-events:start
  // Send five messages with a TraceContext
  for(_ <- 1 to 5) {
    Tracer.withNewContext("simple-test") {
      upperCaser ! "Hello World with TraceContext"
    }
  }
  // tag:sending-async-events:end


  // Wait a bit for everything to be logged and shutdown.
  Thread.sleep(200000)
  system.shutdown()
}

// tag:converter-definition:start
class LogbackTraceTokenConverter extends ClassicConverter {

  def convert(event: ILoggingEvent): String =
    Tracer.currentContext.collect(_.token).getOrElse("undefined")
}
// tag:converter-definition:end

// tag:trace-token-logging:start
class UpperCaser extends Actor with ActorLogging {
  val lengthCalculator = context.actorOf(Props[LengthCalculator].withDispatcher("my-dispatcher"), "length-calculator")

  def receive = {
    case anyString: String =>
      log.info("Upper casing [{}]", anyString)
      lengthCalculator.forward(anyString.toUpperCase)
  }
}

class LengthCalculator extends Actor with ActorLogging {
  def receive = {
    case anyString: String =>
      log.info("Calculating the length of: [{}]", anyString)
  }
}
// tag:trace-token-logging:end


/*
// tag:trace-token-logging-output-without-token:start

Example output without logging the trace token:

06:59:42.538 INFO  [undefined][akka://trace-token-logging/user/upper-caser] kamon.examples.java.TraceTokenLogging$UpperCaser - Upper casing [Hello without context]
06:59:42.541 INFO  [undefined][akka://trace-token-logging/user/upper-caser] kamon.examples.java.TraceTokenLogging$UpperCaser - Upper casing [Hello without context]
06:59:42.542 INFO  [undefined][akka://trace-token-logging/user/upper-caser/length-calculator] kamon.examples.java.TraceTokenLogging$LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]
06:59:42.543 INFO  [undefined][akka://trace-token-logging/user/upper-caser/length-calculator] kamon.examples.java.TraceTokenLogging$LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]
06:59:42.544 INFO  [undefined][akka://trace-token-logging/user/upper-caser] kamon.examples.java.TraceTokenLogging$UpperCaser - Upper casing [Hello without context]
06:59:42.545 INFO  [undefined][akka://trace-token-logging/user/upper-caser/length-calculator] kamon.examples.java.TraceTokenLogging$LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]
06:59:42.545 INFO  [undefined][akka://trace-token-logging/user/upper-caser] kamon.examples.java.TraceTokenLogging$UpperCaser - Upper casing [Hello without context]
06:59:42.545 INFO  [undefined][akka://trace-token-logging/user/upper-caser] kamon.examples.java.TraceTokenLogging$UpperCaser - Upper casing [Hello without context]
06:59:42.546 INFO  [undefined][akka://trace-token-logging/user/upper-caser/length-calculator] kamon.examples.java.TraceTokenLogging$LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]
06:59:42.546 INFO  [undefined][akka://trace-token-logging/user/upper-caser/length-calculator] kamon.examples.java.TraceTokenLogging$LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]

// tag:trace-token-logging-output-without-token:end
// tag:trace-token-logging-output-with-token:start

Example output logging the trace token:

06:59:43.533 INFO  [ivantopo-desktop-1][akka://trace-token-logging/user/upper-caser] kamon.examples.java.TraceTokenLogging$UpperCaser - Upper casing [Hello without context]
06:59:43.533 INFO  [ivantopo-desktop-1][akka://trace-token-logging/user/upper-caser/length-calculator] kamon.examples.java.TraceTokenLogging$LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]
06:59:43.534 INFO  [ivantopo-desktop-2][akka://trace-token-logging/user/upper-caser] kamon.examples.java.TraceTokenLogging$UpperCaser - Upper casing [Hello without context]
06:59:43.534 INFO  [ivantopo-desktop-3][akka://trace-token-logging/user/upper-caser] kamon.examples.java.TraceTokenLogging$UpperCaser - Upper casing [Hello without context]
06:59:43.534 INFO  [ivantopo-desktop-2][akka://trace-token-logging/user/upper-caser/length-calculator] kamon.examples.java.TraceTokenLogging$LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]
06:59:43.534 INFO  [ivantopo-desktop-3][akka://trace-token-logging/user/upper-caser/length-calculator] kamon.examples.java.TraceTokenLogging$LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]
06:59:43.534 INFO  [ivantopo-desktop-4][akka://trace-token-logging/user/upper-caser] kamon.examples.java.TraceTokenLogging$UpperCaser - Upper casing [Hello without context]
06:59:43.535 INFO  [ivantopo-desktop-4][akka://trace-token-logging/user/upper-caser/length-calculator] kamon.examples.java.TraceTokenLogging$LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]
06:59:43.535 INFO  [ivantopo-desktop-5][akka://trace-token-logging/user/upper-caser] kamon.examples.java.TraceTokenLogging$UpperCaser - Upper casing [Hello without context]
06:59:43.535 INFO  [ivantopo-desktop-5][akka://trace-token-logging/user/upper-caser/length-calculator] kamon.examples.java.TraceTokenLogging$LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]

// tag:trace-token-logging-output-with-token:end */