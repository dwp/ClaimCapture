package services.submission

import models.domain._

object YourPartnerSubmission {
  private def getQuestionGroup[T](claim: Claim, questionGroup: QuestionGroup) = {
    claim.questionGroup(questionGroup).asInstanceOf[Option[T]].get
  }
  private def questionGroup[T](claim: Claim, questionGroup: QuestionGroup) = {
    claim.questionGroup(questionGroup).asInstanceOf[Option[T]]
  }
  
  def buildYourPartner(claim: Claim) = {
    // TODO [SKW] Check which of the below are optional.
    val yourPartnerPersonalDetails = getQuestionGroup[YourPartnerPersonalDetails](claim, YourPartnerPersonalDetails)
    val yourPartnerContactDetails = getQuestionGroup[YourPartnerContactDetails](claim, YourPartnerContactDetails)
    val moreAboutYourPartner = getQuestionGroup[MoreAboutYourPartner](claim, MoreAboutYourPartner)
    val personYouCareFor = getQuestionGroup[PersonYouCareFor](claim, PersonYouCareFor)
    YourPartner(yourPartnerPersonalDetails, yourPartnerContactDetails, moreAboutYourPartner, personYouCareFor)
  }

  def buildYourPartner(yourPartner: YourPartner) = {
      <Partner> 
        <NationalityPartner>${yourPartner.yourPartnerPersonalDetails.nationality.getOrElse("")}</NationalityPartner>
        <Surname>${yourPartner.yourPartnerPersonalDetails.surname}</Surname>
        <OtherNames>${yourPartner.yourPartnerPersonalDetails.otherNames.getOrElse("")}</OtherNames>
        <OtherSurnames/>
        <Title>${yourPartner.yourPartnerPersonalDetails.title}</Title>
        <DateOfBirth>${yourPartner.yourPartnerPersonalDetails.dateOfBirth.toXmlString}</DateOfBirth>
        <NationalInsuranceNumber>${yourPartner.yourPartnerPersonalDetails.nationalInsuranceNumber.orNull}</NationalInsuranceNumber>
        {if(yourPartner.yourPartnerContactDetails.address.isDefined){
        <Address>
          <gds:Line>{yourPartner.yourPartnerContactDetails.address.get.lineOne.getOrElse("")}</gds:Line>
          <gds:Line>{yourPartner.yourPartnerContactDetails.address.get.lineTwo.getOrElse("")}</gds:Line>
          <gds:Line>{yourPartner.yourPartnerContactDetails.address.get.lineThree.getOrElse("")}</gds:Line>
          <gds:PostCode>{yourPartner.yourPartnerContactDetails.postcode.getOrElse("")}</gds:PostCode>
        </Address>
        }
        }
        <ConfirmAddress>yes</ConfirmAddress> <!-- Always default to yes -->
        <RelationshipStatus>
          <JoinedHouseholdAfterDateOfClaim>no</JoinedHouseholdAfterDateOfClaim>
          <JoinedHouseholdDate/>
          <SeparatedFromPartner>no</SeparatedFromPartner>
          <SeparationDate/>
        </RelationshipStatus>
      </Partner>
  }
}