package xml

import models.domain._
import XMLHelper.{stringify, postalAddressStructure}
import models.yesNo.YesNoWithDate
import controllers.Mappings._
import scala.xml.NodeSeq

object Partner {

  def xml(claim: Claim) = {
    val moreAboutYouOption = claim.questionGroup[MoreAboutYou]
    val moreAboutYou = moreAboutYouOption.getOrElse(MoreAboutYou(hadPartnerSinceClaimDate = no))

    val yourPartnerPersonalDetailsOption = claim.questionGroup[YourPartnerPersonalDetails]
    val yourPartnerPersonalDetails = yourPartnerPersonalDetailsOption.getOrElse(YourPartnerPersonalDetails())

    val yourPartnerContactDetailsOption = claim.questionGroup[YourPartnerContactDetails]
    val yourPartnerContactDetails =  yourPartnerContactDetailsOption.getOrElse(YourPartnerContactDetails())

    val moreAboutYourPartnerOption = claim.questionGroup[MoreAboutYourPartner]
    val moreAboutYourPartner = moreAboutYourPartnerOption.getOrElse(MoreAboutYourPartner())

    val startedLivingTogether = moreAboutYourPartner.startedLivingTogether.getOrElse(YesNoWithDate(no, None))

    val hadPartner = moreAboutYou.hadPartnerSinceClaimDate == yes

    if (hadPartner) {
      <Partner>
        <NationalityPartner>{yourPartnerPersonalDetails.nationality.orNull}</NationalityPartner>
        <Surname>{yourPartnerPersonalDetails.surname}</Surname>
        <OtherNames>{yourPartnerPersonalDetails.firstName} {yourPartnerPersonalDetails.middleName.orNull}</OtherNames>
        <OtherSurnames>{yourPartnerPersonalDetails.otherSurnames.orNull}</OtherSurnames>
        <Title>{yourPartnerPersonalDetails.title}</Title>
        <DateOfBirth>{yourPartnerPersonalDetails.dateOfBirth.`yyyy-MM-dd`}</DateOfBirth>
        <NationalInsuranceNumber>{stringify(yourPartnerPersonalDetails.nationalInsuranceNumber)}</NationalInsuranceNumber>
        <Address>{postalAddressStructure(yourPartnerContactDetails.address, yourPartnerContactDetails.postcode)}</Address>
        <ConfirmAddress>yes</ConfirmAddress>
        <RelationshipStatus>
          <JoinedHouseholdAfterDateOfClaim>{startedLivingTogether.answer}</JoinedHouseholdAfterDateOfClaim>
          <JoinedHouseholdDate>{stringify(startedLivingTogether.date)}</JoinedHouseholdDate>
          <SeparatedFromPartner>{moreAboutYourPartner.separated.answer}</SeparatedFromPartner>
          <SeparationDate>{stringify(moreAboutYourPartner.separated.date)}</SeparationDate>
        </RelationshipStatus>
      </Partner>
    } else NodeSeq.Empty
  }
}