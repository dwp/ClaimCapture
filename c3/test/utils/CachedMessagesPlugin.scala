package utils


import play.api.Application
import play.api.i18n._
import CachedMessagesPlugin._

/**
 * optimizes the loading of the messages so that they are loaded only one per JVM
 * this is useful for unit & integration tests that start a new FakeApplication for each test
 */
class CachedMessagesPlugin(app: Application) extends MultipleMessagesPlugin(app) {

  /**
   * The underlying internationalisation API.
   */
  override lazy val api = cache match {
    case Some(messagesApi) => messagesApi
    case None => throw new RuntimeException //should not get here
  }

  /**
   * Loads all configuration and message files defined in the classpath into a cache
   */
  override def onStart() = cache match {
    case None => cache = Some(MessagesApi(messages))
    case Some(_) => //do nothing - messages already loaded
  }

}

/**
 * holds the cached messages
 */
object CachedMessagesPlugin {
  var cache: Option[MessagesApi] = None
}
