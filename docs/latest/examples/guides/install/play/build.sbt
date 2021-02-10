name := """play-with-kamon"""
organization := "kamon"

version := "1.0-SNAPSHOT"
// tag:play-enable-javaagent:start
lazy val root = (project in file(".")).enablePlugins(PlayScala, JavaAgent)
// tag:play-enable-javaagent:end

scalaVersion := "2.12.8"

libraryDependencies += guice
// tag:play-dependency:start
libraryDependencies += "io.kamon" %% "kamon-bundle" % "{{versions.latest.bundle}}"
libraryDependencies += "io.kamon" %% "kamon-apm-reporter" % "{{versions.latest.bundle}}"
// tag:play-dependency:end

// Enable JavaAgent plugin
lazy val root = (project in file(".")).enablePlugins(PlayScala, JavaAgent)
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "kamon.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "kamon.binders._"
