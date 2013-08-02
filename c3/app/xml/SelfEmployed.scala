package xml

import models.domain.{Claim, Employment}
import XMLHelper.questionGroup
import controllers.Mappings.{no}

object SelfEmployed {

  def xml(claim:Claim) = {
    val aboutYouEmploymentOption = questionGroup[Employment](claim, Employment)
    val employment = aboutYouEmploymentOption.getOrElse(Employment(beenSelfEmployedSince1WeekBeforeClaim = no))
    <SelfEmployed>{employment.beenSelfEmployedSince1WeekBeforeClaim}</SelfEmployed>
  }
}
