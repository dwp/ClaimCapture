package xml.claim

import models.domain.{ContactDetails, YourDetails, Claim}
import xml.XMLHelper._
import xml.XMLComponent

object Claimant extends XMLComponent {
  def xml(claim: Claim) = {
    val yourDetails = claim.questionGroup[YourDetails].getOrElse(YourDetails())
    val contactDetails = claim.questionGroup[ContactDetails].getOrElse(ContactDetails())

    <Claimant>
      <Surname>{yourDetails.surname}</Surname>
      <OtherNames>{yourDetails.firstName} {yourDetails.middleName.getOrElse("")}</OtherNames>
      {statement(<OtherSurnames/>,yourDetails.otherSurnames)}
      <Title>{yourDetails.title}</Title>
      {statement(<DateOfBirth/>,yourDetails.dateOfBirth)}
      {statement(<NationalInsuranceNumber/>,yourDetails.nationalInsuranceNumber)}
      {postalAddressStructure(contactDetails.address, contactDetails.postcode)}
      {statement(<DayTimePhoneNumber/>,contactDetails.phoneNumber)}
      {statement(<MobileNumber/>,contactDetails.mobileNumber)}
      <MaritalStatus>{yourDetails.maritalStatus}</MaritalStatus>
      {question(<TextPhoneContact/>,"contactYouByTextphone", contactDetails.contactYouByTextphone)}
    </Claimant>
  }
}