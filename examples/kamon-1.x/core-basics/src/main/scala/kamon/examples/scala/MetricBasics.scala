package kamon.examples.scala

import kamon.Kamon
import kamon.metric.MeasurementUnit.{information, time}

object MetricBasics extends App {

  // tag:creating-metrics:start
  // Simple metrics can be one-liners, without refining (tagging)
  Kamon.counter("app.orders.sent").increment() // (1)


  // If refining (tagging) is needed, define a metric first;
  // here is a metric called "app.error" with a counter instrument.
  val errors = Kamon.counter("app.error") // (2)

  // Refine the metric with a specific set of tags
  val invalidUserErrors = errors.refine("class" -> "InvalidUser") // (3)
  val invalidPassErrors = errors.refine("class" -> "InvalidPassword") // (4)

  invalidPassErrors.increment() // (5)

  // Removing a metric from Kamon
  errors.remove("class" -> "InvalidUser") // (6)
  // tag:creating-metrics:end


  // tag:working-with-counters:start
  // One-liner
  Kamon.counter("app.orders.sent").increment()

  // Fully defined and refined (tagged) counter
  val sentBytes = Kamon.counter("network.traffic", information.bytes)
    .refine(
      "direction" -> "out",
      "interface" -> "eth0")

  sentBytes.increment()
  sentBytes.increment(512)
  // tag:working-with-counters:end

  // tag:working-with-gauges:start
  // One-liner
  Kamon.gauge("users").set(42)

  // Fully defined and refined (tagged) gauge
  val onlineUsers = Kamon.gauge("users")
    .refine("status" -> "online")

  onlineUsers.set(42)
  onlineUsers.decrement()
  onlineUsers.decrement(4)
  onlineUsers.increment()
  onlineUsers.increment(7)
  // tag:working-with-gauges:end

  // tag:working-with-histograms:start
  // One-liner
  Kamon.histogram("messaging.payload-size").record(512)

  // Fully defined and refined (tagged) histogram
  val responseSizes = Kamon.histogram("http.response-size", information.bytes)
    .refine("status-code" -> "200")

  responseSizes.record(2048)
  // tag:working-with-histograms:end

  // tag:working-with-timers:start
  // One-liner
  val startedTimer = Kamon.timer("operations").start()
  // do some work
  startedTimer.stop()

  // Fully defined and refined (tagged) timer
  val operationLatency = Kamon.timer("operation-latency")
    .refine("operation" -> "login")

  val timer = operationLatency.start()
  // do some work
  timer.stop()
  // tag:working-with-timers:end


  // tag:working-with-range-samplers:start
  // One-liner
  Kamon.rangeSampler("http.in-flight").increment()


  // Fully defined and refined (tagged) timer
  val mailboxSize = Kamon.rangeSampler("actor.mailbox-size")
    .refine("actor" -> "user/test-actor")

  mailboxSize.increment()
  mailboxSize.increment(12)
  mailboxSize.decrement(12)
  mailboxSize.decrement()

  // tag:working-with-range-samplers:end
}
