package controllers.preview

import play.api.mvc.Controller
import models.view.{Navigable, CachedClaim}
import play.api.Logger
import models.domain.YourDetails

object Preview extends Controller with CachedClaim with Navigable {


  def present = claiming { implicit claim => implicit request => implicit lang =>
    Ok(views.html.preview.preview())
  }
}
