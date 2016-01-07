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
    "nationality" -> nonEmptyText.verifying(ThirdPartyDetails.validNationality),
    "actualnationality" -> optional(carersNonEmptyText(maxLength = 35))
  )(ThirdPartyDetails.apply)(ThirdPartyDetails.unapply)
    .verifying(ThirdPartyDetails.actualNationalityRequired))

  def present = claimingWithCheck {implicit claim => implicit request => implicit request2lang =>
    track(ThirdPartyDetails) { implicit claim =>
      Ok(views.html.third_party.thirdParty(form.fill(ThirdPartyDetails)))
    }
  }

  def submit = claimingWithCheck {implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "actualnationality.required", FormError("actualnationality", errorRequired))
          .replaceError("resideInUK", "error.text.required", FormError("resideInUK.text", errorRequired))
        BadRequest(views.html.third_party.thirdParty(formWithErrorsUpdate))
      },
      thirdParty => claim.update(thirdParty) -> Redirect(controllers.s_claim_date.routes.GClaimDate.present()))
  } withPreview()
}
