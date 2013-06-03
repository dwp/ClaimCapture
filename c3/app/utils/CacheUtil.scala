package utils

import models.view.example.Claim
import play.api.Logger
import play.api.cache.Cache
import play.api.Play.current

object CacheUtil {

  def loadFromCache(key: String): Claim = {
    Logger.debug("loadFromCache: " + key)

    Cache.getAs[Claim](key).getOrElse({
      val claim = Claim()
      Cache.set(key, claim)
      claim
    })
  }


  def updateCache(key: String, model: Claim) {
    Logger.debug("updateCache: " + key + " " + model)
    Cache.set(key, model)
  }


}
