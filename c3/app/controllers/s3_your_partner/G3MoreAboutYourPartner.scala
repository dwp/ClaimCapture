package controllers.s3_your_partner

import controllers.Routing
import controllers.Mappings._
import models.domain.{MoreAboutYourPartner, Claim}
import models.view.CachedClaim
import play.api.data.Forms._
import play.api.data.{FormError, Form}
import play.api.mvc.Controller
import utils.helpers.CarersForm.formBinding
import models.yesNo.YesNoWithDate

object G3MoreAboutYourPartner extends Controller with Routing with CachedClaim {

  override val route = MoreAboutYourPartner.id -> routes.G3MoreAboutYourPartner.present

  val startedLivingTogetherMapping =
    "startedLivingTogether" -> optional(
      mapping(
        "afterClaimDate" -> text.verifying(validYesNo),
        "date" -> optional(dayMonthYear.verifying(validDateOnly))
      )(YesNoWithDate.apply)(YesNoWithDate.unapply)
    )

  val separationMapping =
    "separated" -> mapping(
      "fromPartner" -> nonEmptyText.verifying(validYesNo),
      "date" -> optional(dayMonthYear.verifying(validDate))
    )(YesNoWithDate.apply)(YesNoWithDate.unapply)
      .verifying("required", YesNoWithDate.validate _)

  val form = Form(
    mapping(
      startedLivingTogetherMapping,
      separationMapping
    )(MoreAboutYourPartner.apply)(MoreAboutYourPartner.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(MoreAboutYourPartner)

  def present = claiming {
    implicit claim => implicit request =>
      if (claim.isSectionVisible(models.domain.YourPartner.id)) {

        val currentForm: Form[MoreAboutYourPartner] = claim.questionGroup(MoreAboutYourPartner) match {
          case Some(t: MoreAboutYourPartner) => form.fill(t)
          case _ => form
        }

        Ok(views.html.s3_your_partner.g3_moreAboutYourPartner(currentForm, completedQuestionGroups))
      }
      else Redirect(controllers.s4_care_you_provide.routes.G1TheirPersonalDetails.present())
  }

  def submit = claiming {
    implicit claim => implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => {
          val formWithErrorsUpdate = formWithErrors
            .replaceError("separated", FormError("separated.date", "error.required"))
          BadRequest(views.html.s3_your_partner.g3_moreAboutYourPartner(formWithErrorsUpdate, completedQuestionGroups))
        },
        moreAboutYourPartner => claim.update(moreAboutYourPartner) -> Redirect(routes.G4PersonYouCareFor.present()))
  }
}