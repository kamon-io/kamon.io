package kamon.examples.java;

import kamon.Kamon;
import kamon.metric.instrument.Counter;
import kamon.metric.instrument.Histogram;

// tag:get-started:start
public class GetStarted {
  public static void main(String[] args) {
    Kamon.start();

    final Histogram someHistogram = Kamon.simpleMetrics().histogram("some-histogram");
    final Counter someCounter = Kamon.simpleMetrics().counter("some-counter");

    someHistogram.record(42);
    someHistogram.record(50);
    someCounter.increment();

    // This application wont terminate unless you shutdown Kamon.
    Kamon.shutdown();
  }
}
// tag:get-started:end
