package controllers

import app.ConfigProperties._
import play.api.mvc._
import models.view.CachedClaim

object Application extends Controller with CachedClaim {

  // Feature toggle for new referer mechanism. See US544
  val gdsPageOk: Boolean = getProperty("gov.uk.start.ok", default = true)
  val startUrl: String = getProperty("claim.start.page", "/allowance/benefits")

  val govUk: String = getProperty("gov.uk.start.page", "https://www.gov.uk/apply-carers-allowance")

  def index = Action {
    if (gdsPageOk) {
      MovedPermanently(govUk)
    } else {
      // Temporary
      Redirect(startUrl)
    }
  }
}