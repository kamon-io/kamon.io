scalaVersion := "2.11.6"

resolvers += "Kamon Repository Snapshots" at "http://snapshots.kamon.io"

// tag:base-kamon-dependencies:start

libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-core"          % "0.3.6-5cb9de39cdd6ef258732a82e6f0d519712d1c37b",
  "io.kamon" %% "kamon-annotation"    % "0.3.6-5cb9de39cdd6ef258732a82e6f0d519712d1c37b"
)
// tag:base-kamon-dependencies:end

aspectjSettings

javaOptions in run <++= AspectjKeys.weaverOptions in Aspectj

fork in run := true

