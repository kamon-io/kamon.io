package kamon.examples.java;


import kamon.Kamon;
import kamon.trace.Segment;
import kamon.trace.TraceContext;
import kamon.trace.Tracer;
import scala.Some;

public class TraceContextManipulation {
  public static void main(String[] args) throws InterruptedException {
    Kamon.start();

    // tag:creating-a-trace-context:start
    final TraceContext newContext = Kamon.tracer().newContext("test-trace");

    Thread.sleep(3000);
    newContext.finish();
    // tag:creating-a-trace-context:end

    // tag:new-context:start
    final TraceContext testTrace = Kamon.tracer().newContext("test-trace");
    final TraceContext testTraceWithToken = Kamon.tracer().newContext("trace-with-token", Some.apply("token-42"));

    // You can rename a trace before it is finished.
    testTrace.rename("cool-functinality-X");
    testTrace.finish();

    // And you can also access it's token if you want to.
    System.out.println("Test Trace Token: " + testTrace.token());
    // tag:new-context:end

    // tag:new-context-block:start
    Tracer.withNewContext("GetUserDetails", true, ()-> {
      // While this block of codes executes a new TraceContext
      // is set as the current context and finished after the
      // block returns.
      System.out.println("Current Trace Token: " + Tracer.currentContext().token());

      return "Some awesome result";
    });

    // No TraceContext is present when you reach this point.
    // tag:new-context-block:end


    // tag:storing-the-trace-context:start
    final TraceContext context = Kamon.tracer().newContext("example-trace");

    Tracer.withContext(context, () -> {
      // While this code executes, `context` is the current
      // TraceContext.
      System.out.println("Current Trace Token: " + Tracer.currentContext().token());

      return "Some awesome result";
    });
    // tag:storing-the-trace-context:end


    // tag:creating-segments:start
    Tracer.withNewContext("trace-with-segments", true, () -> {
      final Segment segment = Tracer.currentContext().startSegment("some-cool-section", "business-logic", "kamon");
      // Some application code here.
      segment.finish();

      return "done";
    });
    // tag:creating-segments:end


    // tag:managed-segments:start
    Tracer.withNewContext("trace-with-segments", true, () -> {

      Tracer.currentContext().withNewSegment("some-cool-section", "business-logic", "kamon", () -> {
        // Here is where the segment does it's job.

        return 0;
      });

      return "done";
    });
    // tag:managed-segments:end

  }
}