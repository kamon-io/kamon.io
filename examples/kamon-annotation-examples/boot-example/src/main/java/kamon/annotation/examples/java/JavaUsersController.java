package kamon.annotation.examples.java;

import kamon.annotation.EnableKamon;
import kamon.annotation.Trace;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

// tag:traces:start
@EnableKamon
@Controller
@RequestMapping("/java/orders")
public class JavaUsersController {

    private final JavaUsersService usersService = new JavaUsersService();

    @Trace("ListAllUsers")
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody String listUsers() {
        return usersService.findUsers();
    }
}
// tag:traces:end