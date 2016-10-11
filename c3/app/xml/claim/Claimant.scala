package xml.claim

import app.ConfigProperties._
import models.yesNo.YesNoWithDate
import models.NationalInsuranceNumber
import models.domain._
import xml.XMLHelper._
import xml.XMLComponent
import controllers.mappings.Mappings
import utils.helpers.HtmlLabelHelper.displayPlaybackDatesFormat
import play.api.i18n.Lang

import scala.xml.NodeSeq

object Claimant extends XMLComponent {
  val datePattern = "dd-MM-yyyy"
  private def emailLabel = {
    if(getBooleanProperty("saveForLaterSaveEnabled")) "wantsEmailContactNew"
    else "wantsEmailContactOld"
  }
  def xml(claim: Claim) = {
    val yourDetails = claim.questionGroup[YourDetails].getOrElse(YourDetails())
    val contactDetails = claim.questionGroup[ContactDetails].getOrElse(ContactDetails())
    val nationalityAndResidency = claim.questionGroup[NationalityAndResidency].getOrElse(NationalityAndResidency(""))
    val claimDateDetails = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())
    val maritalStatus = claim.questionGroup[MaritalStatus].getOrElse(MaritalStatus())

    <Claimant>
      {question(<Surname/>, "surname", encrypt(yourDetails.surname))}
      {question(<OtherNames/>, "firstName", yourDetails.firstName)}
      {question(<MiddleNames/>, "middleName", yourDetails.middleName)}
      {question(<Title/>, "title", yourDetails.title)}
      {question(<DateOfBirth/>,"dateOfBirth", yourDetails.dateOfBirth)}
      {question(<NationalInsuranceNumber/>,"nationalInsuranceNumber", encrypt(yourDetails.nationalInsuranceNumber))}
      {postalAddressStructure("address", contactDetails.address, encrypt(contactDetails.postcode.getOrElse("").toUpperCase))}
      {question(<DayTimePhoneNumber/>,"howWeContactYou", contactDetails.howWeContactYou)}
      {question(<Cared35HoursBefore/>,"spent35HoursCaringBeforeClaim.label", claimDateDetails.spent35HoursCaringBeforeClaim.answer, claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy => displayPlaybackDatesFormat(Lang("en"),dmy)))}
      {if(claimDateDetails.spent35HoursCaringBeforeClaim.date.isDefined){
        {question(<DateStartCaring/>,"beforeClaimCaring.date", claimDateDetails.spent35HoursCaringBeforeClaim.date)}
      }}
      {question(<MaritalStatus/>, "maritalStatus", maritalStatus.maritalStatus)}
      {question(<TextPhoneContact/>,"contactYouByTextphone", textPhone(contactDetails))}
      {question(<WantsContactEmail/>,emailLabel, contactDetails.wantsContactEmail)}
      {question(<Email/>,"mail.output", contactDetails.email)}
    </Claimant>
  }

  def textPhone(contactDetails:ContactDetails) = {
    contactDetails.contactYouByTextphone match {
      case Some(_) => Mappings.yes
      case None =>    Mappings.no
    }
  }

  def fromXml(xml: NodeSeq, claim: Claim) : Claim = {
    claim.update(createYourDetailsFromXml(xml))
      .update(createContactDetailsFromXml(xml))
      .update(createMaritalStatusFromXml(xml))
      .update(createClaimDateFromXml(xml))
      .update(createQualifyingBenefitFromXml(xml))
      .update(createEligibilityFromXml(xml))
      .update(createProceedAnywayFromXml(xml))
  }

  private def createYourDetailsFromXml(xml: NodeSeq) = {
    val claimant = (xml \\ "Claimant")
    YourDetails(
      title = (claimant \ "Title" \ "Answer").text,
      firstName = (claimant \ "OtherNames" \ "Answer").text,
      middleName = createStringOptional((claimant \ "MiddleNames" \ "Answer").text),
      surname = decrypt((claimant \ "Surname" \ "Answer").text),
      dateOfBirth = createFormattedDate((claimant \ "DateOfBirth" \ "Answer").text),
      nationalInsuranceNumber = NationalInsuranceNumber(createStringOptional(decrypt((claimant \ "NationalInsuranceNumber" \ "Answer").text)))
    )
  }

  private def createContactDetailsFromXml(xml: NodeSeq) = {
    val claimant = (xml \\ "Claimant")
    ContactDetails(
      address = createAddressFromXml(claimant),
      postcode = createStringOptional(decrypt((claimant \ "Address" \ "Answer" \ "PostCode").text)),
      howWeContactYou = createStringOptional((claimant \ "howWeContactYou" \ "Answer").text),
      contactYouByTextphone = createStringOptional((claimant \ "TextPhoneContact" \ "Answer").text),
      wantsContactEmail = createYesNoText((claimant \ "WantsContactEmail" \ "Answer").text),
      email = createStringOptional((claimant \ "Email" \ "Answer").text),
      emailConfirmation = createStringOptional((claimant \ "Email" \ "Answer").text)
    )
  }

  private def createMaritalStatusFromXml(xml: NodeSeq) = {
    val claimant = (xml \\ "Claimant")
    MaritalStatus(maritalStatus = (claimant \ "MaritalStatus" \ "Answer").text)
  }

  private def createClaimDateFromXml(xml: NodeSeq) = {
    val claimant = (xml \\ "Claimant")
    ClaimDate(
      dateOfClaim = createFormattedDate((xml \\ "DateOfClaim" \ "Answer").text),
      spent35HoursCaringBeforeClaim =
        YesNoWithDate(createYesNoText((claimant \ "Cared35HoursBefore" \ "Answer").text), createFormattedDateOptional((claimant \ "DateStartCaring" \ "Answer").text))
    )
  }

  private def createQualifyingBenefitFromXml(xml: NodeSeq) = {
    Benefits(benefitsAnswer = (xml \\ "QualifyingBenefit" \ "Answer").text)
  }

  private def createEligibilityFromXml(xml: NodeSeq) = {
    Eligibility(hours = Mappings.yes, over16 = Mappings.yes, origin = "GB")
  }

  private def createProceedAnywayFromXml(xml: NodeSeq) = {
    ProceedAnyway(allowedToContinue = true, answerYesNo = None, jsEnabled = true)
  }
}
