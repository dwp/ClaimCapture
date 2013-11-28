package xml.claim

import models.domain._
import scala.xml.NodeSeq
import app.XMLValues._
import xml.XMLHelper._
import models.domain.Claim
import xml.XMLComponent

object Partner extends XMLComponent {

  def xml(claim: Claim) = {
    val moreAboutYou = claim.questionGroup[MoreAboutYou].getOrElse(MoreAboutYou(hadPartnerSinceClaimDate = no))
    val yourPartnerPersonalDetails = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())
    val personYouCareFor = claim.questionGroup[PersonYouCareFor].getOrElse(PersonYouCareFor())
    val hadPartner = moreAboutYou.hadPartnerSinceClaimDate.toLowerCase == yes

    if (hadPartner) {
      <Partner>
        <Surname>{yourPartnerPersonalDetails.surname}</Surname>
        <OtherNames>{yourPartnerPersonalDetails.firstName} {yourPartnerPersonalDetails.middleName.getOrElse("")}</OtherNames>
        {statement(<OtherSurnames/>,yourPartnerPersonalDetails.otherSurnames)}
        <Title>{yourPartnerPersonalDetails.title}</Title>
        {statement(<DateOfBirth/>,yourPartnerPersonalDetails.dateOfBirth)}
        {statement(<NationalInsuranceNumber/>,yourPartnerPersonalDetails.nationalInsuranceNumber)}
        <NationalityPartner>{yourPartnerPersonalDetails.nationality.get}</NationalityPartner>
        <RelationshipStatus>
          {question(<SeparatedFromPartner/>, "separated_fromPartner.label", yourPartnerPersonalDetails.separatedFromPartner, claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))}
        </RelationshipStatus>
        {question(<IsCaree/>, "isPartnerPersonYouCareFor", personYouCareFor.isPartnerPersonYouCareFor)}
      </Partner>
    } else NodeSeq.Empty
  }
}
