scalaVersion := "2.11.6"

resolvers += "Kamon Repository Snapshots" at "http://snapshots.kamon.io"

// tag:base-kamon-dependencies:start

libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-core"          % "0.6.0",
  "io.kamon" %% "kamon-annotation"    % "0.6.0"
)
// tag:base-kamon-dependencies:end

aspectjSettings

javaOptions in run <++= AspectjKeys.weaverOptions in Aspectj

fork in run := true
