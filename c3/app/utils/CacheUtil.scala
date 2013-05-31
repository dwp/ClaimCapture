package utils

import models.view.example.ExampleClaim
import play.api.Logger
import play.api.cache.Cache
import play.api.Play.current

object CacheUtil {

  def loadFromCache(key:String): Option[ExampleClaim] = {
    Logger.debug("loadFromCache: " + key )
    var claimOption:Option[ExampleClaim] = Cache.getAs[ExampleClaim](key)

    if(claimOption.isEmpty) {
      val claim =  ExampleClaim()
      Cache.set(key, claim)
      claimOption = Option(claim)
    }
    claimOption
  }


  def updateCache(key:String, model:ExampleClaim) = {
    Logger.debug("updateCache: " + key + " " + model )
    Cache.set(key, model)
  }


}
