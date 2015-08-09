// tag:servlet:start
class KamonServlet extends ScalatraServlet with KamonSupport with FutureSupport {

  implicit val executor: ExecutionContext = ExecutionContext.Implicits.global

  get("/async") {
    traceFuture("retrievePage") {
        HttpClient.retrievePage()
    }
  }

  get("/time") {
    time("time") {
      Thread.sleep(Random.nextInt(100))
    }
  }

  get("/minMaxCounter") {
    minMaxCounter("minMaxCounter").increment()
  }

  get("/counter") {
    counter("counter").increment()
  }

  get("/histogram") {
    histogram("histogram").record(Random.nextInt(10))
  }
}

object HttpClient {
  def retrievePage()(implicit ctx: ExecutionContext): Future[String] = {
    val prom = Promise[String]()
    dispatch.Http(url("http://slashdot.org/") OK as.String) onComplete {
      case Success(content) => prom.complete(Try(content))
      case Failure(exception) => println(exception)
    }
    prom.future
  }
}
// tag:servlet:end