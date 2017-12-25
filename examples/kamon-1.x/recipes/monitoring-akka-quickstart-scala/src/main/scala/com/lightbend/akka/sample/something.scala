import akka.actor.{Actor, ActorSystem, PoisonPill, Props}
import akka.event.LoggingAdapter
import kamon.Kamon
import kamon.prometheus.PrometheusReporter

object EntryPoint extends App  {
  implicit val system = ActorSystem("cucaracha-system")
  //implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  Kamon.addReporter(new PrometheusReporter())

  println("DONE")
  Thread.sleep(10000)
  var x = 1
  while(x <= 1000) {
    val ref = system.actorOf(Props(classOf[Test]))
    //ref ! "disi"
    ref ! PoisonPill
    x += 1

  }

  println("DONE")
  Thread.sleep(Long.MaxValue)

}

class Test extends Actor {
  override def receive = {
    case _ => //println("primio")
  }
}