scalaVersion := "2.11.6"

// tag:base-kamon-dependencies:start
libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-core" % "0.4.0",

  // [Optional]
  "io.kamon" %% "kamon-statsd" % "0.4.0",
  // [Optional]
  "io.kamon" %% "kamon-datadog" % "0.4.0"

  // ... and so on with all the modules you need.
)
// tag:base-kamon-dependencies:end

// Additional dependencies.
libraryDependencies ++= Seq(
  "ch.qos.logback"    %  "logback-classic" % "1.1.2",
  "com.typesafe.akka" %% "akka-actor"      % "2.3.9",
  "com.typesafe.akka" %% "akka-slf4j"      % "2.3.9",
  "com.google.guava"  %  "guava"           % "18.0"
)

aspectjSettings

javacOptions := Seq("-source", "1.8", "-target", "1.8")

javaOptions in run <++= AspectjKeys.weaverOptions in Aspectj

fork in run := true
