package xml.claim

import app.XMLValues
import app.XMLValues._
import play.api.Logger
import models.domain.MoreAboutYou
import xml.XMLHelper._
import scala.xml.NodeSeq
import xml._
import models.domain.Claim
import scala.Some
import play.api.i18n.Messages
import scala.language.postfixOps

object DWPCAClaim extends XMLComponent {

  def xml(claim: Claim) = {

    val moreAboutYou = claim.questionGroup[MoreAboutYou].getOrElse(MoreAboutYou(beenInEducationSinceClaimDate = no))

    val employment = claim.questionGroup[models.domain.Employment].getOrElse(models.domain.Employment(beenEmployedSince6MonthsBeforeClaim = no, beenSelfEmployedSince1WeekBeforeClaim = no))

    val additionalInfo = claim.questionGroup[models.domain.AdditionalInfo].getOrElse(models.domain.AdditionalInfo())

    Logger.info(s"Build DWPCAClaim")

    <DWPCAClaim>
      <DateOfClaim>
        <QuestionLabel>When do you want your Carer's Allowance claim to start?</QuestionLabel>
        <Answer>{stringify(claim.dateOfClaim)}</Answer>
      </DateOfClaim>
      {Claimant.xml(claim)}
      {Caree.xml(claim)}
      {Residency.xml(claim)}
      <CourseOfEducation>
        <QuestionLabel>CourseOfEducation?</QuestionLabel>
        <Answer>{moreAboutYou.beenInEducationSinceClaimDate match {
          case "yes" => XMLValues.Yes
          case "no" => XMLValues.No
          case n => n
        }}</Answer>
      </CourseOfEducation>
      {FullTimeEducation.xml(claim)}
      <SelfEmployed>
        <QuestionLabel>SelfEmployed?</QuestionLabel>
        <Answer>{employment.beenSelfEmployedSince1WeekBeforeClaim match {
          case "yes" => XMLValues.Yes
          case "no" => XMLValues.No
          case n => n
        }}</Answer>
      </SelfEmployed>
      {SelfEmployment.xml(claim)}
      <Employed>
        <QuestionLabel>{Messages("beenEmployedSince6MonthsBeforeClaim.label", claim.dateOfClaim.fold("{CLAIM DATE - 6 months}")(dmy => (dmy - 6 months).`dd/MM/yyyy`), claim.dateOfClaim.fold("{CLAIM DATE}")(_.`dd/MM/yyyy`))}</QuestionLabel>
        <Answer>{employment.beenEmployedSince6MonthsBeforeClaim match {
          case "yes" => XMLValues.Yes
          case "no" => XMLValues.No
          case n => n
        }}</Answer>
      </Employed>
      {Employment.xml(claim)}
      <HavePartner>
        <QuestionLabel>{Messages("hadPartnerSinceClaimDate.label", claim.dateOfClaim.fold("")(_.`dd/MM/yyyy`))}</QuestionLabel>
        <Answer>{moreAboutYou.hadPartnerSinceClaimDate match {
          case "yes" => XMLValues.Yes
          case "no" => XMLValues.No
          case n => n
        }}</Answer>
      </HavePartner>

      {Partner.xml(claim)}
      {OtherBenefits.xml(claim)}
      {Payment.xml(claim)}
      {
        <OtherInformation>
          {question(<WelshCommunication/>,"welshCommunication", titleCase(booleanStringToYesNo(additionalInfo.welshCommunication)))}
          {question(<AdditionalInformation/>,"anythingElse", additionalInfo.anythingElse)}
        </OtherInformation>
      }
      {Declaration.xml(claim)}
      {Disclaimer.xml(claim)}
      {EvidenceList.buildXml(claim)}
      {Consents.xml(claim)}
    </DWPCAClaim>

  }
}
