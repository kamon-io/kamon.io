// tag:instruments:start
trait KamonSupport {
  def counter(name: String) = Kamon.metrics.counter(name)
  def minMaxCounter(name: String) = Kamon.metrics.minMaxCounter(name)
  def histogram(name: String) = Kamon.metrics.histogram(name)
  def gauge[A](name: String)(thunk: => Long) = Kamon.metrics.gauge(name)(thunk)
  def time[A](name: String)(thunk: => A) = Latency.measure(Kamon.metrics.histogram(name))(thunk)
  def traceFuture[A](name:String)(future: => Future[A]):Future[A] = Tracer.withContext(Kamon.tracer.newContext(name)) {
    future.map(f => Tracer.currentContext.finish())(SameThreadExecutionContext)
    future
  }
}
// tag:instruments:end
