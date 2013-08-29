package controllers.s7_employment

import models.view.{Navigable, CachedClaim}
import play.api.mvc.{AnyContent, Request, Controller}
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{Employment => Emp, Claim, BeenEmployed}
import utils.helpers.CarersForm._
import controllers.Mappings._
import controllers.s7_employment.Employment.jobs

object G1BeenEmployed extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "beenEmployed" -> (nonEmptyText verifying validYesNo)
  )(BeenEmployed.apply)(BeenEmployed.unapply))

  def present = claiming { implicit claim => implicit request =>
    presentConditionally(beenEmployed)
  }

  def beenEmployed(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    request.headers.get("referer") match {
      case Some(referer) if referer.contains(routes.G2JobDetails.present("").url) ||
                            referer.contains(routes.G2JobDetails.job("").url) ||
                            referer.contains(routes.G14JobCompletion.submit().url) => {
        track(BeenEmployed) { implicit claim => Ok(views.html.s7_employment.g1_beenEmployed(form)) }
      }

      case Some(referer) if referer.contains(routes.Employment.completed().url) =>
        track(BeenEmployed) { implicit claim => Ok(views.html.s7_employment.g1_beenEmployed(form.fill(BeenEmployed("no")))) }

      case _ => claim.questionGroup[BeenEmployed] match {
        case Some(b: BeenEmployed) => track(BeenEmployed) { implicit claim => Ok(views.html.s7_employment.g1_beenEmployed(form.fill(b))) }
        case _ =>
          val (updatedClaim, _) = track(BeenEmployed) { implicit claim => Ok(views.html.s7_employment.g1_beenEmployed(form)) }
          updatedClaim -> Redirect(routes.G2JobDetails.present(JobID(form)))
      }
    }
  }

  def presentConditionally(c: => ClaimResult)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    claim.questionGroup[Emp].collect {
      case e: Emp if e.beenEmployedSince6MonthsBeforeClaim == yes => c
    }.getOrElse(redirect)
  }

  def redirect(implicit claim: Claim, request: Request[AnyContent]): ClaimResult =
    claim -> Redirect(controllers.s8_self_employment.routes.G1AboutSelfEmployment.present())

  def submit = claiming { implicit claim => implicit request =>
    import controllers.Mappings.yes

    def next(beenEmployed: BeenEmployed) = beenEmployed.beenEmployed match {
      case `yes` if jobs.size < 5 => Redirect(routes.G2JobDetails.present(JobID(form)))
      case `yes` => Redirect(routes.G1BeenEmployed.present())
      case _ => Redirect(routes.Employment.completed())
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g1_beenEmployed(formWithErrors)),
      beenEmployed => claim.update(beenEmployed) -> next(beenEmployed))
  }
}