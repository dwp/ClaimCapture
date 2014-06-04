package monitoring

import com.codahale.metrics.health.HealthCheck
import com.codahale.metrics.health.HealthCheck.Result
import play.api.cache.Cache
import org.joda.time.DateTime
import play.api.Play.current

class CacheCheck extends HealthCheck {

  override protected def check: HealthCheck.Result = {
    Cache.set("cache-test", new DateTime()) // any exceptions thrown here are converted to Result.unhealthy
    Result.healthy
  }
}

