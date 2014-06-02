package services

import java.util.concurrent.TimeUnit
import org.feijoas.mango.common.cache._
import models.domain.{ContactDetails, ClaimDate, YourDetails, Claim}
import scala.util.control.Breaks._

object CacheService {
  // the function to cache - str is already the encrypted fingerprint
  // so no need to encrypt again
  val buildHashOfClaim = (str: String) => str //EncryptionService.encrypt(str)

  // create a cache with expiration time of 10 minutes
  val cache = CacheBuilder.newBuilder()
    .expireAfterWrite(10, TimeUnit.MINUTES)
    .build(buildHashOfClaim) //> cache  : LoadingCache[String,String]
}

trait CacheService {
  val cache = CacheService.cache

  def getFingerprintFromClaim(claim: Claim): String = {
    val nino = claim.questionGroup[YourDetails].getOrElse(YourDetails()).nationalInsuranceNumber.stringify
    val surname = claim.questionGroup[YourDetails].getOrElse(YourDetails()).surname
    val claimDate = claim.questionGroup[ClaimDate].getOrElse(ClaimDate()).dateOfClaim.`yyyy-MM-dd`.toString
    val postcode = claim.questionGroup[ContactDetails].getOrElse(ContactDetails()).postcode.getOrElse("")

    nino+surname+claimDate+postcode
  }

  def storeInCache(claim: Claim) = {
    val claimFingerprint = EncryptionService.encrypt(getFingerprintFromClaim(claim))

    cache.put(claimFingerprint, claimFingerprint)
  }

  def isInCache(claim: Claim): Boolean = {
    // iterate over the cache, compare fingerprint with existing keys
    // if match then return true, else false
    val cacheMap = cache.asMap()
    val fingerprint = getFingerprintFromClaim(claim)
    var foundDuplicate = false

    if(cacheMap.size > 0) {
      breakable {
        for(cacheEntry <- cacheMap) {
          if(EncryptionService.checkFingerprint(fingerprint,cacheEntry._1))
            foundDuplicate = true; break
        }
      }
    }

    foundDuplicate
  }

  def getFromCache(claim: Claim)= {
    // iterate over the cache, compare fingerprint with existing keys
    // if match then return key
    val cacheMap = cache.asMap()
    val fingerprint = getFingerprintFromClaim(claim)
    var key = ""

    if(cacheMap.size > 0) {
      breakable {
        for(cacheEntry <- cacheMap) {
          if(EncryptionService.checkFingerprint(fingerprint,cacheEntry._1))
            key = cacheEntry._1; break
        }
      }
    }
    key
  }

  def removeFromCache(claim: Claim) = {
    cache.invalidate(getFromCache(claim))
  }
}

