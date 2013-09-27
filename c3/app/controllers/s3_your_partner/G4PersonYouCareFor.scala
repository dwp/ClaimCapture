package controllers.s3_your_partner

import language.reflectiveCalls
import play.api.mvc.{AnyContent, Request, Controller}
import play.api.data.Form
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import controllers.Mappings._
import models.domain.{Claim, YourPartnerPersonalDetails, PersonYouCareFor}
import utils.helpers.CarersForm.formBinding
import YourPartner._

object G4PersonYouCareFor extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "isPartnerPersonYouCareFor" -> nonEmptyText.verifying(validYesNo)
  )(PersonYouCareFor.apply)(PersonYouCareFor.unapply))

  def present = claiming { implicit claim => implicit request =>
    presentConditionally(personYouCareFor)
  }

  def personYouCareFor(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    track(YourPartnerPersonalDetails) { implicit claim =>
      Ok(views.html.s3_your_partner.g4_personYouCareFor(form.fill(PersonYouCareFor)))
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s3_your_partner.g4_personYouCareFor(formWithErrors)),
      f => claim.update(f) -> Redirect(routes.YourPartner.completed())
    )
  }
}