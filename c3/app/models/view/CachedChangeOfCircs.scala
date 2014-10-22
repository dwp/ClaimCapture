package models.view

import java.util.UUID._

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

  override def newInstance(newuuid:String = randomUUID.toString): Claim = new Claim(cacheKey,uuid = newuuid) with ChangeOfCircs

  override def copyInstance(claim: Claim): Claim = new Claim(claim.key, claim.sections, claim.created, claim.lang,claim.uuid,claim.transactionId)(claim.navigation) with ChangeOfCircs
}