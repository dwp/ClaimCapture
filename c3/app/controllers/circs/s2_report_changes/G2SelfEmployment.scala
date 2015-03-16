package controllers.circs.s2_report_changes

import play.api.mvc.Controller
import models.view.{Navigable, CachedChangeOfCircs}
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import models.domain.CircumstancesSelfEmployment
import utils.helpers.CarersForm._
import controllers.CarersForms._
import controllers.mappings.Mappings._
import models.yesNo.{YesNoWithDate, YesNoWithText}

object G2SelfEmployment extends Controller with CachedChangeOfCircs with Navigable {
  val stillCaringMapping =
    "stillCaring" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "date" -> optional(dayMonthYear.verifying(validDate))
    )(YesNoWithDate.apply)(YesNoWithDate.unapply)
      .verifying("dateRequired", YesNoWithDate.validateNo _)

  val form = Form(mapping(
    stillCaringMapping,
    "whenThisSelfEmploymentStarted" -> dayMonthYear.verifying(validDate),
    "typeOfBusiness" -> carersNonEmptyText(maxLength = 35),
    "totalOverWeeklyIncomeThreshold" -> nonEmptyText.verifying(validYesNoDontKnow),
    "moreAboutChanges" -> optional(carersText(maxLength = 300))
  )(CircumstancesSelfEmployment.apply)(CircumstancesSelfEmployment.unapply))

  def present = claiming {implicit circs =>  implicit request =>  lang =>
    track(CircumstancesSelfEmployment) {
      implicit circs => Ok(views.html.circs.s2_report_changes.g2_selfEmployment(form.fill(CircumstancesSelfEmployment))(lang))
    }
  }

  def submit = claiming {implicit circs =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val updatedFormWithErrors = formWithErrors.replaceError("stillCaring","dateRequired", FormError("stillCaring.date", errorRequired))
        BadRequest(views.html.circs.s2_report_changes.g2_selfEmployment(updatedFormWithErrors)(lang))
      },
      f => circs.update(f) -> Redirect(controllers.circs.s3_consent_and_declaration.routes.G1Declaration.present())
    )
  }
}
