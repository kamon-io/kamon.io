// The Lagom plugin
addSbtPlugin("com.lightbend.lagom" % "lagom-sbt-plugin" % "1.6.4")

// tag:kanela-runner-plugin:start
// For Lagom Framework Framework <=1.6.4
addSbtPlugin("io.kamon" % "sbt-kanela-runner-lagom-1.6" % "2.0.13")

// For Lagom Framework Framework >1.6.5
addSbtPlugin("io.kamon" % "sbt-kanela-runner-lagom-1.6" % "{{versions.latest.kanela_runner}}")
// tag:kanela-runner-plugin:end