package kamon.examples.scala

import kamon.Kamon
import kamon.context.Context

object ContextBasics extends App {

  // tag:context-keys:start
  // Propagated in-process only
  val UserID = Context.key[String]("userID", "undefined")

  //  Propagated in-process and across processes.
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
  val requestID: Option[String] = context.get(RequestID)
  // tag:creating-a-context:end

  // tag:storing-a-context:start
  // From this moment on there is a current context
  val scope = Kamon.storeContext(context)

  // Closing the Scope returns the previously active context
  scope.close()

  Kamon.runWithContext(context) {
    // Our context instance is the current Context

    Kamon.runWithContextEntry(UserID, "5678") {
      // The current context has a overridden UserID key
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
