package utils

import play.api.GlobalSettings
import play.api.cache.{CacheAPI, CachePlugin}
import play.api.test.FakeApplication

import scala.collection.mutable

/**
 * factory for creating fake applications
 */
object LightFakeApplication {

  //don't load this plugins when testing
  val without = Seq(
    "gov.dwp.carers.play2.resilientmemcached.ResilientMemcachedPlugin",
    "play.api.i18n.MultipleMessagesPlugin",
    "play.api.cache.EhCachePlugin"
  )

  //replaced the default messages plugin with a custom one that caches the messages the first time
  //and the cache plugin with a custom HashMap implementation
  val additional = Seq(
    "utils.TestEhCachePlugin",
    "utils.CachedMessagesPlugin")


  def apply(withGlobal: Some[GlobalSettings], additionalConfiguration: Map[String, _ <: Any] = Map.empty) = FakeApplication(
    withGlobal = withGlobal,
    additionalPlugins = additional,
    withoutPlugins = without,
    additionalConfiguration = additionalConfiguration
  )


  //cache 2 fake applications
  lazy val faCEAFalse = FakeApplication(
    additionalPlugins = additional,
    withoutPlugins = without,
    additionalConfiguration = Map("circs.employment.active" -> "false")
  )
  lazy val faCEATrue = FakeApplication(
    additionalPlugins = additional,
    withoutPlugins = without,
    additionalConfiguration = Map("circs.employment.active" -> "true")
  )

  def apply(additionalConfiguration: Map[String, _ <: Any]) = (additionalConfiguration.get("circs.employment.active"): @unchecked ) match {
    case Some("true") => faCEATrue
    case Some("false") => faCEAFalse
    case None => FakeApplication(
      additionalPlugins = additional,
      withoutPlugins = without,
      additionalConfiguration = additionalConfiguration
    )
  }


  //cache a standard fake application to 
  lazy val fa = FakeApplication(
    additionalPlugins = additional,
    withoutPlugins = without
  )

  def apply() = fa
}

//-------------------

import play.api.Application
import play.api.i18n._
import utils.CachedMessagesPlugin._

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


//-------------------


/**
 * Test Cache implementation that can be cleared between tests
 */
class TestEhCachePlugin(app: Application) extends CachePlugin {

  override def onStart() {
    TestEhCachePlugin.cachedMap match {
      case None => TestEhCachePlugin.cachedMap = Some(new HashMapCacheImpl())
      case Some(_) => //do nothing - messages already loaded
    }
  }

  override def onStop() {
    TestEhCachePlugin.cachedMap.get.clear()
  }

  lazy val api: CacheAPI = TestEhCachePlugin.cachedMap match {
    case Some(x) => x
    case None => throw new RuntimeException //should not get here
  }
}

object TestEhCachePlugin {
  var cachedMap: Option[HashMapCacheImpl] = None
}

class HashMapCacheImpl() extends CacheAPI {
  val c = mutable.HashMap[String, Any]()

  def set(key: String, value: Any, expiration: Int) {
    c.put(key, value)
  }

  def get(key: String): Option[Any] = {
    c.get(key)
  }

  def remove(key: String) {
    c.remove(key)
  }

  def clear(): Unit = {
    c.clear()
  }
}
