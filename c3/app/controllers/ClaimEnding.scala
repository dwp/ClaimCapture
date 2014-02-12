package controllers

import play.api.mvc.Controller
import models.view.CachedClaim

object ClaimEnding extends Controller with CachedClaim {

  def timeout = ending {implicit claim => implicit request  =>
    Ok(views.html.common.session_timeout(startPage))
  }

  def error = ending {implicit claim => implicit request  =>
    Ok(views.html.common.error(startPage))
  }

  def thankyou = ending { implicit claim => implicit request  =>
    Ok(views.html.common.thankYouClaim())
  }

}
