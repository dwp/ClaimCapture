package controllers.s8_self_employment

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import play.api.mvc.Request
import play.api.mvc.AnyContent
import controllers.Mappings._
import models.domain.AboutSelfEmployment
import models.view.CachedClaim
import utils.helpers.CarersForm._
import SelfEmployment._
import models.view.Navigable
import controllers.CarersForms._
import play.api.data.FormError
import models.domain.Claim
import play.api.i18n.Lang
import models.view.CachedClaim.ClaimResult

object G1AboutSelfEmployment extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "areYouSelfEmployedNow" -> nonEmptyText.verifying(validYesNo),
    "whenDidYouStartThisJob" -> dayMonthYear.verifying(validDate),
    "whenDidTheJobFinish" -> optional(dayMonthYear.verifying(validDate)),
    "haveYouCeasedTrading" -> optional(text.verifying(validYesNo)),
    "natureOfYourBusiness" -> optional(carersText(maxLength = sixty))
  )(AboutSelfEmployment.apply)(AboutSelfEmployment.unapply)
    .verifying("whenDidTheJobFinish.error.required", validateWhenDidTheJobFinish _))

  def validateWhenDidTheJobFinish(f: AboutSelfEmployment) = f.areYouSelfEmployedNow match {
    case `no` => f.whenDidTheJobFinish.isDefined
    case _ => true
  }

  def present = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    presentConditionally(aboutSelfEmployment)
  }

  def aboutSelfEmployment(implicit claim: Claim, request: Request[AnyContent], lang: Lang): ClaimResult = {
    track(AboutSelfEmployment) {
      implicit claim => Ok(views.html.s8_self_employment.g1_aboutSelfEmployment(form.fill(AboutSelfEmployment)))
    }
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors.replaceError("", "whenDidTheJobFinish.error.required", FormError("whenDidTheJobFinish", "error.required"))
        BadRequest(views.html.s8_self_employment.g1_aboutSelfEmployment(formWithErrorsUpdate))},
      f => claim.update(f) -> Redirect(routes.G2SelfEmploymentYourAccounts.present()))
  }
}