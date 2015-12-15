package controllers.s_self_employment

import controllers.mappings.Mappings._
import controllers.s_employment.Employment
import models.domain.{Employment => Emp, _}
import models.view.{CachedClaim, Navigable}
import play.api.Play._
import play.api.data.Forms._
import play.api.data.{Form, FormError}
import play.api.mvc.Controller
import utils.helpers.CarersForm._
import play.api.i18n._

import scala.language.{postfixOps, reflectiveCalls}

object GEmployment extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
      "beenSelfEmployedSince1WeekBeforeClaim" -> nonEmptyText.verifying(validYesNo),
      "beenEmployedSince6MonthsBeforeClaim" -> nonEmptyText.verifying(validYesNo)
      )(Emp.apply)(Emp.unapply))

    def present = claimingWithCheck {  implicit claim => implicit request => implicit request2lang =>
      track(Employment) { implicit claim => Ok(views.html.s_self_employment.g_employment(form.fill(Emp))) }
    }

    def submit = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
      form.bindEncrypted.fold(
        formWithErrors => {
          val formWithErrorsUpdate = formWithErrors
            .replaceError("beenSelfEmployedSince1WeekBeforeClaim", errorRequired,
              FormError("beenSelfEmployedSince1WeekBeforeClaim",
                errorRequired,
                Seq(claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy => (dmy - 1 week).`dd/MM/yyyy`),claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))))
            .replaceError("beenEmployedSince6MonthsBeforeClaim", errorRequired,
              FormError("beenEmployedSince6MonthsBeforeClaim",
                errorRequired,
                Seq(claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy => (dmy - 6 months).`dd/MM/yyyy`),claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))))
          BadRequest(views.html.s_self_employment.g_employment(formWithErrorsUpdate))
        },employment => {
          val updatedClaim = claim.showHideSection(employment.beenEmployedSince6MonthsBeforeClaim == yes, models.domain.Employed)
                                  .showHideSection(employment.beenSelfEmployedSince1WeekBeforeClaim == yes, models.domain.SelfEmployment)

          val deletedEmployment = if(employment.beenEmployedSince6MonthsBeforeClaim == no){
            updatedClaim.delete(BeenEmployed).delete(Jobs)
          } else updatedClaim

          val deletedSelfEmployment = if(employment.beenSelfEmployedSince1WeekBeforeClaim == no){
            deletedEmployment.delete(AboutSelfEmployment).delete(SelfEmploymentYourAccounts).delete(SelfEmploymentPensionsAndExpenses)
          }else deletedEmployment

          deletedSelfEmployment.update(employment) -> Redirect(controllers.s_self_employment.routes.GAboutSelfEmployment.present())
        }
      )
    }.withPreviewConditionally[Emp](checkGoPreview)


  private def checkGoPreview(t:(Option[Emp],Emp),c:(Option[Claim],Claim)):Boolean = {
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

    if(doesNotHaveJobs){
      false
    }else{
      previousEmp.isDefined && ( bothHaveNotChanged || selfENotChangedAndEmploymentNo || empNotChangedAndSENo) ||  bothAnswersAreNo
    }
    //We want to go back to preview from Employment guard questions page if
    // both answers haven't changed or if one hasn't changed and the changed one is 'no' or both answers are no, or


  }
}
