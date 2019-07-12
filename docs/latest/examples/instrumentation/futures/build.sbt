scalaVersion := "2.12.2"

// tag:base-kamon-dependencies:start
libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-scala-future" % "2.0.0-97c08d3fb98a6f3e074dc98711e27694bb8fb05b"
  //"io.kamon" %% "kamon-bundle" % "2.0.0-RC3"
)
// tag:base-kamon-dependencies:end
