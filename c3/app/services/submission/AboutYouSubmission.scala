package services.submission

import models.domain._

object AboutYouSubmission {
  def buildAboutYou(claim: Claim) = {
    val yourDetails = claim.questionGroup(YourDetails).asInstanceOf[Option[YourDetails]].get
    val contactDetails = claim.questionGroup(ContactDetails).asInstanceOf[Option[ContactDetails]].get
    val timeOutsideUK = claim.questionGroup(TimeOutsideUK).asInstanceOf[Option[TimeOutsideUK]]
    val claimDate = claim.questionGroup(ClaimDate).asInstanceOf[Option[ClaimDate]].get
    AboutYou(yourDetails, contactDetails, timeOutsideUK, claimDate)
  }

  def buildClaimant(aboutYou: AboutYou) = {
    <Claimant>
      <DateOfClaim>{aboutYou.claimDate.dateOfClaim.toXmlString}</DateOfClaim>
      <Surname>{aboutYou.yourDetails.surname}</Surname>
      <OtherNames>{s"${aboutYou.yourDetails.firstName} ${aboutYou.yourDetails.middleName.getOrElse("")}"}</OtherNames>
      <OtherSurnames>{aboutYou.yourDetails.otherSurnames.orNull}</OtherSurnames>
      <Title>{aboutYou.yourDetails.title}</Title>
      <MaritalStatus>{aboutYou.yourDetails.maritalStatus}</MaritalStatus>
      <DateOfBirth>{aboutYou.yourDetails.dateOfBirth.toXmlString}</DateOfBirth>
      <NationalInsuranceNumber>{aboutYou.yourDetails.nationalInsuranceNumber.orNull}</NationalInsuranceNumber>
      <ExistingNationalInsuranceNumber>no</ExistingNationalInsuranceNumber>
      <Address>
        <gds:Line>{aboutYou.contactDetails.address.lineOne.orNull}</gds:Line>
        <gds:Line>{aboutYou.contactDetails.address.lineTwo.orNull}</gds:Line>
        <gds:Line>{aboutYou.contactDetails.address.lineThree.orNull}</gds:Line>
        <gds:PostCode>{aboutYou.contactDetails.postcode.orNull}</gds:PostCode>
      </Address>
      <ConfirmAddress>yes</ConfirmAddress> <!-- Always default to yes -->
      <HomePhoneNumber>{aboutYou.contactDetails.mobileNumber.orNull}</HomePhoneNumber>
      <DaytimePhoneNumber>
        <Number>{aboutYou.contactDetails.phoneNumber.orNull}</Number>
        <Qualifier/>
      </DaytimePhoneNumber>
      <EmailAddress/>
      <ClaimedBefore>no</ClaimedBefore> <!--  Default to no -->
    </Claimant>
  }
}