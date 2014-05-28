package services

import java.util.concurrent.TimeUnit
import org.feijoas.mango.common.cache._

object CacheService {
  // the function to cache
  val buildHashOfClaim = (str: String) => EncryptionService.encrypt(str)

  // create a cache with a maximum size of 100 and
  // expiration time of 10 minutes
  val cache = CacheBuilder.newBuilder()
    .maximumSize(100)
    .expireAfterWrite(10, TimeUnit.MINUTES)
    .build(buildHashOfClaim)              //> cache  : LoadingCache[String,String]

  def storeInCache(claimFingerprint: String) = cache.put(claimFingerprint, claimFingerprint)

  def getFromCache(claimFingerprint: String) = cache.getIfPresent(claimFingerprint)

}
