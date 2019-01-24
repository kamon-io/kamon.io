package kamon.akka.examples.scala

import kamon.Kamon
import kamon.context.Key

import scala.concurrent.{ExecutionContext, Future}

object ContextPropagation extends App {
  implicit val ec = ExecutionContext.Implicits.global
  val userID = Key.local[Option[String]]("user-id", None)


  // tag:future-body:start
  Kamon.withContextKey(userID, Some("1234")) {
    // The Context is available here,

    Future {
      // is available here as well.
      "Hello Kamon"

    }.map(_.length)
      .flatMap(len => Future(len.toString))
      .map(s => Kamon.currentContext().get(userID))
      .map(println)
      // And through all async callbacks, even while
      // they are executed at different threads!
  }
  // tag:future-body:end

}
