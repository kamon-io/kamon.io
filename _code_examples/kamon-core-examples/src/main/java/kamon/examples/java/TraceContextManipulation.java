package kamon.examples.java;


import kamon.Kamon;
import kamon.trace.TraceContext;

public class TraceContextManipulation {
  public static void main(String[] args) {
    Kamon.start();

    // tag:creating-a-trace-context:start
    final TraceContext newContext = Kamon.tracer().newContext("test-trace");
    final TraceContext contextWithCustomToken = Kamon.tracer().newContext("test-trace", "token-1234");
    // tag:creating-a-trace-context:end
  }
}