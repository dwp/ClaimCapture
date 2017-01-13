package controllers.circs.start_of_process

import app.{ReportChange => r}
import models.domain._
import models.view.{CachedChangeOfCircs, Navigable}
import play.api.Play._
import play.api.i18n.{I18nSupport, MMessages, MessagesApi}
import play.api.mvc.{Call, Controller}

import scala.annotation.tailrec
import scala.collection.immutable.Stack
import scala.language.{postfixOps, reflectiveCalls}

object GGoToCircsFunction extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  def present = claiming ({ implicit circs => implicit request2lang => implicit request =>
    getReportChangesRedirect(circs)
  })

  def getReportChangesRedirect(circs:Claim) = {
    val reportChanges = circs.questionGroup[ReportChangeReason].getOrElse(ReportChangeReason()).reportChanges

    // for qs groups under this section, if it is not reportedChange - delete
    val optSections = Stack(CircumstancesSelfEmployment,CircumstancesOtherInfo,CircumstancesStoppedCaring,
      CircumstancesPaymentChange, CircumstancesAddressChange, CircumstancesBreaksInCare, CircumstancesEmploymentChange)

    val selectedQG:(QGIdentifier,Call) = {
      reportChanges match {
        case r.EmploymentChange.name => CircumstancesEmploymentChange -> controllers.circs.report_changes.routes.GEmploymentChange.present()
        case r.AddressChange.name => CircumstancesAddressChange  -> controllers.circs.report_changes.routes.GAddressChange.present()
        case r.StoppedCaring.name =>  CircumstancesStoppedCaring  -> controllers.circs.report_changes.routes.GPermanentlyStoppedCaring.present()
        case r.PaymentChange.name => CircumstancesPaymentChange  -> controllers.circs.report_changes.routes.GPaymentChange.present()
        case r.BreakFromCaring.name => CircumstancesBreaksInCare  -> controllers.circs.report_changes.routes.GBreaksInCare.present()
        case _ => CircumstancesOtherInfo -> controllers.circs.report_changes.routes.GOtherChangeInfo.present()
      }
    }

    val updatedCircs = popDeleteQG(circs,optSections.filter(_.id != selectedQG._1.id))
    Redirect(selectedQG._2)
  }

  @tailrec
  private def popDeleteQG(circs:Claim,optSections:Stack[QGIdentifier]):Claim = {
    if (optSections.isEmpty) circs
    else popDeleteQG(circs delete(optSections top),optSections pop)
  }
}
