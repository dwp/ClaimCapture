package controllers

import play.api.mvc.{Action, Controller}
import models.claim.CachedClaim
import play.api.data.Form
import play.api.data.Forms._

case class YesNoForm(answer: String)

object SpikeController extends Controller with CachedClaim {

  val moreAboutYouForm = Form(
    mapping(
      "answer" -> nonEmptyText
    )(YesNoForm.apply)(YesNoForm.unapply)
  )

  def spike = Action {
    Ok(views.html.spike(moreAboutYouForm))
  }

  def spikeSubmit = Action {
    implicit request =>
      moreAboutYouForm.bindFromRequest.fold(
        errors => BadRequest(views.html.spike(errors)),
        answer => Ok(views.html.display(answer))
      )
  }
}
