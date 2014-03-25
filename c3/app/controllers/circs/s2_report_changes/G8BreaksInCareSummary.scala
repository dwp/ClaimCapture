package controllers.circs.s2_report_changes

import play.api.mvc.Controller
import models.view.{CachedChangeOfCircs, Navigable}
import scala.language.postfixOps
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import utils.helpers.CarersForm._
import controllers.Mappings._
import models.yesNo.{YesNoWithDateAndQs, RadioWithText, YesNoWithDateTimeAndText, YesNoDontKnowWithDates}
import models.domain.{CircumstancesAddressChange, CircumstancesBreaksInCare}
import controllers.CarersForms._
import play.api.data.FormError
import utils.helpers.PastPresentLabelHelper._
import play.api.data.FormError

object G8BreaksInCareSummary extends Controller with CachedChangeOfCircs with Navigable {

  def present = claiming { implicit circs => implicit request => implicit lang =>
    track(CircumstancesBreaksInCare) {
      implicit circs => Ok(views.html.circs.s2_report_changes.g8_breaksInCareSummary(circs.questionGroup[CircumstancesBreaksInCare].getOrElse(new CircumstancesBreaksInCare())))
    }
  }
}
