package kamon.akka.examples.scala

import akka.actor.{Actor, ActorSystem, Props}
import akka.util.Timeout
import kamon.Kamon
import akka.pattern.ask
import kamon.akka.examples.scala.ContextPropagation.userID
import kamon.context.Context

import scala.concurrent.duration._

object ContextPropagation extends App {
  implicit val timeout = Timeout(2 seconds)

  val system = ActorSystem("ask-pattern-timeout-warning")
  val someSender = Actor.noSender
  val actor = system.actorOf(Props[TraceTokenPrinter], "trace-token-printer")
  val userID = Context.key[Option[String]]("user-id", None)
  implicit val ec = system.dispatcher

  // tag:tell:start
  Kamon.storeContextKey(userID, Some("1234")) {
    actor ! "Some Message"
    actor.tell("Some message", someSender)
  }
  // tag:tell:end

  // tag:ask:start
  val responseFuture = Kamon.storeContextKey(userID, Some("1234")) {
    (actor ? "Ask Message")
      .mapTo[String]
      .map { response =>
        // The same Context available when asking
        // the actor is available when executing this callback.
        println("Context in MAP: " + Kamon.currentContext.get(userID))
      }
  }
  // tag:ask:end


}

class TraceTokenPrinter extends Actor {
  def receive = {
    case anything =>
      println("Current User: " +  Kamon.currentContext.get(userID))
      sender ! anything
  }
}

class CustomizingSpan extends Actor {
  // tag:customizing-a-span:start
  def receive = {
    case anything =>
      Kamon.currentSpan().tag("my-tag", "awesome-value")

      // do your processing here.
  }
  // tag:customizing-a-span:end
}
