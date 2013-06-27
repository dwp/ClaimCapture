package controllers

import play.api.mvc.Controller
import models.view.CachedClaim
import scala.collection.immutable.ListMap
import s4_care_you_provide._
import play.api.mvc.Call

object CareYouProvide extends Controller with CachedClaim {

  import Routing._

  val route: ListMap[String, Call] = ListMap(G1TheirPersonalDetails,
    G2TheirContactDetails,
    G3MoreAboutThePerson,
    G4PreviousCarerPersonalDetails,
    G5PreviousCarerContactDetails,
    G6RepresentativesForThePerson,
    G7MoreAboutTheCare,
    G8OneWhoPaysPersonalDetails,
    G9ContactDetailsOfPayingPerson,
    G10BreaksInCare).asInstanceOf[ListMap[String, Call]]


  def completed = claiming {
    implicit claim => implicit request =>
      val outcome =
        <html>
          <head>
            <title>Completed - Care You Provide</title>
          </head>
          <body>
            <h1>End of Sprint 2</h1>
            <ul>
              {claim.completedQuestionGroups(models.domain.CareYouProvide.id).map(f => <li>
              {f}
            </li>)}
            </ul>
          </body>
        </html>

      Ok(outcome)
  }
}