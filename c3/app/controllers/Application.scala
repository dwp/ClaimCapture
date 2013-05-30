package controllers

import play.api._
import play.api.mvc._
import models.view.{Eligibility, Claim}
import play.api.cache.Cache
import play.api.Play.current

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Carers Claim Capture"))
  }

  def cookies = Action { request =>



    request.session.get("connected").map { key =>

      val model = loadFromCache(key)

      if (model.isEmpty) {
        val exampleClaim = models.view.example.ExampleClaim()

        val claim =  Claim()
        updateCache(key, claim)
      }

      Logger.debug("Session Key from cookie. " )

      Ok(views.html.index("Key : " + key))

    }.getOrElse {

      val key = java.util.UUID.randomUUID().toString

      Logger.debug("Generating new Session Key: " + key)

      Ok(views.html.index("New Session Key:" + key)).withSession("connected" -> key)
    }

  }

  private def loadFromCache(key:String): Option[Claim] = {
    Logger.debug("loadFromCache: " + key )
    Cache.getAs[Claim](key)
  }


  private def updateCache(key:String, model:Claim) = {
    Logger.debug("updateCache: " + key + " " + model )
    Cache.set(key, model)
  }


}