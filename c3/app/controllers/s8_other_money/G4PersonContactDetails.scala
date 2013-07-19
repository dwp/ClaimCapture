package controllers.s8_other_money

import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain.{ Claim, PersonContactDetails }
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import utils.helpers.CarersForm._

object G4PersonContactDetails extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(PersonContactDetails)

  val form = Form(
    mapping(
      "address" -> optional(address),
      "postcode" -> optional(text verifying validPostcode),
      "call" -> ignored(routes.G4PersonContactDetails.present()))(PersonContactDetails.apply)(PersonContactDetails.unapply))

  def present = claiming { implicit claim =>
    implicit request =>
      OtherMoney.whenVisible(claim)(() => {
        val currentForm = claim.questionGroup(PersonContactDetails) match {
          case Some(t: PersonContactDetails) => form.fill(t)
          case _ => form
        }
        Ok(views.html.s8_other_money.g4_personContactDetails(currentForm, completedQuestionGroups))
      })
  }

  def submit = claiming { implicit claim =>
    implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s8_other_money.g4_personContactDetails(formWithErrors, completedQuestionGroups)),
        f => claim.update(f) -> Redirect(routes.G4PersonContactDetails.present()) // TODO replace with next page to go to
        )
  }
}