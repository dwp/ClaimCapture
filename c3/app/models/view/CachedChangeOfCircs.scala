package models.view

import java.util.UUID._

import app.ConfigProperties._
import controllers.routes
import models.domain.{CircumstancesReportChange, Claim}

object CachedChangeOfCircs {
  val key = "change-of-circs"
}

trait CachedChangeOfCircs extends ClaimHandling {

  override lazy val cacheKey = CachedChangeOfCircs.key

  override lazy val startPage: String = getProperty("cofc.start.page", controllers.circs.s1_start_of_process.routes.G1ReportChanges.present().url)
  override lazy val timeoutPage = routes.CircsEnding.timeout()
  override lazy val errorPageCookie = routes.CircsEnding.errorCookie()
  override lazy val errorPage = routes.CircsEnding.error()
  override lazy val errorPageBrowserBackButton = routes.CircsEnding.errorBrowserBackbutton()
  override lazy val backButtonPage = controllers.routes.Application.backButtonCircsPage()

  override def newInstance(newuuid:String = randomUUID.toString): Claim = new Claim(cacheKey,uuid = newuuid)

  override def copyInstance(claim: Claim): Claim = new Claim(claim.key, claim.sections, claim.created, claim.lang,claim.uuid,claim.transactionId)(claim.navigation)

  override protected def claimNotValid(claim:Claim):Boolean = {
    claim.questionGroup[CircumstancesReportChange] match {
      case None => true
      case Some(CircumstancesReportChange(fullname,nino,_,_,_,_,_,_,_)) if fullname.isEmpty || nino.nino.isEmpty => true
      case _ => false
    }
  }

}
