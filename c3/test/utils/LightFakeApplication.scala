package utils

import java.util.UUID._
import javax.inject.Singleton

import com.google.common.primitives.Primitives
import controllers.circs.consent_and_declaration.GCircsDeclaration
import controllers.s_consent_and_declaration.GDeclaration
import models.domain.Claim
import monitor.HealthMonitor
import monitoring.{ProdHealthMonitor, ClaimTransactionCheck}
import play.api.{Logger, Configuration, Environment, GlobalSettings}
import play.api.cache.CacheApi
import play.api.inject.{Binding, Module}
import play.api.test.FakeApplication
import services.submission.AsyncClaimSubmissionComponent
import utils.module._
import scala.collection.mutable
import scala.concurrent.duration.Duration
import scala.reflect.ClassTag

/**
 * factory for creating fake applications
 */
object LightFakeApplication {
  val memcachedHost1 = "127.0.0.1:11211"
  val memcachedHost2 = "127.0.0.1:11212"
  val configurationMap: Map[String, Object] = Map(
    "play.modules.enabled" -> List(
      "utils.TestEhCacheModule",
      "play.api.i18n.I18nModule",
      "play.api.db.HikariCPModule",
      "play.api.db.DBModule",
      "play.api.inject.BuiltinModule",
      "com.kenshoo.play.metrics.PlayModule",
      "utils.TestSubmissionModule",
      "play.api.i18n.MultiMessageModule",
      "play.api.libs.ws.ning.NingWSModule"
    ),

    "play.modules.disabled" -> List("play.api.cache.EhCacheModule", "gov.dwp.carers.play2.resilientmemcached.MemcachedModule"),
    "play.modules.cache.defaultCache" -> "",
    "play.modules.cache.bindCaches" -> List("play"),
    "memcached.1.host" -> memcachedHost1,
    "memcached.2.host" -> memcachedHost2
  )

  val memcacheConfigurationMap: Map[String, Object] = Map(
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


  lazy val faCEAFalse = FakeApplication(
    additionalConfiguration = configurationMap ++ Map("circs.employment.active" -> "false")
  )
  lazy val faCEATrue = FakeApplication(
    additionalConfiguration = configurationMap ++ Map("circs.employment.active" -> "true")
  )

  lazy val SaveForLaterOff = FakeApplication(
    additionalConfiguration = configurationMap ++ Map("saveForLaterSaveEnabled" -> "false", "saveForLaterResumeEnabled" -> "false")
  )

  def faXmlVersion(xmlSchemaVersionNumber: String) = FakeApplication(
    additionalConfiguration = configurationMap ++ Map("xml.schema.version" -> xmlSchemaVersionNumber)
  )

  def apply(additionalConfiguration: Map[String, _ <: Any]) = (additionalConfiguration.get("circs.employment.active"): @unchecked ) match {
    case Some("true") => faCEATrue
    case Some("false") => faCEAFalse
    case None => FakeApplication(
      additionalConfiguration = configurationMap ++ additionalConfiguration
    )
  }

  def fa = {
    val app = FakeApplication(additionalConfiguration = configurationMap ++ defaultSwitchPositions)
    app
  }

  def memcachefa = {
    val app = FakeApplication(additionalConfiguration = memcacheConfigurationMap ++ defaultSwitchPositions)
    app
  }

  def apply() = {
    fa
  }
}

@Singleton
class TestEhCacheModule extends Module {
  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = {
    Seq (
      bind(classOf[CacheApi]).to(HashMapCacheApiImpl)
    )
  }
}

@Singleton
class TestSubmissionModule extends Module {
  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = {
      (
        configuration.getBoolean("submit.prints.xml") match {
        case Some(true) => {
          Seq(
            bind(classOf[AsyncClaimSubmissionComponent]).to(classOf[AsyncClaimSubmissionComponentXML]),
            bind(classOf[GDeclaration]).to(classOf[GDeclarationXML]),
            bind(classOf[GCircsDeclaration]).to(classOf[GCircsDeclarationXML])
          )
        }
        case _ => {
          Seq(
            bind(classOf[GDeclaration]).to(classOf[GDeclarationDB]),
            bind(classOf[GCircsDeclaration]).to(classOf[GCircsDeclarationDB]),
            bind(classOf[AsyncClaimSubmissionComponent]).to(classOf[AsyncClaimSubmissionComponentDBStubSubmission])
          )
        }
      }
        ) ++ Seq(
          bind(classOf[ClaimTransactionCheck]).to(classOf[ClaimTransactionCheckStub]),
          bind(classOf[HealthMonitor]).to(classOf[ProdHealthMonitor])
        )
  }
}

class AsyncClaimSubmissionComponentDBStubSubmission extends AsyncClaimSubmissionComponent {
  override val claimTransaction = new StubClaimTransaction
  override def submission(claim: Claim): Unit = {
    Logger.debug("Stub claim submission")
  }
}

@Singleton
trait HashMapCacheApi extends CacheApi

object HashMapCacheApiImpl extends HashMapCacheApi {
  val c = mutable.HashMap[String, Any]()
  val hashKey: String = randomUUID.toString

  override def set(key: String, value: Any, expiration: Duration) {
    c.put(key, value)
  }

  override def remove(key: String) {
    c.remove(key)
  }

  override def getOrElse[A](key: String, expiration: Duration)(orElse: => A)(implicit evidence$1: ClassTag[A]): A = {
    get[A](key).getOrElse {
      val value = orElse
      set(key, value, expiration)
      value
    }
  }

  override def get[T](key: String)(implicit ct: ClassTag[T]): Option[T] = {
    c.get(key) match {
      case None => None
      case _ =>
        Option(c.get(key)).map(_.get).filter { v =>
          Primitives.wrap(ct.runtimeClass).isInstance(v) ||
            ct == ClassTag.Nothing || (ct == ClassTag.Unit && v == ((): Unit))
        }.asInstanceOf[Option[T]]
    }
  }
}
