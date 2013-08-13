package controllers.s1_carers_allowance

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.domain.Over16
import language.reflectiveCalls
import controllers.Mappings._
import play.api.data.FormError

object G3Over16 extends Controller with CarersAllowanceRouting with CachedClaim {
  val form = Form(
    mapping(
      "answer" -> nonEmptyText.verifying(validYesNo))(Over16.apply)(Over16.unapply))

  def present = claiming { implicit claim =>
    implicit request =>
      Ok(views.html.s1_carers_allowance.g3_over16(form.fill(Over16), completedQuestionGroups(Over16)))
  }

  def submit = claiming { implicit claim =>
    implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => {
          val formWithErrorsUpdate = formWithErrors
            .replaceError("answer", FormError("over16.answer", "error.required"))
          BadRequest(views.html.s1_carers_allowance.g3_over16(formWithErrorsUpdate, completedQuestionGroups(Over16)))
        },
        f => claim.update(f) -> Redirect(routes.G4LivesInGB.present()))
  }
}