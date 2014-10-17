package controllers

import play.api.mvc.{Controller, Action}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.Logger
import play.api.libs.json._
import com.codahale.metrics.health.HealthCheck
import utils.Injector
import monitor.HealthMonitor
import utils.helpers.CarersLanguageHelper


trait HealthController extends Injector with CarersLanguageHelper{
  this: Controller =>

  val healthMonitor = resolve(classOf[HealthMonitor])

  implicit val healthWrites = new Writes[(String,HealthCheck.Result)] {
    def writes(healthCheck:(String,HealthCheck.Result)) = Json.obj(
      "name" -> healthCheck._1,
      "isHealthy" -> healthCheck._2.isHealthy
    )
  }

  def health = Action {
    request =>
      val now = DateTimeFormat.forPattern("dd-MM-yy HH:mm:ss").print(DateTime.now())

      healthMonitor.runHealthChecks().map(check => Logger.debug(check.toString()))

      Ok(views.html.common.health(now)(request,lang(request)))
  }

  def healthReport = Action {
    request =>
      Ok(Json.prettyPrint(Json.toJson(healthMonitor.runHealthChecks()))).as("application/json").withHeaders("Cache-Control" -> "must-revalidate,no-cache,no-store")
  }
}

object HealthController extends Controller with HealthController
