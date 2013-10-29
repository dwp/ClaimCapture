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
      {theirPersonalDetails.nationalInsuranceNumber match {
        case Some(n) => <NationalInsuranceNumber>{stringify(theirPersonalDetails.nationalInsuranceNumber)}</NationalInsuranceNumber>
        case None => NodeSeq.Empty
      }}
      {postalAddressStructure(theirContactDetails.address, theirContactDetails.postcode.orNull)}
      {if(!theirContactDetails.phoneNumber.isEmpty){
        <DayTimePhoneNumber>{theirContactDetails.phoneNumber.orNull}</DayTimePhoneNumber>
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
      <!--{breaksBeforeClaim(claim)}-->

      <LiveSameAddress>
        <QuestionLabel>Do they live at the same address as you?</QuestionLabel>
        <Answer>{theirPersonalDetails.liveAtSameAddressCareYouProvide match {
          case "yes" => XMLValues.Yes
          case "no" => XMLValues.No
          case n => n
        }}</Answer>
      </LiveSameAddress>

      {moreAboutThePerson.armedForcesPayment match {
        case Some(n) => {<ArmedForcesIndependencePayment>
          <QuestionLabel>Does this person get Armed Forces Independence Payment?</QuestionLabel>
          <Answer>{n match {
            case "yes" => XMLValues.Yes
            case "no" => XMLValues.No
            case n => n
          }}</Answer>
        </ArmedForcesIndependencePayment>}
        case _ => NodeSeq.Empty
      }}
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
        <DateStartCaring>
          <QuestionLabel>DateStartedCaring?</QuestionLabel>
          <Answer>{stringify(moreAboutTheCare.spent35HoursCaringBeforeClaim.date)}</Answer>
        </DateStartCaring>
    } else NodeSeq.Empty
  }

  def careBreak(claim: Claim) = {
    import models.DayMonthYear._

    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())

    for (break <- breaksInCare.breaks) yield {
      <CareBreak>
        <StartDateTime>{break.start.`dd-MM-yyyy HH:mm`}</StartDateTime>
        {break.end match {
          case Some(n) => <EndDateTime>{break.end.get.`dd-MM-yyyy HH:mm`}</EndDateTime>
          case None => NodeSeq.Empty
        }}
         <MedicalCare>
             <QuestionLabel>Did you or the person you care for receive any medical treatment or professional care during the break?</QuestionLabel>
             <Answer>{break.medicalDuringBreak match {
                case "yes" => XMLValues.Yes
                case "no" => XMLValues.No
                case n => n
            }}</Answer>
        </MedicalCare>
      </CareBreak>
    }
  }
}