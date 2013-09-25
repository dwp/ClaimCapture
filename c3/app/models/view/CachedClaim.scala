package models.view

import models.domain.Claim

object CachedClaim {
  val key = "claim"
}

trait CachedClaim extends CachedDigitalForm {

  def timeoutUrl = "/timeout"

  def buildForm = new Claim()

  def cacheKey = CachedClaim.key
}