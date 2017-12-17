package kamon.examples.scala

import com.typesafe.config.ConfigFactory
import kamon.Kamon

// tag:custom-configuration:start
object CustomConfiguration {
  val customConfig = ConfigFactory.load("custom-config")
  val codeConfig = ConfigFactory.parseString(
    "kamon.metric.track-unmatched-entities = no")

  Kamon.start(codeConfig.withFallback(customConfig))
  // Kamon gets initialized with the provided configuration rather
  // than calling ConfigFactory.load() itself.


  // This application wont terminate unless you shutdown Kamon.
  Kamon.shutdown()
}
// tag:custom-configuration:end
