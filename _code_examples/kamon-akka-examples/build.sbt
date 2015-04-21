scalaVersion := "2.11.6"

// tag:base-kamon-dependencies:start
libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-core" % "0.3.6-5cb9de39cdd6ef258732a82e6f0d519712d1c37b",
  "io.kamon" %% "kamon-akka" % "0.3.6-5cb9de39cdd6ef258732a82e6f0d519712d1c37b",
  "io.kamon" %% "kamon-akka-remote" % "0.3.6-5cb9de39cdd6ef258732a82e6f0d519712d1c37b"
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
