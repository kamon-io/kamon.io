name := "install-with-sbt"
scalaVersion := "2.12.7"
resolvers += Resolver.bintrayRepo("kamon-io", "releases")

// tag:base-kamon-dependencies:start
// In your build.sbt file:
libraryDependencies += "io.kamon" %% "kamon-bundle" % "2.0.4"
// tag:base-kamon-dependencies:end

// Additional Dependencies
//libraryDependencies += "io.kamon" %% "kamon-apm-reporter" % "2.0.0-RC2"
//libraryDependencies += "io.kamon" %% "kamon-prometheus" % "2.0.0-RC1"
//libraryDependencies += "io.kamon" %% "kamon-zipkin" % "2.0.0-RC1"

// Additional dependencies.
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)