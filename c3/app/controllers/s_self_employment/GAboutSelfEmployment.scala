package controllers.s_self_employment

import play.api.Play._

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
import models.view.ClaimHandling.ClaimResult
import play.api.i18n._

object GAboutSelfEmployment extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "areYouSelfEmployedNow" -> carersNonEmptyText.verifying(validYesNo),
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

  def present = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    presentConditionally(aboutSelfEmployment)
  }

  private def aboutSelfEmployment(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    track(AboutSelfEmployment) {
      implicit claim => Ok(views.html.s_self_employment.g_aboutSelfEmployment(form.fill(AboutSelfEmployment)))
    }
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
  form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors.replaceError("", "whenDidTheJobFinish.error.required", FormError("whenDidTheJobFinish", errorRequired))
        BadRequest(views.html.s_self_employment.g_aboutSelfEmployment(formWithErrorsUpdate))},
      f => claim.update(f) -> Redirect(routes.GSelfEmploymentYourAccounts.present()))
  }
}
