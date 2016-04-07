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
import scala.xml.NodeSeq

object DWPCAClaim extends XMLComponent {

  def xml(claim: Claim) = {

    val courseDetails = claim.questionGroup[YourCourseDetails].getOrElse(YourCourseDetails(beenInEducationSinceClaimDate = no))
    val employment = claim.questionGroup[models.domain.YourIncomes].getOrElse(models.domain.YourIncomes(beenEmployedSince6MonthsBeforeClaim = no, beenSelfEmployedSince1WeekBeforeClaim = no))
    val additionalInfo = claim.questionGroup[models.domain.AdditionalInfo].getOrElse(models.domain.AdditionalInfo())
    val claimDate = claim.dateOfClaim.fold("")(_.`dd/MM/yyyy`)
    val yourPartnerPersonalDetails = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())
    val havePartner = yourPartnerPersonalDetails.hadPartnerSinceClaimDate
    val qualifyingBenefit = claim.questionGroup[Benefits].getOrElse(Benefits())
    val empAdditionalInfo = claim.questionGroup[EmploymentAdditionalInfo].getOrElse(EmploymentAdditionalInfo())


    Logger.info(s"Build XML for: ${claim.key} ${claim.uuid}.")

    <DWPCAClaim>
      {question(<DateOfClaim/>, "dateOfClaim",claim.dateOfClaim)}
      {question(<QualifyingBenefit/>, "benefitsAnswer",qualifyingBenefit.benefitsAnswer)}
      {Claimant.xml(claim)}
      {Caree.xml(claim)}
      {Residency.xml(claim)}
      {question(<CourseOfEducation/>, "beenInEducationSinceClaimDate.label",courseDetails.beenInEducationSinceClaimDate, claimDate)}
      {FullTimeEducation.xml(claim)}
      {Incomes.xml(claim)}
      {if(!empAdditionalInfo.empAdditionalInfo.answer.isEmpty) questionOther(<EmploymentAdditionalInfo/>, "empAdditionalInfo.answer", empAdditionalInfo.empAdditionalInfo.answer, empAdditionalInfo.empAdditionalInfo.text)}
      {question(<HavePartner/>,"hadPartnerSinceClaimDate",havePartner,claimDate)}
      {Partner.xml(claim)}
      {OtherBenefits.xml(claim)}
      {Payment.xml(claim)}
      {<OtherInformation>
        {question(<WelshCommunication/>,"welshCommunication",additionalInfo.welshCommunication)}
        {questionWhy(<AdditionalInformation/>,"anythingElse.answer", additionalInfo.anythingElse.answer, additionalInfo.anythingElse.text,"anythingElse.text")}
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

  def fromXml(xml: NodeSeq, claim: Claim) : Claim = {
    var newClaim = AssistedDecision.fromXml(xml, claim)
    newClaim = Caree.fromXml(xml, newClaim)
    newClaim = Claimant.fromXml(xml, newClaim)
    newClaim = Consents.fromXml(xml, newClaim)
    newClaim = Declaration.fromXml(xml, newClaim)
    newClaim = Disclaimer.fromXml(xml, newClaim)
    newClaim = Employment.fromXml(xml, newClaim)
    newClaim = FullTimeEducation.fromXml(xml, newClaim)
    newClaim = OtherBenefits.fromXml(xml, newClaim)
    newClaim = Partner.fromXml(xml, newClaim)
    newClaim = Payment.fromXml(xml, newClaim)
    newClaim = Residency.fromXml(xml, newClaim)
    newClaim = SelfEmployment.fromXml(xml, newClaim)
    newClaim
  }
}
