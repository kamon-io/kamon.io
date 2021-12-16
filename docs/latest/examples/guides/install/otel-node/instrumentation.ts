// tag:instrumentation:start
import { diag, DiagConsoleLogger, DiagLogLevel } from "@opentelemetry/api";
import { NodeTracerProvider } from "@opentelemetry/node";
import { Resource } from "@opentelemetry/resources";
import { ResourceAttributes } from "@opentelemetry/semantic-conventions";
import { SimpleSpanProcessor } from "@opentelemetry/tracing";
import { registerInstrumentations } from "@opentelemetry/instrumentation";
import { HttpInstrumentation } from "@opentelemetry/instrumentation-http";
import { ExpressInstrumentation } from "@opentelemetry/instrumentation-express";
import { CollectorTraceExporter } from "@opentelemetry/exporter-collector-proto";
import os from "os";

// Change to the service name you wish to use
const serviceName = "my-otel-service";
const host = os.hostname();

const provider = new NodeTracerProvider({
  resource: new Resource({
    [ResourceAttributes.SERVICE_NAME]: serviceName,
    [ResourceAttributes.HOST_NAME]: host,
  })
});

// Adjust log level when running in production
diag.setLogger(new DiagConsoleLogger(), DiagLogLevel.ALL);

provider.addSpanProcessor(
  new SimpleSpanProcessor(
    new CollectorTraceExporter({
      headers: {
        // Enter your API key here
        "x-kamon-apikey": "YOUR_API_KEY",
      },
      url: "https://otel.apm.kamon.io/v1/traces",
      serviceName,
    }),
  ),
);

provider.register();

registerInstrumentations({
  instrumentations: [
    new ExpressInstrumentation(),
    new HttpInstrumentation(),
  ],
});
// tag:instrumentation:end
