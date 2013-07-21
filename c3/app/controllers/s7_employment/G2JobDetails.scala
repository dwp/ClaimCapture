package controllers.s7_employment

import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{Employed, JobDetails}
import utils.helpers.CarersForm._
import controllers.Mappings._

object G2JobDetails extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "employerName"-> nonEmptyText,
      "jobStartDate" -> optional(dayMonthYear.verifying(validDate)),
      "finishedThisJob" -> nonEmptyText,
      "lastWorkDate" -> optional(dayMonthYear.verifying(validDate)),
      "p45LeavingDate" -> optional(dayMonthYear.verifying(validDate)),
      "hoursPerWeek" -> optional(text),
      "jobTitle" -> optional(text),
      "payrollEmployeeNumber" -> optional(text),
      call(routes.G2JobDetails.present())
    )(JobDetails.apply)(JobDetails.unapply))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s7_employment.g2_jobDetails(form))
  }

  /*def break(id: String) = claiming { implicit claim => implicit request =>
    claim.questionGroup(BreaksInCare) match {
      case Some(b: BreaksInCare) => b.breaks.find(_.id == id) match {
        case Some(b: Break) => Ok(views.html.s4_care_you_provide.g11_break(form.fill(b)))
        case _ => Redirect(routes.G10BreaksInCare.present())
      }

      case _ => Redirect(routes.G10BreaksInCare.present())
    }
  }*/

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g2_jobDetails(formWithErrors)),
      jobDetails => {
        /*val employedSection = claim.section(Employed)
        employedSection.questionGroups.find()*/

        claim.update(jobDetails) -> Redirect(routes.G3EmployerContactDetails.present())
      })
  }
}

/*
val updatedTrips = if (trips.fourWeeksTrips.size >= 5) trips else trips.update(trip.as[FourWeeksTrip])
        claim.update(updatedTrips) -> Redirect(routes.G2AbroadForMoreThan4Weeks.present())



        break => {
        val updatedBreaksInCare = if (breaksInCare.breaks.size >= 10) breaksInCare else breaksInCare.update(break)
        claim.update(updatedBreaksInCare) -> Redirect(routes.G10BreaksInCare.present())
      })



      def breaksInCare(implicit claim: Claim) = claim.questionGroup(BreaksInCare) match {
    case Some(bs: BreaksInCare) => bs
    case _ => BreaksInCare(routes.G10BreaksInCare.present())
  }



  def submit = claiming { implicit claim => implicit request =>
    import controllers.Mappings.yes

    def next(hasBreaks: YesNo) = hasBreaks.answer match {
      case `yes` if breaksInCare.breaks.size < 10 => Redirect(routes.G11Break.present())
      case `yes` => Redirect(routes.G10BreaksInCare.present())
      case _ => Redirect(routes.CareYouProvide.completed())
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g10_breaksInCare(formWithErrors, breaksInCare, completedQuestionGroups, dateOfClaimCheckIfSpent35HoursCaringBeforeClaim(claim))),
      hasBreaks => claim.update(breaksInCare) -> next(hasBreaks))
  }
*/