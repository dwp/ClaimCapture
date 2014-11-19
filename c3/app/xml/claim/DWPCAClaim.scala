package xml.claim

import app.XMLValues._
import play.api.Logger
import models.domain.{YourPartnerPersonalDetails, YourCourseDetails, Claim}
import xml.XMLHelper._
import xml._
import scala.language.postfixOps

object DWPCAClaim extends XMLComponent {

  def xml(claim: Claim) = {

    val courseDetails = claim.questionGroup[YourCourseDetails].getOrElse(YourCourseDetails(beenInEducationSinceClaimDate = no))
    val employment = claim.questionGroup[models.domain.Employment].getOrElse(models.domain.Employment(beenEmployedSince6MonthsBeforeClaim = no, beenSelfEmployedSince1WeekBeforeClaim = no))
    val additionalInfo = claim.questionGroup[models.domain.AdditionalInfo].getOrElse(models.domain.AdditionalInfo())
    val claimDate = claim.dateOfClaim.fold("")(_.`dd/MM/yyyy`)
    val yourPartnerPersonalDetails = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())
    val havePartner = yourPartnerPersonalDetails.hadPartnerSinceClaimDate

    Logger.info(s"Build XML DWPCAClaim for : ${claim.key} ${claim.uuid}.")

    <DWPCAClaim>
      {question(<DateOfClaim/>, "dateOfClaim",claim.dateOfClaim)}
      {Claimant.xml(claim)}
      {Caree.xml(claim)}
      {Residency.xml(claim)}
      {question(<CourseOfEducation/>, "beenInEducationSinceClaimDate.label",courseDetails.beenInEducationSinceClaimDate, claimDate)}
      {FullTimeEducation.xml(claim)}
      {question(<SelfEmployed/>, "beenSelfEmployedSince1WeekBeforeClaim.label",employment.beenSelfEmployedSince1WeekBeforeClaim, claim.dateOfClaim.fold("{CLAIM DATE - 1 week}")(dmy => (dmy - 1 week).`dd/MM/yyyy`),claimDate)}
      {SelfEmployment.xml(claim)}
      {question(<Employed/>,"aboutYou_beenEmployedSince6MonthsBeforeClaim.label",employment.beenEmployedSince6MonthsBeforeClaim, claim.dateOfClaim.fold("{CLAIM DATE - 6 months}")(dmy => (dmy - 6 months).`dd/MM/yyyy`), claimDate)}
      {Employment.xml(claim)}
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
}
