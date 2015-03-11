package models.view

import java.util.UUID._

import app.ConfigProperties._
import controllers.routes
import models.domain.{ChangeOfCircs, CircumstancesReportChange, Claim}
import models.view.ClaimHandling.ClaimResult
import play.api.mvc.{AnyContent, Request}
import play.api.mvc.Results._
import play.api.mvc._
import play.api.i18n.Lang


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

  override protected def fieldsCheck(claim:Claim):Boolean = {
    claim.questionGroup[CircumstancesReportChange] match {
      case None => true
      case Some(CircumstancesReportChange(_,fullname,nino,_,_,_)) if fullname.isEmpty || nino.nino.isEmpty => true
      case _ => false
    }
  }

//
//  /**
//   * Here we are displaying an error page at the start of the application (after the first page) if the cookies are disabled and checking
//   * for the mandatory fields if the user is using browser back and forward buttons
//   * @param f
//   * @return
//   */
//  def claimingCircsWithMandatoryFieldAndCookieCheck(f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult]): Action[AnyContent] = Action {
//    implicit request =>
//      claimingWithCondition(fieldsCheck,Redirect(errorPageCookie))(f)
//  }
}