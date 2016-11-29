package models.view

import java.util.UUID._

import app.ConfigProperties._
import controllers.routes
import models.domain.{CircumstancesYourDetails, Claim}

object CachedChangeOfCircs {
  val key = "change-of-circs"
}

trait CachedChangeOfCircs extends ClaimHandling {

  override lazy val cacheKey = CachedChangeOfCircs.key

  override lazy val startPage: String = getStringProperty("cofc.start.page")
  override lazy val timeoutPage = routes.CircsEnding.timeout()
  override lazy val errorPageCookie = routes.CircsEnding.errorCookie()
  override lazy val errorPage = routes.CircsEnding.error()
  override lazy val errorPageBrowserBackButton = routes.CircsEnding.errorBrowserBackbutton()
  override lazy val backButtonPage = controllers.routes.Application.backButtonCircsPage()

  override def newInstance(newuuid: String = randomUUID.toString): Claim = new Claim(cacheKey, uuid = newuuid)

  override def copyInstance(claim: Claim): Claim = new Claim(claim.key, claim.sections, claim.created, claim.lang, claim.gacid, claim.uuid, claim.transactionId)(claim.navigation)

  override protected def claimNotValid(claim: Claim): Boolean = {
    claim.questionGroup[CircumstancesYourDetails] match {
      case None => true
      case Some(CircumstancesYourDetails(firstname, surname, nino, _, _, _, _, _, _, _, _)) if firstname.isEmpty || surname.isEmpty || nino.nino.isEmpty => true
      case _ => false
    }
  }

}
