package xml.claim

import app.XMLValues._
import play.api.Logger
import models.domain._
import xml.XMLHelper._
import xml._
import scala.language.postfixOps
import utils.helpers.HtmlLabelHelper.displayPlaybackDatesFormat
import models.DayMonthYear
import play.api.i18n.Lang
import models.domain.Claim

object DWPCAClaim extends XMLComponent {

  def xml(claim: Claim) = {

    val courseDetails = claim.questionGroup[YourCourseDetails].getOrElse(YourCourseDetails(beenInEducationSinceClaimDate = no))
    val employment = claim.questionGroup[models.domain.Employment].getOrElse(models.domain.Employment(beenEmployedSince6MonthsBeforeClaim = no, beenSelfEmployedSince1WeekBeforeClaim = no))
    val additionalInfo = claim.questionGroup[models.domain.AdditionalInfo].getOrElse(models.domain.AdditionalInfo())
    val claimDate = claim.dateOfClaim.fold("")(_.`dd/MM/yyyy`)
    val yourPartnerPersonalDetails = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())
    val havePartner = yourPartnerPersonalDetails.hadPartnerSinceClaimDate
    val qualifyingBenefit = claim.questionGroup[Benefits].getOrElse(Benefits())
    val empAdditionalInfo = claim.questionGroup[EmploymentAdditionalInfo].getOrElse(EmploymentAdditionalInfo())


    Logger.info(s"Build XML DWPCAClaim for : ${claim.key} ${claim.uuid}.")

    <DWPCAClaim>
      {question(<DateOfClaim/>, "dateOfClaim",claim.dateOfClaim)}
      {question(<QualifyingBenefit/>, "benefitsAnswer",qualifyingBenefit.benefitsAnswer)}
      {Claimant.xml(claim)}
      {Caree.xml(claim)}
      {Residency.xml(claim)}
      {question(<CourseOfEducation/>, "beenInEducationSinceClaimDate.label",courseDetails.beenInEducationSinceClaimDate, claimDate)}
      {FullTimeEducation.xml(claim)}
      {question(<SelfEmployed/>, "beenSelfEmployedSince1WeekBeforeClaim.label",employment.beenSelfEmployedSince1WeekBeforeClaim, claim.dateOfClaim.fold("{CLAIM DATE - 1 week}")(dmy => displayClaimDate(dmy - 1 week)),claimDate)}
      {SelfEmployment.xml(claim)}
      {question(<Employed/>,"aboutYou_beenEmployedSince6MonthsBeforeClaim.label",employment.beenEmployedSince6MonthsBeforeClaim, claim.dateOfClaim.fold("{CLAIM DATE - 6 months}")(dmy => displayClaimDate(dmy - 6 months)), claimDate)}
      {Employment.xml(claim)}
      {if(!empAdditionalInfo.empAdditionalInfo.answer.isEmpty) questionOther(<EmploymentAdditionalInfo/>, "empAdditionalInfo.answer", empAdditionalInfo.empAdditionalInfo.answer, empAdditionalInfo.empAdditionalInfo.text)}
      {question(<HavePartner/>,"haveLivedWithPartnerSinceClaimDate.label",havePartner,claimDate)}
      {Partner.xml(claim)}
      {OtherBenefits.xml(claim)}
      {Payment.xml(claim)}
      {<OtherInformation>
        {question(<WelshCommunication/>,"welshCommunication",additionalInfo.welshCommunication)}
        {questionWhy(<AdditionalInformation/>,"anythingElse.answer", additionalInfo.anythingElse.answer, additionalInfo.anythingElse.text,"anythingElse_text")}
       </OtherInformation>
      }
      {Declaration.xml(claim)}
      {Disclaimer.xml(claim)}
      {EvidenceList.buildXml(claim)}
      {Consents.xml(claim)}
      {AssistedDecision.xml(claim)}
    </DWPCAClaim>

  }

  def displayClaimDate (date:DayMonthYear) = {
     displayPlaybackDatesFormat(Lang("en"), date)
  }

}
