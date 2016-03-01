package xml.claim

import controllers.mappings.Mappings
import models.domain._
import scala.xml.NodeSeq
import app.XMLValues._
import xml.XMLHelper._
import models.domain.Claim
import xml.XMLComponent
import utils.helpers.HtmlLabelHelper.displayPlaybackDatesFormat
import play.api.i18n.Lang

object Partner extends XMLComponent {
  val datePattern = "dd-MM-yyyy"
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

  def fromXml(xml: NodeSeq, claim: Claim) : Claim = {
    claim.update(createPartnerDetailsFromXml(xml))
  }

  private def createPartnerDetailsFromXml(xml: NodeSeq) = {
    val partner = (xml \\ "Partner")
    partner.isEmpty match {
      case false =>
        models.domain.YourPartnerPersonalDetails(
          title = createStringOptional((partner \ "Title" \ "Answer").text),
          firstName = createStringOptional((partner \ "OtherNames" \ "Answer").text),
          middleName = createStringOptional((partner \ "MiddleNames" \ "Answer").text),
          surname = createStringOptional(decrypt((partner \ "Surname" \ "Answer").text)),
          otherSurnames = createStringOptional((partner \ "OtherSurnames" \ "Answer").text),
          dateOfBirth = createFormattedDateOptional((partner \ "DateOfBirth" \ "Answer").text),
          nationalInsuranceNumber = createNationalInsuranceNumberOptional(partner),
          nationality = createStringOptional((partner \ "NationalityPartner" \ "Answer").text),
          separatedFromPartner = createYesNoTextOptional((partner \ "RelationshipStatus" \ "SeparatedFromPartner" \ "Answer").text),
          isPartnerPersonYouCareFor = createYesNoTextOptional((partner \ "IsCaree" \ "Answer").text),
          hadPartnerSinceClaimDate = Mappings.yes
        )
      case true => models.domain.YourPartnerPersonalDetails(None, None, None, None, None, None, None, None, None, None, Mappings.no)
    }
  }
}
