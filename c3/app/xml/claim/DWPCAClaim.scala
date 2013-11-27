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
      {question(<DateOfClaim/>, "dateOfClaim",claim.dateOfClaim)}
      {Claimant.xml(claim)}
      {Caree.xml(claim)}
      {Residency.xml(claim)}
      {question(<CourseOfEducation/>, "beenInEducationSinceClaimDate.label",moreAboutYou.beenInEducationSinceClaimDate)}
      {FullTimeEducation.xml(claim)}
      {question(<SelfEmployed/>, "beenSelfEmployedSince1WeekBeforeClaim.label",employment.beenSelfEmployedSince1WeekBeforeClaim, claim.dateOfClaim.fold("")(_.`dd/MM/yyyy`))}
      {SelfEmployment.xml(claim)}
      {question(<Employed/>,"beenEmployedSince6MonthsBeforeClaim.label",employment.beenEmployedSince6MonthsBeforeClaim, claim.dateOfClaim.fold("{CLAIM DATE - 6 months}")(dmy => (dmy - 6 months).`dd/MM/yyyy`), claim.dateOfClaim.fold("{CLAIM DATE}")(_.`dd/MM/yyyy`))}
      {Employment.xml(claim)}
      {question(<HavePartner/>,"hadPartnerSinceClaimDate.label",moreAboutYou.hadPartnerSinceClaimDate,claim.dateOfClaim.fold("")(_.`dd/MM/yyyy`))}
      {Partner.xml(claim)}
      {OtherBenefits.xml(claim)}
      {Payment.xml(claim)}
      {
        <OtherInformation>
          {question(<WelshCommunication/>,"welshCommunication",additionalInfo.welshCommunication)}
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
