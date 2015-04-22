name := "spring-boot-kamon"

version := "1.0"

scalaVersion := "2.11.6"

sbtVersion := "0.13.1"

resolvers += "Kamon Repository Snapshots" at "http://snapshots.kamon.io"

libraryDependencies ++= Seq(
  "org.springframework.boot"          % "spring-boot-starter-web" % "1.2.3.RELEASE",
  "io.kamon" %% "kamon-core"          % "0.3.6-5cb9de39cdd6ef258732a82e6f0d519712d1c37b",
  "io.kamon" %% "kamon-annotation"    % "0.3.6-5cb9de39cdd6ef258732a82e6f0d519712d1c37b",
  "io.kamon" %% "kamon-log-reporter"  % "0.3.6-5cb9de39cdd6ef258732a82e6f0d519712d1c37b"
)

aspectjSettings

fork in run := true

javaOptions in run <++= AspectjKeys.weaverOptions in Aspectj