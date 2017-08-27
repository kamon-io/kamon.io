scalaVersion := "2.11.6"

resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases",
  "Kamon Repository Snapshots" at "http://snapshots.kamon.io"
)

// tag:base-kamon-dependencies:start
libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-core" % "1.0.0-RC1-1d0548cb8281202738d8d48cbe9cdd62cf209e21",

  // [Optional]
  "io.kamon" %% "kamon-statsd" % "1.0.0-RC1-1d0548cb8281202738d8d48cbe9cdd62cf209e21",
  // [Optional]
  "io.kamon" %% "kamon-datadog" % "0.6.0"

  // ... and so on with all the modules you need.
)
// tag:base-kamon-dependencies:end

// Additional dependencies.
libraryDependencies ++= Seq(
  "ch.qos.logback"    %  "logback-classic" % "1.1.2"
)

aspectjSettings

javacOptions := Seq("-source", "1.8", "-target", "1.8")

javaOptions in run <++= AspectjKeys.weaverOptions in Aspectj

fork in run := true
