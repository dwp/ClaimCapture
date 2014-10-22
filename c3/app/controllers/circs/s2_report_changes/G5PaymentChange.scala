package controllers.circs.s2_report_changes

import play.api.mvc.Controller
import models.view.{Navigable, CachedChangeOfCircs}
import play.api.data.Form
import play.api.data.Forms._
import models.domain.CircumstancesPaymentChange
import utils.helpers.CarersForm._
import controllers.CarersForms._
import controllers.Mappings._
import models.yesNo.YesNoWith2Text

object G5PaymentChange extends Controller with CachedChangeOfCircs with Navigable {
  val currentlyPaidIntoBankMapping =
    "currentlyPaidIntoBank" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "text1" -> optional(carersNonEmptyText(maxLength = 35)),
      "text2" -> optional(carersNonEmptyText(maxLength = 35))
    )(YesNoWith2Text.apply)(YesNoWith2Text.unapply)
      .verifying("required", YesNoWith2Text.validateText1OnYes _)
      .verifying("required", YesNoWith2Text.validateText2OnNo _)

  val form = Form(mapping(
    currentlyPaidIntoBankMapping,
    "accountHolderName" -> carersNonEmptyText(maxLength = 40),
    "whoseNameIsTheAccountIn" -> carersNonEmptyText(maxLength = 40),
    "bankFullName" -> carersNonEmptyText(maxLength = 100),
    "sortCode" -> (sortCode verifying requiredSortCode),
    "accountNumber" -> carersNonEmptyText(minLength = 6, maxLength = 10),
    "rollOrReferenceNumber" -> carersText(maxLength = 18),
    "paymentFrequency" -> carersNonEmptyText(maxLength = 15),
    "moreAboutChanges" -> optional(carersText(maxLength = 300))
  )
  (CircumstancesPaymentChange.apply)(CircumstancesPaymentChange.unapply))

  def present = claiming { implicit circs => implicit request => implicit lang =>
    track(CircumstancesPaymentChange) {
      implicit circs => Ok(views.html.circs.s2_report_changes.g5_paymentChange(form.fill(CircumstancesPaymentChange)))
    }
  }

  def submit = claiming { implicit circs => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.circs.s2_report_changes.g5_paymentChange(formWithErrors)),
      f => circs.update(f) -> Redirect(controllers.circs.s3_consent_and_declaration.routes.G1Declaration.present())
    )
  }
}
