scalaVersion := "2.12.2"

// tag:base-kamon-dependencies:start
libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-core" % "{{versions.latest.core}}",
  "io.kamon" %% "kamon-executors" % "{{versions.latest.executors}}",
  "io.kamon" %% "kamon-scala-future" % "{{versions.latest.futures}}"
)
// tag:base-kamon-dependencies:end
