Manually add the AspectJ Weaver Agent:
=====================================

<!-- tag:manually-add-aspectj-weaver:start -->
To manually add the agent on application startup you just need
to add the -javaagent:/path/to/aspectj-weaver.jar as show here:

java -javaagent:~/.aspectj/aspectj-weaver.jar your-app.jar
<!-- tag:manually-add-aspectj-weaver:end -->


<!-- tag:using-aspectj-runner:start -->
//Add the aspectj-runner plugin to project/plugins.sbt
addSbtPlugin("io.kamon" % "aspectj-runner" % "0.1.2")

// Run!
aspectj-runner:run
<!-- tag:using-aspectj-runner:end -->
