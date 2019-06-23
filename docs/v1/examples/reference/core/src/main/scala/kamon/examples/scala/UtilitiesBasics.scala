package kamon.examples.scala

import kamon.Kamon
import kamon.context.{Context, Key}

object UtilitiesBasics extends App {

  // tag:applying-filters:start
  // Get a filter instance and apply on separate steps
  val filter = Kamon.filter("my-custom-filter")
  filter.accept("test-string")

  // Applying the filter as a one-liner
  val isIncluded = Kamon.filter("explicit.matcher.type", "test-string")
  // tag:applying-filters:end

}
