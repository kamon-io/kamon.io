scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-core" % "0.3.6-e542aecea307579acf1e95ab6c55ce29ccb01ac5",

  // [Optional]
  "io.kamon" %% "kamon-statsd" % "0.3.6-e542aecea307579acf1e95ab6c55ce29ccb01ac5",
  // [Optional]
  "io.kamon" %% "kamon-datadog" % "0.3.6-e542aecea307579acf1e95ab6c55ce29ccb01ac5"

  // ... and so on with all the modules you need.
)

// Additional dependencies.
libraryDependencies ++= Seq(
  "ch.qos.logback"    %  "logback-classic" % "1.1.2",
  "com.typesafe.akka" %% "akka-actor"      % "2.3.8",
  "com.typesafe.akka" %% "akka-slf4j"      % "2.3.8"
)

aspectjSettings

javaOptions in run <++= AspectjKeys.weaverOptions in Aspectj

fork in run := true
