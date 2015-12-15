package controllers.circs.s1_start_of_process

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


object G1ReportChanges extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val form = Form(mapping(
    "jsEnabled" -> boolean,
    "reportChanges" -> carersNonEmptyText(maxLength = 20)
  )(ReportChanges.apply)(ReportChanges.unapply))

  def present = newClaim{implicit circs => implicit request => lang =>
    Logger.info(s"Starting new $cacheKey - ${circs.uuid}")
    track(ReportChanges) {
      implicit circs => Ok(views.html.circs.s1_start_of_process.g1_reportChanges(form.fill(ReportChanges)))
    }
  }

  def submit = claiming {implicit circs => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.circs.s1_start_of_process.g1_reportChanges(formWithErrors)),
      form => circs.update(form).removeQuestionGroups(CircumstancesReportChanges, Set(CircumstancesReportChange, form.identifier) ) -> {
        if (!form.jsEnabled) {
          Logger.info(s"No JS - Start ${circs.key} ${circs.uuid} User-Agent : ${request.headers.get("User-Agent").orNull}")
        }
        Redirect(routes.G2ReportAChangeInYourCircumstances.present())
      }
    )
  }

}
