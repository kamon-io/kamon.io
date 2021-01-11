// tag:add-aspectj:start
resolvers += Resolver.bintrayIvyRepo("kamon-io", "sbt-plugins")
addSbtPlugin("io.kamon" % "sbt-kanela-runner" % "{{versions.latest.kanela_runner}}")
// tag:add-aspectj:end
