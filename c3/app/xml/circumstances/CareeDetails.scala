package xml.circumstances

import xml.XMLComponent
import models.domain.{DetailsOfThePersonYouCareFor, Claim}
import scala.xml.NodeSeq

/**
 * @author Jorge Migueis
 */
object CareeDetails extends XMLComponent {
  def xml(circs: Claim): NodeSeq = {
      val detailsOfThePerson = circs.questionGroup[DetailsOfThePersonYouCareFor].getOrElse(DetailsOfThePersonYouCareFor())

    <CareeDetails>
        <Surname>{detailsOfThePerson.lastName}</Surname>
        <OtherNames>{detailsOfThePerson.otherNames}</OtherNames>
        <DateOfBirth>{detailsOfThePerson.dateOfBirth.`dd-MM-yyyy`}</DateOfBirth>
        <NationalInsuranceNumber>{detailsOfThePerson.nationalInsuranceNumber.stringify}</NationalInsuranceNumber>
      </CareeDetails>
  }
}
