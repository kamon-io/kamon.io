// tag:play-sbt-plugin:start
// For Play Framework 2.8.8
addSbtPlugin("io.kamon" % "sbt-kanela-runner-play-2.8" % "{{versions.latest.kanela_runner}}")

// OR for Play Framework <= 2.8.7
addSbtPlugin("io.kamon" % "sbt-kanela-runner-play-2.8" % "2.0.10")
// tag:play-sbt-plugin:end

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.7.3")
addSbtPlugin("org.foundweekends.giter8" % "sbt-giter8-scaffold" % "0.11.0")
