package kamon.examples.scala

import kamon.Kamon
import kamon.context.Context
import kamon.tag.Tag

object ContextBasics extends App {

  // tag:context-keys:start
  // Keys are propagated in-process only
  val UserID = Context.key[String]("userID", "undefined")

  val SessionID = Context.key[Option[Int]]("sessionID", None)
  val RequestID = Context.key("requestID", None)
  //  tag:context-keys:end

  // tag:creating-a-context:start
  // Creating a Context with two keys
  val context = Context.of(UserID, "1234", SessionID, Some(42))

  // Reading values from a Context
  val userID = context.get(UserID)
  val sessionID = context.get(SessionID)

  // The default value is returned for non-existent keys
  val requestID = context.get(RequestID)
  // tag:creating-a-context:end

  // tag:storing-a-context:start
  // From this moment on there is a current context
  val scope = Kamon.storeContext(context)

  // Closing the Scope activates the previously stored context
  scope.close()

  Kamon.runWithContext(context) {
    // Our context instance is the current Context

    Kamon.runWithContextEntry(UserID, "5678") {
      // The current context has a overridden UserID key
      // The entry is propagated across process boundaries
    }
  }

  // tag:storing-a-context:end


  // tag:creating-a-codec:start
  object Codecs {

    trait ForEntry[T] {
      def encode(context: Context): T

      def decode(carrier: T, context: Context): Context
    }

  }

  // tag:creating-a-codec:end
}
