package controllers.s3_your_partner

import play.api.mvc._
import models.view._
import play.api.templates.Html
import scala.collection.immutable.ListMap

object YourPartner extends Controller with CachedClaim {
  val route: ListMap[String, Call] = ListMap(
    G1YourPartnerPersonalDetails,
    G2YourPartnerContactDetails,
    G3MoreAboutYourPartner,
    G4DateOfSeparation)


  def yourPartner = claiming {
    implicit claim => implicit request =>
      val outcome =
        <html>
          <body>
            <h1>End of Sprint 3</h1>
            <h2>Completed - Your Partner</h2>

            <ul>
              {claim.completedQuestionGroups(models.domain.YourPartner.id).map(f => <li>
              {f}
            </li>)}
            </ul>
          </body>
        </html>

      Ok(Html(outcome.toString))
  }
}