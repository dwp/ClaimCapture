package controllers.s7_employment

import models.view.{Navigable, CachedClaim}
import play.api.mvc.{AnyContent, Request, Controller}
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{Employment => Emp, DigitalForm, Jobs, BeenEmployed}
import utils.helpers.CarersForm._
import controllers.Mappings._
import controllers.s7_employment.Employment.jobs

object G1BeenEmployed extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "beenEmployed" -> (nonEmptyText verifying validYesNo)
  )(BeenEmployed.apply)(BeenEmployed.unapply))

  def presentConditionally(c: => FormResult)(implicit claim: DigitalForm, request: Request[AnyContent]): FormResult = {
    claim.questionGroup[Emp].collect {
      case e: Emp if e.beenEmployedSince6MonthsBeforeClaim == yes => c
    }.getOrElse(redirect)
  }

  def redirect(implicit claim: DigitalForm, request: Request[AnyContent]): FormResult =
    claim -> Redirect("/self-employment/about-self-employment")

  def present = executeOnForm {implicit claim => implicit request =>
      presentConditionally(beenEmployed)
  }

  def beenEmployed(implicit claim: DigitalForm, request: Request[AnyContent]): FormResult = {
    claim.questionGroup[BeenEmployed] match {
      case Some(b: BeenEmployed) => track(BeenEmployed) { implicit claim => Ok(views.html.s7_employment.g1_beenEmployed(form.fill(b))) }
      case _ =>
        val (updatedClaim, _) = track(BeenEmployed) { implicit claim => Ok(views.html.s7_employment.g1_beenEmployed(form)) }
        updatedClaim.update(BeenEmployed("")) -> Redirect(routes.G2JobDetails.present(JobID(form)))
    }
  }

  def submit = executeOnForm {implicit claim => implicit request =>
    import controllers.Mappings.yes

    def next(beenEmployed: BeenEmployed) = beenEmployed.beenEmployed match {
      case `yes` if jobs.size < 5 => Redirect(routes.G2JobDetails.present(JobID(form)))
      case `yes` => Redirect(routes.G1BeenEmployed.present())
      case _ => Redirect(routes.Employment.completed())
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g1_beenEmployed(formWithErrors)),
      beenEmployed => clearUnfinishedJobs.update(beenEmployed) -> next(beenEmployed))
  }

  def clearUnfinishedJobs(implicit claim:DigitalForm) = {
    val jobs = claim.questionGroup[Jobs].getOrElse(Jobs())
    claim.update(Jobs(jobs.jobs.filter(_.completed == true)))
  }

}