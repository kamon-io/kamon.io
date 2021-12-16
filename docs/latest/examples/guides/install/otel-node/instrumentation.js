// tag:instrumentation:start
const { diag, DiagConsoleLogger, DiagLogLevel } = require("@opentelemetry/api");
const { NodeTracerProvider } = require("@opentelemetry/node");
const { Resource } = require("@opentelemetry/resources");
const { ResourceAttributes } = require("@opentelemetry/semantic-conventions");
const { SimpleSpanProcessor } = require("@opentelemetry/tracing");
const { registerInstrumentations } = require("@opentelemetry/instrumentation");
const { HttpInstrumentation } = require("@opentelemetry/instrumentation-http");
const { ExpressInstrumentation } = require("@opentelemetry/instrumentation-express");
const { CollectorTraceExporter } = require("@opentelemetry/exporter-collector-proto");
const os = require("os");

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
