package kamon.examples.java;

import kamon.Kamon;
import kamon.metric.instrument.Counter;
import kamon.metric.instrument.Histogram;

public class UserMetrics {
  public static void main(String[] args) {
    final Kamon kamon = Kamon.create();

    final Histogram myHistogram = kamon.userMetrics().histogram("my-histogram");
    myHistogram.record(42);
    myHistogram.record(43);
    myHistogram.record(44);

    final Counter myCounter = kamon.userMetrics().counter("my-counter");
    myCounter.increment();
    myCounter.increment(17);

    kamon.shutdown();
  }
}
