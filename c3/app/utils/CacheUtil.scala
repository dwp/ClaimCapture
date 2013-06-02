package utils

import models.view.example.Claim
import play.api.Logger
import play.api.cache.Cache
import play.api.Play.current

object CacheUtil {

  def loadFromCache(key:String): Option[Claim] = {
    Logger.debug("loadFromCache: " + key )
    var claimOption:Option[Claim] = Cache.getAs[Claim](key)

    if(claimOption.isEmpty) {
      val claim =  Claim()
      Cache.set(key, claim)
      claimOption = Option(claim)
    }
    claimOption
  }


  def updateCache(key:String, model:Claim) = {
    Logger.debug("updateCache: " + key + " " + model )
    Cache.set(key, model)
  }


}
