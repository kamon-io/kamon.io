package kamon.akka.examples.scala

import akka.actor.{Actor, Props, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import kamon.Kamon
import kamon.trace.Tracer
import scala.concurrent.duration._

object AskPatternTimeoutWarning extends App {
  Kamon.start()
  implicit val timeout = Timeout(2 seconds)

  val system = ActorSystem("ask-pattern-timeout-warning")
  val dumbActor = system.actorOf(Props[DumbActor], "dumb-actor")
  implicit val ec = system.dispatcher

  Tracer.withNewContext("testTrace") {
    // tag:simple-ask:start
    (dumbActor ? "Hello") onFailure {
      case t: Throwable => t.printStackTrace()
    }
    // tag:simple-ask:end
  }




  // Wait for the ask pattern to timeout
  Thread.sleep(4000)
  system.shutdown()
  Kamon.shutdown()

}

class DumbActor extends Actor {
  def receive = {
    case "hello" => sender ! "world"
    case other =>
  }
}


/*
// tag:typical-ask-timeout-exception:start
akka.pattern.AskTimeoutException: Ask timed out on [Actor[akka://ask-pattern-timeout-warning/user/dumb-actor#778114812]] after [2000 ms]
	at akka.pattern.PromiseActorRef$$anonfun$1.apply$mcV$sp(AskSupport.scala:335)
	at akka.actor.Scheduler$$anon$7.run(Scheduler.scala:117)
	at scala.concurrent.Future$InternalCallbackExecutor$.unbatchedExecute(Future.scala:599)
	at scala.concurrent.BatchingExecutor$class.execute(BatchingExecutor.scala:109)
	at scala.concurrent.Future$InternalCallbackExecutor$.execute(Future.scala:597)
	at akka.actor.LightArrayRevolverScheduler$TaskHolder.executeTask(Scheduler.scala:467)
	at akka.actor.LightArrayRevolverScheduler$$anon$8.executeBucket$1(Scheduler.scala:419)
	at akka.actor.LightArrayRevolverScheduler$$anon$8.nextTick(Scheduler.scala:423)
	at akka.actor.LightArrayRevolverScheduler$$anon$8.run(Scheduler.scala:375)
	at java.lang.Thread.run(Thread.java:745)
// tag:typical-ask-timeout-exception:end

// tag:lightweight-warning:start
Timeout triggered for ask pattern to actor [dumb-actor] at [AskPatternTimeoutWarning.scala:21]
// tag:lightweight-warning:end

// tag:heavyweight-warning:start
Timeout triggered for ask pattern to actor [dumb-actor] at [kamon.akka.examples.scala.AskPatternTimeoutWarning$$anonfun$1.apply(AskPatternTimeoutWarning.scala:21)
kamon.trace.Tracer$$anonfun$withNewContext$1.apply(TracerModule.scala:66)
kamon.trace.Tracer$.withContext(TracerModule.scala:57)
kamon.trace.Tracer$.withNewContext(TracerModule.scala:65)
kamon.trace.Tracer$.withNewContext(TracerModule.scala:75)
kamon.akka.examples.scala.AskPatternTimeoutWarning$.delayedEndpoint$kamon$akka$examples$scala$AskPatternTimeoutWarning$1(AskPatternTimeoutWarning.scala:19)
kamon.akka.examples.scala.AskPatternTimeoutWarning$delayedInit$body.apply(AskPatternTimeoutWarning.scala:11)
scala.Function0$class.apply$mcV$sp(Function0.scala:40)
scala.runtime.AbstractFunction0.apply$mcV$sp(AbstractFunction0.scala:12)
scala.App$$anonfun$main$1.apply(App.scala:76)
scala.App$$anonfun$main$1.apply(App.scala:76)
scala.collection.immutable.List.foreach(List.scala:381)
scala.collection.generic.TraversableForwarder$class.foreach(TraversableForwarder.scala:35)
scala.App$class.main(App.scala:76)
kamon.akka.examples.scala.AskPatternTimeoutWarning$.main(AskPatternTimeoutWarning.scala:11)
kamon.akka.examples.scala.AskPatternTimeoutWarning.main(AskPatternTimeoutWarning.scala)
sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
java.lang.reflect.Method.invoke(Method.java:497)
com.intellij.rt.execution.application.AppMain.main(AppMain.java:140)
// tag:heavyweight-warning:end

*/

