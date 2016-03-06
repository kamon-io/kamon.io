package kamon.examples.java;

import kamon.Kamon;
import kamon.examples.scala.HeavyWeightTask;
import kamon.util.executors.ExecutorServiceMetrics;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorMetrics {
    public static void main(String... args) {
        Kamon.start();

//tag:executor-metrics:start
final ExecutorService forkJoinPool = Executors.newWorkStealingPool();
final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

ExecutorServiceMetrics.register("fork-join-pool", forkJoinPool);
ExecutorServiceMetrics.register("fixed-thread-pool", fixedThreadPool);

for(int i = 0; i < 100; i++) {
    forkJoinPool.submit(new HeavyWeightTask());
    fixedThreadPool.submit(new HeavyWeightTask());
}

ExecutorServiceMetrics.remove(forkJoinPool);
ExecutorServiceMetrics.remove(fixedThreadPool);

forkJoinPool.shutdown();
fixedThreadPool.shutdown();

//tag:executor-metrics:end
        forkJoinPool.shutdown();
        fixedThreadPool.shutdown();
    }
}
