package controllers.circs.report_changes

import controllers.CarersForms._
import controllers.mappings.AccountNumberMappings._
import controllers.mappings.Mappings._
import models.domain.CircumstancesPaymentChange
import models.view.{CachedChangeOfCircs, Navigable}
import models.yesNo.YesNoWith2Text
import play.api.Play._
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.mvc.Controller
import utils.CommonValidation
import utils.helpers.CarersForm._
import play.api.i18n._

object GPaymentChange extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "currentlyPaidIntoBankAnswer" -> nonEmptyText.verifying(validYesNo),
    "currentlyPaidIntoBankText1" -> optional(carersNonEmptyText(maxLength = 35)),
    "currentlyPaidIntoBankText2" -> optional(carersNonEmptyText(maxLength = 35)),
    "accountHolderName" -> carersNonEmptyText(maxLength = CommonValidation.ACCOUNT_HOLDER_NAME_MAX_LENGTH),
    "bankFullName" -> carersNonEmptyText(maxLength = 100),
    "sortCode" -> (sortCode verifying requiredSortCode),
    "accountNumber" -> (text verifying stopOnFirstFail(accountNumberFilledIn, accountNumberDigits, accountNumberLength)),
    "rollOrReferenceNumber" -> carersText(maxLength = 18),
    "paymentFrequency" -> carersNonEmptyText(maxLength = 20),
    "moreAboutChanges" -> optional(carersText(maxLength = CircumstancesPaymentChange.textMaxLength))
  )
  (CircumstancesPaymentChange.apply)(CircumstancesPaymentChange.unapply)
    .verifying("currentlyPaidIntoBankText1.required", validateBankText1 _)
    .verifying("currentlyPaidIntoBankText2.required", validateBankText2 _)
  )

  private def validateBankText1(input: CircumstancesPaymentChange) = {
    input.currentlyPaidIntoBankAnswer match {
      case `yes` => input.currentlyPaidIntoBankText1.isDefined
      case _ => true
    }
  }

  private def validateBankText2(input: CircumstancesPaymentChange) = {
    input.currentlyPaidIntoBankAnswer match {
      case `no` => input.currentlyPaidIntoBankText2.isDefined
      case _ => true
    }
  }

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
        val updatedErrors=afterIgnoreGroupBy
          .replaceError("", "currentlyPaidIntoBankText1.required", FormError("currentlyPaidIntoBankText1", errorRequired))
          .replaceError("", "currentlyPaidIntoBankText2.required", FormError("currentlyPaidIntoBankText2", errorRequired))
        BadRequest(views.html.circs.report_changes.paymentChange(updatedErrors))
      },
      f => circs.update(f) -> Redirect(controllers.circs.your_details.routes.GYourDetails.present())
    )
  }
}
