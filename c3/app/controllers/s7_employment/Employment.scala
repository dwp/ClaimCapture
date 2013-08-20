package controllers.s7_employment

import scala.language.implicitConversions
import play.api.mvc._
import play.api.data.Form
import scala.reflect.ClassTag
import play.api.i18n.Messages
import models.view.CachedClaim
import controllers.Mappings._
import models.domain._
import play.api.mvc.Call

object Employment extends Controller with CachedClaim {
  implicit def jobFormFiller[Q <: QuestionGroup](form: Form[Q])(implicit classTag: ClassTag[Q]) = new {
    def fillWithJobID(qi: QuestionGroup.Identifier, jobID: String)(implicit claim: Claim): Form[Q] = {
      claim.questionGroup(Jobs).getOrElse(Jobs()).asInstanceOf[Jobs].jobs.find(_.jobID == jobID).getOrElse(Job("", List())).find(_.identifier.id == qi.id) match {
        case Some(q: Q) => form.fill(q)
        case _ => form
      }
    }
  }

  /* TODO The following will go */
  def dispatch(f: => Result)(implicit claim: Claim) = {
    if (claim.isSectionVisible(models.domain.Employed)) f
    else Redirect(controllers.s8_self_employment.routes.G1AboutSelfEmployment.present())
  }

  def jobs(implicit claim: Claim) = claim.questionGroup(Jobs) match {
    case Some(js: Jobs) => js
    case _ => Jobs()
  }

  /* TODO The following will go */
  def completedQuestionGroups(questionGroupIdentifier: QuestionGroup.Identifier, jobID: String)(implicit claim: Claim): List[(QuestionGroup, Call)] = {
    val result = for { jobs <- claim.questionGroup[Jobs]
                       job <- jobs.find(_.jobID == jobID) }
    yield job.questionGroups.filter(_.identifier.index < questionGroupIdentifier.index).map(qg => qg -> route(qg, jobID))

    result.getOrElse(Nil)
  }

  def completed = claiming { implicit claim => implicit request =>
    Ok(views.html.s7_employment.g15_completed(claim.questionGroup(BeenEmployed).map(qg => qg -> routes.G1BeenEmployed.present()).toList))
  }

  def submit = claiming { implicit claim => implicit request =>
    Redirect(claim.nextSection(models.domain.Employed).firstPage)
  }

  def delete(jobID: String) = claiming { implicit claim => implicit request =>
    import play.api.libs.json.Json

    val updatedJobs = jobs.delete(jobID)

    if (updatedJobs.isEmpty) claim.update(updatedJobs) -> Ok(Json.obj("answer" -> Messages("answer.label")))
    else claim.update(updatedJobs) -> Ok(Json.obj("answer" -> Messages("answer.more.label")))
  }

  /* TODO The following will go */
  private def route(qg: QuestionGroup, jobID: String) = qg.identifier match {
    case JobDetails => routes.G2JobDetails.presentInJob(jobID)
    case EmployerContactDetails => routes.G3EmployerContactDetails.present(jobID)
    case LastWage => routes.G4LastWage.present(jobID)
    case AdditionalWageDetails => routes.G5AdditionalWageDetails.present(jobID)
    case MoneyOwedbyEmployer => routes.G6MoneyOwedbyEmployer.present(jobID)
    case PensionSchemes => routes.G7PensionSchemes.present(jobID)
    case AboutExpenses => routes.G8AboutExpenses.present(jobID)
    case NecessaryExpenses => routes.G9NecessaryExpenses.present(jobID)
    case ChildcareExpenses => routes.G10ChildcareExpenses.present(jobID)
    case ChildcareProvider => routes.G11ChildcareProvider.present(jobID)
    case PersonYouCareForExpenses => routes.G12PersonYouCareForExpenses.present(jobID)
    case CareProvider => routes.G13CareProvider.present(jobID)
    case _ => routes.G1BeenEmployed.present()
  }
}

trait EmploymentPresentation extends CachedClaim with Results {
  def presentConditionally(c: => ClaimResult)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    claim.questionGroup[models.domain.Employment].collect {
      case e: models.domain.Employment if e.beenEmployedSince6MonthsBeforeClaim == yes => c
    }.getOrElse(redirect)
  }

  def redirect(implicit claim: Claim, request: Request[AnyContent]): ClaimResult =
    claim -> Redirect(controllers.s8_self_employment.routes.G1AboutSelfEmployment.present())
}