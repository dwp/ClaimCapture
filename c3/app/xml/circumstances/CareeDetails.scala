package xml.circumstances

import xml.XMLComponent
import models.domain.{DetailsOfThePersonYouCareFor, Claim}
import scala.xml.NodeSeq
import xml.XMLHelper._

/**
 * @author Jorge Migueis
 */
object CareeDetails extends XMLComponent {
  def xml(circs: Claim): NodeSeq = {
      val detailsOfThePerson = circs.questionGroup[DetailsOfThePersonYouCareFor].getOrElse(DetailsOfThePersonYouCareFor())

    <CareeDetails>
      <Surname>{detailsOfThePerson.lastName}</Surname>
      <OtherNames>{detailsOfThePerson.firstName} {detailsOfThePerson.middleName.getOrElse("")}</OtherNames>
      {statement(<DateOfBirth/>,detailsOfThePerson.dateOfBirth)}
      <NationalInsuranceNumber>{detailsOfThePerson.nationalInsuranceNumber.stringify}</NationalInsuranceNumber>
    </CareeDetails>
  }
}
