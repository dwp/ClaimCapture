package controllers

import play.api.mvc.{Controller, Action}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import monitoring.HealthMonitor
import play.api.Logger

trait HealthController {
  this: Controller =>

  def health = Action { request =>
    val now = DateTimeFormat.forPattern("dd-MM-yy HH:mm:ss").print(DateTime.now())

    HealthMonitor.runHealthChecks().map( check => Logger.debug(check.toString()))

    Ok(views.html.common.health(now)(lang(request), request))
  }
}

object HealthController extends Controller with HealthController
