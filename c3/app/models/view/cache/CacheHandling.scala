package models.view.cache

import app.ConfigProperties._
import models.domain._
import models.view.ClaimHandling
import monitoring.Histograms
import net.sf.ehcache.CacheManager
import play.api.Logger
import play.api.Play.current
import play.api.cache.CacheApi
import play.api.mvc.{AnyContent, Request}
import utils.SaveForLaterEncryption
import scala.concurrent.duration._

import scala.concurrent.duration.Duration
import scala.util.{Success, Try, Failure}

protected trait CacheHandling {
  val cache = current.injector.instanceOf[CacheApi]
  val saveForLaterKey = "SFL-"
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

  def createSaveForLaterKey(claim: Claim): String = {
    val yourDetails = claim.section(AboutYou).questionGroup(YourDetails).get.asInstanceOf[YourDetails]
    yourDetails.firstName.toUpperCase() + yourDetails.surname.toUpperCase +
      yourDetails.nationalInsuranceNumber.nino.getOrElse("").toUpperCase +
      yourDetails.dateOfBirth.`yyyy-MM-dd`
  }

  def decryptClaim(saveForLaterUuid: String, saveForLater: SaveForLater, resumeSaveForLater: ResumeSaveForLater): SaveForLater = {
    if (saveForLater.remainingAuthenticationAttempts > 0) {
      val key = createSaveForLaterKey(resumeSaveForLater)
      Try (SaveForLaterEncryption.decryptClaim(key, saveForLater.claim)) match {
        case Failure(e) =>
          //reset status to FAILED-FINAL, save status and remove claim from cache when attempts get to 0
          //otherwise send FAILED-RETRY but not save in cache
          val remainingAttempts = saveForLater.remainingAuthenticationAttempts - 1
          remainingAttempts match {
            case 0 => return updateSaveForLaterInCacheAndRemoveClaim(saveForLaterUuid, saveForLater, 0, "FAILED-FINAL")
            case _ => return updateSaveForLaterInCache(saveForLaterUuid, saveForLater, remainingAttempts)
          }
        case Success(s) =>
          saveInCache(s)
          return updateSaveForLaterInCache(saveForLaterUuid, saveForLater, CacheHandling.saveForLaterAuthenticationAttempts)
      }
    }
    saveForLater
  }

  def updateSaveForLaterInCache(saveForLaterUuid: String, saveForLater: SaveForLater, remainingAuthenticationAttempts : Int) = {
    val updatedSaveForLater = saveForLater.update(remainingAuthenticationAttempts)
    cache.set(s"$saveForLaterKey$saveForLaterUuid", updatedSaveForLater, Duration(CacheHandling.saveForLaterCacheExpiry + CacheHandling.saveForLaterGracePeriod, DAYS))
    if (remainingAuthenticationAttempts != CacheHandling.saveForLaterAuthenticationAttempts) updatedSaveForLater.update("FAILED-RETRY")
    else updatedSaveForLater
  }

  def updateSaveForLaterInCacheAndRemoveClaim(saveForLaterUuid: String, saveForLater: SaveForLater, remainingAuthenticationAttempts: Int, status: String) = {
    val updatedSaveForLater = saveForLater.update(status, remainingAuthenticationAttempts, null)
    cache.set(s"$saveForLaterKey$saveForLaterUuid", updatedSaveForLater, Duration(CacheHandling.saveForLaterCacheExpiry + CacheHandling.saveForLaterGracePeriod, DAYS))
    updatedSaveForLater
  }

  def resumeSaveForLaterFromCache(resumeSaveForLater: ResumeSaveForLater, uuid: String): Option[SaveForLater] = {
    cache.get[SaveForLater](s"$saveForLaterKey$uuid") match {
      case Some(saveForLater) =>
        if (saveForLater.status == "OK") {
          Some(decryptClaim(uuid, saveForLater, resumeSaveForLater))
        } else Some(saveForLater)
      case _ => None
    }
  }

  def checkSaveForLaterInCache(uuid: String) = {
    cache.get[SaveForLater](s"$saveForLaterKey$uuid") match {
      case Some(saveForLater) => saveForLater.status
      case _ => "NO-CLAIM"
    }
  }

  def saveForLaterInCache(claim: Claim, path: String): Unit = {
    val key = createSaveForLaterKey(claim)
    val uuid = claim.uuid
    val saveForLater = new SaveForLater(claim = SaveForLaterEncryption.encryptClaim(claim, key), location = path,
                    remainingAuthenticationAttempts = CacheHandling.saveForLaterAuthenticationAttempts,
                    status="OK", applicationExpiry = System.currentTimeMillis() + Duration(CacheHandling.saveForLaterCacheExpiry, DAYS).toMillis, appVersion = ClaimHandling.C3VERSION_VALUE)
    cache.set(s"$saveForLaterKey$uuid", saveForLater, Duration(CacheHandling.saveForLaterCacheExpiry + CacheHandling.saveForLaterGracePeriod, DAYS))
  }
}

object CacheHandling {
  // Expiration values
  lazy val expiration = getProperty("cache.expiry", 3600)

  lazy val saveForLaterCacheExpiry = getProperty("cache.saveForLaterCacheExpiry", 5)

  lazy val saveForLaterGracePeriod = getProperty("cache.saveForLaterGracePeriod", 25)

  lazy val saveForLaterAuthenticationAttempts = getProperty("cache.saveForLaterAuthenticationAttempts", 3)
}
