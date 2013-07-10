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
    val personYouCareFor = questionGroup[PersonYouCareFor](claim, PersonYouCareFor) // New to schema
    YourPartner(yourPartnerPersonalDetails, yourPartnerContactDetails, moreAboutYourPartner, personYouCareFor)
  }

  def buildClaimant(yourPartner: YourPartner) = {
      <Partner> 
        <NationalityPartner>{yourPartner.yourPartnerPersonalDetails.nationality.getOrElse("")}</NationalityPartner>
        <Surname>{yourPartner.yourPartnerPersonalDetails.surname}</Surname>
        <OtherNames>{s"${yourPartner.yourPartnerPersonalDetails.firstName} ${yourPartner.yourPartnerPersonalDetails.middleName.getOrElse("")}"}</OtherNames>
        <OtherSurnames/>
        <Title>{yourPartner.yourPartnerPersonalDetails.title}</Title>
        <DateOfBirth>{yourPartner.yourPartnerPersonalDetails.dateOfBirth.toXmlString}</DateOfBirth>
        <NationalInsuranceNumber>{if(yourPartner.yourPartnerPersonalDetails.nationalInsuranceNumber.isDefined) yourPartner.yourPartnerPersonalDetails.nationalInsuranceNumber.get.toXmlString else ""}</NationalInsuranceNumber>
        <Address>
          <gds:Line>{if(yourPartner.yourPartnerContactDetails.address.isDefined) yourPartner.yourPartnerContactDetails.address.get.lineOne.getOrElse("") else ""}</gds:Line>
          <gds:Line>{if(yourPartner.yourPartnerContactDetails.address.isDefined) yourPartner.yourPartnerContactDetails.address.get.lineTwo.getOrElse("") else ""}</gds:Line>
          <gds:Line>{if(yourPartner.yourPartnerContactDetails.address.isDefined) yourPartner.yourPartnerContactDetails.address.get.lineThree.getOrElse("") else ""}</gds:Line>
          <gds:PostCode>{if(yourPartner.yourPartnerContactDetails.address.isDefined)yourPartner.yourPartnerContactDetails.postcode.getOrElse("") else ""}</gds:PostCode>
        </Address>
        <ConfirmAddress>yes</ConfirmAddress> <!-- Always default to yes -->
        <RelationshipStatus>
          <JoinedHouseholdAfterDateOfClaim>{if(yourPartner.moreAboutYourPartner.dateStartedLivingTogether.isDefined) "yes" else "no"}</JoinedHouseholdAfterDateOfClaim>
          <JoinedHouseholdDate>{if(yourPartner.moreAboutYourPartner.dateStartedLivingTogether.isDefined) yourPartner.moreAboutYourPartner.dateStartedLivingTogether.get.toXmlString else ""}</JoinedHouseholdDate>
          <SeparatedFromPartner>{yourPartner.moreAboutYourPartner.separated.answer}</SeparatedFromPartner>
          <SeparationDate>{if(yourPartner.moreAboutYourPartner.separated.answer == "yes") yourPartner.moreAboutYourPartner.separated.date.getOrElse("") else ""}</SeparationDate>
        </RelationshipStatus>
      </Partner>
  }
}