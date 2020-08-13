package kamon.examples.scala

import kamon.Kamon
import kamon.util.Filter

object UtilitiesBasics extends App {

  // tag:applying-filters:start
  // Get a filter instance and apply on separate steps
  val filter = Kamon.filter("my-custom-filter")
  filter.accept("test-string")

  // Applying the filter as a one-liner
  // TODO: remove comment, it's just for review-ers
  // i have no idea if this is correct, it just compiles
  val isIncluded = Kamon.filter("explicit.matcher.type").accept("test-string")
  // tag:applying-filters:end
}
