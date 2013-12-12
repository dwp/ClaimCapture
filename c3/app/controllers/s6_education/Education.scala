package controllers.s6_education

import play.api.mvc._
import models.view.{Navigable, CachedClaim}

object Education extends Controller with CachedClaim with Navigable {
  def completed = claiming { implicit claim => implicit request => implicit lang =>
    track(models.domain.Education) { implicit claim => Ok(views.html.s6_education.g3_completed()) }
  }

  def completedSubmit = claiming { implicit claim => implicit request => implicit lang =>
    Redirect("/employment/been-employed")
  }
}