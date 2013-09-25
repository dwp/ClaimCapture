package controllers.s7_employment

import scala.language.implicitConversions
import scala.reflect.ClassTag
import play.api.mvc._
import play.api.data.Form
import play.api.i18n.Messages
import models.view.{Navigable, CachedClaim}
import models.domain._

object Employment extends Controller with CachedClaim  with Navigable{
  implicit def jobFormFiller[Q <: QuestionGroup](form: Form[Q])(implicit classTag: ClassTag[Q]) = new {
    def fillWithJobID(qi: QuestionGroup.Identifier, jobID: String)(implicit claim: DigitalForm): Form[Q] = {
      claim.questionGroup(Jobs).getOrElse(Jobs()).asInstanceOf[Jobs].jobs.find(_.jobID == jobID).getOrElse(Job("", List())).find(_.identifier.id == qi.id) match {
        case Some(q: Q) => form.fill(q)
        case _ => form
      }
    }
  }

  def completedQuestionGroups(questionGroupIdentifier: QuestionGroup.Identifier, jobID: String)(implicit claim: DigitalForm): List[QuestionGroup] = {
    (for { jobs <- claim.questionGroup[Jobs]
           job <- jobs.find(_.jobID == jobID) }
      yield job.questionGroups.filter(_.identifier.index < questionGroupIdentifier.index)).getOrElse(Nil)
  }

  def jobs(implicit claim: DigitalForm) = claim.questionGroup(Jobs) match {
    case Some(js: Jobs) => js
    case _ => Jobs()
  }

  def completed = executeOnForm { implicit claim => implicit request =>
    track(Employed) { implicit claim => Ok(views.html.s7_employment.g15_completed()) }
  }

  def submit = executeOnForm { implicit claim => implicit request =>
    Redirect("/self-employment/about-self-employment")
  }

  def delete(jobID: String) = executeOnForm { implicit claim => implicit request =>
    import play.api.libs.json.Json

    val updatedJobs = jobs.delete(jobID)

    if (updatedJobs.isEmpty) claim.update(updatedJobs) -> Ok(Json.obj("answer" -> Messages("answer.label")))
    else claim.update(updatedJobs) -> Ok(Json.obj("answer" -> Messages("answer.more.label")))
  }
}