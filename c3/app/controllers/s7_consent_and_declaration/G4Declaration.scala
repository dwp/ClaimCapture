package controllers.s7_consent_and_declaration

import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import utils.helpers.CarersForm._
import models.domain.Claim
import scala.Some

object G4Declaration extends Controller with CachedClaim{
  val form = Form(
    mapping(
      "call" -> ignored(routes.G4Declaration.present()),
      "confirm" -> nonEmptyText,
      "someoneElse" -> nonEmptyText
    )(Declaration.apply)(Declaration.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(Declaration)

  def present = claiming { implicit claim => implicit request =>
    val currentForm: Form[Declaration] = claim.questionGroup(Declaration) match {
      case Some(t: Declaration) => form.fill(t)
      case _ => form
    }

    Ok(views.html.s7_consent_and_declaration.g4_declaration(currentForm,completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_consent_and_declaration.g4_declaration(formWithErrors,completedQuestionGroups)),
      declaration => claim.update(declaration) -> Redirect(routes.G5Submit.present()))
  }
}