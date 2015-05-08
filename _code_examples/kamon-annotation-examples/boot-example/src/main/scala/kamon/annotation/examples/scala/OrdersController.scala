package kamon.annotation.examples.scala

import kamon.annotation.{EnableKamon, Count}
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping

@EnableKamon
@RestController
class OrdersController {

    @Count(name = "GetOrdersIndex")
    @RequestMapping(name = "/orders")
    def index: String = {
        "This one is coming from scala!"
    }

}
