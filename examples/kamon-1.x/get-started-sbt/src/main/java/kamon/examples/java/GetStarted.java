package kamon.examples.java;

import kamon.Kamon;
import kamon.metric.Counter;
import kamon.metric.Histogram;
import kamon.prometheus.PrometheusReporter;
import kamon.trace.Span;
import kamon.zipkin.ZipkinReporter;


public class GetStarted {
  public static void main(String[] args) {
    // tag:get-started-metrics:start
    final Histogram myHistogram = Kamon.histogram("my.histogram");
    final Counter myCounter = Kamon.counter("my.counter");
    final Counter myTaggedCounter = Kamon.counter("my.tagged.counter").refine("env", "test");

    myHistogram.record(42);
    myHistogram.record(50);
    myCounter.increment();
    // tag:get-started-metrics:end

    // tag:get-started-spans:start
    final Span span = Kamon.buildSpan("my.operation").start();
    // Do some work here
    span
      .tag("key", "value")
      .finish();
    // tag:get-started-spans:end

    // tag:get-started-reporters:start
    Kamon.addReporter(new PrometheusReporter());
    Kamon.addReporter(new ZipkinReporter());
    // tag:get-started-reporters:end
  }
}

