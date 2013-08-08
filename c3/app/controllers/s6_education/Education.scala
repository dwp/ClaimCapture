package controllers.s6_education

import play.api.mvc._
import models.view.CachedClaim
import models.domain.{AddressOfSchoolCollegeOrUniversity, YourCourseDetails, QuestionGroup, Claim}
import play.api.templates.Html
import controllers.Routing

object Education extends Controller with EducationRouting with CachedClaim {
  def whenSectionVisible(f: => SimpleResult[Html])(implicit claim: Claim) = {
    if (claim.isSectionVisible(models.domain.Education)) f
    else Redirect(controllers.s7_employment.routes.G1BeenEmployed.present())
  }

  def completed = claiming { implicit claim => implicit request =>
    whenSectionVisible(Ok(views.html.s6_education.g3_completed(completedQuestionGroups.map(qg => qg -> route(qg)))))
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    Redirect(claim.nextSection(models.domain.Education).firstPage)
  }

  private def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(models.domain.Education)
}

trait EducationRouting extends Routing {
  override def route(qgi: QuestionGroup.Identifier) = qgi match {
    case YourCourseDetails => routes.G1YourCourseDetails.present()
    case AddressOfSchoolCollegeOrUniversity => routes.G2AddressOfSchoolCollegeOrUniversity.present()
  }
}