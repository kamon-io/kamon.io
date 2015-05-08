package kamon.annotation.examples.scala

import kamon.annotation.{Trace, EnableKamon}
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMethod, RequestMapping}

// tag:enable-kamon:start
@EnableKamon
@Controller
@RequestMapping(value = Array("/scala/orders"))
class ScalaOrdersController {

    @Trace("ListOrders")
    @RequestMapping(method = Array(RequestMethod.GET))
    @ResponseBody
    def listOrders: String = {
        "Listing all orders."
    }
}
// tag:enable-kamon:end
