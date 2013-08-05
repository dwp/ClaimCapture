package controllers.s7_employment

import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{Employment => employment, BeenEmployed}
import utils.helpers.CarersForm._
import controllers.Mappings._
import controllers.s7_employment.Employment.jobs
import controllers.s7_employment.Employment.dispatch

object G1BeenEmployed extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "beenEmployed" -> (nonEmptyText verifying validYesNo)
    )(BeenEmployed.apply)(BeenEmployed.unapply))

  def present = claiming { implicit claim => implicit request =>
    dispatch(Ok(views.html.s7_employment.g1_beenEmployed(form)))
  }

  def submit = claiming { implicit claim => implicit request =>
    import controllers.Mappings.yes

    def next(beenEmployed: BeenEmployed) = beenEmployed.beenEmployed match {
      case `yes` if jobs.size < 5 => Redirect(routes.G2JobDetails.present())
      case `yes` => Redirect(routes.G1BeenEmployed.present())
      case _ => Redirect(routes.Employment.completed())
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g1_beenEmployed(formWithErrors)),
      beenEmployed => claim.update(beenEmployed) -> next(beenEmployed))
  }
}