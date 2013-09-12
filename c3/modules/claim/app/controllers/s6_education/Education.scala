package controllers.s6_education

import play.api.mvc._
import models.view.{Navigable, CachedClaim}

object Education extends Controller with CachedClaim with Navigable {
  def completed = executeOnForm {implicit claim => implicit request =>
    track(models.domain.Education) { implicit claim => Ok(views.html.s6_education.g3_completed()) }
  }

  def completedSubmit = executeOnForm {implicit claim => implicit request =>
    Redirect("/employment/been-employed")
  }
}