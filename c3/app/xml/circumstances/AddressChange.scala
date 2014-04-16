package xml.circumstances

import models.domain.CircumstancesAddressChange
import scala.xml.NodeSeq
import xml.XMLHelper._
import scala.Some
import models.domain.Claim
import models.MultiLineAddress

/**
 * Created by neddakaltcheva on 3/13/14.
 */
object AddressChange {
  def xml(circs :Claim): NodeSeq = {
    val circsAddressChangeOption: Option[CircumstancesAddressChange] = circs.questionGroup[CircumstancesAddressChange]

    circsAddressChangeOption match {
      case Some(circsAddressChange) => {
        <AddressChange>
          {postalAddressStructurePreviousAddress("previousAddress", circsAddressChange.previousAddress, circsAddressChange.previousPostcode)}

          {question(<Caring35Hours/>,"stillCaring.answer", circsAddressChange.stillCaring.answer)}

          {question(<DateStoppedCaring35Hours/>,"stillCaring.date", circsAddressChange.stillCaring.date)}

          {postalAddressStructureNewAddress("newAddress", circsAddressChange.newAddress, circsAddressChange.newPostcode)}

          {question(<CareeChangedAddress/>,"caredForChangedAddress.answer", circsAddressChange.caredForChangedAddress.answer)}

          {question(<CareeSameAddress/>,"sameAddress.answer", circsAddressChange.sameAddress.answer)}

          {postalAddressStructureCareeAddress("sameAddress.theirNewAddress", circsAddressChange.sameAddress.address, circsAddressChange.sameAddress.postCode)}

          {question(<OtherChanges/>, "moreAboutChanges", circsAddressChange.moreAboutChanges)}
        </AddressChange>
      }
      case _ => NodeSeq.Empty
    }
  }
}
