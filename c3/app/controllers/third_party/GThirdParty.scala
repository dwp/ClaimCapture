package controllers.third_party

import controllers.CarersForms._
import controllers.mappings.Mappings._
import models.domain.ThirdPartyDetails
import models.view.{CachedClaim, Navigable}
import play.api.Play._
import play.api.data.Forms._
import play.api.data.{Form, FormError}
import play.api.i18n._
import play.api.mvc.Controller
import utils.helpers.CarersForm._

object GThirdParty extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val form = Form(mapping(
    "thirdParty" -> nonEmptyText.verifying(ThirdPartyDetails.validThirdParty),
    "thirdParty.nameAndOrganisation" -> optional(carersNonEmptyText(maxLength = 60))
  )(ThirdPartyDetails.apply)(ThirdPartyDetails.unapply)
    .verifying(ThirdPartyDetails.nameAndOrganisationRequired))

  def present = claiming {implicit claim => implicit request => implicit request2lang =>
    track(ThirdPartyDetails) { implicit claim =>
      Ok(views.html.third_party.thirdParty(form.fill(ThirdPartyDetails)))
    }
  }

  def submit = claiming {implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "thirdParty.nameAndOrganisation.required", FormError("thirdParty.nameAndOrganisation", errorRequired))
        BadRequest(views.html.third_party.thirdParty(formWithErrorsUpdate))
      },
      thirdParty => claim.update(thirdParty) -> Redirect(controllers.s_claim_date.routes.GClaimDate.present()))
  } withPreview()
}
