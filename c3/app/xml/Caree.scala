package xml

import app.XMLValues
import scala.xml.NodeSeq
import app.XMLValues._
import models.domain._
import xml.XMLHelper._

object Caree {

  def xml(claim: Claim) = {
    val theirPersonalDetails = claim.questionGroup[TheirPersonalDetails].getOrElse(TheirPersonalDetails())
    val theirContactDetails = claim.questionGroup[TheirContactDetails].getOrElse(TheirContactDetails())
    val moreAboutThePerson = claim.questionGroup[MoreAboutThePerson].getOrElse(MoreAboutThePerson())
    val moreAboutTheCare = claim.questionGroup[MoreAboutTheCare].getOrElse(MoreAboutTheCare())

    <Caree>
      <Surname>{theirPersonalDetails.surname}</Surname>
      <OtherNames>{theirPersonalDetails.firstName} {theirPersonalDetails.middleName.orNull}</OtherNames>
      <Title>{theirPersonalDetails.title}</Title>
      <DateOfBirth>{theirPersonalDetails.dateOfBirth.`dd-MM-yyyy`}</DateOfBirth>
      <NationalInsuranceNumber>{stringify(theirPersonalDetails.nationalInsuranceNumber)}</NationalInsuranceNumber>
      <Address>{postalAddressStructure(theirContactDetails.address, theirContactDetails.postcode.orNull)}</Address>
      {if(!theirContactDetails.phoneNumber.isEmpty){
      <DayTimePhoneNumber>
        <Number>{theirContactDetails.phoneNumber.orNull}</Number>
        <Qualifier/>
      </DayTimePhoneNumber>
      }}
      <RelationToClaimant>
        <QuestionLabel>What's their relationship to you?</QuestionLabel>
        <Answer>{moreAboutThePerson.relationship}</Answer>
      </RelationToClaimant>
      <Cared35Hours>
        <QuestionLabel>Do you spend 35 hours or more each week caring for this person?</QuestionLabel>
        <Answer>{moreAboutTheCare.spent35HoursCaring match {
          case "yes" => XMLValues.Yes
          case "no" => XMLValues.No
          case n => n
        }}</Answer>
      </Cared35Hours>
      {breaksSinceClaim(claim)}
      {careBreak(claim)}

      <Cared35HoursBefore>
        <QuestionLabel>spent35HoursCaringBeforeClaim?</QuestionLabel>
        <Answer>{moreAboutTheCare.spent35HoursCaringBeforeClaim.answer match {
          case "yes" => XMLValues.Yes
          case "no" => XMLValues.No
          case n => n
        }}</Answer>
      </Cared35HoursBefore>

      {dateStartedCaring(moreAboutTheCare)}
      {breaksBeforeClaim(claim)}
    </Caree>
  }

  def breaksSinceClaim(claim: Claim) = {
    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())

    <BreaksSinceClaim>
      <QuestionLabel>Have you had any breaks in caring for this person since claim date?</QuestionLabel>
      <Answer>{if (breaksInCare.hasBreaks) Yes else No}</Answer>
    </BreaksSinceClaim>
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
    import models.DayMonthYear._

    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())

    for (break <- breaksInCare.breaks) yield {
      <CareBreak>
        <StartDateTime>{break.start.`yyyy-MM-dd'T'HH:mm:00`}</StartDateTime>
        <EndDateTime>{if (break.end.isDefined) break.end.get.`yyyy-MM-dd'T'HH:mm:00`}</EndDateTime>
        <Reason>{break.whereYou.location}</Reason>
        <MedicalCare>{break.medicalDuringBreak}</MedicalCare>
      </CareBreak>
    }
  }
}