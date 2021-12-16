//tag:instrumented-flask-app:start
import flask
import requests

from opentelemetry import trace
from opentelemetry.exporter.otlp.proto.http.trace_exporter import OTLPSpanExporter
from opentelemetry.instrumentation.flask import FlaskInstrumentor
from opentelemetry.instrumentation.requests import RequestsInstrumentor
from opentelemetry.sdk.resources import HOST_NAME, SERVICE_NAME, Resource
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor

# Rename service and host to the appropriate names
trace.set_tracer_provider(TracerProvider(
  resource=Resource.create({ SERVICE_NAME: "my-otel-flask-service", HOST_NAME: "local" })
))

trace.get_tracer_provider().add_span_processor(
  BatchSpanProcessor(
    OTLPSpanExporter(
      endpoint="https://otel.apm.kamon.io/v1/traces",
      # Enter your API key here
      headers=(("x-kamon-apikey", "YOUR_API_KEY"),)
    )
  )
)

app = flask.Flask(__name__)

FlaskInstrumentor().instrument_app(app)
RequestsInstrumentor().instrument()

@app.route("/db-status")
def db_status():
  return "ok"

@app.route("/app-status")
def app_status():
  return "ok"

@app.route("/status")
def status():
  tracer = trace.get_tracer(__name__)
  with tracer.start_as_current_span("status-handler"):
    with tracer.start_as_current_span("db-status"):
      db_status = requests.get("http://localhost:5000/db-status").text
    with tracer.start_as_current_span("app-status"):
      app_status = requests.get("http://localhost:5000/app-status").text
    return f"(db {db_status}, app {app_status})"

# Debug flag for development mode only
app.run(debug=True, port=5000)
//tag:instrumented-flask-app:end
