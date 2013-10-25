package controllers.s4_care_you_provide

import language.reflectiveCalls
import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain.{PersonYouCareFor, Claim, MoreAboutThePerson}
import play.api.data.Form
import play.api.data.Forms._
import utils.helpers.CarersForm._
import controllers.Mappings._

object G3RelationshipAndOtherClaims extends Controller with CareYouProvideRouting with CachedClaim {
  val form = Form(
    mapping(
      "relationship" -> nonEmptyText(maxLength = 20),
      "armedForcesPayment" -> nonEmptyText.verifying(validYesNo)
    )(MoreAboutThePerson.apply)(MoreAboutThePerson.unapply))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s4_care_you_provide.g3_relationshipAndOtherClaims(form.fill(MoreAboutThePerson)(claim), completedQuestionGroups(MoreAboutThePerson)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g3_relationshipAndOtherClaims(formWithErrors, completedQuestionGroups(MoreAboutThePerson))),
      moreAboutThePerson => claim.update(moreAboutThePerson) -> Redirect(routes.G7MoreAboutTheCare.present()))
  }
}