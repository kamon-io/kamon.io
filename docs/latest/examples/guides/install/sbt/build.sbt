name := "install-with-sbt"
scalaVersion := "2.12.7"
resolvers += Resolver.bintrayRepo("kamon-io", "releases")

// tag:base-kamon-dependencies:start
// In your build.sbt file:
libraryDependencies += "io.kamon" %% "kamon-bundle" % "{{versions.latest.bundle}}"
// tag:base-kamon-dependencies:end

// Additional Dependencies
//libraryDependencies += "io.kamon" %% "kamon-apm-reporter" % "{{versions.latest.apm}}"
//libraryDependencies += "io.kamon" %% "kamon-prometheus" % "{{versions.latest.prometheus}}"
//libraryDependencies += "io.kamon" %% "kamon-zipkin" % "{{versions.latest.zipkin}}"

// Additional dependencies.
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)