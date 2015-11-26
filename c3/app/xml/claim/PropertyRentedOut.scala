package xml.claim

import models.domain.Claim
import app.XMLValues._

object PropertyRentedOut {

  def xml(claim: Claim) = {
    <PropertyRentedOut>
      <PayNationalInsuranceContributions/>
      <RentOutProperty>{NotAsked}</RentOutProperty>
      <SubletHome>{NotAsked}</SubletHome>
    </PropertyRentedOut>
  }
}
