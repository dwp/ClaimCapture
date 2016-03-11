package controllers.circs.report_changes

import controllers.CarersForms._
import controllers.mappings.Mappings._
import models.domain.CircumstancesPaymentChange
import models.view.{CachedChangeOfCircs, Navigable}
import models.yesNo.YesNoWith2Text
import play.api.Play._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import utils.CommonValidation
import utils.helpers.CarersForm._
import play.api.i18n._

object GPaymentChange extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
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
    "accountHolderName" -> carersNonEmptyText(maxLength = CommonValidation.ACCOUNT_HOLDER_NAME_MAX_LENGTH),
    "bankFullName" -> carersNonEmptyText(maxLength = 100),
    "sortCode" -> (sortCode verifying requiredSortCode),
    "accountNumber" -> carersNonEmptyText(minLength = 6, maxLength = 10),
    "rollOrReferenceNumber" -> carersText(maxLength = 18),
    "paymentFrequency" -> carersNonEmptyText(maxLength = 20),
    "moreAboutChanges" -> optional(carersText(maxLength = CircumstancesPaymentChange.textMaxLength))
  )
  (CircumstancesPaymentChange.apply)(CircumstancesPaymentChange.unapply))

  def present = claiming {implicit circs => implicit request => implicit request2lang =>
    track(CircumstancesPaymentChange) {
      implicit circs => Ok(views.html.circs.report_changes.paymentChange(form.fill(CircumstancesPaymentChange)))
    }
  }

  def submit = claiming {implicit circs => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val updatedFormWithErrors = manageErrorsSortCode(formWithErrors)
        val afterIgnoreGroupBy = ignoreGroupByForSortCode(updatedFormWithErrors)
        BadRequest(views.html.circs.report_changes.paymentChange(afterIgnoreGroupBy))
      },
      f => circs.update(f) -> Redirect(circsPathAfterFunction)
    )
  }
}
