package kamon.examples.scala

import java.util.concurrent.Executors

import kamon.Kamon
import kamon.metric.Entity
import kamon.util.executors.ExecutorServiceMetrics


case class HeavyWeightTask() extends Runnable {
  override def run(): Unit = {
    Thread.sleep(100)
  }
}

object ExecutorMetrics extends App {
  Kamon.start()
  //tag:executor-metrics:start

val forkJoinPool = Executors.newWorkStealingPool()
val fixedThreadPool = Executors.newFixedThreadPool(10)

val forkJoinEntity = ExecutorServiceMetrics.register("java-fork-join-pool", forkJoinPool)
val threadPoolEntity = ExecutorServiceMetrics.register("fixed-thread-pool", fixedThreadPool)

for (_ <- 0 to 100) {
  forkJoinPool.submit(HeavyWeightTask())
  fixedThreadPool.submit(HeavyWeightTask())
}

ExecutorServiceMetrics.remove(forkJoinEntity)
ExecutorServiceMetrics.remove(threadPoolEntity)

forkJoinPool.shutdown()
fixedThreadPool.shutdown()

  //tag:executor-metrics:end
  forkJoinPool.shutdown()
  fixedThreadPool.shutdown()
}

/*
tag:executor-metrics-output:start
+------------------------------------------------------------------------------------------------+
|  Thread-Pool-Executor                                                                          |
|                                                                                                |
|  Name: fixed-thread-pool                                                                       |
|                                                                                                |
|  Core Pool Size: 10                                                                            |
|  Max  Pool Size: 10                                                                            |
|                                                                                                |
|                                                                                                |
|                         Pool Size        Active Threads          Processed Task                |
|           Min              10                  0                      0                        |
|           Avg              10.0                1.0                    0.0                      |
|           Max              10                  10                     0                        |
|                                                                                                |
+------------------------------------------------------------------------------------------------+

+------------------------------------------------------------------------------------------------+
|  Fork-Join-Pool                                                                                |
|                                                                                                |
|  Name: fork-join-pool                                                                          |
|                                                                                                |
|  Paralellism: 4                                                                                |
|                                                                                                |
|                 Pool Size       Active Threads     Running Threads     Queue Task Count        |
|      Min           2                 0                   0                   0                 |
|      Avg           3.0               0.0                 1.0                 0.0               |
|      Max           4                 0                   4                   0                 |
|                                                                                                |
+------------------------------------------------------------------------------------------------+
tag:executor-metrics-output:end
 */
