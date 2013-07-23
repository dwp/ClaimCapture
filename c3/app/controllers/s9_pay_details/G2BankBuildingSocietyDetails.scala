package controllers.s9_pay_details

import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import utils.helpers.CarersForm._
import controllers.Mappings._

object G2BankBuildingSocietyDetails extends Controller with CachedClaim {
  val form = Form(
    mapping(
      call(routes.G2BankBuildingSocietyDetails.present()),
      "accountHolderName" -> nonEmptyText(maxLength = sixty),
      "bankFullName" -> nonEmptyText(maxLength = 100),
      "sortCode" -> (sortCode verifying requiredSortCode),
      "accountNumber" -> nonEmptyText(minLength = 6, maxLength = 10),
      "rollOrReferenceNumber" -> text(maxLength = 18)
    )(BankBuildingSocietyDetails.apply)(BankBuildingSocietyDetails.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(BankBuildingSocietyDetails)

  def present = claiming { implicit claim => implicit request =>
    claim.questionGroup(HowWePayYou) match {
      case Some(y: HowWePayYou) if y.likeToBePaid != "01" => claim.delete(BankBuildingSocietyDetails) -> Redirect(routes.PayDetails.completed())
      case _ =>
        val currentForm: Form[BankBuildingSocietyDetails] = claim.questionGroup(BankBuildingSocietyDetails) match {
          case Some(t: BankBuildingSocietyDetails) => form.fill(t)
          case _ => form
        }

        Ok(views.html.s9_pay_details.g2_bankBuildingSocietyDetails(currentForm, completedQuestionGroups))
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s9_pay_details.g2_bankBuildingSocietyDetails(formWithErrors,claim.completedQuestionGroups(BankBuildingSocietyDetails))),
      howWePayYou => claim.update(howWePayYou) -> Redirect(routes.PayDetails.completed()))
  }
}