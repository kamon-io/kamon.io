package kamon.examples.scala

import akka.actor.{ActorSystem, Props, Actor, ActorLogging}
import kamon.trace.TraceContext
/*

object TraceTokenLogging extends App {
  implicit val system = ActorSystem("trace-token-logging")
  val upperCaser = system.actorOf(Props[UpperCaser], "upper-caser")


  // Send five messages without a TraceContext
  for(_ <- 1 to 5) {
    upperCaser ! "Hello without context"
  }


  // Wait a bit to avoid spaghetti logs.
  Thread.sleep(1000)


  // Send five messages with a TraceContext
  for(_ <- 1 to 5) {
    TraceContext.withNewTraceContext("simple-test") {
      upperCaser ! "Hello World with TraceContext"
    }
  }


  // Wait a bit for everything to be logged and shutdown.
  Thread.sleep(200000)
  system.shutdown()
}

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
*/

/*

Example output without logging the trace token:

05:19:15.929 INFO  [undefined][akka://trace-token-logging/user/upper-caser] io.kamon.examples.UpperCaser - Upper casing [Hello without context]
05:19:15.934 INFO  [undefined][akka://trace-token-logging/user/upper-caser] io.kamon.examples.UpperCaser - Upper casing [Hello without context]
05:19:15.934 INFO  [undefined][akka://trace-token-logging/user/upper-caser/length-calculator] io.kamon.examples.LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]
05:19:15.936 INFO  [undefined][akka://trace-token-logging/user/upper-caser] io.kamon.examples.UpperCaser - Upper casing [Hello without context]
05:19:15.936 INFO  [undefined][akka://trace-token-logging/user/upper-caser/length-calculator] io.kamon.examples.LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]
05:19:15.936 INFO  [undefined][akka://trace-token-logging/user/upper-caser] io.kamon.examples.UpperCaser - Upper casing [Hello without context]
05:19:15.937 INFO  [undefined][akka://trace-token-logging/user/upper-caser/length-calculator] io.kamon.examples.LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]
05:19:15.937 INFO  [undefined][akka://trace-token-logging/user/upper-caser] io.kamon.examples.UpperCaser - Upper casing [Hello without context]
05:19:15.937 INFO  [undefined][akka://trace-token-logging/user/upper-caser/length-calculator] io.kamon.examples.LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]
05:19:15.937 INFO  [undefined][akka://trace-token-logging/user/upper-caser/length-calculator] io.kamon.examples.LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]



Example output logging the trace token:

05:19:20.935 INFO  [ivantopo-desktop-1][akka://trace-token-logging/user/upper-caser] io.kamon.examples.UpperCaser - Upper casing [Hello World with TraceContext]
05:19:20.935 INFO  [ivantopo-desktop-2][akka://trace-token-logging/user/upper-caser] io.kamon.examples.UpperCaser - Upper casing [Hello World with TraceContext]
05:19:20.936 INFO  [ivantopo-desktop-1][akka://trace-token-logging/user/upper-caser/length-calculator] io.kamon.examples.LengthCalculator - Calculating the length of: [HELLO WORLD WITH TRACECONTEXT]
05:19:20.936 INFO  [ivantopo-desktop-3][akka://trace-token-logging/user/upper-caser] io.kamon.examples.UpperCaser - Upper casing [Hello World with TraceContext]
05:19:20.936 INFO  [ivantopo-desktop-2][akka://trace-token-logging/user/upper-caser/length-calculator] io.kamon.examples.LengthCalculator - Calculating the length of: [HELLO WORLD WITH TRACECONTEXT]
05:19:20.936 INFO  [ivantopo-desktop-3][akka://trace-token-logging/user/upper-caser/length-calculator] io.kamon.examples.LengthCalculator - Calculating the length of: [HELLO WORLD WITH TRACECONTEXT]
05:19:20.937 INFO  [ivantopo-desktop-4][akka://trace-token-logging/user/upper-caser] io.kamon.examples.UpperCaser - Upper casing [Hello World with TraceContext]
05:19:20.937 INFO  [ivantopo-desktop-5][akka://trace-token-logging/user/upper-caser] io.kamon.examples.UpperCaser - Upper casing [Hello World with TraceContext]
05:19:20.937 INFO  [ivantopo-desktop-4][akka://trace-token-logging/user/upper-caser/length-calculator] io.kamon.examples.LengthCalculator - Calculating the length of: [HELLO WORLD WITH TRACECONTEXT]
05:19:20.938 INFO  [ivantopo-desktop-5][akka://trace-token-logging/user/upper-caser/length-calculator] io.kamon.examples.LengthCalculator - Calculating the length of: [HELLO WORLD WITH TRACECONTEXT]

 */