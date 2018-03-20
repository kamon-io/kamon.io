package kamon.examples.scala

import kamon.Kamon
import kamon.prometheus.PrometheusReporter
import kamon.zipkin.ZipkinReporter

object GetStarted extends App {
  // tag:get-started-metrics:start
  val myHistogram = Kamon.histogram("my.histogram")
  val myCounter = Kamon.counter("my.counter")
  val myTaggedCounter = Kamon.counter("my.tagged.counter").refine("env" -> "test")

  myHistogram.record(42)
  myHistogram.record(50)
  myCounter.increment()
  myTaggedCounter.increment()
  // tag:get-started-metrics:end


  // tag:get-started-spans:start
  val span = Kamon.buildSpan("my.operation").start()
  // Do some work here
  span
    .tag("key", "value")
    .finish()
  // tag:get-started-spans:end

  // tag:get-started-reporters:start
  Kamon.addReporter(new PrometheusReporter())
  Kamon.addReporter(new ZipkinReporter())
  // tag:get-started-reporters:end
}
