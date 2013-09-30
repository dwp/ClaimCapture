package models.view

import models.domain.ChangeOfCircs
import models.domain.Claim

object CachedChangeOfCircs {
  val key = "change-of-circs"
}

trait CachedChangeOfCircs extends CachedClaim {
  override val cacheKey = CachedChangeOfCircs.key

  override val timeOutURL = "/circs-timeout"

  override def newInstance: Claim = new Claim(cacheKey) with ChangeOfCircs

  override def copyInstance(claim: Claim): Claim = new Claim(claim.key, claim.sections)(claim.navigation) with ChangeOfCircs
}