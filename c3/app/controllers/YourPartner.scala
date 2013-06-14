package controllers

import play.api.mvc._
import models.claim._

object YourPartner extends Controller with CachedClaim {
  def goto(formID: String) = TODO

  def yourPartner = claiming {
    implicit claim => implicit request =>
      Ok("")
  }
}