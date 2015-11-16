package xml.claim

import models.domain.Claim
import app.XMLValues._
import play.api.i18n.Messages
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

object PropertyRentedOut {

  def xml(claim: Claim) = {
    <PropertyRentedOut>
      <PayNationalInsuranceContributions/>
      <RentOutProperty>{NotAsked}</RentOutProperty>
      <SubletHome>{NotAsked}</SubletHome>
    </PropertyRentedOut>
  }
}
