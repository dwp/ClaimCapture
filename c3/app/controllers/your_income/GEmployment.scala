package controllers.your_income

import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import controllers.s_employment.Employment
import models.domain.{Employment => Emp, _}
import models.view.{CachedClaim, Navigable}
import play.api.Play._
import play.api.data.Forms._
import play.api.data.{Form, FormError}
import play.api.i18n._
import play.api.mvc.Controller
import utils.helpers.CarersForm._
import scala.language.{postfixOps, reflectiveCalls}

object GEmployment extends Controller with CachedClaim with Navigable with I18nSupport {
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
      )(Emp.apply)(Emp.unapply)
        .verifying("required", validateOtherAndNonePaymentsSelected _)
        .verifying("value.required", validateOtherPaymentsSelected _))

    def present = claimingWithCheck {  implicit claim => implicit request => implicit request2lang =>
      track(Employment) { implicit claim => Ok(views.html.your_income.g_your_income(form.fill(Emp))) }
    }

    def submit = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
      println(request.body.asFormUrlEncoded.get)
      form.bindEncrypted.fold(
        formWithErrors => {
          val formWithErrorsUpdate = formWithErrors
            .replaceError("beenSelfEmployedSince1WeekBeforeClaim", errorRequired, FormError("beenSelfEmployedSince1WeekBeforeClaim", errorRequired, Seq(claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy => (dmy - 1 week).`dd/MM/yyyy`),claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))))
            .replaceError("beenEmployedSince6MonthsBeforeClaim", errorRequired, FormError("beenEmployedSince6MonthsBeforeClaim", errorRequired, Seq(claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy => (dmy - 6 months).`dd/MM/yyyy`),claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))))
            .replaceError("","value.required", FormError("yourIncome", Mappings.errorRequired, Seq(claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy => (dmy).`dd/MM/yyyy`),claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))))
            .replaceError("","required", FormError("yourIncome", messagesApi("yourIncome.otherIncome.selectOne"), Seq(claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy => (dmy).`dd/MM/yyyy`),claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))))
          BadRequest(views.html.your_income.g_your_income(formWithErrorsUpdate))
        },employment => {
          val updatedClaim = showHideSections(claim, employment)
          val deletedClaim = deleteUnselectedSections(updatedClaim, employment)

          redirect(deletedClaim, employment)
        }
      )
    }.withPreviewConditionally[Emp](checkGoPreview)

  private def redirect(claim: Claim, employment: Emp) = {
    claim.update(employment) -> Redirect(controllers.s_employment.routes.GBeenEmployed.present())
  }

  private def showHideSections(claim: Claim, employment: Emp): Claim = {
    claim.showHideSection(employment.beenEmployedSince6MonthsBeforeClaim == yes, models.domain.Employed)
      .showHideSection(employment.beenSelfEmployedSince1WeekBeforeClaim == yes, models.domain.SelfEmployment)
      .showHideSection(employment.yourIncome_sickpay == someTrue, models.domain.YourIncomeStatutorySickPay)
      .showHideSection(employment.yourIncome_patmatadoppay == someTrue, models.domain.YourIncomeStatutoryMaternityAdoptionPay)
      .showHideSection(employment.yourIncome_fostering == someTrue, models.domain.YourIncomeFosteringAllowance)
      .showHideSection(employment.yourIncome_directpay == someTrue, models.domain.YourIncomeDirectPayment)
      .showHideSection(employment.yourIncome_anyother == someTrue, models.domain.YourIncomeAnyOtherIncome)
  }

  private def deleteUnselectedSections(claim: Claim, employment: Emp): Claim = {
    val deletedAnyOther = if(employment.yourIncome_anyother == None) {
      claim.delete(AnyOtherIncome)
    } else claim

    val directPayment = if(employment.yourIncome_directpay == None) {
      deletedAnyOther.delete(DirectPayment)
    } else deletedAnyOther

    val fosteringAllowance = if(employment.yourIncome_fostering == None) {
      directPayment.delete(FosteringAllowance)
    } else directPayment

    val statutoryMaternityAdoptionPay = if(employment.yourIncome_patmatadoppay == None) {
      fosteringAllowance.delete(StatutoryMaternityAdoptionPay)
    } else fosteringAllowance

    val statutorySickPay = if(employment.yourIncome_sickpay == None) {
      statutoryMaternityAdoptionPay.delete(StatutorySickPay)
    } else statutoryMaternityAdoptionPay

    val deletedSelfEmployment = if(employment.beenSelfEmployedSince1WeekBeforeClaim == no) {
      statutorySickPay.delete(AboutSelfEmployment).delete(SelfEmploymentYourAccounts).delete(SelfEmploymentPensionsAndExpenses)
    } else statutorySickPay

    val deletedEmployment = if(employment.beenEmployedSince6MonthsBeforeClaim == no) {
      deletedSelfEmployment.delete(BeenEmployed).delete(Jobs)
    } else deletedSelfEmployment
    deletedEmployment
  }

  private def validateOtherAndNonePaymentsSelected(employment: Emp) = {
    someTrue match {
      case (employment.yourIncome_anyother | employment.yourIncome_directpay | employment.yourIncome_fostering | employment.yourIncome_patmatadoppay | employment.yourIncome_sickpay) if(employment.yourIncome_none == someTrue) => false
      case _ => true
    }
  }

  private def validateOtherPaymentsSelected(employment: Emp) = {
    employment match {
      case Emp(_, _, None, None, None, None, None, None) => false
      case _ => true
    }
  }

  private def checkGoPreview(t:(Option[Emp],Emp),c:(Option[Claim],Claim)): Boolean = {
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

    if (doesNotHaveJobs) {
      false
    } else {
      previousEmp.isDefined && ( bothHaveNotChanged || selfENotChangedAndEmploymentNo || empNotChangedAndSENo) ||  bothAnswersAreNo
    }
    //We want to go back to preview from Employment guard questions page if
    // both answers haven't changed or if one hasn't changed and the changed one is 'no' or both answers are no, or
  }
}
