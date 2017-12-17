package kamon.recipe.futuresandexecutors

import kamon.executors.Executors

import scala.concurrent.{ExecutionContext, Future}

object FuturesAndExecutors extends App {
  // tag:registering-a-executor:start
  val threadPool = java.util.concurrent.Executors.newFixedThreadPool(10)
  val registration = Executors.register("sample-thread-pool", threadPool)


  registration.cancel() // Later on, when shutting down the executor service.
  // tag:registering-a-executor:end

  implicit val executionContext = ExecutionContext.fromExecutor(threadPool)

  Future {
    "test"
  }
}
