package kamon.akka.examples.scala

import akka.actor.{Actor, Props, ActorSystem}
import akka.util.Timeout
import kamon.Kamon
import kamon.trace.Tracer
import akka.pattern.ask
import scala.concurrent.duration._

object AutomaticTraceContextPropagation extends App {
  Kamon.start()
  implicit val timeout = Timeout(2 seconds)

  val system = ActorSystem("ask-pattern-timeout-warning")
  val someSender = Actor.noSender
  val actor = system.actorOf(Props[TraceTokenPrinter], "trace-token-printer")
  implicit val ec = system.dispatcher

  // tag:tell:start
  Tracer.withNewContext("sample-trace") {
    actor ! "Some Message"
    actor.tell("Some message", someSender)
  }
  // tag:tell:end

  // tag:ask:start
  val responseFuture = Tracer.withNewContext("sample-trace") {
    (actor ? "Ask Message")
      .mapTo[String]
      .map { response =>
        // The same TraceContext available when asking
        // the actor is available when executing this callback.
        println("Context in MAP: " + Tracer.currentContext)
      }
  }
  // tag:ask:end


}

class TraceTokenPrinter extends Actor {
  def receive = {
    case anything =>
      println("Current Token: " + Tracer.currentContext.token)
      sender ! anything
  }
}
