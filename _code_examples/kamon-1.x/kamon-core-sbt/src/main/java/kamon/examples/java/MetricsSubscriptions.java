package kamon.examples.java;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.google.common.collect.Maps;
import kamon.Kamon;
import kamon.metric.Entity;
import kamon.metric.EntitySnapshot;
import kamon.metric.SubscriptionsDispatcher.TickMetricSnapshot;
import kamon.metric.TraceMetrics;
import kamon.metric.instrument.Counter;
import kamon.metric.instrument.Histogram;

import java.util.Map;

import static scala.collection.JavaConversions.mapAsJavaMap;

public class MetricsSubscriptions {
  public static void main(String[] args) throws InterruptedException {
    Kamon.start();

    final ActorSystem mySystem = ActorSystem.create("my-system");
    final ActorRef subscriber = mySystem.actorOf(Props.create(SimplePrinter.class), "subscriber");

    final TraceMetrics traceRecorder = Kamon.metrics().entity(TraceMetrics.factory(), "test-trace");
    traceRecorder.elapsedTime().record(500);
    traceRecorder.elapsedTime().record(600);

    // tag:metrics-subscriptions:start
    Kamon.metrics().subscribe("trace", "test-trace", subscriber);
    Kamon.metrics().subscribe("trace", "test-*", subscriber);
    Kamon.metrics().subscribe("trace", "**", subscriber);
    // tag:metrics-subscriptions:end


    // Register a couple histograms
    final Histogram testHistogram = Kamon.metrics().histogram("test-histogram");
    final Histogram otherHistogram = Kamon.metrics().histogram("other-histogram");

    for(int i = 0; i < 100; i++) {
      testHistogram.record(i);
      otherHistogram.record(i);
    }

    // Register a couple counters as well
    Kamon.metrics().counter("first-counter").increment();
    Kamon.metrics().counter("second-counter").increment(42);

    // tag:custom-subscriptions:start
    Kamon.metrics().subscribe("counter", "**", subscriber);
    Kamon.metrics().subscribe("histogram", "test-histogram", subscriber);
    // tag:custom-subscriptions:end



    // Wait a bit for the snapshot to be flushed.
    Thread.sleep(15000);

    Kamon.shutdown();
  }


  // tag:subscriber:start
  public static class SimplePrinter extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
      if(message instanceof TickMetricSnapshot) {
        final TickMetricSnapshot tickSnapshot = (TickMetricSnapshot) message;
        final Map<Entity, EntitySnapshot> counters = filterCategory("counter", tickSnapshot);
        final Map<Entity, EntitySnapshot> histograms = filterCategory("histogram", tickSnapshot);

        System.out.println("#################################################");
        System.out.println("From: " + tickSnapshot.from());
        System.out.println("To: " + tickSnapshot.to());

        counters.forEach((e, s) -> {
          final Counter.Snapshot counterSnapshot = s.counter("counter").get();
          System.out.println(String.format("Counter [%s] was incremented [%d] times.", e.name(), counterSnapshot.count()));
        });

        histograms.forEach((e, s) -> {
          final Histogram.Snapshot histogramSnapshot = s.histogram("histogram").get();
          System.out.println(String.format("Histogram [%s] has [%d] recordings.", e.name(), histogramSnapshot.numberOfMeasurements()));
        });


      } else unhandled(message);
    }

    private Map<Entity, EntitySnapshot> filterCategory(String categoryName, TickMetricSnapshot snapshot) {
      return Maps.filterKeys(mapAsJavaMap(snapshot.metrics()), (e) -> e.category().equals(categoryName));
    }
  }
  // tag:subscriber:end
}
