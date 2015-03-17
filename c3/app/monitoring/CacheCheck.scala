package monitoring

import com.codahale.metrics.health.HealthCheck
import com.codahale.metrics.health.HealthCheck.Result
import play.api.cache.Cache
import play.api.Play.current
import scala.concurrent.duration._

class CacheCheck extends HealthCheck {

  override protected def check: HealthCheck.Result = {
    Cache.set("cache-test", "testing", Duration(500, MILLISECONDS) )
    Cache.get("cache-test") match {
      case Some(date) => Result.healthy
      case None => Result.unhealthy("cache unavailable or too slow.")
    }
  }
}

