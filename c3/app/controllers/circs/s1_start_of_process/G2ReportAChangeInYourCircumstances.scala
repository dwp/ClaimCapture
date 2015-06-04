package controllers.circs.s1_start_of_process

import app.{ReportChange => r}
import controllers.CarersForms._
import controllers.circs.s1_start_of_process.G1ReportChanges._
import controllers.mappings.Mappings._
import controllers.mappings.NINOMappings._
import models.domain._
import models.view.{CachedChangeOfCircs, Navigable}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Call, Controller}
import utils.helpers.CarersForm._

import scala.annotation.tailrec
import scala.collection.immutable.Stack
import scala.language.reflectiveCalls

object G2ReportAChangeInYourCircumstances extends Controller with CachedChangeOfCircs with Navigable {

  val fullName = "fullName"
  val nationalInsuranceNumber = "nationalInsuranceNumber"
  val dateOfBirth = "dateOfBirth"
  val theirFullName = "theirFullName"
  val theirRelationshipToYou = "theirRelationshipToYou"

  val form = Form(mapping(
    fullName -> carersNonEmptyText(maxLength = 35),
    nationalInsuranceNumber -> nino.verifying(filledInNino).verifying(validNino),
    dateOfBirth -> dayMonthYear.verifying(validDate),
    theirFullName -> carersNonEmptyText(maxLength = 35),
    theirRelationshipToYou -> carersNonEmptyText(maxLength = 35)
  )(CircumstancesReportChange.apply)(CircumstancesReportChange.unapply))

  def present = claiming ({ implicit circs =>  implicit request =>  lang =>
      track(CircumstancesReportChange) {
        implicit circs => Ok(views.html.circs.s1_start_of_process.g2_reportAChangeInYourCircumstances(form.fill(CircumstancesReportChange))(lang))
      }
  },checkCookie=true)

  def submit = claiming ({ implicit circs =>  implicit request =>  lang =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.circs.s1_start_of_process.g2_reportAChangeInYourCircumstances(formWithErrors)(lang)),
        f => circs.update(f) -> getReportChangesRedirect(circs)
      )
  },checkCookie=true)

  private def getReportChangesRedirect(circs:Claim) = {
    import controllers.circs.s2_report_changes.routes

    val reportChanges = circs.questionGroup[ReportChanges].getOrElse(ReportChanges()).reportChanges

    // for qs groups under this section, if it is not reportedChange - delete
    val optSections = Stack(CircumstancesSelfEmployment,CircumstancesOtherInfo,CircumstancesStoppedCaring,
      CircumstancesPaymentChange, CircumstancesAddressChange, CircumstancesBreaksInCare, CircumstancesEmploymentChange)

    val selectedQG:(QuestionGroup.Identifier,Call) = {
      reportChanges match {
        case r.SelfEmployment.name => CircumstancesSelfEmployment -> routes.G2SelfEmployment.present()
        case r.EmploymentChange.name => CircumstancesEmploymentChange -> routes.G9EmploymentChange.present()
        case r.AddressChange.name => CircumstancesAddressChange  -> routes.G6AddressChange.present()
        case r.StoppedCaring.name =>  CircumstancesStoppedCaring  -> routes.G3PermanentlyStoppedCaring.present()
        case r.PaymentChange.name => CircumstancesPaymentChange  -> routes.G5PaymentChange.present()
        case r.BreakFromCaring.name => CircumstancesBreaksInCare  -> routes.G7BreaksInCare.present()
        case _ => CircumstancesOtherInfo      -> routes.G4OtherChangeInfo.present()
      }
    }

    val updatedCircs = popDeleteQG(circs,optSections.filter(_.id != selectedQG._1.id))

    Redirect(selectedQG._2)
  }

  @tailrec
  private def popDeleteQG(circs:Claim,optSections:Stack[QuestionGroup.Identifier]):Claim = {
    if (optSections.isEmpty) circs
    else popDeleteQG(circs delete(optSections top),optSections pop)
  }
}
