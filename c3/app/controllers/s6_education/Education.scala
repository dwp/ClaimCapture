package controllers.s6_education

import play.api.mvc._
import models.view.CachedClaim
import collection.immutable.ListMap
import play.api.mvc.Call
import models.domain.Claim
import play.api.templates.Html

object Education extends Controller with CachedClaim {

  val route: ListMap[String, Call] = ListMap(G1YourCourseDetails,
    G2AddressOfSchoolCollegeOrUniversity)


  def whenVisible(claim: Claim)(closure: () => SimpleResult[Html]) = {
    val iAmVisible = claim.isSectionVisible(models.domain.Education.id)

    if (iAmVisible) closure() else Ok(<title>TODO</title><body>TODO: Should Redirect To Employment SECTION</body>).as(HTML)
  }

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(models.domain.Education.id)

  def completed = claiming {
    implicit claim => implicit request =>
      Ok(views.html.s6_education.g3_completed(completedQuestionGroups))
  }

  def completedSubmit = claiming {
    implicit claim => implicit request =>
      ???
  }
}
