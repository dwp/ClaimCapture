package controllers.s8_self_employment

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.domain.CareProvidersContactDetails
import models.view.CachedClaim
import utils.helpers.CarersForm._
import controllers.s8_self_employment.SelfEmployment.whenSectionVisible

object G8CareProvidersContactDetails extends Controller with SelfEmploymentRouting with CachedClaim {
  val form = Form(
    mapping(
      "address" -> optional(address),
      "postcode" -> optional(text verifying validPostcode)
    )(CareProvidersContactDetails.apply)(CareProvidersContactDetails.unapply))

  def present = claiming { implicit claim => implicit request =>
    whenSectionVisible(Ok(views.html.s8_self_employment.g8_careProvidersContactDetails(form.fill(CareProvidersContactDetails), completedQuestionGroups(CareProvidersContactDetails))))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s8_self_employment.g8_careProvidersContactDetails(formWithErrors, completedQuestionGroups(CareProvidersContactDetails))),
      f => claim.update(f) -> Redirect(routes.SelfEmployment.completed()))
  }
}
