package services

import app.ConfigProperties._
import java.util.concurrent.TimeUnit
import org.feijoas.mango.common.cache._
import models.domain.Claim

object CacheService {
  // the function to cache - str is already the encrypted fingerprint
  val buildHashOfClaim = (str: String) => str

  var timeout = getProperty("claim.cache.expiry", 10)

  // create a cache with expiration time of 10 minutes
  val cache = CacheBuilder.newBuilder()
    .expireAfterWrite(timeout, TimeUnit.MINUTES)
    .build(buildHashOfClaim) //> cache  : LoadingCache[String,String]
}

trait CacheService {
  private val cache = CacheService.cache

  def storeInCache(claim: Claim): Unit = {
    val fingerprint = claim.getFingerprint
    cache.put(fingerprint, fingerprint)
  }

  def getFromCache(claim: Claim): Option[String] = cache.getIfPresent(claim.getFingerprint)

  def removeFromCache(claim: Claim): Unit = cache.invalidate(claim.getFingerprint)
}

