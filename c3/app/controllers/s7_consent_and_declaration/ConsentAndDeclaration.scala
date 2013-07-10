package controllers.s7_consent_and_declaration

import play.api.mvc.{Call, Controller}
import models.view.CachedClaim
import scala.collection.immutable.ListMap


object ConsentAndDeclaration extends Controller with CachedClaim {

  val route: ListMap[String, Call] = ListMap(G1Consent,G2Disclaimer,G3Declaration,G4AdditionalInfo,G5Submit)


  def completed = claiming { implicit claim => implicit request =>
    Ok(views.html.s6_pay_details.g3_completed(claim.completedQuestionGroups(models.domain.PayDetails.id)))
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    Redirect(controllers.s1_carers_allowance.routes.G1Benefits.present())
  }
}
