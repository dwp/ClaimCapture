package xml.claim

import models.domain._
import xml.XMLHelper._
import xml.XMLComponent
import controllers.mappings.Mappings
import utils.helpers.HtmlLabelHelper.displayPlaybackDatesFormat
import play.api.i18n.Lang

object Claimant extends XMLComponent {
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
      {question(<TitleOther/>, "titleOther", yourDetails.titleOther)}
      {question(<DateOfBirth/>,"dateOfBirth", yourDetails.dateOfBirth)}
      {question(<NationalInsuranceNumber/>,"nationalInsuranceNumber", encrypt(yourDetails.nationalInsuranceNumber))}
      {postalAddressStructure("address", contactDetails.address, encrypt(contactDetails.postcode.getOrElse("").toUpperCase))}
      {question(<DayTimePhoneNumber/>,"howWeContactYou", contactDetails.howWeContactYou)}
      {question(<Cared35HoursBefore/>,"spent35HoursCaringBeforeClaim.label", claimDateDetails.spent35HoursCaringBeforeClaim.answer, claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy => displayPlaybackDatesFormat(Lang("en"),dmy)))}
      {if(claimDateDetails.spent35HoursCaringBeforeClaim.date.isDefined){
        {question(<DateStartCaring/>,"beforeClaimCaring_date", claimDateDetails.spent35HoursCaringBeforeClaim.date)}
      }}
      {question(<MaritalStatus/>, "maritalStatus", maritalStatus.maritalStatus)}
      {question(<TextPhoneContact/>,"contactYouByTextphone", textPhone(contactDetails))}
      {question(<WantsContactEmail/>,"wantsEmailContact", contactDetails.wantsContactEmail)}
      {question(<Email/>,"mail.output", contactDetails.email)}
    </Claimant>
  }

  def textPhone(contactDetails:ContactDetails) = {
    contactDetails.contactYouByTextphone match {
      case Some(_) => Mappings.yes
      case None =>    Mappings.no
    }
  }
}