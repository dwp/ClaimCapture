package xml

import app.XMLValues._
import play.api.Logger
import models.domain.{MoreAboutYou, Claim}
import xml.XMLHelper._

object DWPCAClaim {

  def xml(claim: Claim, transactionId : String) = {

    val moreAboutYou = claim.questionGroup[MoreAboutYou].getOrElse(MoreAboutYou(beenInEducationSinceClaimDate = no))

    val employment = claim.questionGroup[models.domain.Employment].getOrElse(models.domain.Employment(beenEmployedSince6MonthsBeforeClaim = no, beenSelfEmployedSince1WeekBeforeClaim = no))

    val additionalInfo = claim.questionGroup[models.domain.AdditionalInfo].getOrElse(models.domain.AdditionalInfo())

    val havePartner = if(moreAboutYou.maritalStatus == "s") NotAsked else if(moreAboutYou.maritalStatus == "p") yes else moreAboutYou.hadPartnerSinceClaimDate.get

    Logger.info(s"Build DWPCAClaim : $transactionId")

    <DWPCAClaim id={transactionId}>
      {Claimant.xml(claim)}
      {Caree.xml(claim)}
      <ClaimADI>{no}</ClaimADI>
      {Residency.xml(claim)}
      <CourseOfEducation>{moreAboutYou.beenInEducationSinceClaimDate}</CourseOfEducation>
      {FullTimeEducation.xml(claim)}
      <SelfEmployed>{employment.beenSelfEmployedSince1WeekBeforeClaim}</SelfEmployed>
      {SelfEmployment.xml(claim)}
      <Employed>{employment.beenEmployedSince6MonthsBeforeClaim}</Employed>
      {Employment.xml(claim)}
      {PropertyRentedOut.xml(claim)}
      <HavePartner>{havePartner}</HavePartner>
      {Partner.xml(claim)}
      {OtherBenefits.xml(claim)}
      {Payment.xml(claim)}
      {<OtherInformation/> +++ additionalInfo.anythingElse.text}
      <ThirdParty>{no}</ThirdParty>
      {Declaration.xml(claim)}
      {EvidenceList.xml(claim)}
    </DWPCAClaim>
  }
}
