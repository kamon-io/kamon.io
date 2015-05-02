import javax.servlet.ServletContext

import kamon.Kamon
import kamon.example.KamonServlet
import org.scalatra.LifeCycle

// tag:bootstrap:start
class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext):Unit = {
    Kamon.start()
    context.mount(new KamonServlet(), "/kamon")
  }

  override def destroy(context: ServletContext): Unit = {
    Kamon.shutdown()
  }
}
// tag:bootstrap:end
