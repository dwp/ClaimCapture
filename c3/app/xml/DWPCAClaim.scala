package xml

import app.XMLValues
import app.XMLValues._
import play.api.Logger
import models.domain.{MoreAboutYou, Claim}
import xml.XMLHelper._
import scala.xml.NodeSeq

object DWPCAClaim {

  def xml(claim: Claim, transactionId : String) = {

    val moreAboutYou = claim.questionGroup[MoreAboutYou].getOrElse(MoreAboutYou(beenInEducationSinceClaimDate = no))

    val employment = claim.questionGroup[models.domain.Employment].getOrElse(models.domain.Employment(beenEmployedSince6MonthsBeforeClaim = no, beenSelfEmployedSince1WeekBeforeClaim = no))

    val additionalInfo = claim.questionGroup[models.domain.AdditionalInfo].getOrElse(models.domain.AdditionalInfo())

    Logger.info(s"Build DWPCAClaim : $transactionId")

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
        <QuestionLabel>Employed?</QuestionLabel>
        <Answer>{employment.beenEmployedSince6MonthsBeforeClaim match {
          case "yes" => XMLValues.Yes
          case "no" => XMLValues.No
          case n => n
        }}</Answer>
      </Employed>
      {Employment.xml(claim)}
      <!--{PropertyRentedOut.xml(claim)}-->
      <HavePartner>
        <QuestionLabel>HavePartner?</QuestionLabel>
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
          <WelshCommunication>
            <QuestionLabel>welsh.communication</QuestionLabel>
            <Answer>{additionalInfo.welshCommunication match {
              case "yes" => XMLValues.Yes
              case "no" => XMLValues.No
              case n => n
            }}</Answer>
          </WelshCommunication>
          {additionalInfo.anythingElse match {
            case Some(n) => {
              <AdditionalInformation>
                <QuestionLabel>anything.else</QuestionLabel>
                <Answer>{additionalInfo.anythingElse}</Answer>
              </AdditionalInformation>
            }
            case None => NodeSeq.Empty
        }}
        </OtherInformation>
      }
      {Declaration.xml(claim)}
      {EvidenceList.buildXml(claim)}
    </DWPCAClaim>
  }
}
