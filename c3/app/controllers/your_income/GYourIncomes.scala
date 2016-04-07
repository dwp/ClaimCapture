package controllers.your_income

import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import models.domain._
import models.view.{CachedClaim, Navigable}
import play.api.Play._
import play.api.data.Forms._
import play.api.data.{Form, FormError}
import play.api.i18n._
import play.api.mvc.Controller
import utils.helpers.CarersForm._
import utils.helpers.ReturnToSummaryHelper
import scala.language.{postfixOps, reflectiveCalls}

object GYourIncomes extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
      "beenSelfEmployedSince1WeekBeforeClaim" -> nonEmptyText.verifying(validYesNo),
      "beenEmployedSince6MonthsBeforeClaim" -> nonEmptyText.verifying(validYesNo),
      "yourIncome_sickpay" -> optional(nonEmptyText),
      "yourIncome_patmatadoppay" -> optional(nonEmptyText),
      "yourIncome_fostering" -> optional(nonEmptyText),
      "yourIncome_directpay" -> optional(nonEmptyText),
      "yourIncome_anyother" -> optional(nonEmptyText),
      "yourIncome_none" -> optional(nonEmptyText)
      )(YourIncomes.apply)(YourIncomes.unapply)
        .verifying("required", validateOtherAndNonePaymentsSelected _)
        .verifying("value.required", validateOtherPaymentsSelected _))

    def present = claimingWithCheck {  implicit claim => implicit request => implicit request2lang =>
      track(YourIncomes) { implicit claim => Ok(views.html.your_income.yourIncome(form.fill(YourIncomes))) }
    }

    def submit = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
      form.bindEncrypted.fold(
        formWithErrors => {
          val formWithErrorsUpdate = formWithErrors
            .replaceError("beenSelfEmployedSince1WeekBeforeClaim", errorRequired, FormError("beenSelfEmployedSince1WeekBeforeClaim", errorRequired, Seq(claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy => (dmy - 1 week).`dd/MM/yyyy`),claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))))
            .replaceError("beenEmployedSince6MonthsBeforeClaim", errorRequired, FormError("beenEmployedSince6MonthsBeforeClaim", errorRequired, Seq(claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy => (dmy - 6 months).`dd/MM/yyyy`),claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))))
            .replaceError("","value.required", FormError("yourIncome", Mappings.errorRequired, Seq(claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy => (dmy).`dd/MM/yyyy`),claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))))
            .replaceError("","required", FormError("yourIncome", messagesApi("yourIncome.otherIncome.selectOne"), Seq(claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy => (dmy).`dd/MM/yyyy`),claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))))
          BadRequest(views.html.your_income.yourIncome(formWithErrorsUpdate))
        }, yourIncomes => {
          val updatedClaim = showHideSections(claim, yourIncomes)
          val deletedClaim = deleteUnselectedSections(updatedClaim, yourIncomes)

          redirect(deletedClaim, yourIncomes)
        }
      )
    }.withPreviewConditionally[YourIncomes](checkGoPreview)

  private def redirect(claim: Claim, yourIncomes: YourIncomes) = {
    claim.update(yourIncomes) -> Redirect(controllers.s_employment.routes.GBeenEmployed.present())
  }

  private def showHideSections(claim: Claim, yourIncomes: YourIncomes): Claim = {
    claim.showHideSection(yourIncomes.beenEmployedSince6MonthsBeforeClaim == yes, models.domain.Employed)
      .showHideSection(yourIncomes.beenSelfEmployedSince1WeekBeforeClaim == yes, models.domain.SelfEmployment)
      .showHideSection(yourIncomes.yourIncome_sickpay == someTrue, models.domain.YourIncomeStatutorySickPay)
      .showHideSection(yourIncomes.yourIncome_patmatadoppay == someTrue, models.domain.YourIncomeStatutoryMaternityPaternityAdoptionPay)
      .showHideSection(yourIncomes.yourIncome_fostering == someTrue, models.domain.YourIncomeFosteringAllowance)
      .showHideSection(yourIncomes.yourIncome_directpay == someTrue, models.domain.YourIncomeDirectPayment)
      .showHideSection(yourIncomes.yourIncome_anyother == someTrue, models.domain.YourIncomeOtherPayments)
  }

  private def deleteUnselectedSections(claim: Claim, yourIncomes: YourIncomes): Claim = {
    val deletedAnyOther = if(yourIncomes.yourIncome_anyother == None) {
      claim.delete(OtherPayments)
    } else claim

    val directPayment = if(yourIncomes.yourIncome_directpay == None) {
      deletedAnyOther.delete(DirectPayment)
    } else deletedAnyOther

    val fosteringAllowance = if(yourIncomes.yourIncome_fostering == None) {
      directPayment.delete(FosteringAllowance)
    } else directPayment

    val statutoryMaternityAdoptionPay = if(yourIncomes.yourIncome_patmatadoppay == None) {
      fosteringAllowance.delete(StatutoryMaternityPaternityAdoptionPay)
    } else fosteringAllowance

    val statutorySickPay = if(yourIncomes.yourIncome_sickpay == None) {
      statutoryMaternityAdoptionPay.delete(StatutorySickPay)
    } else statutoryMaternityAdoptionPay

    val deletedSelfEmployment = if(yourIncomes.beenSelfEmployedSince1WeekBeforeClaim == no) {
      statutorySickPay.delete(SelfEmploymentDates).delete(SelfEmploymentPensionsAndExpenses)
    } else statutorySickPay

    val deletedEmployment = if(yourIncomes.beenEmployedSince6MonthsBeforeClaim == no) {
      deletedSelfEmployment.delete(BeenEmployed).delete(Jobs)
    } else deletedSelfEmployment
    deletedEmployment
  }

  private def validateOtherAndNonePaymentsSelected(yourIncomes: YourIncomes) = {
    someTrue match {
      case (yourIncomes.yourIncome_anyother | yourIncomes.yourIncome_directpay | yourIncomes.yourIncome_fostering | yourIncomes.yourIncome_patmatadoppay | yourIncomes.yourIncome_sickpay) if(yourIncomes.yourIncome_none == someTrue) => false
      case _ => true
    }
  }

  private def validateOtherPaymentsSelected(yourIncomes: YourIncomes) = {
    yourIncomes match {
      case YourIncomes(_, _, None, None, None, None, None, None) => false
      case _ => true
    }
  }

  private def checkGoPreview(t:(Option[YourIncomes], YourIncomes), c:(Option[Claim],Claim)): Boolean = {
    val previousEmp = t._1
    val currentEmp = t._2
    val currentClaim = c._2

    lazy val employmentHasChanged = previousEmp.get.beenEmployedSince6MonthsBeforeClaim != currentEmp.beenEmployedSince6MonthsBeforeClaim
    lazy val selfEmploymentHasChanged = previousEmp.get.beenSelfEmployedSince1WeekBeforeClaim != currentEmp.beenSelfEmployedSince1WeekBeforeClaim

    lazy val bothHaveNotChanged = !employmentHasChanged && !selfEmploymentHasChanged
    lazy val selfENotChangedAndEmploymentNo = previousEmp.get.beenEmployedSince6MonthsBeforeClaim == yes && currentEmp.beenEmployedSince6MonthsBeforeClaim == no && ! selfEmploymentHasChanged
    lazy val empNotChangedAndSENo = previousEmp.get.beenSelfEmployedSince1WeekBeforeClaim == yes && currentEmp.beenSelfEmployedSince1WeekBeforeClaim == no && ! employmentHasChanged
    val bothAnswersAreNo = currentEmp.beenEmployedSince6MonthsBeforeClaim == no && currentEmp.beenSelfEmployedSince1WeekBeforeClaim == no
    val doesNotHaveJobs = currentEmp.beenEmployedSince6MonthsBeforeClaim == yes && currentClaim.questionGroup[Jobs].getOrElse(Jobs()).isEmpty

    val result = {
      if (doesNotHaveJobs) { false }
      else { previousEmp.isDefined && ( bothHaveNotChanged || selfENotChangedAndEmploymentNo || empNotChangedAndSENo) ||  bothAnswersAreNo }
    }

    if (!result) result else ReturnToSummaryHelper.haveOtherPaymentsChanged(currentClaim)
    //We want to go back to preview from Employment guard questions page if
    // both answers haven't changed or if one hasn't changed and the changed one is 'no' or both answers are no, or
  }
}
