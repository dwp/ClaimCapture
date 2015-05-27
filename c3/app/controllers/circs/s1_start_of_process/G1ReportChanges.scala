package controllers.circs.s1_start_of_process

import app.{ReportChange => r}
import controllers.CarersForms._
import models.domain._
import models.view.{CachedChangeOfCircs, Navigable}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Call, Controller}
import utils.helpers.CarersForm._

import scala.annotation.tailrec
import scala.collection.immutable.Stack
import scala.language.postfixOps

object G1ReportChanges extends Controller with CachedChangeOfCircs with Navigable {

  val form = Form(mapping(
    "reportChanges" -> carersNonEmptyText(maxLength = 20)
  )(ReportChanges.apply)(ReportChanges.unapply))

  def present = claimingWithCheck ({implicit circs =>  implicit request =>  lang =>
    track(ReportChanges) {
      implicit circs => Ok(views.html.circs.s2_report_changes.g1_reportChanges(form.fill(ReportChanges))(lang))
    }},checkCookie=true)


  def submit = claiming {implicit circs =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.circs.s2_report_changes.g1_reportChanges(formWithErrors)(lang)),
      form => updateCircs(form, circs)
    )
  }

  @tailrec
  private def popDeleteQG(circs:Claim,optSections:Stack[QuestionGroup.Identifier]):Claim = {
    if (optSections.isEmpty) circs
    else popDeleteQG(circs delete(optSections top),optSections pop)
  }

  private def updateCircs(f:ReportChanges, circs:Claim) = {
    import controllers.circs.s2_report_changes.routes

    // for qs groups under this section, if it is not reportedChange - delete
    val optSections = Stack(CircumstancesSelfEmployment,CircumstancesOtherInfo,CircumstancesStoppedCaring,
      CircumstancesPaymentChange, CircumstancesAddressChange, CircumstancesBreaksInCare, CircumstancesEmploymentChange)

    val selectedQG:(QuestionGroup.Identifier,Call) = {
      f.reportChanges match {
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

    updatedCircs.update(f) -> Redirect(selectedQG._2)
  }

}
