package kamon.examples.scala

import java.util.concurrent.TimeUnit

import kamon.Kamon
import kamon.trace.logging.MdcKeysSupport
import kamon.trace.{Tracer, TraceLocal}
import kamon.trace.TraceLocal.AvailableToMdc
import org.slf4j.{LoggerFactory, MDC}

object MDCSupport extends App with MdcKeysSupport {
  Kamon.start()

  case class Request(client:String)

  val logger = LoggerFactory.getLogger("Logger")

  // tag:what-is-mdc:start
def requestProcessor(request:Request):Unit = {
  MDC.put("X-ApplicationId", request.client)
  try {

    logger.info("processing Request...")

  } finally {
    MDC.remove("X-ApplicationId") //clean up the mdc
  }
}
  // tag:what-is-mdc:end
  requestProcessor(new Request("AwesomeClient"))

  // tag:kamon-way-mdc:start
  val ApplicationIdKey = AvailableToMdc("X-ApplicationId")

def tracedRequestProcessor(request:Request):Unit = {
  TraceLocal.store(ApplicationIdKey)(request.client)

  withMdc { // exposes all AvailableToMdc keys in the MDC.
    logger.info("processing Traced Request...")
  }
}
  // tag:kamon-way-mdc:end

  Tracer.withNewContext("MDC", autoFinish = true) {
      tracedRequestProcessor(new Request("AwesomeClient"))
  }

  TimeUnit.SECONDS.sleep(5)
  Kamon.shutdown()
}

/*
tag:what-is-mdc-output:start
16:51:49.605 [info] Logger AwesomeClient - processing Request...
tag:what-is-mdc-output:end
*/

/*
tag:kamon-way-mdc-output:start
18:20:27.118 [info] Logger AwesomeClient - processing Traced Request...
tag:kamon-way-mdc-output:end
*/
