package controllers

import play.api.mvc.{Controller, Action}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import monitoring.HealthMonitor
import play.api.Logger
import play.api.libs.json._
import com.codahale.metrics.health.HealthCheck

trait HealthController {
  this: Controller =>

  implicit val healthWrites = new Writes[(String,HealthCheck.Result)] {
    def writes(healthCheck:(String,HealthCheck.Result)) = Json.obj(
      "name" -> healthCheck._1,
      "isHealthy" -> healthCheck._2.isHealthy
    )
  }

  def health = Action {
    request =>
      val now = DateTimeFormat.forPattern("dd-MM-yy HH:mm:ss").print(DateTime.now())

      HealthMonitor.runHealthChecks().map(check => Logger.debug(check.toString()))

      Ok(views.html.common.health(now)(lang(request), request))
  }

  def healthReport = Action {
    request =>
      Ok(Json.toJson(HealthMonitor.runHealthChecks())).as("application/json").withHeaders("Cache-Control" -> "must-revalidate,no-cache,no-store")
  }
}

object HealthController extends Controller with HealthController
