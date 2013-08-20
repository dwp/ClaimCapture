package xml

import models.domain._
import XMLHelper.{stringify, postalAddressStructure}
import models.yesNo.YesNoWithDate
import controllers.Mappings._
import scala.xml.NodeSeq
import app.XMLValues

object Partner {

  def xml(claim: Claim) = {
    val moreAboutYou = claim.questionGroup[MoreAboutYou].getOrElse(MoreAboutYou(hadPartnerSinceClaimDate = no))

    val yourPartnerPersonalDetails = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())

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
        <Address>{postalAddressStructure(models.MultiLineAddress(Some(XMLValues.NotAsked)), "")}</Address>
        <ConfirmAddress>yes</ConfirmAddress>
        <RelationshipStatus>
          <JoinedHouseholdAfterDateOfClaim>{XMLValues.NotAsked}</JoinedHouseholdAfterDateOfClaim>
          <JoinedHouseholdDate></JoinedHouseholdDate>
          <SeparatedFromPartner>{XMLValues.NotAsked}</SeparatedFromPartner>
          <SeparationDate></SeparationDate>
        </RelationshipStatus>
      </Partner>
    } else NodeSeq.Empty
  }
}
