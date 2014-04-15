package models.view

import app.ConfigProperties._
import models.domain.ChangeOfCircs
import controllers.routes
import models.domain.Claim

object CachedChangeOfCircs {
  val key = "change-of-circs"
}

trait CachedChangeOfCircs extends CachedClaim {

  override val cacheKey = CachedChangeOfCircs.key

  override val startPage: String = getProperty("cofc.start.page", "/circumstances/identification/about-you")

  override val timeoutPage = routes.CircsEnding.timeout()

  override val errorPage = routes.CircsEnding.error()

  override def newInstance: Claim = new Claim(cacheKey) with ChangeOfCircs

  override def copyInstance(claim: Claim): Claim = new Claim(claim.key, claim.sections, claim.created, claim.lang,claim.transactionId)(claim.navigation) with ChangeOfCircs
}