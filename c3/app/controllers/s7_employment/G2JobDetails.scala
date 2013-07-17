package controllers.s7_employment

import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{JobDetails, BeenEmployed}
import utils.helpers.CarersForm._
import controllers.Mappings._
import models.DayMonthYear

object G2JobDetails extends Controller with CachedClaim {

   val form = Form(
     mapping(
       "employerName"-> nonEmptyText,
       "jobStartDate" -> optional(dayMonthYear),
       "finishedThisJob" -> nonEmptyText,
       "lastWorkDate" -> optional(dayMonthYear),
       "p45LeavingDate" -> optional(dayMonthYear),
       "hoursPerWeek" -> optional(text),
       "jobTitle" -> optional(text),
       "payrollEmpNumber" -> optional(text)
     )(JobDetails.apply)(JobDetails.unapply)
   )


   def present = claiming {
     implicit claim => implicit request =>


       Ok(views.html.s7_employment.g2_jobDetails(form))
   }

   def submit = claiming { implicit claim => implicit request =>
     form.bindEncrypted.fold(
       formWithErrors =>BadRequest(views.html.s7_employment.g2_jobDetails(formWithErrors)),
       beenEmployed => claim.update(beenEmployed) -> Redirect(routes.G2JobDetails.present()))
   }

 }
