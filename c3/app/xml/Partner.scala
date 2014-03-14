package xml

import models.domain._
import XMLHelper.{stringify, postalAddressStructure}
import scala.xml.NodeSeq
import app.XMLValues._

object Partner {

  def xml(claim: Claim) = {
    val moreAboutYou = claim.questionGroup[MoreAboutYou].getOrElse(MoreAboutYou(hadPartnerSinceClaimDate = Some(no)))

    val yourPartnerPersonalDetails = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())

    val hadPartner = (if(moreAboutYou.maritalStatus == "s") NotAsked else if(moreAboutYou.maritalStatus == "p") yes else moreAboutYou.hadPartnerSinceClaimDate.get) == yes

    if (hadPartner) {
      <Partner>
        <NationalityPartner>{yourPartnerPersonalDetails.nationality.orNull}</NationalityPartner>
        <Surname>{yourPartnerPersonalDetails.surname}</Surname>
        <OtherNames>{yourPartnerPersonalDetails.firstName} {yourPartnerPersonalDetails.middleName.orNull}</OtherNames>
        <OtherSurnames>{yourPartnerPersonalDetails.otherSurnames.orNull}</OtherSurnames>
        <Title>{yourPartnerPersonalDetails.title}</Title>
        <DateOfBirth>{yourPartnerPersonalDetails.dateOfBirth.`yyyy-MM-dd`}</DateOfBirth>
        <NationalInsuranceNumber>{stringify(yourPartnerPersonalDetails.nationalInsuranceNumber)}</NationalInsuranceNumber>
        <Address>{postalAddressStructure(models.MultiLineAddress(Some(NotAsked)), "")}</Address>
        <ConfirmAddress>{yes}</ConfirmAddress>
        <RelationshipStatus>
          <JoinedHouseholdAfterDateOfClaim>{NotAsked}</JoinedHouseholdAfterDateOfClaim>
          <JoinedHouseholdDate></JoinedHouseholdDate>
          <SeparatedFromPartner>{yourPartnerPersonalDetails.separatedFromPartner}</SeparatedFromPartner>
          <SeparationDate></SeparationDate>
        </RelationshipStatus>
      </Partner>
    } else NodeSeq.Empty
  }
}
