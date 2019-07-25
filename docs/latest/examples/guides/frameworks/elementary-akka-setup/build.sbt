name := "akka-quickstart-scala"
version := "1.0"
scalaVersion := "2.12.8"

lazy val akkaVersion = "2.5.23"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

// tag:base-dependencies:start
libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-bundle" % "2.0.0",
  "io.kamon" %% "kamon-prometheus" % "1.0.0",
  "io.kamon" %% "kamon-zipkin" % "1.0.0"
)
// tag:base-dependencies:end