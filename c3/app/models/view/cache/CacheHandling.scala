package models.view.cache

import app.ConfigProperties._
import models.domain._
import monitoring.Histograms
import net.sf.ehcache.CacheManager
import play.api.Logger
import play.api.Play.current
import play.api.cache.CacheApi
import play.api.mvc.{AnyContent, Request}
import utils.SaveForLaterEncryption
import scala.concurrent.duration._

import scala.concurrent.duration.Duration
import scala.util.Try

protected trait CacheHandling {
  val cache = current.injector.instanceOf[CacheApi]
  def cacheKey: String

  def keyFrom(request: Request[AnyContent]): String = request.session.get(cacheKey).getOrElse("")

  /**
   * Tries to get the claim of change of circs from the cache.
   * @param request the http request that has the session with uuid of claim which is the key used by cache.
   * @return None if could not find claim/CoCs. Some(claim) is could find it.
   */
  def fromCache(request: Request[AnyContent], required: Boolean = true): Option[Claim] = {
    val key = keyFrom(request)
    if (key.isEmpty) {
      if (required) {
        // Log an error if session empty or with no cacheKey entry so we know it is not a cache but a cookie issue.
        Logger.error(s"Did not receive Session information for a $cacheKey for ${request.method} url path ${request.path} and agent ${request.headers.get("User-Agent").getOrElse("Unknown agent")}. Probably a cookie issue: ${request.cookies.filterNot(_.name.startsWith("_"))}.")
      }
      None
    } else {
      cache.get[Claim](key)
    }
  }

  def saveInCache(claim: Claim) = cache.set(claim.uuid, claim, Duration(CacheHandling.expiration, SECONDS))

  def removeFromCache(key: String) = cache.remove(key)

  protected def recordMeasurements() = {
    Histograms.recordCacheSize(Try(CacheManager.getInstance().getCache("play").getKeysWithExpiryCheck.size()).getOrElse(0))
  }

  def createSaveForLaterKey(resumeSaveForLater: ResumeSaveForLater): String = {
    resumeSaveForLater.firstName.toUpperCase() + resumeSaveForLater.surname.toUpperCase +
    resumeSaveForLater.nationalInsuranceNumber.nino.getOrElse("").toUpperCase +
    resumeSaveForLater.dateOfBirth.`yyyy-MM-dd`
  }

  def resumeSaveForLaterFromCache(resumeSaveForLater: ResumeSaveForLater): Option[SaveForLater] = {
    val key = createSaveForLaterKey(resumeSaveForLater)
    cache.get[SaveForLater](key)
  }

  def createSaveForLaterKey(claim: Claim): String = {
    val yourDetails = claim.section(AboutYou).questionGroup(YourDetails).get.asInstanceOf[YourDetails]
    yourDetails.firstName.toUpperCase() + yourDetails.surname.toUpperCase +
    yourDetails.nationalInsuranceNumber.nino.getOrElse("").toUpperCase +
    yourDetails.dateOfBirth.`yyyy-MM-dd`
  }

  def saveForLaterInCache(claim: Claim, path: String): Unit = {
    val key = createSaveForLaterKey(claim)
    val saveForLater = new SaveForLater(claim = SaveForLaterEncryption.encryptClaim(claim, key), location = path,
                    remainingAuthenticationAttempts = 0, status="ok", expiryDateTime = System.currentTimeMillis())
    cache.set(claim.uuid, saveForLater, Duration(CacheHandling.saveForLaterExpiration, DAYS))
  }
}

object CacheHandling {
  // Expiration value
  lazy val expiration = getProperty("cache.expiry", 3600)

  lazy val saveForLaterExpiration = getProperty("cache.saveForLaterExpiry", 30)
}
