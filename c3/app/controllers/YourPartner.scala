package controllers

import play.api.mvc._
import models.claim._

object YourPartner extends Controller with CachedClaim {
  def yourPartner = claiming {
    implicit claim => implicit request =>
      val outcome = <h>BLAH</h>

      Ok(outcome)
  }
}