package controllers.s3_your_partner

import controllers.Routing
import controllers.Mappings._
import models.domain.{MoreAboutYourPartner, Claim}
import models.view.CachedClaim
import play.api.data.Forms._
import play.api.data.Form
import play.api.mvc.Controller
import utils.helpers.CarersForm.formBinding
import scala.Some

object G3MoreAboutYourPartner extends Controller with Routing with CachedClaim {

  override val route = MoreAboutYourPartner.id -> routes.G3MoreAboutYourPartner.present

  val form = Form(
    mapping(
      "dateStartedLivingTogether" -> optional(dayMonthYear verifying validDateOnly),
      "separatedFromPartner" -> nonEmptyText.verifying(validYesNo),
      "separationDate" -> optional(dayMonthYear.verifying(validDate))
    )(MoreAboutYourPartner.apply)(MoreAboutYourPartner.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(MoreAboutYourPartner)

  def present = claiming {
    implicit claim => implicit request =>
      val currentForm: Form[MoreAboutYourPartner] = claim.questionGroup(MoreAboutYourPartner) match {
        case Some(t: MoreAboutYourPartner) => form.fill(t)
        case _ => form
      }

      Ok(views.html.s3_your_partner.g3_moreAboutYourPartner(currentForm, completedQuestionGroups))
  }

  def submit = claiming {
    implicit claim => implicit request =>

      def separatedValidation(pageForm: Form[MoreAboutYourPartner])(implicit moreAboutYourPartner: MoreAboutYourPartner): Form[MoreAboutYourPartner] = {
        if (moreAboutYourPartner.separatedFromPartner == yes && moreAboutYourPartner.separationDate == None) pageForm.fill(moreAboutYourPartner).withError("separationDate", "error.required")
        else pageForm
      }

      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s3_your_partner.g3_moreAboutYourPartner(formWithErrors, completedQuestionGroups)),

        implicit moreAboutYourPartner => {
          val formValidations = separatedValidation _
          val validatedForm = formValidations(form)

          if (validatedForm.hasErrors) BadRequest(views.html.s3_your_partner.g3_moreAboutYourPartner(validatedForm, completedQuestionGroups))
          else claim.update(moreAboutYourPartner) -> Redirect(routes.G4PersonYouCareFor.present())
        })
  }
}