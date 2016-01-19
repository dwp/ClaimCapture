package models.view.cache

import app.ConfigProperties._
import gov.dwp.carers.play2.resilientmemcached.MemcachedCacheApi
import models.domain._
import models.view.ClaimHandling
import monitoring.Histograms
import net.sf.ehcache.CacheManager
import net.spy.memcached.CASMutation
import org.joda.time.DateTime
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
  val saveForLaterKey = "SFL"

  def cacheKey: String

  def keyFrom(request: Request[AnyContent]): String = request.session.get(cacheKey).getOrElse("")

  def cookieAppVersion(request: Request[AnyContent]): String = {
    request.cookies.get(ClaimHandling.C3VERSION) match {
      case Some(cookie) => cookie.value
      case None => "N/A"
    }
  }

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
      Logger.info("Retrieving cache entry for request key:" + key + " with cookie application version:" + cookieAppVersion(request))
      cache.get[Claim](key)
    }
  }

  def fromCache(request: Request[AnyContent], key: String): Option[Claim] = {
    Logger.info("Retrieving cache entry for key:" + key + " with cookie application version:" + cookieAppVersion(request))
    cache.get[Claim](key)
  }

  def saveInCache(claim: Claim) = cache.set(claim.uuid, claim, Duration(CacheHandling.expiration, SECONDS))

  def removeFromCache(key: String) = cache.remove(key)

  protected def recordMeasurements() = {
    Histograms.recordCacheSize(Try(CacheManager.getInstance().getCache("play").getKeysWithExpiryCheck.size()).getOrElse(0))
  }

  def secsToDate(secs: Long) = {
    new DateTime(secs * 1000)
  }

  def claimExpirySecs() = {
    CacheHandling.saveForLaterCacheExpiry
  }

  def claimExpiryDate() = {
    DateTime.now.plusSeconds(claimExpirySecs)
  }

  def claimExpiryDateSecs() = {
    claimExpiryDate.getMillis / 1000
  }

  def memcacheExpirySecs() = {
    CacheHandling.saveForLaterCacheExpiry + CacheHandling.saveForLaterGracePeriod
  }

  def memcacheExpiryDate() = {
    DateTime.now.plusSeconds(memcacheExpirySecs)
  }

  def memcacheExpiryDateSecs() = {
    memcacheExpiryDate().getMillis / 1000
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
      Try(SaveForLaterEncryption.decryptClaim(key, saveForLater.claim)) match {
        case Failure(e) => return setSaveForLaterRemainingAttempts(saveForLaterUuid, saveForLater)
        case Success(s) =>
          saveInCache(s)
          return updateSaveForLaterInCache(saveForLaterUuid, saveForLater, CacheHandling.saveForLaterAuthenticationAttempts, claimExpiryDateSecs, memcacheExpiryDateSecs())
      }
    }
    saveForLater
  }

  def setSaveForLaterRemainingAttempts(saveForLaterUuid: String, saveForLater: SaveForLater): SaveForLater = {
    //reset status to FAILED-FINAL, save status and remove claim from cache when attempts get to 0
    //otherwise send FAILED-RETRY but not save in cache
    val remainingAttempts = saveForLater.remainingAuthenticationAttempts - 1
    remainingAttempts match {
      case 0 => updateSaveForLaterInCacheAndRemoveClaim(saveForLaterUuid, saveForLater, 0, "FAILED-FINAL")
      case _ => updateSaveForLaterInCache(saveForLaterUuid, saveForLater, remainingAttempts, saveForLater.applicationExpiry, saveForLater.cacheExpiryPeriod)
    }
  }

  def updateSaveForLaterInCache(saveForLaterUuid: String, saveForLater: SaveForLater, remainingAuthenticationAttempts: Int, applicationExpiry: Long, cacheExpiry: Long) = {
    val updatedSaveForLater = saveForLater.update(remainingAuthenticationAttempts, applicationExpiry, cacheExpiry)
    cache.set(s"$saveForLaterKey-$saveForLaterUuid", updatedSaveForLater, Duration(updatedSaveForLater.cacheExpiryPeriod, SECONDS))
    Logger.info(s"SFL updateSaveForLater $saveForLaterKey-$saveForLaterUuid updated with triesleft:$remainingAuthenticationAttempts claim expires:" + secsToDate(updatedSaveForLater.applicationExpiry) + " and memcache expires:" + secsToDate(updatedSaveForLater.cacheExpiryPeriod))
    if (remainingAuthenticationAttempts != CacheHandling.saveForLaterAuthenticationAttempts) updatedSaveForLater.update("FAILED-RETRY-LEFT" + remainingAuthenticationAttempts)
    else updatedSaveForLater
  }

  def updateSaveForLaterInCacheAndRemoveClaim(saveForLaterUuid: String, saveForLater: SaveForLater, remainingAuthenticationAttempts: Int, status: String) = {
    val updatedSaveForLater = saveForLater.update(status, remainingAuthenticationAttempts, null)
    cache.set(s"$saveForLaterKey-$saveForLaterUuid", updatedSaveForLater, Duration(updatedSaveForLater.cacheExpiryPeriod, SECONDS))
    Logger.info(s"SFL updateSaveForLater $saveForLaterKey-$saveForLaterUuid updated with status:$updatedSaveForLater.status claim expires:" + secsToDate(updatedSaveForLater.applicationExpiry) + " and memcache expires:" + secsToDate(updatedSaveForLater.cacheExpiryPeriod))
    updatedSaveForLater
  }

  def resumeSaveForLaterFromCache(resumeSaveForLater: ResumeSaveForLater, uuid: String): Option[SaveForLater] = {
    cache.get[SaveForLater](s"$saveForLaterKey-$uuid") match {
      case Some(saveForLater) =>
        if (saveForLater.status == "OK") {
          Logger.info(s"SFL resumeSaveForLater resumed $saveForLaterKey-$uuid with status OK. Claim expiry:" + secsToDate(saveForLater.applicationExpiry) + " Cache expiry:" + secsToDate(saveForLater.cacheExpiryPeriod))
          Some(decryptClaim(uuid, saveForLater, resumeSaveForLater))
        } else {
          Logger.info(s"SFL resumeSaveForLater $saveForLaterKey-$uuid failed resume with status ${saveForLater.status}")
          Some(saveForLater)
        }
      case _ => {
        Logger.info(s"SFL resumeSaveForLater $saveForLaterKey-$uuid failed resume no claim found in memcache")
        None
      }
    }
  }

  def checkSaveForLaterInCache(uuid: String) = {
    cache.get[SaveForLater](s"$saveForLaterKey-$uuid") match {
      case Some(saveForLater) if saveForLater.remainingAuthenticationAttempts == 2 => "FAILED-RETRY-LEFT" + saveForLater.remainingAuthenticationAttempts
      case Some(saveForLater) if saveForLater.remainingAuthenticationAttempts == 1 => "FAILED-RETRY-LEFT" + saveForLater.remainingAuthenticationAttempts
      case Some(saveForLater) => {
        Logger.info(s"SFL checkSaveForLater $uuid with status " + saveForLater.status + " Claim expiry:" + secsToDate(saveForLater.applicationExpiry) + " Cache expiry:" + secsToDate(saveForLater.cacheExpiryPeriod))
        saveForLater.status
      }
      case _ => "NO-CLAIM"
    }
  }

  def createClaimInSaveForLaterList(uuid: String): Unit = {
    setSaveForLaterListInCache(uuid, Duration(0, SECONDS))
  }

  def saveForLaterInCache(claim: Claim, path: String): Unit = {
    val key = createSaveForLaterKey(claim)
    val uuid = claim.uuid
    val saveForLater = new SaveForLater(claim = SaveForLaterEncryption.encryptClaim(claim, key), location = path,
      remainingAuthenticationAttempts = CacheHandling.saveForLaterAuthenticationAttempts, status = "OK",
      applicationExpiry = claimExpiryDateSecs,
      cacheExpiryPeriod = memcacheExpiryDateSecs,
      appVersion = ClaimHandling.C3VERSION_VALUE)
    cache.set(s"$saveForLaterKey-$uuid", saveForLater, Duration(saveForLater.cacheExpiryPeriod, SECONDS))
    Logger.info(s"SFL save $saveForLaterKey-$uuid saved with claim expires:" + secsToDate(saveForLater.applicationExpiry) + " and memcache expires:" + secsToDate(saveForLater.cacheExpiryPeriod))
    createClaimInSaveForLaterList(s"$saveForLaterKey-$uuid")
  }

  def setSaveForLaterListInCache(uuid: String, expiration: Duration): Unit = {
    isMemcached match {
      case true => {
        Logger.info("Using memcached CAS to store values")
        val memcached = cache.asInstanceOf[MemcachedCacheApi]
        memcached.setCASList(createKeyInSaveListMutation(uuid), List[String](uuid), saveForLaterKey, expiration)
      }
      case _ => cache.set(saveForLaterKey, uuid :: cache.get[List[String]](saveForLaterKey).getOrElse(List[String]()).filter(_ > uuid), expiration)
    }
  }

  private def removeSaveForLaterClaimKeyFromList(uuid: String) = {
    isMemcached match {
      case true => {
        Logger.info("Using memcached CAS to remove values")
        val memcached = cache.asInstanceOf[MemcachedCacheApi]
        memcached.setCASList(removeKeyInSaveListMutation(uuid), List[String](), saveForLaterKey, Duration(0, SECONDS))
      }
      case _ => cache.set(saveForLaterKey, cache.get[List[String]](saveForLaterKey).getOrElse(List[String]()).filter(_ > uuid), Duration(0, SECONDS))
    }
  }

  private def createKeyInSaveListMutation(newKey: String) = {
    new CASMutation[List[String]] {
      // This is only invoked when a value actually exists.
      def getNewValue(currentList: List[String]): List[String] = newKey :: currentList.filter(_ > newKey)
    }
  }

  private def removeKeyInSaveListMutation(newKey: String) = {
    new CASMutation[List[String]] {
      // This is only invoked when a value actually exists.
      def getNewValue(currentList: List[String]): List[String] = currentList.filter(_ > newKey)
    }
  }

  def removeSaveForLaterFromCache(uuid: String): Unit = {
    cache.remove(s"$saveForLaterKey-$uuid")
    Logger.info(s"SFL save $saveForLaterKey-$uuid removed")
    removeSaveForLaterClaimKeyFromList(uuid)
  }

  private def isMemcached: Boolean = {
    if (cache.isInstanceOf[MemcachedCacheApi]) return true
    return false
  }

}

object CacheHandling {
  // Expiration values
  lazy val expiration = getProperty("cache.expiry", 3600)

  lazy val saveForLaterCacheExpiry = {
    val expiry = getProperty("cache.saveForLaterCacheExpirySecs", 0)
    Logger.info("SaveForLater initialised with expiry:" + expiry + " secs")
    expiry
  }

  lazy val saveForLaterGracePeriod = {
    val grace = getProperty("cache.saveForLaterGracePeriodSecs", 0)
    Logger.info("SaveForLater initialised with grace:" + grace + " secs")
    grace
  }

  lazy val saveForLaterAuthenticationAttempts = {
    val attempts = getProperty("cache.saveForLaterAuthenticationAttempts", 0)
    Logger.info("SaveForLater initialised with max authent attempts:" + attempts)
    attempts
  }
}
