package controllers.s8_self_employment

import language.reflectiveCalls
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.mvc.Controller
import play.api.mvc.Request
import play.api.mvc.AnyContent
import controllers.Mappings._
import models.domain.{DigitalForm, AboutSelfEmployment}
import models.view.CachedClaim
import utils.helpers.CarersForm._
import SelfEmployment._
import models.view.Navigable

object G1AboutSelfEmployment extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "areYouSelfEmployedNow" -> nonEmptyText.verifying(validYesNo),
    "whenDidYouStartThisJob" -> dayMonthYear.verifying(validDate),
    "whenDidTheJobFinish" -> optional(dayMonthYear.verifying(validDate)),
    "haveYouCeasedTrading" -> optional(text.verifying(validYesNo)),
    "natureOfYourBusiness" -> optional(text(maxLength = sixty))
  )(AboutSelfEmployment.apply)(AboutSelfEmployment.unapply)
    .verifying("whenDidTheJobFinish.error.required", validateWhenDidTheJobFinish _))

  def validateWhenDidTheJobFinish(f: AboutSelfEmployment) = f.areYouSelfEmployedNow match {
    case `no` => f.whenDidTheJobFinish.isDefined
    case _ => true
  }

  def present = executeOnForm {
    implicit claim => implicit request =>
      presentConditionally(aboutSelfEmployment)
  }

  def aboutSelfEmployment(implicit claim: DigitalForm, request: Request[AnyContent]): FormResult = {
    track(AboutSelfEmployment) {
      implicit claim => Ok(views.html.s8_self_employment.g1_aboutSelfEmployment(form.fill(AboutSelfEmployment)))
    }
  }

  def submit = executeOnForm {
    implicit claim => implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => {
          val formWithErrorsUpdate = formWithErrors.replaceError("", "whenDidTheJobFinish.error.required", FormError("whenDidTheJobFinish", "error.required"))
          BadRequest(views.html.s8_self_employment.g1_aboutSelfEmployment(formWithErrorsUpdate))},
        f => claim.update(f) -> Redirect(routes.G2SelfEmploymentYourAccounts.present()))
  }
}