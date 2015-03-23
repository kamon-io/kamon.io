scalaVersion := "2.11.5"

// tag:base-kamon-dependencies:start
libraryDependencies ++= Seq(
  "io.kamon" % "kamon-core" % "0.3.6-cd300053cfec39dc75c1ea47b08ab5c78fe3f4bb",

  // [Optional]
  "io.kamon" % "kamon-statsd" % "0.3.6-cd300053cfec39dc75c1ea47b08ab5c78fe3f4bb",
  // [Optional]
  "io.kamon" % "kamon-datadog" % "0.3.6-cd300053cfec39dc75c1ea47b08ab5c78fe3f4bb"

  // ... and so on with all the modules you need.
)
// tag:base-kamon-dependencies:end

// Additional dependencies.
libraryDependencies ++= Seq(
  "ch.qos.logback"    %  "logback-classic" % "1.1.2",
  "com.typesafe.akka" %% "akka-actor"      % "2.3.9",
  "com.typesafe.akka" %% "akka-slf4j"      % "2.3.9"
)

aspectjSettings

javaOptions in run <++= AspectjKeys.weaverOptions in Aspectj

fork in run := true
