package monitoring

import gov.dwp.carers.CADSHealthCheck
import gov.dwp.carers.CADSHealthCheck.Result
import models.view.ClaimHandling
import play.api.cache.CacheApi
import play.api.Play.current
import scala.concurrent.duration._

class CacheCheck extends CADSHealthCheck(ClaimHandling.C3NAME, ClaimHandling.C3VERSION_VALUE) {

  override protected def check: CADSHealthCheck.Result = {
    val cache = current.injector.instanceOf[CacheApi]
    cache.set("cache-test", "testing", Duration(500, MILLISECONDS) )
    cache.get("cache-test") match {
      case Some(date) => Result.healthy
      case None => Result.unhealthy("cache unavailable or too slow.")
    }
  }
}

