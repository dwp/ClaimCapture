package controllers.s_employment

import models.yesNo.DeleteId
import play.api.Play._
import play.api.data.Forms._

import scala.language.implicitConversions
import utils.helpers.CarersForm._
import scala.reflect.ClassTag
import play.api.mvc._
import play.api.data.Form
import models.view.{Navigable, CachedClaim}
import models.domain._
import play.api.i18n._

object Employment extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  implicit def jobFormFiller[Q <: QuestionGroup](form: Form[Q])(implicit classTag: ClassTag[Q]) = new {
    def fillWithJobID(qi: QGIdentifier, iterationID: String)(implicit claim: Claim): Form[Q] = {
      claim.saveForLaterCurrentPageData.isEmpty match {
        case true => {
          claim.questionGroup(Jobs).getOrElse(Jobs()).asInstanceOf[Jobs].jobs.find(_.iterationID == iterationID).getOrElse(Iteration("", List())).find(_.identifier.id == qi.id) match {
            case Some(q: Q) => form.fill(q)
            case _ => form
          }
        }
        case false => form.copy[Q](data=claim.saveForLaterCurrentPageData)
      }
    }
  }

  def completedQuestionGroups(questionGroupIdentifier: QGIdentifier, iterationID: String)(implicit claim: Claim): List[QuestionGroup] = {
    (for {jobs <- claim.questionGroup[Jobs]
          job <- jobs.find(_.iterationID == iterationID)}
      yield job.questionGroups.filter(_.identifier.index < questionGroupIdentifier.index)).getOrElse(Nil)
  }

  def jobs(implicit claim: Claim) = claim.questionGroup(Jobs) match {
    case Some(js: Jobs) => js
    case _ => Jobs()
  }


  def fillForm(implicit request: Request[_], claim: Claim) = {
    claim.questionGroup[BeenEmployed] match {
      case Some(qg) => GBeenEmployed.form.fill(qg)
      case None => GBeenEmployed.form
    }
  }

  val deleteForm = Form(mapping(
    "deleteId" -> nonEmptyText
  )(DeleteId.apply)(DeleteId.unapply))

  def deleteRedirect(updatedJobs: Jobs) = {


  }

  def delete = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    deleteForm.bindEncrypted.fold(
      errors => BadRequest(views.html.s_employment.g_beenEmployed(fillForm)),
      deleteForm => {
        val updatedJobs = jobs.delete(deleteForm.id)
        if (updatedJobs.jobs == jobs.jobs) BadRequest(views.html.s_employment.g_beenEmployed(fillForm))
        else {
          val updatedClaim = claim.update(updatedJobs)
          (updatedJobs.jobs.size, claim.navigation.beenInPreview) match {
            case (0, true) => {
              // Reset the hasEmployment flag if we delete the last employment and going back to preview
              val claimNoEmp=updatedClaim.update(updatedClaim.questionGroup[YourIncomes].get.copy(beenEmployedSince6MonthsBeforeClaim = "no"))
              claimNoEmp->Redirect(controllers.preview.routes.Preview.present().url + "#" + "employment_been_employed_since")
            }
            case (0, false) => updatedClaim -> Redirect(controllers.your_income.routes.GYourIncomes.present())
            case (_, _) => updatedClaim -> Redirect(controllers.s_employment.routes.GBeenEmployed.present())
          }
        }
      }
    )
  }
}
