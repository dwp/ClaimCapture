package xml.claim

import models.domain.{MoreAboutYou, ContactDetails, YourDetails, Claim}
import xml.XMLHelper._
import xml.XMLComponent

object Claimant extends XMLComponent {
  def xml(claim: Claim) = {
    val yourDetails = claim.questionGroup[YourDetails].getOrElse(YourDetails())
    val contactDetails = claim.questionGroup[ContactDetails].getOrElse(ContactDetails())
    val moreAboutYou = claim.questionGroup[MoreAboutYou].getOrElse(MoreAboutYou())

    <Claimant>
      {question(<Surname/>, "surname", encrypt(yourDetails.surname))}
      {question(<OtherNames/>, "firstName", yourDetails.firstName+" "+yourDetails.middleName.getOrElse(""))}
      {question(<OtherSurnames/>,"otherNames", yourDetails.otherSurnames.getOrElse(""))}
      {question(<Title/>, "title", yourDetails.title)}
      {question(<DateOfBirth/>,"dateOfBirth", yourDetails.dateOfBirth)}
      {question(<NationalInsuranceNumber/>,"nationalInsuranceNumber", encrypt(yourDetails.nationalInsuranceNumber))}
      {postalAddressStructure("address", contactDetails.address, encrypt(contactDetails.postcode.getOrElse("").toUpperCase))}
      {question(<DayTimePhoneNumber/>,"howWeContactYou", contactDetails.howWeContactYou)}
      {question(<MaritalStatus/>, "maritalStatus", moreAboutYou.maritalStatus)}
      {question(<TextPhoneContact/>,"contactYouByTextphone", contactDetails.contactYouByTextphone.getOrElse(""))}
    </Claimant>
  }
}