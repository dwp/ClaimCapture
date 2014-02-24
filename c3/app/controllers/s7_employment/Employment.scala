package controllers.s7_employment

import scala.language.implicitConversions
import scala.reflect.ClassTag
import play.api.mvc._
import play.api.data.Form
import play.api.i18n.{MMessages => Messages}
import models.view.{Navigable, CachedClaim}
import models.domain._

object Employment extends Controller with CachedClaim  with Navigable{
  implicit def jobFormFiller[Q <: QuestionGroup](form: Form[Q])(implicit classTag: ClassTag[Q]) = new {
    def fillWithJobID(qi: QuestionGroup.Identifier, jobID: String)(implicit claim: Claim): Form[Q] = {
      claim.questionGroup(Jobs).getOrElse(Jobs()).asInstanceOf[Jobs].jobs.find(_.jobID == jobID).getOrElse(Job("", List())).find(_.identifier.id == qi.id) match {
        case Some(q: Q) => form.fill(q)
        case _ => form
      }
    }
  }

  def completedQuestionGroups(questionGroupIdentifier: QuestionGroup.Identifier, jobID: String)(implicit claim: Claim): List[QuestionGroup] = {
    (for { jobs <- claim.questionGroup[Jobs]
           job <- jobs.find(_.jobID == jobID) }
      yield job.questionGroups.filter(_.identifier.index < questionGroupIdentifier.index)).getOrElse(Nil)
  }

  def jobs(implicit claim: Claim) = claim.questionGroup(Jobs) match {
    case Some(js: Jobs) => js
    case _ => Jobs()
  }

  def delete(jobID: String) = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    import play.api.libs.json.Json

    val updatedJobs = jobs.delete(jobID)

    if (updatedJobs == jobs) BadRequest(s"""Failed to delete job with ID "$jobID" as it does not exist""")
    else if (updatedJobs.isEmpty) claim.update(updatedJobs) -> Ok(Json.obj("answer" -> Messages("answer.label")))
    else claim.update(updatedJobs) -> Ok(Json.obj("answer" -> Messages("answer.more.label")))
  }
}