package controllers.s11_pay_details

import language.reflectiveCalls
import play.api.mvc.{AnyContent, Request, Controller}
import play.api.data.Form
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import models.domain._
import utils.helpers.CarersForm._
import controllers.mappings.Mappings._
import controllers.s11_pay_details.PayDetails._
import app.AccountStatus
import controllers.CarersForms._
import models.domain.Claim
import play.api.i18n.Lang
import models.view.ClaimHandling.ClaimResult

object G2BankBuildingSocietyDetails extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "accountHolderName" -> carersNonEmptyText(maxLength = 40),
    "bankFullName" -> carersNonEmptyText(maxLength = 100),
    "sortCode" -> (sortCode verifying requiredSortCode),
    "accountNumber" -> carersNonEmptyText(minLength = 6, maxLength = 10),
    "rollOrReferenceNumber" -> carersText(maxLength = 18)
  )(BankBuildingSocietyDetails.apply)(BankBuildingSocietyDetails.unapply))

  def present = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
    presentConditionally(bankBuildingSocietyDetails(lang))
  }


  private def bankBuildingSocietyDetails(lang: Lang)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    val iAmVisible = claim.questionGroup(HowWePayYou) match {
      case Some(y: HowWePayYou) => y.likeToBePaid == AccountStatus.BankBuildingAccount
      case _ => true
    }

    if (iAmVisible) track(BankBuildingSocietyDetails) { implicit claim => Ok(views.html.s11_pay_details.g2_bankBuildingSocietyDetails(form.fill(BankBuildingSocietyDetails))(lang)) }
    else claim.delete(BankBuildingSocietyDetails) -> redirectPath
  }

  def submit = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val updatedFormWithErrors = manageErrorsSortCode(formWithErrors)
        val afterIgnoreGroupBy = ignoreGroupByForSortCode(updatedFormWithErrors)
        BadRequest(views.html.s11_pay_details.g2_bankBuildingSocietyDetails(afterIgnoreGroupBy)(lang))
      },
      howWePayYou => claim.update(howWePayYou) -> redirectPath)
  }
}