package kamon.examples.java;

import com.google.common.collect.ImmutableMap;
import kamon.Kamon;
import kamon.metric.instrument.Counter;
import kamon.metric.instrument.Histogram;
import kamon.metric.instrument.MinMaxCounter;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;
import static kamon.util.JavaTags.tagsFromMap;
import kamon.examples.java.CreatingEntityRecorders.ActorMetrics;

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

    // tag:entity-metrics:start
    final ActorMetrics myManagedActorMetrics = Kamon.metrics().entity(ActorMetrics.Factory, "my-managed-actor");

    myManagedActorMetrics.mailboxSize().increment();
    myManagedActorMetrics.processingTime().record(42);
    myManagedActorMetrics.mailboxSize().decrement();
    // tag:entity-metrics:end


    // tag:cleanup:start
    Kamon.metrics().removeHistogram("my-histogram");
    Kamon.metrics().removeCounter("my-counter");
    Kamon.metrics().removeMinMaxCounter("my-mm-counter");

    // The histogram was created with tags and the same tags need to be
    // provided to correctly remove it.
    Kamon.metrics().removeHistogram("my-tagged-histogram", tagsFromMap(ImmutableMap.of("algorithm", "X")));

    // For entities you need to provide the entity category as well.
    Kamon.metrics().removeEntity("my-managed-actor", "akka-actor");
    // tag:cleanup:end

    Kamon.shutdown();
  }
}
