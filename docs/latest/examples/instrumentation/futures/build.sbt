scalaVersion := "2.12.2"

// tag:base-kamon-dependencies:start
libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-bundle" % "{{versions.latest.bundle}}"
)
// tag:base-kamon-dependencies:end
