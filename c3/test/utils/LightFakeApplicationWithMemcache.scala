package utils

import play.api.{GlobalSettings}
import play.api.test.FakeApplication


/**
 * factory for creating fake applications with memcache loaded ( not ehcache )
 */
object LightFakeApplicationWithMemcache {
  val memcachedHost1 = "127.0.0.1:11211"
  val memcachedHost2 = "127.0.0.1:11212"
  val configurationMap: Map[String, Object] = Map(
    "play.modules.enabled" -> List(
      "gov.dwp.carers.play2.resilientmemcached.MemcachedModule",
      "play.api.i18n.I18nModule",
      "play.api.db.HikariCPModule",
      "play.api.db.DBModule",
      "play.api.inject.BuiltinModule",
      "com.kenshoo.play.metrics.PlayModule",
      "utils.TestSubmissionModule",
      "play.api.i18n.MultiMessageModule",
      "play.api.libs.ws.ning.NingWSModule"
    ),

    "play.modules.disabled" -> List("play.api.cache.EhCacheModule", "utils.TestEhCacheModule"),
    "play.modules.cache.defaultCache" -> "",
    "play.modules.cache.bindCaches" -> List("play"),
    "memcached.1.host" -> memcachedHost1,
    "memcached.2.host" -> memcachedHost2
  )

  def apply(withGlobal: Some[GlobalSettings], additionalConfiguration: Map[String, _ <: Any] = configurationMap) = FakeApplication(
    withGlobal = withGlobal,
    additionalConfiguration = additionalConfiguration
  )

  // Default switch positions ... so we dont need to set config for every switch position during tests, add default them here and override if required.
  lazy val defaultSwitchPositions=Map(
    "origin.tag" -> "GB",
    "i18n.messagelisting" -> "messagelisting.properties",
    "saveForLaterSaveEnabled" -> "true",
    "feedback.cads.enabled" -> "true"
  )

  lazy val faCEATrue = FakeApplication(
    additionalConfiguration = configurationMap
  )

  lazy val SaveForLaterOff = FakeApplication(
    additionalConfiguration = configurationMap ++ Map("saveForLaterSaveEnabled" -> "false", "saveForLaterResumeEnabled" -> "false")
  )

  def faXmlVersion(xmlSchemaVersionNumber: String) = FakeApplication(
    additionalConfiguration = configurationMap ++ Map("xml.schema.version" -> xmlSchemaVersionNumber)
  )

  def apply(additionalConfiguration: Map[String, _ <: Any]) = (additionalConfiguration.get("circs.employment.active"): @unchecked ) match {
    case Some(_) => faCEATrue
    case None => FakeApplication(
      additionalConfiguration = configurationMap ++ additionalConfiguration
    )
  }

  def fa = {
    val app = FakeApplication(additionalConfiguration = configurationMap ++ defaultSwitchPositions)
    app
  }

  def apply() = {
    fa
  }
}

