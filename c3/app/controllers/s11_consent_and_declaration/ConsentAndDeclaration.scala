package controllers.s11_consent_and_declaration

import play.api.mvc.Controller
import models.view.CachedClaim
import controllers.Routing
import models.domain._

object ConsentAndDeclaration extends Controller with ConsentAndDeclarationRouting with CachedClaim {
  def completed = claiming { implicit claim => implicit request =>
    Ok(views.html.s11_consent_and_declaration.g5_submit(completedQuestionGroups.map(qg => qg -> route(qg))))
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    Redirect(controllers.s1_carers_allowance.routes.G1Benefits.present())
  }

  private def completedQuestionGroups(implicit claim: Claim): List[QuestionGroup] = {
    claim.completedQuestionGroups(models.domain.ConsentAndDeclaration)
  }
}

trait ConsentAndDeclarationRouting extends Routing {
  override def route(qgi: QuestionGroup.Identifier) = qgi match {
    case AdditionalInfo => routes.G1AdditionalInfo.present()
    case Consent => routes.G2Consent.present()
    case Disclaimer => routes.G3Disclaimer.present()
    case Declaration => routes.G4Declaration.present()
    case Submit => routes.G5Submit.present()
    case Error => routes.G6Error.present()
  }
}