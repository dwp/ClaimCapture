package controllers

import play.api.mvc._
import models.claim._

object YourPartner extends Controller with CachedClaim {
  def yourPartner = claiming {
    implicit claim => implicit request =>
      val outcome =
        <endOfSprint>
          <title>End of Sprint</title>

          <ul>
            {claim.completedFormsForSection(models.claim.AboutYou.id).map(f => <li>{f}</li>)}
          </ul>
        </endOfSprint>

      Ok(outcome)
  }
}