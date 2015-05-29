package controllers.circs.s1_start_of_process

import app.{ReportChange => r}
import controllers.CarersForms._
import models.domain._
import models.view.{CachedChangeOfCircs, Navigable}
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import utils.helpers.CarersForm._

import scala.annotation.tailrec
import scala.collection.immutable.Stack
import scala.language.postfixOps

object G1ReportChanges extends Controller with CachedChangeOfCircs with Navigable {

  val form = Form(mapping(
    "jsEnabled" -> boolean,
    "reportChanges" -> carersNonEmptyText(maxLength = 20)
  )(ReportChanges.apply)(ReportChanges.unapply))

  def present = newClaim{implicit circs =>  implicit request =>  lang =>
    Logger.info(s"Starting new $cacheKey - ${circs.uuid}")
    track(ReportChanges) {
      implicit circs => Ok(views.html.circs.s1_start_of_process.g1_reportChanges(form.fill(ReportChanges))(lang))
    }
  }

  def submit = claiming {implicit circs =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.circs.s1_start_of_process.g1_reportChanges(formWithErrors)(lang)),
      form => circs.update(form) -> {
        if (!form.jsEnabled) {
          Logger.info(s"No JS - Start ${circs.key} ${circs.uuid} User-Agent : ${request.headers.get("User-Agent").orNull}")
        }
        Redirect(routes.G2ReportAChangeInYourCircumstances.present())
      }
    )
  }

}
