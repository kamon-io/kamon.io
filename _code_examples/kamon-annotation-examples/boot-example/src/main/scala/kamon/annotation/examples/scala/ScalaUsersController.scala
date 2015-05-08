package kamon.annotation.examples.scala

import kamon.annotation.{EnableKamon, Trace}
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, ResponseBody}

// tag:traces:start
@EnableKamon
@Controller
@RequestMapping(value = Array("/scala/users"))
class ScalaUsersController {

  val usersService = new ScalaUsersService

  @Trace("ListAllUsers")
  @RequestMapping(method = Array(RequestMethod.GET))
  @ResponseBody
  def listUsers: String =
    usersService.findUsers

}
// tag:traces:end
