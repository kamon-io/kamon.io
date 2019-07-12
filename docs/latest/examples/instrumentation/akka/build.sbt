scalaVersion := "2.12.2"

// tag:base-kamon-dependencies:start
libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-bundle" % "2.0.0-RC2"
)
// tag:base-kamon-dependencies:end

// Additional dependencies.
libraryDependencies ++= Seq(
  "ch.qos.logback"    %  "logback-classic" % "1.2.3",
  "com.typesafe.akka" %% "akka-actor"      % "2.5.23",
  "com.typesafe.akka" %% "akka-slf4j"      % "2.5.23"
)
