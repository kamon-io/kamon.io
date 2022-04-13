package io.kamon.hellostream.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import io.kamon.hellostream.api.HelloStreamService
import io.kamon.hello.api.HelloService
import kamon.Kamon
import com.softwaremill.macwire._

class HelloStreamLoader extends LagomApplicationLoader {

  // tag:initialize-kamon:start
  override def load(context: LagomApplicationContext): LagomApplication = {
    Kamon.initWithoutAttaching(context.playContext.initialConfiguration.underlying)

    context.playContext.lifecycle.addStopHook(() => {
      Kamon.stop()
    })
    
    new HelloStreamApplication(context) {
      override def serviceLocator: NoServiceLocator.type = NoServiceLocator
    }
  }
  // tag:initialize-kamon:end
    

  override def loadDevMode(context: LagomApplicationContext): LagomApplication = {
    new HelloStreamApplication(context) with LagomDevModeComponents
  }

  override def describeService = Some(readDescriptor[HelloStreamService])
}

abstract class HelloStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer = serverFor[HelloStreamService](wire[HelloStreamServiceImpl])

  // Bind the HelloService client
  lazy val helloService: HelloService = serviceClient.implement[HelloService]
}
