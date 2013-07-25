package controllers.s9_self_employment

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.domain.{SelfEmploymentAccountantContactDetails, Claim}
import models.view.CachedClaim
import utils.helpers.CarersForm._
import controllers.Mappings


object G3SelfEmploymentAccountantContactDetails extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(SelfEmploymentAccountantContactDetails)

  val form = Form(
    mapping(
      call(routes.G3SelfEmploymentAccountantContactDetails.present()),
      "accountantsName" -> nonEmptyText(maxLength = Mappings.hundred),
      "address" -> address.verifying(requiredAddress),
      "postCode" -> optional(text verifying validPostcode),
      "telephoneNumber" -> optional(text verifying validPhoneNumber),
      "faxNumber" -> optional(text verifying validPhoneNumber)
    )(SelfEmploymentAccountantContactDetails.apply)(SelfEmploymentAccountantContactDetails.unapply)
  )

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s9_self_employment.g3_selfEmploymentAccountantContactDetails(form, completedQuestionGroups))
  }

  def submit = claiming { implicit claim =>
    implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s9_self_employment.g3_selfEmploymentAccountantContactDetails(formWithErrors, completedQuestionGroups)),
        f => claim.update(f) -> Redirect(routes.G3SelfEmploymentAccountantContactDetails.present())
      )
  }
}
