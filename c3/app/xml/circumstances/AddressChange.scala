package xml.circumstances

import models.domain.CircumstancesAddressChange
import scala.xml.NodeSeq
import xml.XMLHelper._
import scala.Some
import models.domain.Claim

/**
 * Created by neddakaltcheva on 3/13/14.
 */
object AddressChange {
  def xml(circs :Claim): NodeSeq = {
    val circsAddressChangeOption: Option[CircumstancesAddressChange] = circs.questionGroup[CircumstancesAddressChange]

    circsAddressChangeOption match {
      case Some(circsAddressChange) => {
        <AddressChange>
          <PreviousAddress>
            {postalAddressStructure("previousAddress", circsAddressChange.previousAddress, circsAddressChange.previousPostcode)}
          </PreviousAddress>
          {question(<Caring35Hours/>,"stillCaring.answer", circsAddressChange.stillCaring.answer)}
          <NewAddress>
            {postalAddressStructure("newAddress", circsAddressChange.newAddress, circsAddressChange.newPostcode)}
          </NewAddress>
          {circsAddressChange.stillCaring.answer match {
            case "no" => {question(<DateStoppedCaring35Hours/>,"stillCaring.date", circsAddressChange.stillCaring.date)}
            case "yes" => {
              {question(<CareeChangedAddress/>,"caredForChangedAddress.answer", circsAddressChange.caredForChangedAddress.answer)}
              {question(<CareeChangedAddress/>,"sameAddress.answer", circsAddressChange.sameAddress.answer)}

              {circsAddressChange.sameAddress.answer match {
                case Some("yes") => {
                  <CareeAddress>
                    {postalAddressStructure("sameAddress.theirNewAddress", circsAddressChange.sameAddress.address, circsAddressChange.sameAddress.postCode)}
                  </CareeAddress>
                }
                case Some("no") => NodeSeq.Empty
                case _ => throw new RuntimeException("circsAddressChange.sameAddress is either Yes Or No")
                }
              }
            }
          }}
          {question(<OtherChanges/>, "moreAboutChanges", circsAddressChange.moreAboutChanges)}
        </AddressChange>
      }
      case _ => NodeSeq.Empty
    }
  }
}
