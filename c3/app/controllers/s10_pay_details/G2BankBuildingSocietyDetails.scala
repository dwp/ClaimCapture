package controllers.s10_pay_details

import language.reflectiveCalls
import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import utils.helpers.CarersForm._
import controllers.Mappings._
import controllers.s10_pay_details.PayDetails._
import app.AccountStatus
import controllers.CarersForms

object G2BankBuildingSocietyDetails extends Controller with PayDetailsRouting with CachedClaim {
  val form = Form(
    mapping(
      "accountHolderName" -> CarersForms.carersNonEmptyText(maxLength = 40),
      "whoseNameIsTheAccountIn" -> nonEmptyText,
      "bankFullName" -> nonEmptyText(maxLength = 100),
      "sortCode" -> (sortCode verifying requiredSortCode),
      "accountNumber" -> nonEmptyText(minLength = 6, maxLength = 10),
      "rollOrReferenceNumber" -> text(maxLength = 18)
    )(BankBuildingSocietyDetails.apply)(BankBuildingSocietyDetails.unapply))

  def present = claiming { implicit claim => implicit request =>
    whenSectionVisible {
      val iAmVisible = claim.questionGroup(HowWePayYou) match {
        case Some(y: HowWePayYou) => y.likeToBePaid == AccountStatus.BankBuildingAccount.name
        case _ => true
      }

      if (iAmVisible) Ok(views.html.s10_pay_details.g2_bankBuildingSocietyDetails(form.fill(BankBuildingSocietyDetails), completedQuestionGroups(BankBuildingSocietyDetails)))
      else claim.delete(BankBuildingSocietyDetails) -> Redirect(routes.PayDetails.completed())
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s10_pay_details.g2_bankBuildingSocietyDetails(formWithErrors, completedQuestionGroups(BankBuildingSocietyDetails))),
      howWePayYou => claim.update(howWePayYou) -> Redirect(routes.PayDetails.completed()))
  }
}