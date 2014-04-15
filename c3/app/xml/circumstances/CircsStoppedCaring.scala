package xml.circumstances

import models.domain.{CircumstancesStoppedCaring, Claim}
import play.api.i18n.{MMessages => Messages}

object CircsStoppedCaring {
  def xml(circs: Claim) = {
    val circumstancesStoppedCaringOption = circs.questionGroup[CircumstancesStoppedCaring]

    if (circumstancesStoppedCaringOption.isDefined) {
      <StoppedCaring>
        <DateStoppedCaring>{circumstancesStoppedCaringOption.get.stoppedCaringDate.`yyyy-MM-dd`}</DateStoppedCaring>
      </StoppedCaring>
    }
  }
}
