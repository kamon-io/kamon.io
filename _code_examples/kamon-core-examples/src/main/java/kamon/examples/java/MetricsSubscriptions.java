package kamon.examples.java;


import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import kamon.Kamon;
import kamon.metric.SubscriptionsDispatcher.TickMetricSnapshot;
import kamon.metric.TraceMetrics;

public class MetricsSubscriptions {
  // tag:metrics-subscriptions:start
  public static void main(String[] args) {
    final Kamon kamon = Kamon.create();
    final ActorRef subscriber = kamon.actorSystem().actorOf(Props.create(SimplePrinter.class), "subscriber");

    final TraceMetrics traceRecorder = kamon.metrics().<TraceMetrics>register(TraceMetrics.factory(), "test-trace").get().recorder();
    traceRecorder.ElapsedTime().record(500);
    traceRecorder.ElapsedTime().record(600);

    kamon.metrics().subscribe("trace", "test-trace", subscriber);
    kamon.metrics().subscribe("trace", "test-*", subscriber);
    kamon.metrics().subscribe("trace", "**", subscriber);

    kamon.shutdown();
  }
  // tag:metrics-subscriptions:end

  public static class SimplePrinter extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
      if(message instanceof TickMetricSnapshot)
        System.out.println(message);
      else
        unhandled(message);
    }
  }
}
