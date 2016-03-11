package controllers.circs.start_of_process

import controllers.CarersForms._
import models.domain._
import models.view.{CachedChangeOfCircs, Navigable}
import play.api.Logger
import play.api.Play._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import utils.helpers.CarersForm._
import play.api.i18n.{MessagesApi, I18nSupport, MMessages}

import scala.language.postfixOps


object GReportChangeReason extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val form = Form(mapping(
    "jsEnabled" -> boolean,
    "reportChanges" -> carersNonEmptyText(maxLength = 20)
  )(ReportChangeReason.apply)(ReportChangeReason.unapply))

  def present = newClaim{implicit circs => implicit request => lang =>
    Logger.info(s"Starting new $cacheKey - ${circs.uuid}")
    track(ReportChangeReason) {
      implicit circs => Ok(views.html.circs.start_of_process.reportChangeReason(form.fill(ReportChangeReason)))
    }
  }

  def submit = claiming {implicit circs => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.circs.start_of_process.reportChangeReason(formWithErrors)),
      f => checkForChangeInSelection(circs, f) -> {
        if (!f.jsEnabled) {
          Logger.info(s"No JS - Start ${circs.key} ${circs.uuid} User-Agent : ${request.headers.get("User-Agent").orNull}")
        }
        Redirect(circsPathAfterReason)
      }
    )
  }

  private def checkForChangeInSelection(circs: Claim, reportNewChanges: ReportChangeReason) = {
    val reportChanges = circs.questionGroup[ReportChangeReason].getOrElse(ReportChangeReason()).reportChanges
    if (reportNewChanges.reportChanges != reportChanges)
      circs.update(reportNewChanges).removeQuestionGroups(CircumstancesReportChanges, Set(CircumstancesYourDetails, reportNewChanges.identifier) )
    else circs.update(reportNewChanges)
  }
}
