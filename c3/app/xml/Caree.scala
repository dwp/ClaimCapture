package xml

import models.domain._
import xml.XMLHelper._
import controllers.Mappings.{yes,no}
import scala.xml.NodeSeq

object Caree {

  def xml(claim: Claim) = {
    val theirPersonalDetails = claim.questionGroup[TheirPersonalDetails].getOrElse(models.domain.TheirPersonalDetails())
    val theirContactDetails = claim.questionGroup[TheirContactDetails].getOrElse(TheirContactDetails())
    val moreAboutThePerson = claim.questionGroup[MoreAboutThePerson].getOrElse(MoreAboutThePerson())
    val moreAboutTheCare = claim.questionGroup[MoreAboutTheCare].getOrElse(MoreAboutTheCare())
    val representatives = claim.questionGroup[RepresentativesForPerson].getOrElse(RepresentativesForPerson())
    <Caree>
      <Surname>{theirPersonalDetails.surname}</Surname>
      <OtherNames>{theirPersonalDetails.firstName} {theirPersonalDetails.middleName.orNull}</OtherNames>
      <Title>{theirPersonalDetails.title}</Title>
      <DateOfBirth>{theirPersonalDetails.dateOfBirth.`yyyy-MM-dd`}</DateOfBirth>
      <NationalInsuranceNumber>{stringify(theirPersonalDetails.nationalInsuranceNumber)}</NationalInsuranceNumber>
      <Address>{postalAddressStructure(theirContactDetails.address, theirContactDetails.postcode.orNull)}</Address>
      <ConfirmAddress>yes</ConfirmAddress>
      <HomePhoneNumber/>
      <DaytimePhoneNumber>
        <Number>{theirContactDetails.phoneNumber.orNull}</Number>
        <Qualifier/>
      </DaytimePhoneNumber>
      <RelationToClaimant>{moreAboutThePerson.relationship}</RelationToClaimant>
      <Cared35hours>{moreAboutTheCare.spent35HoursCaring}</Cared35hours>
      <CanCareeSign>yes</CanCareeSign>
      <CanSomeoneElseSign>{representatives.someoneElseAct.answer.orNull}</CanSomeoneElseSign>
      <CanClaimantSign>{representatives.youAct.answer}</CanClaimantSign>
      <ClaimantActingType>
        <ParentOrGuardian></ParentOrGuardian>
        <PowerOfAttorney></PowerOfAttorney>
        <Appointee></Appointee>
        <JudicialFactor></JudicialFactor>
        <Receiver></Receiver>
      </ClaimantActingType>
      {breaksSinceClaim(claim)}
      {careBreak(claim)}
      <Cared35hoursBefore>{moreAboutTheCare.spent35HoursCaringBeforeClaim.answer}</Cared35hoursBefore>
      {breaksBeforeClaim(claim)}
      {dateStartedCaring(moreAboutTheCare)}
      <PaidForCaring>{moreAboutTheCare.hasSomeonePaidYou}</PaidForCaring>
      {payReceived(claim)}
      <ClaimedPreviously>{moreAboutThePerson.claimedAllowanceBefore}</ClaimedPreviously>
      {previousClaimant(claim)}
    </Caree>
  }

  def breaksSinceClaim(claim:Claim) = {
    val moreAboutTheCare = claim.questionGroup[MoreAboutTheCare].getOrElse(MoreAboutTheCare())
    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())
    val hasNotSpent35HoursCaringBeforeClaimDate = moreAboutTheCare.spent35HoursCaringBeforeClaim.answer == no

    if(hasNotSpent35HoursCaringBeforeClaimDate) {
      <BreaksSinceClaim>{if(breaksInCare.hasBreaks) yes else no}</BreaksSinceClaim>
    } else NodeSeq.Empty

  }

  def breaksBeforeClaim(claim:Claim) = {
    val moreAboutTheCare = claim.questionGroup[MoreAboutTheCare].getOrElse(MoreAboutTheCare())
    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())
    val hasSpent35HoursCaringBeforeClaimDate = moreAboutTheCare.spent35HoursCaringBeforeClaim.answer == yes

    if(hasSpent35HoursCaringBeforeClaimDate) {
      <BreaksBeforeClaim>{if(breaksInCare.hasBreaks) yes else no}</BreaksBeforeClaim>
    } else NodeSeq.Empty
  }

  def dateStartedCaring(moreAboutTheCare:MoreAboutTheCare) = {
    if(moreAboutTheCare.spent35HoursCaringBeforeClaim.answer == yes) {
      <DateStartedCaring>{stringify(moreAboutTheCare.spent35HoursCaringBeforeClaim.date)}</DateStartedCaring>
    } else NodeSeq.Empty
  }

  def careBreak(claim:Claim) = {
    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())
    for (break <- breaksInCare.breaks) yield {
      <CareBreak>
        <StartDateTime>{break.start.`yyyy-MM-dd'T'HH:mm:00`}</StartDateTime>
        <EndDateTime>{if(break.end.isDefined) break.end.get.`yyyy-MM-dd'T'HH:mm:00`}</EndDateTime>
        <Reason>{break.whereYou.location}</Reason>
        <MedicalCare>{break.medicalDuringBreak}</MedicalCare>
        <AwayFromHome>yes</AwayFromHome>
      </CareBreak>
    }
  }

  def payReceived(claim:Claim) = {
    val moreAboutTheCare = claim.questionGroup[MoreAboutTheCare].getOrElse(MoreAboutTheCare())
    val oneWhoPays = claim.questionGroup[OneWhoPaysPersonalDetails].getOrElse(OneWhoPaysPersonalDetails())
    val contactDetailsPayingPerson = claim.questionGroup[ContactDetailsOfPayingPerson].getOrElse(ContactDetailsOfPayingPerson())
    val hasReceivedPayment = moreAboutTheCare.hasSomeonePaidYou == yes

    if(hasReceivedPayment) {
      <PayReceived>
        <PayerName>{oneWhoPays.organisation.orNull} {oneWhoPays.title.orNull} {oneWhoPays.firstName.orNull} {oneWhoPays.middleName.orNull} {oneWhoPays.surname.orNull}</PayerName>
        <PayerAddress>{postalAddressStructure(contactDetailsPayingPerson.address, contactDetailsPayingPerson.postcode)}</PayerAddress>
        <ConfirmAddress>yes</ConfirmAddress>
        <Payment>{moneyStructure(oneWhoPays.amount.orNull)}</Payment>
        <DatePaymentStarted>{stringify(oneWhoPays.startDatePayment)}</DatePaymentStarted>
      </PayReceived>
    } else NodeSeq.Empty

  }

  def previousClaimant(claim:Claim) = {
    val moreAboutThePerson = claim.questionGroup[MoreAboutThePerson].getOrElse(MoreAboutThePerson())
    val previousCarerPersonalDetails = claim.questionGroup[PreviousCarerPersonalDetails].getOrElse(PreviousCarerPersonalDetails())
    val previousCarerContactDetails = claim.questionGroup[PreviousCarerContactDetails].getOrElse(PreviousCarerContactDetails())
    val claimedAllowanceBefore = moreAboutThePerson.claimedAllowanceBefore == yes

    if(claimedAllowanceBefore) {
      <PreviousClaimant>
        <Surname>{previousCarerPersonalDetails.surname.orNull}</Surname>
        <OtherNames>{previousCarerPersonalDetails.firstName.orNull} {previousCarerPersonalDetails.middleName.orNull}</OtherNames>
        <DateOfBirth>{stringify(previousCarerPersonalDetails.dateOfBirth)}</DateOfBirth>
        <NationalInsuranceNumber>{stringify(previousCarerPersonalDetails.nationalInsuranceNumber)}</NationalInsuranceNumber>
        <Address>{postalAddressStructure(previousCarerContactDetails.address, previousCarerContactDetails.postcode)}</Address>
      </PreviousClaimant>
    } else NodeSeq.Empty
  }

}
