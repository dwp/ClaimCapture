package controllers.submission

import play.api.mvc.Controller
import models.view.CachedClaim


object AsyncSubmissionController extends Controller with CachedClaim{

  def submit = claiming{ implicit claim => implicit request => implicit lang =>
    Ok("")
  }

}
