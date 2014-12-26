package io.kamon.examples

import akka.actor.ActorSystem
import kamon.trace.TraceRecorder

import scala.concurrent.Future

object TraceContextInFuture extends App {
  implicit val system = ActorSystem("example-system")
  implicit val execContext = system.dispatcher

  TraceRecorder.withNewTraceContext(name = "future-example", token = Some("example-01")) {
    Future {
      println(s"[$currentThread - ${TraceRecorder.currentContext.token}] The TraceContext is available during the Future's body execution")

    } map { _ =>
      println(s"[$currentThread - ${TraceRecorder.currentContext.token}] The TraceContext is available during the Future's callbacks execution")

    } flatMap { _ =>
      println(s"[$currentThread - ${TraceRecorder.currentContext.token}] The TraceContext is available during the Future's callbacks execution")
      Future.successful({})
    }
  }

  // Let's wait a bit to see the result.
  Thread.sleep(1000)
  system.shutdown()

  def currentThread: String = Thread.currentThread().getName
}
