package kamon.examples.java;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import kamon.Kamon;
import kamon.trace.SegmentInfo;
import kamon.trace.TraceInfo;
import kamon.trace.Tracer;

import java.util.List;
import static scala.collection.JavaConversions.seqAsJavaList;

public class TracesSubscriptions {
  public static void main(String[] args) throws InterruptedException {
    Kamon.start();

    final ActorSystem mySystem = ActorSystem.create("my-system");
    final ActorRef subscriber = mySystem.actorOf(Props.create(TracePrinter.class), "subscriber");

    // tag:subscribe-to-traces:start
    Kamon.tracer().subscribe(subscriber);
    // tag:subscribe-to-traces:end


    // tag:create-some-traces-and-segments:start
    Tracer.withNewContext("trace-with-segments", true, () -> {
      Tracer.currentContext().withNewSegment("quick-segment", "code", "kamon", () -> {
        try{ Thread.sleep(100); } catch (Exception e) { e.printStackTrace(); }
        return 0;
      });

      Tracer.currentContext().withNewSegment("slow-segment", "code", "kamon", () -> {
        try{ Thread.sleep(3000); } catch (Exception e) { e.printStackTrace(); }
        return 0;
      });

      return "done";
    });
    // tag:create-some-traces-and-segments:end



    // Wait a bit for the snapshot to be flushed.
    Thread.sleep(15000);

    Kamon.shutdown();
  }


  // tag:subscriber:start
  public static class TracePrinter extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
      if(message instanceof TraceInfo) {
        final TraceInfo traceInfo = (TraceInfo) message;
        final List<SegmentInfo> segments = seqAsJavaList(traceInfo.segments());

        System.out.println("#################################################");
        System.out.println("Trace Name: " + traceInfo.name());
        System.out.println("Timestamp: " + traceInfo.timestamp());
        System.out.println("Elapsed Time: " + traceInfo.elapsedTime());
        System.out.println("Segments: ");

        segments.forEach(segmentInfo -> {
          System.out.println("    ------------------------------------------");
          System.out.println("    Name: " + segmentInfo.name());
          System.out.println("    Category: " + segmentInfo.category());
          System.out.println("    Library: " + segmentInfo.library());
          System.out.println("    Timestamp: " + segmentInfo.timestamp());
          System.out.println("    Elapsed Time: " + segmentInfo.elapsedTime());
        });

      } else unhandled(message);
    }
  }
  // tag:subscriber:end
}
