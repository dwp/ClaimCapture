package utils

import play.api.GlobalSettings
import play.api.test.FakeApplication

/**
 * factory for creating fake applications
 */
object LightFakeApplication {

  //don't load this plugins when testing
  val without = Seq(
    "gov.dwp.carers.play2.resilientmemcached.ResilientMemcachedPlugin",
    "play.api.i18n.MultipleMessagesPlugin")

  //replaced the default messages plugin with a custom one that caches the messages the first time
  val additional = Seq("utils.CachedMessagesPlugin")

  def apply(withGlobal: Some[GlobalSettings], additionalConfiguration: Map[String, _ <: Any] = Map.empty) = FakeApplication(
    withGlobal = withGlobal,
    additionalPlugins = additional,
    withoutPlugins = without,
    additionalConfiguration = additionalConfiguration
  )

  def apply(additionalConfiguration: Map[String, _ <: Any]) = FakeApplication(
    additionalPlugins = additional,
    withoutPlugins = without,
    additionalConfiguration = additionalConfiguration
  )

  def apply() = FakeApplication(
    additionalPlugins = additional,
    withoutPlugins = without
  )

}