package controllers.s_about_you

import controllers.CarersForms._

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import models.view.CachedClaim
import models.domain.OtherEEAStateOrSwitzerland
import controllers.mappings.Mappings._
import utils.helpers.CarersForm._
import models.view.Navigable
import controllers.CarersForms._
import play.api.i18n.{MMessages => Messages}

object GOtherEEAStateOrSwitzerland extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "benefitsFromEEA" -> nonEmptyText.verifying(validYesNo),
    "benefitsFromEEADetails" -> optional(carersNonEmptyText(maxLength = 3000)),
    "workingForEEA" -> nonEmptyText.verifying(validYesNo),
    "workingForEEADetails" -> optional(carersNonEmptyText(maxLength = 3000))
  )(OtherEEAStateOrSwitzerland.apply)(OtherEEAStateOrSwitzerland.unapply)
    .verifying(OtherEEAStateOrSwitzerland.requiredBenefitsFromEEADetails)
    .verifying(OtherEEAStateOrSwitzerland.requiredWorkingForEEADetails)
  )

  def present = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    track(OtherEEAStateOrSwitzerland) { implicit claim => Ok(views.html.s_about_you.g_otherEEAStateOrSwitzerland(form.fill(OtherEEAStateOrSwitzerland))(lang)) }
  }

  def submit = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "benefitsfromeeadetails.required", FormError("benefitsFromEEADetails", "error.benefitsFromEEADetails.notFilled"))
          .replaceError("", "workingForEEADetails.required", FormError("workingForEEADetails", "error.workingForEEADetails.required"))
        BadRequest(views.html.s_about_you.g_otherEEAStateOrSwitzerland(formWithErrorsUpdate)(lang))
      },
      benefitsFromEEA => claim.update(benefitsFromEEA) -> Redirect(controllers.s_your_partner.routes.GYourPartnerPersonalDetails.present())
    )
  } withPreview()
}