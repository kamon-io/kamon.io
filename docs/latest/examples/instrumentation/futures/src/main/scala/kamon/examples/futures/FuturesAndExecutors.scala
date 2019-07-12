package kamon.recipe.futuresandexecutors

import kamon.instrumentation.executor.ExecutorInstrumentation

import scala.concurrent.{ExecutionContext, Future}

object FuturesAndExecutors extends App {
  // tag:registering-a-executor:start
  val threadPool = java.util.concurrent.Executors.newFixedThreadPool(10)
  val registration = ExecutorInstrumentation.instrument(threadPool, "sample-thread-pool")

  // tag:registering-a-executor:end

  implicit val executionContext = ExecutionContext.fromExecutor(threadPool)

  Future {
    "test"
  }
}
