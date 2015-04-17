package kamon.examples.java;


import kamon.Kamon;
import kamon.trace.TraceContext;

public class TraceContextManipulation {
  public static void main(String[] args) throws InterruptedException {
    Kamon.start();

    // tag:creating-a-trace-context:start
    final TraceContext newContext = Kamon.tracer().newContext("test-trace");

    Thread.sleep(3000);
    newContext.finish();
    // tag:creating-a-trace-context:end
  }
}