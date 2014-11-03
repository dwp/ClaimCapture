package controllers.s2_about_you

import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.AbroadForMoreThan52Weeks
import AboutYou.trips
import models.view.Navigable
import utils.helpers.CarersForm._
import models.view.CachedClaim
import controllers.CarersForms._
import scala.Some

object G5AbroadForMoreThan52Weeks extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "anyTrips" -> nonEmptyText.verifying(validYesNo),
    "tripDetails" -> optional(carersNonEmptyText(maxLength = 3000))
  )(AbroadForMoreThan52Weeks.apply)(AbroadForMoreThan52Weeks.unapply))

  def present = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    /*
    val filledForm = request.headers.get("referer") match {
      case Some(referer) if referer endsWith routes.G6Trip.present().url => form
      case _ if claim.questionGroup[AbroadForMoreThan52Weeks].isDefined => form.fill(AbroadForMoreThan52Weeks("no"))
      case _ => form
    }*/

    //track(AbroadForMoreThan52Weeks) { implicit claim => Ok(views.html.s2_about_you.g5_abroad_for_more_than_52_weeks(filledForm, trips)(lang)) }
    track(AbroadForMoreThan52Weeks) { implicit claim => Ok(views.html.s2_about_you.g5_abroad_for_more_than_52_weeks(form, trips)(lang)) }
  }

  def submit = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    def next(abroadForMoreThan52Weeks: AbroadForMoreThan52Weeks) = abroadForMoreThan52Weeks.anyTrips match {
      case `yes` if trips.fiftyTwoWeeksTrips.size < 5 => Redirect(routes.G6Trip.present())
      case `yes` => {
        Redirect(routes.G5AbroadForMoreThan52Weeks.present())
      }
      case _ => Redirect(routes.G7OtherEEAStateOrSwitzerland.present())
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g5_abroad_for_more_than_52_weeks(formWithErrors, trips)(lang)),
      //abroadForMoreThan52Weeks => claim.update(abroadForMoreThan52Weeks).update(trips) -> next(abroadForMoreThan52Weeks))
      abroadForMoreThan52Weeks => claim.update(abroadForMoreThan52Weeks) -> Redirect(routes.G7OtherEEAStateOrSwitzerland.present())
    )
  }

  /*
  implicit def actionWrapper(action:Action[AnyContent]):Action = new {

    def submitDecorator():Action[AnyContent] = Action.async(action.parser){ request =>
      import ExecutionContext.Implicits.global

      action(request).map{ result =>
        result.header.status match {
          case play.api.http.Status.SEE_OTHER => Redirect(routes.G7OtherEEAStateOrSwitzerland.present())
          case _ => result
        }
      }
    }
  }*/
}