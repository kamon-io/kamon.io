scalaVersion := "2.12.2"

// tag:base-kamon-dependencies:start
libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-core" % "1.0.0",
  "io.kamon" %% "kamon-akka-2.5" % "1.0.0"
  //"io.kamon" %% "kamon-akka-remote-2.5" % "1.0.0"
)
// tag:base-kamon-dependencies:end

// Additional dependencies.
libraryDependencies ++= Seq(
  "ch.qos.logback"    %  "logback-classic" % "1.2.3",
  "com.typesafe.akka" %% "akka-actor"      % "2.5.8",
  "com.typesafe.akka" %% "akka-slf4j"      % "2.5.8"
)
