package kamon.examples.java;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import kamon.Kamon;
import kamon.trace.Tracer;

public class TraceTokenLogging {
  public static void main(String[] args) throws InterruptedException {
    Kamon.start();

    final ActorSystem system = ActorSystem.create("trace-token-logging");
    final ActorRef upperCaser = system.actorOf(Props.create(UpperCaser.class), "upper-caser");

    // Send five messages without a TraceContext
    for(int i = 0; i < 5; i++) {
      upperCaser.tell("Hello without context", ActorRef.noSender());
    }

    // Wait a bit to avoid spaghetti logs.
    Thread.sleep(1000);


    // tag:sending-async-events:start
    // Send five messages with a TraceContext
    for(int i = 0; i < 5; i++) {
      Tracer.withNewContext("simple-test", () -> {
          upperCaser.tell("Hello without context", ActorRef.noSender());
          return null;
      });
    }
    // tag:sending-async-events:end


    // Wait a bit for everything to be logged and shutdown.
    Thread.sleep(2000);
    system.shutdown();
  }


// tag:trace-token-logging:start
  public static class UpperCaser extends UntypedActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private final ActorRef lengthCalculator = context().actorOf(Props.create(LengthCalculator.class), "length-calculator");

    @Override
    public void onReceive(Object message) throws Exception {
      if(message instanceof String) {
        log.info("Upper casing [{}]", message);
        lengthCalculator.forward(((String) message).toUpperCase(), getContext());
      }
    }
  }

  public static class LengthCalculator extends UntypedActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
      if(message instanceof String) {
        log.info("Calculating the length of: [{}]", message);
      }
    }
  }
// tag:trace-token-logging:end
}



/*
// tag:trace-token-logging-output-without-token:start

Example output without logging the trace token:

06:59:42.538 INFO  [undefined][akka://trace-token-logging/user/upper-caser] kamon.examples.java.TraceTokenLogging$UpperCaser - Upper casing [Hello without context]
06:59:42.541 INFO  [undefined][akka://trace-token-logging/user/upper-caser] kamon.examples.java.TraceTokenLogging$UpperCaser - Upper casing [Hello without context]
06:59:42.542 INFO  [undefined][akka://trace-token-logging/user/upper-caser/length-calculator] kamon.examples.java.TraceTokenLogging$LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]
06:59:42.543 INFO  [undefined][akka://trace-token-logging/user/upper-caser/length-calculator] kamon.examples.java.TraceTokenLogging$LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]
06:59:42.544 INFO  [undefined][akka://trace-token-logging/user/upper-caser] kamon.examples.java.TraceTokenLogging$UpperCaser - Upper casing [Hello without context]
06:59:42.545 INFO  [undefined][akka://trace-token-logging/user/upper-caser/length-calculator] kamon.examples.java.TraceTokenLogging$LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]
06:59:42.545 INFO  [undefined][akka://trace-token-logging/user/upper-caser] kamon.examples.java.TraceTokenLogging$UpperCaser - Upper casing [Hello without context]
06:59:42.545 INFO  [undefined][akka://trace-token-logging/user/upper-caser] kamon.examples.java.TraceTokenLogging$UpperCaser - Upper casing [Hello without context]
06:59:42.546 INFO  [undefined][akka://trace-token-logging/user/upper-caser/length-calculator] kamon.examples.java.TraceTokenLogging$LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]
06:59:42.546 INFO  [undefined][akka://trace-token-logging/user/upper-caser/length-calculator] kamon.examples.java.TraceTokenLogging$LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]

// tag:trace-token-logging-output-without-token:end
// tag:trace-token-logging-output-with-token:start

Example output logging the trace token:

06:59:43.533 INFO  [ivantopo-desktop-1][akka://trace-token-logging/user/upper-caser] kamon.examples.java.TraceTokenLogging$UpperCaser - Upper casing [Hello without context]
06:59:43.533 INFO  [ivantopo-desktop-1][akka://trace-token-logging/user/upper-caser/length-calculator] kamon.examples.java.TraceTokenLogging$LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]
06:59:43.534 INFO  [ivantopo-desktop-2][akka://trace-token-logging/user/upper-caser] kamon.examples.java.TraceTokenLogging$UpperCaser - Upper casing [Hello without context]
06:59:43.534 INFO  [ivantopo-desktop-3][akka://trace-token-logging/user/upper-caser] kamon.examples.java.TraceTokenLogging$UpperCaser - Upper casing [Hello without context]
06:59:43.534 INFO  [ivantopo-desktop-2][akka://trace-token-logging/user/upper-caser/length-calculator] kamon.examples.java.TraceTokenLogging$LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]
06:59:43.534 INFO  [ivantopo-desktop-3][akka://trace-token-logging/user/upper-caser/length-calculator] kamon.examples.java.TraceTokenLogging$LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]
06:59:43.534 INFO  [ivantopo-desktop-4][akka://trace-token-logging/user/upper-caser] kamon.examples.java.TraceTokenLogging$UpperCaser - Upper casing [Hello without context]
06:59:43.535 INFO  [ivantopo-desktop-4][akka://trace-token-logging/user/upper-caser/length-calculator] kamon.examples.java.TraceTokenLogging$LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]
06:59:43.535 INFO  [ivantopo-desktop-5][akka://trace-token-logging/user/upper-caser] kamon.examples.java.TraceTokenLogging$UpperCaser - Upper casing [Hello without context]
06:59:43.535 INFO  [ivantopo-desktop-5][akka://trace-token-logging/user/upper-caser/length-calculator] kamon.examples.java.TraceTokenLogging$LengthCalculator - Calculating the length of: [HELLO WITHOUT CONTEXT]

// tag:trace-token-logging-output-with-token:end */
