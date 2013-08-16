package xml

import models.domain.{Claim}
import app.XMLValues

object PropertyRentedOut {

  def xml(claim: Claim) = {
    <PropertyRentedOut>
      <PayNationalInsuranceContributions/>
      <RentOutProperty>{XMLValues.NotAsked}</RentOutProperty>
      <SubletHome>{XMLValues.NotAsked}</SubletHome>
    </PropertyRentedOut>
  }

}
