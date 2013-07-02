package controllers.s4_care_you_provide

import play.api.mvc.Controller
import models.view.CachedClaim
import scala.collection.immutable.ListMap
import play.api.mvc.Call
import controllers.Routing._
import play.api.templates.Html

object CareYouProvide extends Controller with CachedClaim {

  val route: ListMap[String, Call] = ListMap(
    G1TheirPersonalDetails,
    G2TheirContactDetails,
    G3MoreAboutThePerson,
    G4PreviousCarerPersonalDetails,
    G5PreviousCarerContactDetails,
    G6RepresentativesForThePerson,
    G7MoreAboutTheCare,
    G8OneWhoPaysPersonalDetails,
    G9ContactDetailsOfPayingPerson,
    G10BreaksInCare)

  def completed = claiming {
    implicit claim => implicit request =>
      val outcome =
        <html>
          <body>
            <h1>End of Sprint 3</h1>
            <h2>Completed - Care You Provide</h2>

            <ul>
              {claim.completedQuestionGroups(models.domain.CareYouProvide.id).map(f => <li>{f}</li>)}
            </ul>
          </body>
        </html>

      Ok(Html(outcome.toString))
  }
}