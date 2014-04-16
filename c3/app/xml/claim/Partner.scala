package xml.claim

import models.domain._
import scala.xml.NodeSeq
import app.XMLValues._
import xml.XMLHelper._
import models.domain.Claim
import xml.XMLComponent

object Partner extends XMLComponent {

  def xml(claim: Claim) = {
    val moreAboutYou = claim.questionGroup[MoreAboutYou].getOrElse(MoreAboutYou(hadPartnerSinceClaimDate = Some(no)))
    val yourPartnerPersonalDetails = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())
    val personYouCareFor = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())
    val hadPartner = (if(moreAboutYou.maritalStatus == "Living with partner") yes else moreAboutYou.hadPartnerSinceClaimDate.get) == yes

    if (hadPartner) {
      <Partner>
        {question(<Surname/>,"surname",yourPartnerPersonalDetails.surname)}
        {question(<OtherNames/>, "firstName", yourPartnerPersonalDetails.firstName+" "+yourPartnerPersonalDetails.middleName.getOrElse(""))}
        {question(<OtherSurnames/>,"otherNames", yourPartnerPersonalDetails.otherSurnames)}
        {question(<Title/>, "title", yourPartnerPersonalDetails.title)}
        {question(<DateOfBirth/>,"dateOfBirth", yourPartnerPersonalDetails.dateOfBirth)}
        {question(<NationalInsuranceNumber/>,"nationalInsuranceNumber",yourPartnerPersonalDetails.nationalInsuranceNumber)}
        {question(<NationalityPartner/>, "partner.nationality", yourPartnerPersonalDetails.nationality.get)}
        <RelationshipStatus>
          {question(<SeparatedFromPartner/>, "separated_fromPartner.label", yourPartnerPersonalDetails.separatedFromPartner, claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))}
        </RelationshipStatus>
        {question(<IsCaree/>, "isPartnerPersonYouCareFor", personYouCareFor.isPartnerPersonYouCareFor)}
      </Partner>
    } else NodeSeq.Empty
  }
}
