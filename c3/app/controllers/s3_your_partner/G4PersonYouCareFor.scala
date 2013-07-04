package controllers.s3_your_partner

import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.{Claim, PersonYouCareFor}
import utils.helpers.CarersForm.formBinding

object G4PersonYouCareFor extends Controller with Routing with CachedClaim {

  override val route = PersonYouCareFor.id -> routes.G4PersonYouCareFor.present

  val form = Form(
    mapping(
      "isPartnerPersonYouCareFor" -> nonEmptyText.verifying(validYesNo)
    )(PersonYouCareFor.apply)(PersonYouCareFor.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(PersonYouCareFor)

  def present = claiming {
    implicit claim => implicit request =>
      val currentForm: Form[PersonYouCareFor] = claim.questionGroup(PersonYouCareFor) match {
        case Some(t: PersonYouCareFor) => form.fill(t)
        case _ => form
      }

      Ok(views.html.s3_your_partner.g4_personYouCareFor(currentForm, completedQuestionGroups))
  }

  def submit = claiming {
    implicit claim => implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s3_your_partner.g4_personYouCareFor(formWithErrors, completedQuestionGroups)),
        f => claim.update(f) -> Redirect(routes.YourPartner.completed())
      )
  }

}
