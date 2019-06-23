package kamon.example

import kamon.Kamon
import kamon.prometheus.PrometheusReporter
import kamon.zipkin.ZipkinReporter

// tag:load-modules:start
fun main(args: Array<String>) {
  Kamon.addReporter(PrometheusReporter())
  Kamon.addReporter(ZipkinReporter())
}
// tag:load-modules:end