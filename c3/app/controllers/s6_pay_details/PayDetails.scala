package controllers.s6_pay_details

import play.api.mvc.{Call, Controller}
import models.view.CachedClaim
import scala.collection.immutable.ListMap



object PayDetails extends Controller with CachedClaim {

  val route: ListMap[String, Call] = ListMap(G1HowWePayYou,G2BankBuildingSocietyDetails)


  def completed = claiming { implicit claim => implicit request =>
    Ok(views.html.s6_pay_details.g3_completed(claim.completedQuestionGroups(models.domain.PayDetails.id)))
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    Redirect(controllers.s7_consent_and_declaration.routes.G1Consent.present())
  }
}
