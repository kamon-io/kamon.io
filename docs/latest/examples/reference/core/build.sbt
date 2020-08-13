scalaVersion := "2.12.2"
resolvers += Resolver.bintrayRepo("kamon-io", "snapshots")

// tag:base-kamon-dependencies:start
libraryDependencies += "io.kamon" %% "kamon-core" % "2.1.4"

// Optional Dependencies
libraryDependencies += "io.kamon" %% "kamon-prometheus" % "2.1.4"
libraryDependencies += "io.kamon" %% "kamon-zipkin" % "2.1.4"
libraryDependencies += "io.kamon" %% "kamon-logback" % "2.1.4"
// tag:base-kamon-dependencies:end


// Additional dependencies.
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

javacOptions := Seq("-source", "1.8", "-target", "1.8")
