package controllers.s3_your_partner

import language.reflectiveCalls
import play.api.mvc.Controller
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.PersonYouCareFor
import utils.helpers.CarersForm.formBinding
import YourPartner.whenSectionVisible

object G4PersonYouCareFor extends Controller with YourPartnerRouting with CachedClaim {
  val form = Form(mapping(
    "isPartnerPersonYouCareFor" -> nonEmptyText.verifying(validYesNo)
  )(PersonYouCareFor.apply)(PersonYouCareFor.unapply))

  def present = claiming { implicit claim => implicit request =>
    whenSectionVisible {
      val currentForm: Form[PersonYouCareFor] = claim.questionGroup(PersonYouCareFor) match {
        case Some(t: PersonYouCareFor) => form.fill(t)
        case _ => form
      }

      Ok(views.html.s3_your_partner.g4_personYouCareFor(currentForm, completedQuestionGroups(PersonYouCareFor)))
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s3_your_partner.g4_personYouCareFor(formWithErrors, completedQuestionGroups(PersonYouCareFor))),
      f => claim.update(f) -> Redirect(routes.YourPartner.completed())
    )
  }
}