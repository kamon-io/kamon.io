scalaVersion := "2.12.2"
resolvers += Resolver.bintrayRepo("kamon-io", "snapshots")

// tag:base-kamon-dependencies:start
libraryDependencies += "io.kamon" %% "kamon-core" % "1.0.0-RC4"

// Optional Dependencies
libraryDependencies += "io.kamon" %% "kamon-prometheus" % "1.0.0-RC4"
libraryDependencies += "io.kamon" %% "kamon-zipkin" % "1.0.0-RC4-3e433d8ae40a05435e81f37cb9347c5ec99756c1"
// tag:base-kamon-dependencies:end


// Additional dependencies.
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.2"
)

javacOptions := Seq("-source", "1.8", "-target", "1.8")
