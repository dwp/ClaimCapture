package controllers.s7_employment

import play.api.Logger

import language.reflectiveCalls
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm._
import controllers.mappings.Mappings._
import models.domain.{Employment => Emp, _}
import scala.language.postfixOps
import controllers.mappings.Mappings._

object G1Employment extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
      "beenSelfEmployedSince1WeekBeforeClaim" -> nonEmptyText.verifying(validYesNo),
      "beenEmployedSince6MonthsBeforeClaim" -> nonEmptyText.verifying(validYesNo)
      )(Emp.apply)(Emp.unapply))

    def present = claimingWithCheck {  implicit claim =>  implicit request =>  lang =>
      claim.questionGroup(ClaimDate) match {
        case Some(n) => track(Employment) { implicit claim => Ok(views.html.s7_employment.g1_employment(form.fill(Emp))(lang)) }
        case _ => Redirect("/")
      }
    }

    def submit = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
      form.bindEncrypted.fold(
        formWithErrors => {
          val formWithErrorsUpdate = formWithErrors
            .replaceError("beenEmployedSince6MonthsBeforeClaim", "error.required",
              FormError("aboutYou_beenEmployedSince6MonthsBeforeClaim.label",
                "error.required",
                Seq(claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy => (dmy - 6 months).`dd/MM/yyyy`),claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))))
            .replaceError("beenSelfEmployedSince1WeekBeforeClaim", "error.required",
              FormError("aboutYou_beenSelfEmployedSince1WeekBeforeClaim.label",
                "error.required",
                Seq(claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy => (dmy - 6 months).`dd/MM/yyyy`),claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))))
          BadRequest(views.html.s7_employment.g1_employment(formWithErrorsUpdate)(lang))
        },employment => {
          val updatedClaim = claim.showHideSection(employment.beenEmployedSince6MonthsBeforeClaim == yes, Employed)
                                  .showHideSection(employment.beenSelfEmployedSince1WeekBeforeClaim == yes, SelfEmployment)

          val deletedEmployment = if(employment.beenEmployedSince6MonthsBeforeClaim == no){
            updatedClaim.delete(BeenEmployed).delete(Jobs)
          } else updatedClaim

          val deletedSelfEmployment = if(employment.beenSelfEmployedSince1WeekBeforeClaim == no){
            deletedEmployment.delete(AboutSelfEmployment).delete(SelfEmploymentYourAccounts).delete(SelfEmploymentPensionsAndExpenses)
          }else deletedEmployment

          deletedSelfEmployment.update(employment) -> Redirect(controllers.s8_self_employment.routes.G1AboutSelfEmployment.present())
        }
      )
    }.withPreviewConditionally[Emp](checkGoPreview)


  private def checkGoPreview(t:(Option[Emp],Emp)):Boolean = {
    val previousEmp = t._1
    val actualEmp = t._2

    lazy val employmentHasChanged = previousEmp.get.beenEmployedSince6MonthsBeforeClaim != actualEmp.beenEmployedSince6MonthsBeforeClaim
    lazy val selfEmploymentHasChanged = previousEmp.get.beenSelfEmployedSince1WeekBeforeClaim != actualEmp.beenSelfEmployedSince1WeekBeforeClaim

    lazy val bothHaveNotChanged = !employmentHasChanged && !selfEmploymentHasChanged
    lazy val selfENotChangedAndEmploymentNo = previousEmp.get.beenEmployedSince6MonthsBeforeClaim == yes && actualEmp.beenEmployedSince6MonthsBeforeClaim == no && ! selfEmploymentHasChanged
    lazy val empNotChangedAndSENo = previousEmp.get.beenSelfEmployedSince1WeekBeforeClaim == yes && actualEmp.beenSelfEmployedSince1WeekBeforeClaim == no && ! employmentHasChanged
    val bothAnswersAreNo = actualEmp.beenEmployedSince6MonthsBeforeClaim == no && actualEmp.beenSelfEmployedSince1WeekBeforeClaim == no
    //We want to go back to preview from Employment guard questions page if
    // both answers haven't changed or if one hasn't changed and the changed one is 'no' or both answers are no, or
    val goToPreview = previousEmp.isDefined && ( bothHaveNotChanged || selfENotChangedAndEmploymentNo || empNotChangedAndSENo) ||  bothAnswersAreNo

    goToPreview

  }
}
