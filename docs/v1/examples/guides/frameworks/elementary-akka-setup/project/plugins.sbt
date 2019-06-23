addSbtPlugin("io.spray" % "sbt-revolver" % "0.8.0")

// tag:add-aspectj:start
resolvers += Resolver.bintrayRepo("kamon-io", "sbt-plugins")
addSbtPlugin("io.kamon" % "sbt-aspectj-runner" % "1.1.0")
// tag:add-aspectj:end
