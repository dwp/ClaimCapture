package controllers.circs.start_of_process

import app.{ReportChange => r}
import controllers.CarersForms._
import models.domain._
import models.view.{CachedChangeOfCircs, Navigable}
import play.api.Logger
import play.api.Play._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Call, Controller}
import utils.helpers.CarersForm._
import play.api.i18n.{MessagesApi, I18nSupport, MMessages}
import scala.annotation.tailrec
import scala.collection.immutable.Stack
import scala.language.{postfixOps, reflectiveCalls}

object GReportChangeReason extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val form = Form(mapping(
    "jsEnabled" -> boolean,
    "reportChanges" -> carersNonEmptyText(maxLength = 20)
  )(ReportChangeReason.apply)(ReportChangeReason.unapply))

  def present = optionalClaim {implicit circs => implicit request => lang =>
    Logger.info(s"Starting new $cacheKey - ${circs.uuid}")
    track(ReportChangeReason) {
      implicit circs => Ok(views.html.circs.start_of_process.reportChangeReason(form.fill(ReportChangeReason)))
    }
  }

  def submit = claiming {implicit circs => implicit request => implicit request2lang =>
    Logger.info(s"Circs change reason page with google-analytics:${circs.gacid}.")
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.circs.start_of_process.reportChangeReason(formWithErrors)),
      f => checkForChangeInSelection(circs, f) -> getReportChangesRedirect(checkForChangeInSelection(circs, f))
    )
  }

  private def getReportChangesRedirect(circs:Claim) = {
    val reportChanges = circs.questionGroup[ReportChangeReason].getOrElse(ReportChangeReason()).reportChanges
    val breakInCare = circs.questionGroup[CircumstancesBreaksInCare].getOrElse(CircumstancesBreaksInCare())

    // for qs groups under this section, if it is not reportedChange - delete
    val optSections = Stack(CircumstancesSelfEmployment,CircumstancesOtherInfo,CircumstancesStoppedCaring,
      CircumstancesPaymentChange, CircumstancesAddressChange, CircumstancesBreaksInCare, CircumstancesEmploymentChange)

    val selectedQG:(QGIdentifier,Call) = {
      reportChanges match {
        case r.EmploymentChange.name => CircumstancesEmploymentChange -> controllers.circs.report_changes.routes.GEmploymentChange.present()
        case r.AddressChange.name => CircumstancesAddressChange  -> controllers.circs.report_changes.routes.GAddressChange.present()
        case r.StoppedCaring.name =>  CircumstancesStoppedCaring  -> controllers.circs.report_changes.routes.GPermanentlyStoppedCaring.present()
        case r.PaymentChange.name => CircumstancesPaymentChange  -> controllers.circs.report_changes.routes.GPaymentChange.present()
        case r.BreakFromCaring.name if(breakInCare.medicalCareDuringBreak != "") => CircumstancesBreaksInCare  -> controllers.circs.breaks_in_care.routes.GBreaksInCareSummary.present()
        case r.BreakFromCaring.name => CircumstancesBreaksInCare  -> controllers.circs.breaks_in_care.routes.GBreaksInCareSummary.present()
        case _ => CircumstancesOtherInfo -> controllers.circs.report_changes.routes.GOtherChangeInfo.present()
      }
    }

    val updatedCircs = popDeleteQG(circs, optSections.filter(_.id != selectedQG._1.id))
    Redirect(controllers.circs.your_details.routes.GYourDetails.present())
  }

  @tailrec
  private def popDeleteQG(circs:Claim,optSections:Stack[QGIdentifier]):Claim = {
    if (optSections.isEmpty) circs
    else popDeleteQG(circs delete(optSections top),optSections pop)
  }

  private def checkForChangeInSelection(circs: Claim, reportNewChanges: ReportChangeReason): Claim = {
    val reportChanges = circs.questionGroup[ReportChangeReason].getOrElse(ReportChangeReason()).reportChanges
    if (reportNewChanges.reportChanges != reportChanges)
      circs.update(reportNewChanges).removeQuestionGroups(CircumstancesReportChanges, Set(CircumstancesYourDetails, reportNewChanges.identifier) )
    else
      circs.update(reportNewChanges)
  }
}
