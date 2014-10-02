package xml.claim

import models.domain._
import xml.XMLHelper._
import xml.XMLComponent
import controllers.Mappings
import play.api.Logger

object Claimant extends XMLComponent {
  def xml(claim: Claim) = {
    val yourDetails = claim.questionGroup[YourDetails].getOrElse(YourDetails())
    val contactDetails = claim.questionGroup[ContactDetails].getOrElse(ContactDetails())
    val nationalityAndResidency = claim.questionGroup[NationalityAndResidency].getOrElse(NationalityAndResidency(""))

    <Claimant>
      {question(<Surname/>, "surname", encrypt(yourDetails.surname))}
      {question(<OtherNames/>, "firstName", yourDetails.firstName+" "+yourDetails.middleName.getOrElse(""))}
      {question(<OtherSurnames/>,"otherNames", yourDetails.otherSurnames.getOrElse(""))}
      {question(<Title/>, "title", yourDetails.title)}
      {question(<DateOfBirth/>,"dateOfBirth", yourDetails.dateOfBirth)}
      {question(<NationalInsuranceNumber/>,"nationalInsuranceNumber", encrypt(yourDetails.nationalInsuranceNumber))}
      {postalAddressStructure("address", contactDetails.address, encrypt(contactDetails.postcode.getOrElse("").toUpperCase))}
      {question(<DayTimePhoneNumber/>,"howWeContactYou", contactDetails.howWeContactYou)}
      {question(<MaritalStatus/>, "maritalStatus", nationalityAndResidency.maritalStatus)}
      {question(<TextPhoneContact/>,"contactYouByTextphone", textPhone(contactDetails))}
    </Claimant>
  }

  def textPhone(contactDetails:ContactDetails) = {
    contactDetails.contactYouByTextphone match {
      case Some(_) => Mappings.yes
      case None =>    Mappings.no
    }
  }
}