package kamon.spray.examples

import _root_.spray.routing.SimpleRoutingApp
import akka.actor.ActorSystem
import kamon.Kamon
import kamon.spray.KamonTraceDirectives.traceName
import kamon.trace.Tracer


object SimpleSprayApp extends App with SimpleRoutingApp {
  Kamon.start()

  implicit val system = ActorSystem("kamon-spray-examples")

  // tag:traceName-directive:start
  startServer(interface = "127.0.0.1", port = 8090) {
    get {
      path("users") {
        traceName("GetAllUsers") {
          complete {
            Tracer.setCurrentContext(Kamon.tracer.newContext("other-context"))
            "All Users"
          }
        }
      } ~
      path("users" / IntNumber) { userID =>
        traceName("GetUserDetails") {
          complete {
            "Data about a specific user"
          }
        }
      }
    }
  }
  // tag:traceName-directive:end
}

/*

When there is no current TraceContext while processing the request response:
  EmptyTraceContext present while closing the trace with token [ivantopo-desktop-1]

When the current TraceContext is not the same as the one created when the request arrived:
  Different trace token found when trying to close a trace, original: [ivantopo-desktop-1] - incoming: [ivantopo-desktop-2]

 */