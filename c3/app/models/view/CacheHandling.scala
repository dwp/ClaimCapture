package models.view

import app.ConfigProperties._
import models.domain.Claim
import monitoring.Histograms
import net.sf.ehcache.CacheManager
import play.api.Logger
import play.api.cache.Cache
import play.api.mvc.{AnyContent, Request}
import play.api.Play.current

import scala.util.Try

trait CacheHandling {

  def cacheKey: String

  // Expiration value
  protected val expiration = getProperty("cache.expiry", 3600)

  protected def keyAndExpiration(r: Request[AnyContent]): (String, Int) = {
    r.session.get(cacheKey).getOrElse("") -> expiration
  }

  /**
   * Tries to get the claim of change of circs from the cache.
   * @param request the http request that has the session with uuid of claim which is the key used by cache.
   * @return None if could not find claim/CoCs. Some(claim) is could find it.
   */
  def fromCache(request: Request[AnyContent]): Option[Claim] = {
    val key = keyAndExpiration(request)._1
    if (key.isEmpty) {
      // Log an error if session empty or with no cacheKey entry so we know it is not a cache but a cookie issue.
      Logger.error(s"Did not receive Session information for a $cacheKey for ${request.method} url path ${request.path} and agent ${request.headers.get("User-Agent").getOrElse("Unknown agent")}. Probably a cookie issue: ${request.cookies.filterNot(_.name.startsWith("_"))}.")
      None
    } else {
      Cache.getAs[Claim](key)
    }
  }

  protected def recordMeasurements() = {
    Histograms.recordCacheSize(Try(CacheManager.getInstance().getCache("play").getKeysWithExpiryCheck.size()).getOrElse(0))
  }
}