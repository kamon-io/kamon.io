scalaVersion := "2.12.2"
resolvers += Resolver.bintrayRepo("kamon-io", "snapshots")

// tag:base-kamon-dependencies:start
libraryDependencies += "io.kamon" %% "kamon-core" % "1.1.0"

// Optional Dependencies
libraryDependencies += "io.kamon" %% "kamon-prometheus" % "1.0.0"
libraryDependencies += "io.kamon" %% "kamon-zipkin" % "1.0.0"
// tag:base-kamon-dependencies:end


// Additional dependencies.
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.2"
)

javacOptions := Seq("-source", "1.8", "-target", "1.8")
