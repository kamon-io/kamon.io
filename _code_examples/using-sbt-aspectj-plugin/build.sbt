scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-core" % "0.5.2"
)

// tag:using-sbt-aspectj:start

// Include the sbt-aspectj (https://github.com/sbt/sbt-aspectj)
// plugin in your build by adding the following line to your
// `project/plugins.sbt` file.
//
//  addSbtPlugin("com.typesafe.sbt" % "sbt-aspectj" % "0.10.0")
//

// Bring the sbt-aspectj settings into this build
aspectjSettings

// Here we are effectively adding the `-javaagent` JVM startup
// option with the location of the AspectJ Weaver provided by
// the sbt-aspectj plugin.
javaOptions in run <++= AspectjKeys.weaverOptions in Aspectj

// We need to ensure that the JVM is forked for the
// AspectJ Weaver to kick in properly and do it's magic.
fork in run := true
// tag:using-sbt-aspectj:end
