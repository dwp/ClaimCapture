package controllers.s7_self_employment

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import play.api.mvc.Request
import play.api.mvc.AnyContent
import controllers.mappings.Mappings._
import models.domain.AboutSelfEmployment
import models.view.CachedClaim
import utils.helpers.CarersForm._
import SelfEmployment._
import models.view.Navigable
import controllers.CarersForms._
import play.api.data.FormError
import models.domain.Claim
import play.api.i18n.Lang
import models.view.ClaimHandling.ClaimResult

object G1AboutSelfEmployment extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "areYouSelfEmployedNow" -> nonEmptyText.verifying(validYesNo),
    "whenDidYouStartThisJob" -> dayMonthYear.verifying(validDate),
    "whenDidTheJobFinish" -> optional(dayMonthYear.verifying(validDate)),
    "haveYouCeasedTrading" -> optional(text.verifying(validYesNo)),
    "natureOfYourBusiness" -> carersNonEmptyText(maxLength = sixty)
  )(AboutSelfEmployment.apply)(AboutSelfEmployment.unapply)
    .verifying("whenDidTheJobFinish.error.required", validateWhenDidTheJobFinish _))

  private def validateWhenDidTheJobFinish(f: AboutSelfEmployment) = f.areYouSelfEmployedNow match {
    case `no` => f.whenDidTheJobFinish.isDefined
    case _ => true
  }

  def present = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
    presentConditionally(aboutSelfEmployment(lang), lang)
  }

  private def aboutSelfEmployment(lang: Lang)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    track(AboutSelfEmployment) {
      implicit claim => Ok(views.html.s7_self_employment.g1_aboutSelfEmployment(form.fill(AboutSelfEmployment))(lang))
    }
  }

  def submit = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors.replaceError("", "whenDidTheJobFinish.error.required", FormError("whenDidTheJobFinish", errorRequired))
        BadRequest(views.html.s7_self_employment.g1_aboutSelfEmployment(formWithErrorsUpdate)(lang))},
      f => claim.update(f) -> Redirect(routes.G2SelfEmploymentYourAccounts.present()))
  }
}