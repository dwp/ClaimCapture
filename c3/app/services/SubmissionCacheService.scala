package services

import app.ConfigProperties._
import java.util.concurrent.TimeUnit
import org.feijoas.mango.common.cache._
import models.domain.Claim
import play.api.Logger

object SubmissionCacheService {
  // the function to cache - str is already the encrypted fingerprint
  val buildHashOfClaim = (str: String) => str

  val timeout = getProperty("submission.cache.expiry", 10)

  // create a cache with expiration time of 10 minutes
  val cache = CacheBuilder.newBuilder()
    .expireAfterWrite(timeout, TimeUnit.MINUTES)
    .build(buildHashOfClaim) //> cache  : LoadingCache[String,String]
}

trait SubmissionCacheService {
  private val cache = SubmissionCacheService.cache

  def checkEnabled : Boolean = {
    val checkLabel: String = "duplicate.submission.check"
    val check = getProperty(checkLabel, default = true)
    Logger.info(s"$checkLabel = $check")
    check
  }

  def storeInCache(claim: Claim): Unit = {
    val fingerprint = claim.getFingerprint
    cache.put(fingerprint, fingerprint)
  }

  def getFromCache(claim: Claim): Option[String] = cache.getIfPresent(claim.getFingerprint)

  def removeFromCache(claim: Claim): Unit = cache.invalidate(claim.getFingerprint)
}

