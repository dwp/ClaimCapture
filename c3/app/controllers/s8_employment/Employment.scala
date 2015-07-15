package controllers.s8_employment


import models.yesNo.DeleteId
import play.api.data.Forms._

import scala.language.implicitConversions
import utils.helpers.CarersForm._
import scala.reflect.ClassTag
import play.api.mvc._
import play.api.data.Form
import play.api.i18n.{MMessages => Messages}
import models.view.{Navigable, CachedClaim}
import models.domain._

object Employment extends Controller with CachedClaim  with Navigable{
  implicit def jobFormFiller[Q <: QuestionGroup](form: Form[Q])(implicit classTag: ClassTag[Q]) = new {
    def fillWithJobID(qi: QuestionGroup.Identifier, iterationID: String)(implicit claim: Claim): Form[Q] = {
      claim.questionGroup(Jobs).getOrElse(Jobs()).asInstanceOf[Jobs].jobs.find(_.iterationID == iterationID).getOrElse(Iteration("", List())).find(_.identifier.id == qi.id) match {
        case Some(q: Q) => form.fill(q)
        case _ => form
      }
    }
  }

  def completedQuestionGroups(questionGroupIdentifier: QuestionGroup.Identifier, iterationID: String)(implicit claim: Claim): List[QuestionGroup] = {
    (for { jobs <- claim.questionGroup[Jobs]
           job <- jobs.find(_.iterationID == iterationID) }
      yield job.questionGroups.filter(_.identifier.index < questionGroupIdentifier.index)).getOrElse(Nil)
  }

  def jobs(implicit claim: Claim) = claim.questionGroup(Jobs) match {
    case Some(js: Jobs) => js
    case _ => Jobs()
  }


  def fillForm(implicit request:Request[_],claim:Claim)= {
    claim.questionGroup[BeenEmployed] match {
      case Some(qg) => G2BeenEmployed.form.fill(qg)
      case None => G2BeenEmployed.form
    }
  }

  val deleteForm = Form(mapping(
    "deleteId" -> nonEmptyText
  )(DeleteId.apply)(DeleteId.unapply))

  def deleteRedirect(updatedJobs:Jobs) = {


  }

  def delete = claimingWithCheck {implicit claim =>  implicit request =>  lang =>

    deleteForm.bindEncrypted.fold(
      errors    =>  BadRequest(views.html.s8_employment.g2_beenEmployed(fillForm)(lang)),
      deleteForm=>  {
        val updatedJobs = jobs.delete(deleteForm.id)
        if (updatedJobs.jobs == jobs.jobs) BadRequest(views.html.s8_employment.g2_beenEmployed(fillForm)(lang))
        else claim.update(updatedJobs) -> (if(updatedJobs.jobs.size == 0 ) Redirect(controllers.s7_self_employment.routes.G0Employment.present())
                                          else Redirect(controllers.s8_employment.routes.G2BeenEmployed.present()))
      }
    )
  }
}