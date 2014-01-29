package controllers.s7_employment

import models.view.{Navigable, CachedClaim}
import play.api.mvc.{AnyContent, Request, Controller}
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{Employment => Emp, Claim, Jobs, BeenEmployed}
import utils.helpers.CarersForm._
import controllers.Mappings._
import controllers.s7_employment.Employment.jobs

object G2BeenEmployed extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "beenEmployed" -> (nonEmptyText verifying validYesNo)
  )(BeenEmployed.apply)(BeenEmployed.unapply))

  def presentConditionally(c: => ClaimResult)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    claim.questionGroup[Emp].collect {
      case e: Emp if e.beenEmployedSince6MonthsBeforeClaim == yes => c
    }.getOrElse(redirect)
  }

  def redirect(implicit claim: Claim, request: Request[AnyContent]): ClaimResult =
    claim -> Redirect("/self-employment/about-self-employment")

  def present = claiming { implicit claim => implicit request =>
      presentConditionally(beenEmployed)
  }

  def beenEmployed(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    claim.questionGroup[BeenEmployed] match {
      case Some(b: BeenEmployed) => track(BeenEmployed) { implicit claim => Ok(views.html.s7_employment.g2_beenEmployed(form.fill(b))) }
      case _ =>
        val (updatedClaim, _) = track(BeenEmployed) { implicit claim => Ok(views.html.s7_employment.g2_beenEmployed(form)) }
        updatedClaim.update(BeenEmployed("")) -> Redirect(routes.G3JobDetails.present(JobID(form)))
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    import controllers.Mappings.yes

    def next(beenEmployed: BeenEmployed) = beenEmployed.beenEmployed match {
      case `yes` if jobs.size < 5 => Redirect(routes.G3JobDetails.present(JobID(form)))
      case `yes` => Redirect(routes.G2BeenEmployed.present())
      case _ => Redirect(routes.Employment.completed())
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g2_beenEmployed(formWithErrors)),
      beenEmployed => clearUnfinishedJobs.update(beenEmployed) -> next(beenEmployed))
  }

  def clearUnfinishedJobs(implicit claim: Claim) = {
    val jobs = claim.questionGroup[Jobs].getOrElse(Jobs())
    claim.update(Jobs(jobs.jobs.filter(_.completed == true)))
  }
}