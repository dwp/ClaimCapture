package services.submission

import models.domain._

object YourPartnerSubmission {
  def buildYourPartner(claim: Claim): Option[YourPartner] = claim.isSectionVisible(models.domain.YourPartner) match {
    case true => {
      val yourPartnerPersonalDetails = getQuestionGroup[YourPartnerPersonalDetails](claim, YourPartnerPersonalDetails)
      val yourPartnerContactDetails = getQuestionGroup[YourPartnerContactDetails](claim, YourPartnerContactDetails)
      val moreAboutYourPartner = getQuestionGroup[MoreAboutYourPartner](claim, MoreAboutYourPartner)
      val personYouCareFor = questionGroup[PersonYouCareFor](claim, PersonYouCareFor) // New to schema
      Some(YourPartner(yourPartnerPersonalDetails, yourPartnerContactDetails, moreAboutYourPartner, personYouCareFor))
    }
    case false => None
  }

  def buildClaimant(yourPartner: Option[YourPartner]) = yourPartner match {
    case Some(y) => {
      <Partner>
        <NationalityPartner>{ y.yourPartnerPersonalDetails.nationality.getOrElse("") }</NationalityPartner>
        <Surname>{ y.yourPartnerPersonalDetails.surname }</Surname>
        <OtherNames>{ s"${y.yourPartnerPersonalDetails.firstName} ${y.yourPartnerPersonalDetails.middleName.getOrElse("")}" }</OtherNames>
        <OtherSurnames/>
        <Title>{ y.yourPartnerPersonalDetails.title }</Title>
        <DateOfBirth>{ y.yourPartnerPersonalDetails.dateOfBirth.toXmlString }</DateOfBirth>
        <NationalInsuranceNumber>{ if (y.yourPartnerPersonalDetails.nationalInsuranceNumber.isDefined) y.yourPartnerPersonalDetails.nationalInsuranceNumber.get.stringify else "" }</NationalInsuranceNumber>
        <Address>
          <gds:Line>{ if (y.yourPartnerContactDetails.address.isDefined) y.yourPartnerContactDetails.address.get.lineOne.getOrElse("") else "" }</gds:Line>
          <gds:Line>{ if (y.yourPartnerContactDetails.address.isDefined) y.yourPartnerContactDetails.address.get.lineTwo.getOrElse("") else "" }</gds:Line>
          <gds:Line>{ if (y.yourPartnerContactDetails.address.isDefined) y.yourPartnerContactDetails.address.get.lineThree.getOrElse("") else "" }</gds:Line>
          <gds:PostCode>{ if (y.yourPartnerContactDetails.address.isDefined) y.yourPartnerContactDetails.postcode.getOrElse("") else "" }</gds:PostCode>
        </Address>
        <ConfirmAddress>yes</ConfirmAddress><!-- Always default to yes -->
        <RelationshipStatus>
          <JoinedHouseholdAfterDateOfClaim>{ if (y.moreAboutYourPartner.startedLivingTogether.isDefined) y.moreAboutYourPartner.startedLivingTogether.get.answer }</JoinedHouseholdAfterDateOfClaim>
          <JoinedHouseholdDate>{ if (y.moreAboutYourPartner.startedLivingTogether.isDefined && y.moreAboutYourPartner.startedLivingTogether.get.answer == "yes") y.moreAboutYourPartner.startedLivingTogether.get.date.get.toXmlString else "" }</JoinedHouseholdDate>
          <SeparatedFromPartner>{ y.moreAboutYourPartner.separated.answer }</SeparatedFromPartner>
          <SeparationDate>{ if (y.moreAboutYourPartner.separated.answer == "yes") y.moreAboutYourPartner.separated.date.getOrElse("") else "" }</SeparationDate>
        </RelationshipStatus>
      </Partner>
    }
    case None => {} // dwp-ca-claim-v1_10.xsd specifies minOccurs=0, so we have to deal with the situation where there is no Partner.
  }

  private def getQuestionGroup[T](claim: Claim, qi: QuestionGroup.Identifier) = {
    claim.questionGroup(qi).asInstanceOf[Option[T]].get
  }

  private def questionGroup[T](claim: Claim, qi: QuestionGroup.Identifier) = {
    claim.questionGroup(qi).asInstanceOf[Option[T]]
  }
}