package kamon.akka.examples.scala

import kamon.Kamon
import kamon.trace.Tracer

import scala.concurrent.{Future, ExecutionContext}

object AutomaticTraceContextPropagationWithFutures extends App {
  Kamon.start()

  implicit val ec = ExecutionContext.Implicits.global


  // tag:future-body:start
  Tracer.withNewContext("sample-trace") {
    // The same TraceContext available here,

    Future {
      // is available here as well.
      "Hello Kamon"

    }.map(_.length)
      .flatMap(len ⇒ Future(len.toString))
      .map(s ⇒ Tracer.currentContext)
      .map(println)
      // And through all async callbacks, even while
      // they are executed at different threads!
  }
  // tag:future-body:end

}
