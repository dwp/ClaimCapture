package xml.claim

import models.domain._
import scala.xml.NodeSeq
import app.XMLValues._
import xml.XMLHelper._
import models.domain.Claim
import xml.XMLComponent

object Partner extends XMLComponent {

  def xml(claim: Claim) = {
    val moreAboutYou = claim.questionGroup[MoreAboutYou].getOrElse(MoreAboutYou())
    val yourPartnerPersonalDetails = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())
    val personYouCareFor = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())
    val hadPartner = (if(moreAboutYou.maritalStatus == "Living with partner") yes else yourPartnerPersonalDetails.hadPartnerSinceClaimDate) == yes

    if (hadPartner) {
      <Partner>
        {question(<Surname/>,"surname",encrypt(yourPartnerPersonalDetails.surname))}
        {question(<OtherNames/>, "firstName", yourPartnerPersonalDetails.firstName.getOrElse("")+" "+yourPartnerPersonalDetails.middleName.getOrElse(""))}
        {question(<OtherSurnames/>,"otherNames", yourPartnerPersonalDetails.otherSurnames)}
        {question(<Title/>, "title", yourPartnerPersonalDetails.title)}
        {question(<DateOfBirth/>,"dateOfBirth", yourPartnerPersonalDetails.dateOfBirth)}
        {question(<NationalInsuranceNumber/>,"nationalInsuranceNumber",encrypt(yourPartnerPersonalDetails.nationalInsuranceNumber))}
        {question(<NationalityPartner/>, "partner.nationality", yourPartnerPersonalDetails.nationality)}
        <RelationshipStatus>
          {question(<SeparatedFromPartner/>, "separated_fromPartner.label", yourPartnerPersonalDetails.separatedFromPartner, claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))}
        </RelationshipStatus>
        {question(<IsCaree/>, "isPartnerPersonYouCareFor", personYouCareFor.isPartnerPersonYouCareFor)}
      </Partner>
    } else NodeSeq.Empty
  }
}
