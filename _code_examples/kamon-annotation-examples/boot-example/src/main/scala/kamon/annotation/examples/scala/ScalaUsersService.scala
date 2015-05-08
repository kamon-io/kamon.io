package kamon.annotation.examples.scala

import kamon.annotation.{Segment, EnableKamon}

// tag:segments:start
@EnableKamon
class ScalaUsersService {

  @Segment(name = "FindUsers", category = "database", library = "kamon")
  def findUsers: String = {
    "Some list of users."
  }
}
// tag:segments:end
