package controllers.s9_self_employment

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.domain.{Claim, AboutSelfEmployment}
import models.view.CachedClaim
import utils.helpers.CarersForm._



object G1AboutSelfEmployment extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(AboutSelfEmployment)

  val form = Form(
    mapping(
      call(routes.G1AboutSelfEmployment.present()),
      "areYouSelfEmployedNow" -> nonEmptyText.verifying(validYesNo),
      "whenDidYouStartThisJob" -> optional(dayMonthYear.verifying(validDateOnly)),
      "whenDidTheJobFinish" -> optional(dayMonthYear.verifying(validDateOnly)),
      "haveYouCeasedTrading" -> optional(nonEmptyText.verifying(validYesNo)),
      "natureOfYourBusiness" -> optional(text(maxLength = sixty))
    )(AboutSelfEmployment.apply)(AboutSelfEmployment.unapply)
  )

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s9_self_employment.g1_aboutSelfEmployment(form.fill(AboutSelfEmployment), completedQuestionGroups))
  }

  def submit = claiming { implicit claim =>
    implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s9_self_employment.g1_aboutSelfEmployment(formWithErrors, completedQuestionGroups)),
        f => claim.update(f) -> Redirect(routes.G2SelfEmploymentYourAccounts.present())
      )
  }
}
