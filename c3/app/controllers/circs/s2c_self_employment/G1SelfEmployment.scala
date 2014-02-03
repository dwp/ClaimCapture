package controllers.circs.s2c_self_employment

import play.api.mvc.Controller
import models.view.{Navigable, CachedChangeOfCircs}
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{CircumstancesSelfEmployment, CircumstancesOtherInfo}
import utils.helpers.CarersForm._
import controllers.CarersForms._
import controllers.Mappings._
import models.yesNo.{YesNoWithDate, YesNoWithText}

object G1SelfEmployment extends Controller with CachedChangeOfCircs with Navigable {
  val stillCaringMapping =
    "stillCaring" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "date" -> optional(dayMonthYear.verifying(validDate))
    )(YesNoWithDate.apply)(YesNoWithDate.unapply)
      .verifying("required", YesNoWithDate.validate _)

  val form = Form(mapping(
    stillCaringMapping,
    "startOfSelfEmployment" -> dayMonthYear.verifying(validDate),
    "typeOfBusiness" -> carersNonEmptyText(maxLength = 60),
    "totalOverWeeklyIncomeThreshold" -> nonEmptyText.verifying(validYesNo)
  )(CircumstancesSelfEmployment.apply)(CircumstancesSelfEmployment.unapply))

  def present = claiming { implicit circs => implicit request =>
    track(CircumstancesSelfEmployment) {
      implicit circs => Ok(views.html.circs.s2c_self_employment.g1_selfEmployment(form.fill(CircumstancesSelfEmployment)))
    }
  }

  def submit = claiming { implicit circs => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.circs.s2c_self_employment.g1_selfEmployment(formWithErrors)),
      f => circs.update(f) -> Redirect(controllers.circs.s3_consent_and_declaration.routes.G1Declaration.present())
    )
  }
}
