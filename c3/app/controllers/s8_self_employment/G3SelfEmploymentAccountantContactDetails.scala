package controllers.s8_self_employment

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.domain._
import models.view.CachedClaim
import utils.helpers.CarersForm._
import controllers.Mappings
import controllers.s8_self_employment.SelfEmployment.whenSectionVisible

object G3SelfEmploymentAccountantContactDetails extends Controller with SelfEmploymentRouting with CachedClaim {
  val form = Form(
    mapping(
      "accountantsName" -> nonEmptyText(maxLength = Mappings.hundred),
      "address" -> address.verifying(requiredAddress),
      "postcode" -> optional(text verifying validPostcode),
      "telephoneNumber" -> optional(text verifying validPhoneNumber),
      "faxNumber" -> optional(text verifying validPhoneNumber)
    )(SelfEmploymentAccountantContactDetails.apply)(SelfEmploymentAccountantContactDetails.unapply)
  )

  def present = claiming { implicit claim => implicit request =>
    val doYouHaveAnAccountant = true

    doYouHaveAnAccountant match {
      case true => whenSectionVisible(Ok(views.html.s8_self_employment.g3_selfEmploymentAccountantContactDetails(form.fill(SelfEmploymentAccountantContactDetails), completedQuestionGroups(SelfEmploymentAccountantContactDetails))))
      case false => claim.delete(SelfEmploymentAccountantContactDetails) -> Redirect(routes.G4SelfEmploymentPensionsAndExpenses.present())
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s8_self_employment.g3_selfEmploymentAccountantContactDetails(formWithErrors, completedQuestionGroups(SelfEmploymentAccountantContactDetails))),
      f => claim.update(f) -> Redirect(routes.G4SelfEmploymentPensionsAndExpenses.present()))
  }
}
