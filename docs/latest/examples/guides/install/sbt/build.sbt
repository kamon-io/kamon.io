name := "install-with-sbt"
scalaVersion := "2.12.7"
resolvers += Resolver.bintrayRepo("kamon-io", "releases")

// tag:base-kamon-dependencies:start
libraryDependencies += "io.kamon" %% "kamon-core" % "1.1.3"

// Additional Dependencies
libraryDependencies += "io.kamon" %% "kamon-akka-2.5" % "1.1.2"
libraryDependencies += "io.kamon" %% "kamon-prometheus" % "1.1.1"
libraryDependencies += "io.kamon" %% "kamon-zipkin" % "1.0.0"
// tag:base-kamon-dependencies:end


// Additional dependencies.
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)