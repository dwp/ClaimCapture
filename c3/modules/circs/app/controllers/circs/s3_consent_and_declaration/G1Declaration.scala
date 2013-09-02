package controllers.circs.s3_consent_and_declaration

import play.api.mvc.Controller
import models.view.{Navigable, CachedClaim}

object G1Declaration extends Controller with CachedClaim with Navigable {

  def present = claiming { implicit claim => implicit request =>
    Ok(<html><title>Declaration - Changes of Circumstances</title><body>To Be Done</body></html>).as(HTML)
  }

}
