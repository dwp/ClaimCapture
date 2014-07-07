package xml.claim

import scala.xml.NodeSeq
import app.XMLValues._
import models.domain._
import xml.XMLHelper._

object Caree {

  def xml(claim: Claim) = {
    val theirPersonalDetails = claim.questionGroup[TheirPersonalDetails].getOrElse(TheirPersonalDetails())
    val theirContactDetails = claim.questionGroup[TheirContactDetails].getOrElse(TheirContactDetails())
    val moreAboutTheCare = claim.questionGroup[MoreAboutTheCare].getOrElse(MoreAboutTheCare())

    <Caree>
      <Surname>{theirPersonalDetails.surname}</Surname>
      <OtherNames>{theirPersonalDetails.firstName} {theirPersonalDetails.middleName.orNull}</OtherNames>
      <Title>{theirPersonalDetails.title}</Title>
      <DateOfBirth>{theirPersonalDetails.dateOfBirth.`yyyy-MM-dd`}</DateOfBirth>
      <NationalInsuranceNumber>{stringify(theirPersonalDetails.nationalInsuranceNumber)}</NationalInsuranceNumber>
      <Address>{postalAddressStructure(theirContactDetails.address, theirContactDetails.postcode.orNull)}</Address>
      <ConfirmAddress>{yes}</ConfirmAddress>
      <HomePhoneNumber/>
      <DaytimePhoneNumber>
        <Number>{theirContactDetails.phoneNumber.orNull}</Number>
        <Qualifier/>
      </DaytimePhoneNumber>
      <RelationToClaimant>{other}</RelationToClaimant>
      <Cared35hours>{NotAsked}</Cared35hours>
      <CanCareeSign>{NotAsked}</CanCareeSign>
      <CanSomeoneElseSign>{NotAsked}</CanSomeoneElseSign>
      <CanClaimantSign>{NotAsked}</CanClaimantSign>
      {breaksSinceClaim(claim)}
      {careBreak(claim)}
      <Cared35hoursBefore>{NotAsked}</Cared35hoursBefore>
      {dateStartedCaring(moreAboutTheCare)}
      {breaksBeforeClaim(claim)}
      <PaidForCaring>{NotAsked}</PaidForCaring>
      <ClaimedPreviously>{NotAsked}</ClaimedPreviously>
    </Caree>
  }

  def breaksSinceClaim(claim: Claim) = {
    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())
    <BreaksSinceClaim>{if (breaksInCare.hasBreaks) yes else no}</BreaksSinceClaim>
  }

  def breaksBeforeClaim(claim: Claim) = {
    val moreAboutTheCare = claim.questionGroup[MoreAboutTheCare].getOrElse(MoreAboutTheCare())
    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())
    val hasSpent35HoursCaringBeforeClaimDate = moreAboutTheCare.spent35HoursCaringBeforeClaim.answer == yes

    if (hasSpent35HoursCaringBeforeClaimDate) {
      <BreaksBeforeClaim>{if (breaksInCare.hasBreaks) yes else no}</BreaksBeforeClaim>
    } else NodeSeq.Empty
  }

  def dateStartedCaring(moreAboutTheCare: MoreAboutTheCare) = {
    val startedCaringBeforeClaimDate = moreAboutTheCare.spent35HoursCaringBeforeClaim.answer == yes

    if (startedCaringBeforeClaimDate) {
      <DateStartedCaring>{stringify(moreAboutTheCare.spent35HoursCaringBeforeClaim.date)}</DateStartedCaring>
    } else NodeSeq.Empty
  }

  def careBreak(claim: Claim) = {

    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())

    for (break <- breaksInCare.breaks) yield {
      <CareBreak>
        <StartDateTime>{break.start.`yyyy-MM-dd'T'HH:mm:00`}</StartDateTime>
        <EndDateTime>{if (break.end.isDefined) break.end.get.`yyyy-MM-dd'T'HH:mm:00`}</EndDateTime>
        <Reason>{break.whereYou.location}</Reason>
        <MedicalCare>{break.medicalDuringBreak}</MedicalCare>
        <AwayFromHome>{NotAsked}</AwayFromHome>
      </CareBreak>
    }
  }
}