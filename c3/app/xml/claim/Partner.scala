package xml.claim

import models.domain._
import scala.xml.NodeSeq
import app.XMLValues._
import xml.XMLHelper._
import models.domain.Claim
import scala.Some
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
        {yourPartnerPersonalDetails.otherSurnames match {
          case Some(n) => <OtherSurnames>{yourPartnerPersonalDetails.otherSurnames.get}</OtherSurnames>
          case None => NodeSeq.Empty
        }}
        <Title>{yourPartnerPersonalDetails.title}</Title>
        <DateOfBirth>{yourPartnerPersonalDetails.dateOfBirth.`dd-MM-yyyy`}</DateOfBirth>
        <NationalInsuranceNumber>{stringify(yourPartnerPersonalDetails.nationalInsuranceNumber)}</NationalInsuranceNumber>
        <NationalityPartner>{yourPartnerPersonalDetails.nationality.get}</NationalityPartner>
        <RelationshipStatus>
          {question(<SeparatedFromPartner/>, "separated_fromPartner.label", yourPartnerPersonalDetails.separatedFromPartner, claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))}
        </RelationshipStatus>
        {question(<IsCaree/>, "isPartnerPersonYouCareFor", personYouCareFor.isPartnerPersonYouCareFor)}
      </Partner>
    } else NodeSeq.Empty
  }
}
