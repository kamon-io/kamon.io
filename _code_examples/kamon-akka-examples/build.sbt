scalaVersion := "2.11.6"

// tag:base-kamon-dependencies:start
libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-core" % "0.3.6-f476985fd26b4be2281bb7e68dc916190c681467",
  "io.kamon" %% "kamon-akka" % "0.3.6-f476985fd26b4be2281bb7e68dc916190c681467",
  "io.kamon" %% "kamon-scala" % "0.3.6-f476985fd26b4be2281bb7e68dc916190c681467",
  "io.kamon" %% "kamon-akka-remote" % "0.3.6-f476985fd26b4be2281bb7e68dc916190c681467"
)
// tag:base-kamon-dependencies:end

// Additional dependencies.
libraryDependencies ++= Seq(
  "ch.qos.logback"    %  "logback-classic" % "1.1.2",
  "com.typesafe.akka" %% "akka-actor"      % "2.3.9",
  "com.typesafe.akka" %% "akka-slf4j"      % "2.3.9"
)

aspectjSettings

javaOptions in run <++= AspectjKeys.weaverOptions in Aspectj

fork in run := true
