package xml.circumstances

import xml.XMLComponent
import models.domain.{CircumstancesReportChange, Claim}
import scala.xml.NodeSeq
import xml.XMLHelper._

/**
 * @author Jorge Migueis
 */
object CareeDetails extends XMLComponent {
  def xml(circs: Claim): NodeSeq = {
      val detailsOfThePerson = circs.questionGroup[CircumstancesReportChange].getOrElse(CircumstancesReportChange())

    // TODO : Schema and in turn the followwing xml needs to be changed to reflect the new changes : Prafulla Chandra
    <CareeDetails>
      <Surname>{detailsOfThePerson.fullName}</Surname>
      <OtherNames>{detailsOfThePerson.fullName}</OtherNames>
      {statement(<DateOfBirth/>,detailsOfThePerson.dateOfBirth)}
      <NationalInsuranceNumber>{detailsOfThePerson.nationalInsuranceNumber.stringify}</NationalInsuranceNumber>
    </CareeDetails>
  }
}
