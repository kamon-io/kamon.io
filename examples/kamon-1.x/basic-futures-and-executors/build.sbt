scalaVersion := "2.12.2"

// tag:base-kamon-dependencies:start
libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-core" % "1.0.0-RC5",
  "io.kamon" %% "kamon-executors" % "1.0.0-RC5",
  "io.kamon" %% "kamon-scala-future" % "1.0.0-RC5"
)
// tag:base-kamon-dependencies:end
