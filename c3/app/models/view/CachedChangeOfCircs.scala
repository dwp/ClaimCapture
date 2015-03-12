package models.view

import java.util.UUID._

import app.ConfigProperties._
import controllers.routes
import models.domain.{ChangeOfCircs, CircumstancesReportChange, Claim}

object CachedChangeOfCircs {
  val key = "change-of-circs"
}

trait CachedChangeOfCircs extends ClaimHandling {

  override lazy val cacheKey = CachedChangeOfCircs.key

  override lazy val startPage: String = getProperty("cofc.start.page", "/circumstances/identification/about-you")
  override lazy val timeoutPage = routes.CircsEnding.timeout()
  override lazy val errorPageCookie = routes.CircsEnding.errorCookie()
  override lazy val errorPage = routes.CircsEnding.error()
  override lazy val errorPageBrowserBackButton = routes.CircsEnding.errorBrowserBackbutton()

  override def newInstance(newuuid:String = randomUUID.toString): Claim = new Claim(cacheKey,uuid = newuuid) with ChangeOfCircs

  override def copyInstance(claim: Claim): Claim = new Claim(claim.key, claim.sections, claim.created, claim.lang,claim.uuid,claim.transactionId)(claim.navigation) with ChangeOfCircs

  override protected def claimNotValid(claim:Claim):Boolean = {
    claim.questionGroup[CircumstancesReportChange] match {
      case None => true
      case Some(CircumstancesReportChange(_,fullname,nino,_,_,_)) if fullname.isEmpty || nino.nino.isEmpty => true
      case _ => false
    }
  }

}