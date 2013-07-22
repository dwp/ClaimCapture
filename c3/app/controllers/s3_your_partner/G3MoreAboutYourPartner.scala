package controllers.s3_your_partner

import controllers.Mappings._
import models.domain.{MoreAboutYourPartner, Claim}
import models.view.CachedClaim
import play.api.data.Forms._
import play.api.data.{FormError, Form}
import play.api.mvc.Controller
import utils.helpers.CarersForm.formBinding
import models.yesNo.YesNoWithDate

object G3MoreAboutYourPartner extends Controller with CachedClaim {
  val startedLivingTogetherMapping =
    "startedLivingTogether" -> optional(
      mapping(
        "afterClaimDate" -> text.verifying(validYesNo),
        "date" -> optional(dayMonthYear.verifying(validDateOnly))
      )(YesNoWithDate.apply)(YesNoWithDate.unapply))

  val separationMapping =
    "separated" -> mapping(
      "fromPartner" -> nonEmptyText.verifying(validYesNo),
      "date" -> optional(dayMonthYear.verifying(validDate))
    )(YesNoWithDate.apply)(YesNoWithDate.unapply)
      .verifying("required", YesNoWithDate.validate _)

  val form = Form(
    mapping(
      call(routes.G3MoreAboutYourPartner.present()),
      startedLivingTogetherMapping,
      separationMapping
    )(MoreAboutYourPartner.apply)(MoreAboutYourPartner.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(MoreAboutYourPartner)

  def present = claiming { implicit claim => implicit request =>
    YourPartner.whenVisible(claim)(() => {
      val currentForm: Form[MoreAboutYourPartner] = claim.questionGroup(MoreAboutYourPartner) match {
        case Some(t: MoreAboutYourPartner) => form.fill(t)
        case _ => form
      }

      Ok(views.html.s3_your_partner.g3_moreAboutYourPartner(currentForm, completedQuestionGroups))
    })
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors.replaceError("separated", FormError("separated.date", "error.required"))
        BadRequest(views.html.s3_your_partner.g3_moreAboutYourPartner(formWithErrorsUpdate, completedQuestionGroups))
      },
      moreAboutYourPartner => claim.update(moreAboutYourPartner) -> Redirect(routes.G4PersonYouCareFor.present()))
  }
}