package services

import app.ConfigProperties._
import models.domain.Claim
import play.api.Play.current
import play.api.cache.Cache


trait SubmissionCacheService {

  private val TWO_MINUTES = 120

  def checkEnabled: Boolean = {
    val checkLabel: String = "duplicate.submission.check"
    val check = getProperty(checkLabel, default = true)
    check
  }

  def storeInCache(claim: Claim): Unit = {
    val fingerprint = claim.getFingerprint
    Cache.set(fingerprint, fingerprint, getProperty("submission.cache.expiry", default=TWO_MINUTES))
  }

  def getFromCache(claim: Claim): Option[String] = Cache.getAs[String](claim.getFingerprint)

  def removeFromCache(claim: Claim): Unit = Cache.remove(claim.getFingerprint)
}
