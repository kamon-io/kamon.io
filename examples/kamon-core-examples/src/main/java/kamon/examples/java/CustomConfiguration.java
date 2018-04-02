package kamon.examples.java;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import kamon.Kamon;

// tag:custom-configuration:start
public class CustomConfiguration {
  public static void main(String[] args) {
    final Config customConfig = ConfigFactory.load("custom-config");
    final Config codeConfig = ConfigFactory.parseString(
      "kamon.metric.track-unmatched-entities = no");

    Kamon.start(codeConfig.withFallback(customConfig));
    // Kamon gets initialized with the provided configuration rather
    // than calling ConfigFactory.load() itself.


    // This application wont terminate unless you shutdown Kamon.
    Kamon.shutdown();
  }
}
// tag:custom-configuration:end
