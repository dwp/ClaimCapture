package xml.claim

import models.domain._
import scala.xml.NodeSeq
import app.XMLValues._
import xml.XMLHelper._
import models.domain.Claim
import xml.XMLComponent
import utils.helpers.HtmlLabelHelper.displayPlaybackDatesFormat
import play.api.i18n.Lang

object Partner extends XMLComponent {

  def xml(claim: Claim) = {
    val yourPartnerPersonalDetails = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())
    val personYouCareFor = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())
    val hadPartner = (yourPartnerPersonalDetails.hadPartnerSinceClaimDate == yes)

    if (hadPartner) {
      <Partner>
        {question(<Surname/>,"surname",encrypt(yourPartnerPersonalDetails.surname))}
        {question(<OtherNames/>, "firstName", yourPartnerPersonalDetails.firstName.getOrElse(""))}
        {question(<MiddleNames/>, "middleName", yourPartnerPersonalDetails.middleName)}
        {question(<OtherSurnames/>,"otherNames", yourPartnerPersonalDetails.otherSurnames)}
        {question(<Title/>, "title", yourPartnerPersonalDetails.title)}
        {question(<TitleOther/>, "titleOther", yourPartnerPersonalDetails.titleOther)}
        {question(<DateOfBirth/>,"dateOfBirth", yourPartnerPersonalDetails.dateOfBirth)}
        {question(<NationalInsuranceNumber/>,"nationalInsuranceNumber",encrypt(yourPartnerPersonalDetails.nationalInsuranceNumber))}
        {question(<NationalityPartner/>, "partner.nationality", yourPartnerPersonalDetails.nationality)}
        <RelationshipStatus>
          {question(<SeparatedFromPartner/>, "separated_fromPartner.label", yourPartnerPersonalDetails.separatedFromPartner, claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy => displayPlaybackDatesFormat(Lang("en"), dmy)))}
        </RelationshipStatus>
        {question(<IsCaree/>, "isPartnerPersonYouCareFor", personYouCareFor.isPartnerPersonYouCareFor)}
      </Partner>
    } else NodeSeq.Empty
  }
}
