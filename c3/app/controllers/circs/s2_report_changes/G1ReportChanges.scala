package controllers.circs.s2_report_changes

import play.api.mvc.{Call, Controller}
import models.view.{Navigable, CachedChangeOfCircs}
import play.api.data.Form
import play.api.data.Forms._
import controllers.CarersForms._
import scala.language.postfixOps
import utils.helpers.CarersForm._
import app.{ReportChange => r}
import models.domain._
import scala.collection.immutable.Stack
import scala.annotation.tailrec

object G1ReportChanges extends Controller with CachedChangeOfCircs with Navigable {

  val form = Form(mapping(
    "reportChanges" -> nonEmptyText(maxLength = 20)
  )(ReportChanges.apply)(ReportChanges.unapply))

  def present = claiming { implicit circs => implicit request => implicit lang =>
    track(ReportChanges) {
      implicit circs => Ok(views.html.circs.s2_report_changes.g1_reportChanges(form.fill(ReportChanges)))
    }
  }

  def submit = claiming { implicit circs => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.circs.s2_report_changes.g1_reportChanges(formWithErrors)),
      form => updateCircs(form, circs)
    )
  }

  @tailrec
  def popDeleteQG(circs:Claim,optSections:Stack[QuestionGroup.Identifier]):Claim = {
    if (optSections.isEmpty) circs
    else popDeleteQG(circs delete(optSections top),optSections pop)
  }

  def updateCircs(f:ReportChanges, circs:Claim) = {
    import controllers.circs.s2_report_changes.{routes => routes}

    // for qs groups under this section, if it is not reportedChange - delete
    val optSections = Stack(CircumstancesSelfEmployment,CircumstancesOtherInfo,CircumstancesStoppedCaring,CircumstancesPaymentChange)

    val selectedQG:(QuestionGroup.Identifier,Call) =
      if (f.reportChanges      == r.SelfEmployment.name) CircumstancesSelfEmployment -> routes.G2SelfEmployment.present()
      else if (f.reportChanges == r.AddressChange.name)  CircumstancesAddressChange  -> routes.G6AddressChange.present()
      else if (f.reportChanges == r.StoppedCaring.name)  CircumstancesStoppedCaring  -> routes.G3PermanentlyStoppedCaring.present()
      else if (f.reportChanges == r.PaymentChange.name)  CircumstancesPaymentChange  -> routes.G5PaymentChange.present()
      else                                               CircumstancesOtherInfo      -> routes.G4OtherChangeInfo.present()

    val updatedCircs = popDeleteQG(circs,optSections.filter(_.id != selectedQG._1.id))

    updatedCircs.update(f) -> Redirect(selectedQG._2)
  }
}
