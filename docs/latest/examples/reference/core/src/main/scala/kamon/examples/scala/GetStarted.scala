package kamon.examples.scala

import kamon.Kamon
import kamon.prometheus.PrometheusReporter
import kamon.zipkin.ZipkinReporter

object GetStarted extends App {
  // tag:get-started-metrics:start
  val myHistogram = Kamon.histogram("my.histogram").withoutTags()
  val myCounter = Kamon.counter("my.counter").withoutTags()
  val myTaggedCounter = Kamon.counter("my.tagged.counter").withTag("env", "test")

  myHistogram.record(42)
  myHistogram.record(50)
  myCounter.increment()
  myTaggedCounter.increment()
  // tag:get-started-metrics:end


  // tag:get-started-spans:start
  val span = Kamon.spanBuilder("my.operation").start()
  // Do some work here
  span
    .tag("key", "value")
    .finish()
  // tag:get-started-spans:end

  // tag:get-started-reporters:start
  Kamon.registerModule("Foo", new PrometheusReporter())
  Kamon.registerModule("Bar", new ZipkinReporter())
  // tag:get-started-reporters:end
}
