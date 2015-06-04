package controllers.circs.s2_report_changes

import controllers.CarersForms._
import controllers.mappings.Mappings._
import models.domain.CircumstancesPaymentChange
import models.view.{CachedChangeOfCircs, Navigable}
import models.yesNo.YesNoWith2Text
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import utils.helpers.CarersForm._

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
    "bankFullName" -> carersNonEmptyText(maxLength = 100),
    "sortCode" -> (sortCode verifying requiredSortCode),
    "accountNumber" -> carersNonEmptyText(minLength = 6, maxLength = 10),
    "rollOrReferenceNumber" -> carersText(maxLength = 18),
    "paymentFrequency" -> carersNonEmptyText(maxLength = 20),
    "moreAboutChanges" -> optional(carersText(maxLength = 300))
  )
  (CircumstancesPaymentChange.apply)(CircumstancesPaymentChange.unapply))

  def present = claimingWithCheck {implicit circs =>  implicit request =>  lang =>
    track(CircumstancesPaymentChange) {
      implicit circs => Ok(views.html.circs.s2_report_changes.g5_paymentChange(form.fill(CircumstancesPaymentChange))(lang))
    }
  }

  def submit = claiming {implicit circs =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val updatedFormWithErrors = manageErrorsSortCode(formWithErrors)
        val afterIgnoreGroupBy = ignoreGroupByForSortCode(updatedFormWithErrors)
        BadRequest(views.html.circs.s2_report_changes.g5_paymentChange(afterIgnoreGroupBy)(lang))
      },
      f => circs.update(f) -> Redirect(controllers.circs.s3_consent_and_declaration.routes.G1Declaration.present())
    )
  }
}
