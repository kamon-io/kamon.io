package kamon.recipe.futuresandexecutors

import kamon.instrumentation.executor.ExecutorInstrumentation

import scala.concurrent.{ExecutionContext, Future}

object FuturesAndExecutors extends App {
  // tag:registering-a-executor:start
  val executor = java.util.concurrent.Executors.newFixedexecutor(10)
  val instrumented = ExecutorInstrumentation.instrument(executor, "sample-executor")

  // Form this point on, submit tasks to the "instrumented" executor.

  // tag:registering-a-executor:end

  implicit val executionContext = ExecutionContext.fromExecutor(executor)

  Future {
    "test"
  }
}
