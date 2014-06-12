package services

import app.ConfigProperties._
import java.util.concurrent.TimeUnit
import org.feijoas.mango.common.cache._
import play.api.Logger

object CacheService {
  // the function to cache - str is already the encrypted fingerprint
  val buildHashOfClaim = (str: String) => str

  val timeout = getProperty("claim.cache.expiry", 10)

  // create a cache with expiration time of 10 minutes
  val cache = CacheBuilder.newBuilder()
    .expireAfterWrite(timeout, TimeUnit.MINUTES)
    .build(buildHashOfClaim) //> cache  : LoadingCache[String,String]
}

trait CacheService extends EncryptionService {
  private val cache = CacheService.cache

  private def getEncryptedFingerprintFromClaim(fingerprint: String): String = digest(fingerprint)

  def storeInCache(fingerprint: String): Unit = {
    val claimFingerprint = getEncryptedFingerprintFromClaim(fingerprint)
    Logger.debug("CacheService: fingerprint is: "+ claimFingerprint)
    cache.put(claimFingerprint, claimFingerprint)
  }

  def getFromCache(fingerprint: String): Option[String] = {
    val key = getEncryptedFingerprintFromClaim(fingerprint)
    Logger.debug("CacheService: Getting key: "+ key)
    cache.getIfPresent(key)
  }

  // shall we check for the key's existence before attempting to remove it?
  def removeFromCache(fingerprint: String): Unit = {
    val key = getEncryptedFingerprintFromClaim(fingerprint)
    Logger.debug("CacheService: Removing key: "+ key)
    cache.invalidate(key)
  }

}

