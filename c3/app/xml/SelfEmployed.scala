package xml

import models.domain.{Claim, Employment}
import controllers.Mappings.no

object SelfEmployed {

  def xml(claim: Claim) = {
    val aboutYouEmploymentOption = claim.questionGroup[Employment]
    val employment = aboutYouEmploymentOption.getOrElse(Employment(beenSelfEmployedSince1WeekBeforeClaim = no))

    <SelfEmployed>{employment.beenSelfEmployedSince1WeekBeforeClaim}</SelfEmployed>
  }
}