package controllers.s3_your_partner

import language.reflectiveCalls
import play.api.mvc.{AnyContent, Request, Controller}
import play.api.data.Form
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import controllers.Mappings._
import models.domain.{DigitalForm, YourPartnerPersonalDetails, PersonYouCareFor}
import utils.helpers.CarersForm.formBinding
import YourPartner._

object G4PersonYouCareFor extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "isPartnerPersonYouCareFor" -> nonEmptyText.verifying(validYesNo)
  )(PersonYouCareFor.apply)(PersonYouCareFor.unapply))

  def present = executeOnForm {implicit claim => implicit request =>
    presentConditionally(personYouCareFor)
  }

  def personYouCareFor(implicit claim: DigitalForm, request: Request[AnyContent]): FormResult = {
    track(YourPartnerPersonalDetails) { implicit claim =>
      Ok(views.html.s3_your_partner.g4_personYouCareFor(form.fill(PersonYouCareFor)))
    }
  }

  def submit = executeOnForm {implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s3_your_partner.g4_personYouCareFor(formWithErrors)),
      f => claim.update(f) -> Redirect(routes.YourPartner.completed())
    )
  }
}