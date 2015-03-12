---
layout: post
title: Scalatra Kamon Integragion
date: 2015-03-20
categories: teamblog
---
### Build Setup ###

you need include in your `build.scala` some dependencies. It should look like this.

```scala
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

  val kamonVersion = "0.3.5"

  val dependencies = Seq(
    "io.kamon"    	          %% "kamon-core"           	     % kamonVersion,
    "io.kamon"    	          %% "kamon-statsd"         	     % kamonVersion,
    "io.kamon"    	          %% "kamon-log-reporter"   	     % kamonVersion,
    "io.kamon"    	          %% "kamon-system-metrics" 	     % kamonVersion,
    "org.scalatra" 	          %% "scalatra" 			             % "2.4.0-SNAPSHOT",
    "org.aspectj" 	          %  "aspectjweaver"        	     % "1.8.5",
    "ch.qos.logback"          %  "logback-classic"             % "1.1.1"                % "runtime",
    "org.eclipse.jetty"       %  "jetty-webapp"                % "9.1.3.v20140225"      % "compile;runtime;",
    "org.eclipse.jetty.orbit" % "javax.servlet"                % "3.0.0.v201112011016"  % "runtime;provided;test" artifacts (Artifact("javax.servlet", "jar", "jar"))
    )

  val main = Project(appName, file(".")).settings(libraryDependencies ++= dependencies)
                                        .settings(defaultSettings: _*)
}
```
### Configuring Scalatra ###
in order to `scalatra` listen web requests, it necessary register a listener in `web.xml` inside of  `../WEB-INF` folder.

```xml
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0">
    <listener>
        <listener-class>org.scalatra.servlet.ScalatraListener</listener-class>
    </listener>
</web-app>
```
also we will need `bootstrap` Scalatra, creating `ScalatraBootstrap.scala` in `src/main/scala` folder and adding the following code.

```scala
import javax.servlet.ServletContext
import org.scalatra.LifeCycle
import org.scalatra.servlet._

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) = {
    context.mount(new kamon.example.KamonServlet, "/test")
  }
}
```
### Create a Simple Servlet ###

Now let’s create a simple servlet that will record some metrics. Create a file called `KamonServlet.scala` and introduce following code

```scala
package kamon.example

import org.scalatra.{Ok, ScalatraServlet}

class KamonServlet extends ScalatraServlet {

  get("/") {
    println("blabla")
  }

  get("/timer") {
    println("timer")

  }

  get("/counter") {
    println("counter")
    "counter"
  }

  get("/histogram") {
    println("histogram")
  }
}
```
### Start the Server ###

```scala
package kamon.example

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext

object EmbeddedServer {
  def main(args: Array[String]) {
    val server = new Server(8080)
    val context: WebAppContext = new WebAppContext();
    context.setServer(server)
    context.setContextPath("/");
    context.setWar("src/webapp")
    server.setHandler(context);

    try {
      server.start()
      server.join()
    } catch {
      case e: Exception => {
        e.printStackTrace()
        System.exit(1)
     }
   }
 }
}
```
