package kamon.examples.java;

import com.google.common.collect.ImmutableMap;
import kamon.Kamon;
import kamon.metric.instrument.Counter;
import kamon.metric.instrument.Histogram;
import kamon.metric.instrument.MinMaxCounter;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;
import static kamon.util.JavaTags.tagsFromMap;

public class RecordingMetrics {
  public static void main(String[] args) {
    Kamon.start();

    // tag:simple-metrics:start
    final Histogram myHistogram = Kamon.metrics().histogram("my-histogram");
    myHistogram.record(42);
    myHistogram.record(43);
    myHistogram.record(44);

    final Counter myCounter = Kamon.metrics().counter("my-counter");
    myCounter.increment();
    myCounter.increment(17);

    final MinMaxCounter myMMCounter = Kamon.metrics().minMaxCounter("my-mm-counter", new FiniteDuration(500, TimeUnit.MILLISECONDS));
    myMMCounter.increment();
    myMMCounter.decrement();

    final Histogram myTaggedHistogram = Kamon.metrics().histogram("my-tagged-histogram", tagsFromMap(ImmutableMap.of("algorithm", "X")));
    myTaggedHistogram.record(700L);
    myTaggedHistogram.record(800L);

    // kamon.util.JavaTags.tagsFromMap can be statically imported
    // to easily transform a java.util.Map<String, String> into a
    // scala.collection.immutable.Map[String, String] as expected
    // by the metrics module API.


    // tag:simple-metrics:end


    Kamon.metrics().entity(TraceMetrics)

    Kamon.shutdown();
  }
}
