package kamon.examples.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

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

  public static void main(String... args) {
    MDCSupport mdcSupport = new MDCSupport();
    mdcSupport.requestProcessor(new MDCSupport.Request("AwesomeClient"));
  }
}