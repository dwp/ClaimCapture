package controllers

import gov.dwp.carers.CADSHealthCheck
import play.api.Play._
import play.api.mvc.{Controller, Action}
import play.api.libs.json._
import monitor.HealthMonitor
import utils.helpers.CarersLanguageHelper


trait HealthController extends CarersLanguageHelper {
  this: Controller =>

  private val healthMonitor = current.injector.instanceOf[HealthMonitor]

  implicit val healthWrites = new Writes[(String, CADSHealthCheck.Result)] {
    def writes(healthCheck:(String, CADSHealthCheck.Result)) = Json.obj(
      "application name" -> healthCheck._2.getApplication,
      "version" -> healthCheck._2.getVersion,
      "name" -> healthCheck._1,
      "isHealthy" -> healthCheck._2.isHealthy
    )
  }

  def healthReport = Action {
    request =>
      Ok(Json.prettyPrint(Json.toJson(healthMonitor.runHealthChecks()))).as("application/json").withHeaders("Cache-Control" -> "must-revalidate,no-cache,no-store")
  }

  def ping = Action {
    request => Ok("ping")
  }
}

object HealthController extends Controller with HealthController
