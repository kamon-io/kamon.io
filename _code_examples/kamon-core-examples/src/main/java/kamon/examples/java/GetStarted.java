package kamon.examples.java;

import kamon.Kamon;
import kamon.metric.instrument.Counter;
import kamon.metric.instrument.Histogram;

// tag:get-started:start
public class GetStarted {
  public static void main(String[] args) {
    final Kamon kamon = Kamon.create();
    final Histogram someHistogram = kamon.userMetrics().histogram("some-histogram");
    final Counter someCounter = kamon.userMetrics().counter("some-counter");

    someHistogram.record(42);
    someHistogram.record(50);
    someCounter.increment();

    // This application wont terminate unless you shutdown Kamon.
    kamon.shutdown();
  }
}
// tag:get-started:end
