scalaVersion := "2.12.2"
resolvers += Resolver.bintrayRepo("kamon-io", "snapshots")

// tag:base-kamon-dependencies:start
libraryDependencies += "io.kamon" %% "kamon-core" % "{{versions.latest.core}}"

// Optional Dependencies
libraryDependencies += "io.kamon" %% "kamon-prometheus" % "{{versions.latest.prometheus}}"
libraryDependencies += "io.kamon" %% "kamon-zipkin" % "{{versions.latest.zipkin}}"
libraryDependencies += "io.kamon" %% "kamon-logback" % "{{versions.latest.logback}}"
// tag:base-kamon-dependencies:end


// Additional dependencies.
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

javacOptions := Seq("-source", "1.8", "-target", "1.8")
