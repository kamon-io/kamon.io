package kamon.example;

import kamon.Kamon;
import kamon.prometheus.PrometheusReporter;
import kamon.zipkin.ZipkinReporter;

public class Start {

  // tag:load-modules:start
  public static void main(String[] args) {
    Kamon.addReporter(new PrometheusReporter());
    Kamon.addReporter(new ZipkinReporter());
  }
  // tag:load-modules:end
}
