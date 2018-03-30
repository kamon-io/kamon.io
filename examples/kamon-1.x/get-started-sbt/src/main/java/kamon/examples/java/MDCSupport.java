package kamon.examples.java;

import kamon.Kamon;
import kamon.trace.TraceLocal;
import kamon.trace.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import java.util.concurrent.TimeUnit;
import static kamon.trace.logging.MdcKeysSupport.*;

public class MDCSupport {

  static class Request {
    final String client;

    public Request(String client) {
      this.client = client;
    }
  }

  Logger logger = LoggerFactory.getLogger("Logger");

// tag:what-is-mdc:start
public void requestProcessor(Request request) {
  MDC.put("X-ApplicationId", request.client);
  try {

    logger.info("processing Request...");

  } finally {
    MDC.remove("X-ApplicationId"); //clean up the mdc
  }
}
// tag:what-is-mdc:end

// tag:kamon-way-mdc:start
public static final String ApplicationIdKey = "X-ApplicationId";

public void tracedRequestProcessor(Request request) {
  TraceLocal.storeForMdc(ApplicationIdKey, request.client);

  withMdc(() ->  // exposes all AvailableToMdc keys in the MDC.
    logger.info("processing Traced Request...");
  );
}

// tag:kamon-way-mdc:end

public static void main(String... args) throws InterruptedException {
    MDCSupport mdcSupport = new MDCSupport();
    mdcSupport.requestProcessor(new MDCSupport.Request("AwesomeClient"));

    Tracer.withNewContext("MDC", true, () -> {
        tracedRequestProcessor(new Request("AwesomeClient"));
        return "";
    });

    TimeUnit.SECONDS.sleep(5);
    Kamon.shutdown();
  }
}
