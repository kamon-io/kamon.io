package kamon.example

import kamon.Kamon
import kamon.prometheus.PrometheusReporter
import kamon.zipkin.ZipkinReporter

// tag:load-modules:start
object Start extends App {
  Kamon.addReporter(new PrometheusReporter())
  Kamon.addReporter(new ZipkinReporter())
}
// tag:load-modules:end
