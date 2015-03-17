package controllers

import play.api.mvc.{Controller, Action}
import play.api.libs.json._
import com.codahale.metrics.health.HealthCheck
import utils.Injector
import monitor.HealthMonitor
import utils.helpers.CarersLanguageHelper


trait HealthController extends Injector with CarersLanguageHelper{
  this: Controller =>

  private val healthMonitor = resolve(classOf[HealthMonitor])

  implicit val healthWrites = new Writes[(String,HealthCheck.Result)] {
    def writes(healthCheck:(String,HealthCheck.Result)) = Json.obj(
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
