package controllers.s7_employment

import scala.language.implicitConversions
import play.api.mvc.{AnyContent, Request, Controller}
import models.view.CachedClaim
import models.domain._
import models.domain.Claim
import play.api.data.Form
import scala.reflect.ClassTag

object Employment extends Controller with CachedClaim {
  implicit def jobFormFiller[Q <: QuestionGroup](form: Form[Q])(implicit classTag: ClassTag[Q]) = new {
    def fillWithJobID(jobID:String, qi:QuestionGroup.Identifier)(implicit claim:Claim): Form[Q] = {
      claim.questionGroup(Jobs).getOrElse(Jobs()).asInstanceOf[Jobs].jobs.find(_.jobID == jobID).getOrElse(Job("",List())).find(_.identifier.id == qi.id)match {
        case Some(q: Q) => form.fill(q)
        case _ => form
      }
    }
  }


  def jobs(implicit claim: Claim) = claim.questionGroup(Jobs) match {
    case Some(js: Jobs) => js
    case _ => Jobs()
  }

  def completedQuestionGroups(questionGroupIdentifier: QuestionGroup.Identifier)(implicit claim: Claim, request: Request[AnyContent]) = {
    claim.questionGroup(Jobs) match {
      case Some(js: Jobs) => js.jobs.find(_.jobID == request.flash("jobID")) match {
        case Some(j: Job) => j.questionGroups.takeWhile(_.identifier.index < questionGroupIdentifier.index)
        case _ => Nil
      }
      case _ => Nil
    }

  }

  def completed = claiming { implicit claim => implicit request =>
    Ok("")
  }

  def submit = claiming { implicit claim => implicit request =>
    Redirect("")
  }
}