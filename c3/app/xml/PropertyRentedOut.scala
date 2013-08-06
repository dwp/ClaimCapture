package xml

import models.domain.{PropertyAndRent, Claim}

object PropertyRentedOut {

  def xml(claim: Claim) = {
    val propertyAndRentOption = claim.questionGroup[PropertyAndRent]
    val propertyAndRent = propertyAndRentOption.getOrElse(PropertyAndRent())

    <PropertyRentedOut>
      <PayNationalInsuranceContributions/>
      <RentOutProperty>{propertyAndRent.ownProperty}</RentOutProperty>
      <SubletHome>{propertyAndRent.hasSublet}</SubletHome>
    </PropertyRentedOut>
  }

}
