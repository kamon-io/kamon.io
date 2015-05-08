package kamon.annotation.examples.java;

import kamon.annotation.EnableKamon;
import kamon.annotation.Trace;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;

// tag:enable-kamon:start
@EnableKamon
@Controller
@RequestMapping("/java/orders")
public class JavaOrdersController {

    @Trace("ListOrders")
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody String listOrders() {
        return "Listing all orders.";
    }
}
// tag:enable-kamon:end