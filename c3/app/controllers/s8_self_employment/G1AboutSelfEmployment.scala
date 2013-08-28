package controllers.s8_self_employment

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.domain.AboutSelfEmployment
import models.view.CachedClaim
import utils.helpers.CarersForm._
import SelfEmployment._
import models.view.Navigable
import models.domain.Claim
import play.api.mvc.Request
import play.api.mvc.AnyContent

object G1AboutSelfEmployment extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "areYouSelfEmployedNow" -> nonEmptyText.verifying(validYesNo),
    "whenDidYouStartThisJob" -> optional(dayMonthYear.verifying(validDateOnly)),
    "whenDidTheJobFinish" -> optional(dayMonthYear.verifying(validDateOnly)),
    "haveYouCeasedTrading" -> optional(text.verifying(validYesNo)),
    "natureOfYourBusiness" -> optional(text(maxLength = sixty))
  )(AboutSelfEmployment.apply)(AboutSelfEmployment.unapply))

  def present = claiming { implicit claim => implicit request =>
    presentConditionally(aboutSelfEmployment)
  }

  def aboutSelfEmployment(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    track(AboutSelfEmployment) { implicit claim => Ok(views.html.s8_self_employment.g1_aboutSelfEmployment(form.fill(AboutSelfEmployment)))}
  }
  
  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s8_self_employment.g1_aboutSelfEmployment(formWithErrors)),
      f => claim.update(f) -> Redirect(routes.G2SelfEmploymentYourAccounts.present()))
  }
}