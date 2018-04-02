---
title: Kamon | Get Started
layout: documentation-1.x
---

Adding the AspectJ Weaver
=========================

This is a necessary step to benefit from most of the instrumentation modules available in Kamon and even though it is
just about adding a `-javaagent:aspectjweaver.jar` option when starting your JVM, this topic can become tricky in some
scenarios so we decided to dedicate a whole recipe to it.

You can get the AspectJ Weaver latest stable release from the [official website][2] or simply [search for it in Maven Central][1]
and then, as mentioned above, start your JVM with one additional option: `-javaagent:path-to-aspectjweaver.jar`.
That's it, the Weaver will pick up the instrumentation from our modules and you will get the metrics and traces you need.
In some cases though, it's not so straight forward, specially if:

  - you are running the application from SBT.
  - you are running a Play Framework application in development mode.
  - you are deploying a fat jar created with sbt-assembly.
  - you are deploying a package created with sbt-native-packager.

In any of those scenarios just follow in relevant sections bellow.



### Running from SBT

The simplest solution is to use the [sbt-aspectj-runner][3] plugin. This plugin will ensure that the instrumentation is
applied to your classes regardless of whether you are forking the process (via `fork in run := true`) or not. To get it
working add these lines to your `project/plugins.sbt` file:

{% code_block scala %}
resolvers += Resolver.bintrayRepo("kamon-io", "sbt-plugins")
addSbtPlugin("io.kamon" % "sbt-aspectj-runner" % "1.1.0")
{% endcode_block scala %}

That's it! You can visit the GitHub repo for additional details on how the plugin works.



### Running a Play application in Development Mode

Once again, the [sbt-aspectj-runner][3] is the way to go. The plugin has special variants for Play applications that
will have special treatment of Play's infrastructure for running on Development mode, to include it you must add the
right plugin depending on your Play version:

#### For Play 2.6:

{% code_block scala %}
resolvers += Resolver.bintrayIvyRepo("kamon-io", "sbt-plugins")
addSbtPlugin("io.kamon" % "sbt-aspectj-runner-play-2.6" % "1.1.0")
{% endcode_block scala %}


#### For Play 2.4 and 2.5:

{% code_block scala %}
resolvers += Resolver.bintrayIvyRepo("kamon-io", "sbt-plugins")
addSbtPlugin("io.kamon" % "sbt-aspectj-play-runner" % "1.0.4")
{% endcode_block scala %}



### Deploying a Fat Jar from sbt-assembly

Since sbt-assembly puts your entire classpath in a single jar, it needs to merge files that have the same path across
several jars and that includes the `META-INF/aop.xml` files that ship in all the instrumentation modules. The default
merge strategy will resolve the conflict by picking the first file found in the classpath and even though that would
generate a jar with all the instrumentation, you will notice that some instrumentation is working and some is missing,
depending on which `aop.xml` file was picked.

Cool thing is, there is a [custom merge strategy][4] that takes care of merging these files. This is how it looks like:

{% code_block scala %}
// Create a new MergeStrategy for aop.xml files
val aopMerge: MergeStrategy = new MergeStrategy {
  val name = "aopMerge"
  import scala.xml._
  import scala.xml.dtd._

  def apply(tempDir: File, path: String, files: Seq[File]): Either[String, Seq[(File, String)]] = {
    val dt = DocType("aspectj", PublicID("-//AspectJ//DTD//EN", "http://www.eclipse.org/aspectj/dtd/aspectj.dtd"), Nil)
    val file = MergeStrategy.createMergeTarget(tempDir, path)
    val xmls: Seq[Elem] = files.map(XML.loadFile)
    val aspectsChildren: Seq[Node] = xmls.flatMap(_ \\ "aspectj" \ "aspects" \ "_")
    val weaverChildren: Seq[Node] = xmls.flatMap(_ \\ "aspectj" \ "weaver" \ "_")
    val options: String = xmls.map(x => (x \\ "aspectj" \ "weaver" \ "@options").text).mkString(" ").trim
    val weaverAttr = if (options.isEmpty) Null else new UnprefixedAttribute("options", options, Null)
    val aspects = new Elem(null, "aspects", Null, TopScope, false, aspectsChildren: _*)
    val weaver = new Elem(null, "weaver", weaverAttr, TopScope, false, weaverChildren: _*)
    val aspectj = new Elem(null, "aspectj", Null, TopScope, false, aspects, weaver)
    XML.save(file.toString, aspectj, "UTF-8", xmlDecl = false, dt)
    IO.append(file, IO.Newline.getBytes(IO.defaultCharset))
    Right(Seq(file -> path))
  }
}

// Use defaultMergeStrategy with a case for aop.xml
// I like this better than the inline version mentioned in assembly's README
val customMergeStrategy: String => MergeStrategy = {
  case PathList("META-INF", "aop.xml") =>
    aopMerge
  case s =>
    defaultMergeStrategy(s)
}

// Use the customMergeStrategy in your settings
mergeStrategy in assembly := customMergeStrategy
{% endcode_block scala %}

You might need to integrate this slightly different in your build if you have other merge strategy configurations. Then
the fat jar can be ran as usual with the `-javaagent:path-to-weaver.jar` option.


### Deploying with sbt-native-packager

You can use the [sbt-javaagent plugin][5] together with sbt-native-packager to get the AspectJ Weaver options automatically
added to the startup scripts. To achieve this, first add the plugin to your `project/plugins.sbt` file:

{% code_block scala %}
addSbtPlugin("com.lightbend.sbt" % "sbt-javaagent" % "0.1.4")
{% endcode_block scala %}

And enable the plugin in your `build.sbt` file:

{% code_block scala %}
lazy val root = (project in file(".")).enablePlugins(JavaAppPackaging, JavaAgent) // (1)
javaAgents += "org.aspectj" % "aspectjweaver" % "1.8.13" // (2)
javaOptions in Universal += "-Dorg.aspectj.tracing.factory=default" // (3)
{% endcode_block scala %}

A few comments from the above:
  - (1) You need to enable the `JavaAgent` plugin in your project, if not the root project then make sure you do it in
    the right one.
  - (2) This line defines the AspectJ Weaver dependency, always try to use the latest stable version.
  - (3) This additional settings helps get less "false errors" from the AspectJ Weaver which are usually confused with
    real errors.

You can find additional details on the [sbt-javaagent GitHub repo][5]

[1]: http://search.maven.org/#search%7Cga%7C1%7Caspectjweaver
[2]: https://www.eclipse.org/aspectj/downloads.php
[3]: https://github.com/kamon-io/sbt-aspectj-runner
[4]: https://gist.github.com/colestanfield/fac042d3108b0c06e952
[5]: https://github.com/sbt/sbt-javaagent