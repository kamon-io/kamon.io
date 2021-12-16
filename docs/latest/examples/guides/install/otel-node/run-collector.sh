//tag:run-collector:start
docker run \
  -p 14268:14268 \
  -p 55680-55681:55680-55681 \
  -v $(pwd)/otel_collector_config.yaml:/etc/otel/config.yaml \
  otel/opentelemetry-collector
//tag:run-collector:end
