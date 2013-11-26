package controllers.s4_care_you_provide

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import models.domain.MoreAboutThePerson
import utils.helpers.CarersForm._
import controllers.Mappings._

object G3RelationshipAndOtherClaims extends Controller with CachedClaim with Navigable {
  val form = Form(
    mapping(
      "relationship" -> nonEmptyText(maxLength = 30),
      "armedForcesPayment" -> nonEmptyText.verifying(validYesNo)
    )(MoreAboutThePerson.apply)(MoreAboutThePerson.unapply))

  def present = claiming { implicit claim => implicit request =>
    track(MoreAboutThePerson) { implicit claim =>
      Ok(views.html.s4_care_you_provide.g3_relationshipAndOtherClaims(form.fill(MoreAboutThePerson)(claim)))
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g3_relationshipAndOtherClaims(formWithErrors)),
      moreAboutThePerson => claim.update(moreAboutThePerson) -> Redirect(routes.G7MoreAboutTheCare.present()))
  }
}