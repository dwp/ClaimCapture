package xml

import models.domain.{PropertyAndRent, Claim}

object PropertyRentedOut {

  def xml(claim: Claim) = {
    val propertyAndRent = claim.questionGroup[PropertyAndRent].getOrElse(PropertyAndRent())

    <PropertyRentedOut>
      <PayNationalInsuranceContributions/>
      <RentOutProperty>{propertyAndRent.ownProperty}</RentOutProperty>
      <SubletHome>{propertyAndRent.hasSublet}</SubletHome>
    </PropertyRentedOut>
  }

}
