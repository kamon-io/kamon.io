import sbt._
import Keys._



object ApplicationBuild extends Build {

val appName         = "Kamon-Scalatra-Example"
val appVersion      = "1.0-SNAPSHOT"

val resolutionRepos = Seq(
    "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
    "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases",
    "Kamon Repository Snapshots" at "http://snapshots.kamon.io"
  )

val defaultSettings = Seq(
    scalaVersion := "2.11.6",
    resolvers ++= resolutionRepos,
    scalacOptions := Seq(
      "-encoding",
      "utf8",
      "-g:vars",
      "-feature",
      "-unchecked",
      "-deprecation",
      "-target:jvm-1.6",
      "-language:postfixOps",
      "-language:implicitConversions",
      "-Xlog-reflective-calls"
    ))

// tag:dependencies:start
val kamonVersion = "0.3.5"

val dependencies = Seq(
  "io.kamon"                %% "kamon-core"                  % kamonVersion,
  "io.kamon"                %% "kamon-statsd"                % kamonVersion,
  "io.kamon"                %% "kamon-log-reporter"          % kamonVersion,
  "io.kamon"                %% "kamon-system-metrics"        % kamonVersion,
  "org.scalatra"            %% "scalatra"                    % "2.4.0-SNAPSHOT",
  "org.aspectj"             %  "aspectjweaver"               % "1.8.5",
  "ch.qos.logback"          %  "logback-classic"             % "1.1.1"                % "runtime",
  "org.eclipse.jetty"       %  "jetty-webapp"                % "9.1.3.v20140225"      % "compile;runtime;",
  "org.eclipse.jetty.orbit" % "javax.servlet"                % "3.0.0.v201112011016"  % "runtime;provided;test" artifacts (Artifact("javax.servlet", "jar", "jar"))
  )
// tag:dependencies:end
val main = Project(appName, file(".")).settings(libraryDependencies ++= dependencies)
                                      .settings(defaultSettings: _*)
}

