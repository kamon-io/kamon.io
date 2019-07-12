package kamon.akka.examples.scala

import kamon.Kamon
import kamon.context.Context
import kamon.instrumentation.futures.scala.ScalaFutureInstrumentation.{trace, traceAsync}

import scala.concurrent.{ExecutionContext, Future}

object ContextPropagation extends App {
  implicit val ec = ExecutionContext.Implicits.global
  val userID = Context.key[Option[String]]("user-id", None)


  // tag:future-body:start
  Kamon.storeContextKey(userID, Some("1234")) {
    // The userID Context key is available here,

    Future {
      // is available here as well.
      "Hello Kamon"

    }.map(_.length)
      .flatMap(len => Future(len.toString))
      .map(s => Kamon.currentContext().get(userID))
      .map(println)
      // And through all async callbacks, even though
      // they are executed in different threads!
  }
  // tag:future-body:end



  // tag:future-spans:start
  Future(trace("future-body") {

    // Here goes the actual future work, same as usual.
    "Hello Kamon"

  }).map(traceAsync("calculate-length")(_.length))
    .flatMap(traceAsync("to-string")(len => Future(len.toString)))
    .map(_ => Kamon.currentContext().get(userID))
    .map(println)
  // And through all async callbacks, even though
  // they are executed in different threads!

  // tag:future-spans:end
}
