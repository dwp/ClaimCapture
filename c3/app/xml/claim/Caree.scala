package xml.claim

import scala.xml.NodeSeq
import app.XMLValues._
import models.domain._
import xml.XMLHelper._
import xml.XMLComponent

object Caree extends XMLComponent {

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
      {question(<RelationToClaimant/>,"whatRelationIsToYou", moreAboutThePerson.relationship)}
      {question(<Cared35Hours/>,"hours.answer", moreAboutTheCare.spent35HoursCaring)}
      {breaksSinceClaim(claim)}
      {careBreak(claim)}
      {question(<Cared35HoursBefore/>,"beforeClaimCaring.answer", moreAboutTheCare.spent35HoursCaringBeforeClaim.answer)}
      {dateStartedCaring(moreAboutTheCare)}
      <!--{breaksBeforeClaim(claim)}-->
      {question(<LiveSameAddress/>,"liveAtSameAddressCareYouProvide", theirPersonalDetails.liveAtSameAddressCareYouProvide)}
      {question(<ArmedForcesIndependencePayment/>,"armedForcesPayment", moreAboutThePerson.armedForcesPayment)}
    </Caree>
  }

  def breaksSinceClaim(claim: Claim) = {
    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())
    question(<BreaksSinceClaim/>,"answer.label",breaksInCare.hasBreaks)
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
      {question(<DateStartCaring/>,"beforeClaimCaring_date", stringify(moreAboutTheCare.spent35HoursCaringBeforeClaim.date))}
    } else NodeSeq.Empty
  }

  def careBreak(claim: Claim) = {

    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())

    for (break <- breaksInCare.breaks) yield {
      <CareBreak>
        <StartDateTime>{break.start.`dd-MM-yyyy HH:mm`}</StartDateTime>
        {break.end match {
          case Some(n) => <EndDateTime>{break.end.get.`dd-MM-yyyy HH:mm`}</EndDateTime>
          case None => NodeSeq.Empty
        }}
        {question(<MedicalCare/>,"medicalDuringBreak", break.medicalDuringBreak)}
        {questionOther(<ReasonClaimant/>,"whereYou", break.whereYou.location, break.whereYou.other)}

        {questionOther(<ReasonCaree/>,"wherePerson", break.wherePerson.location, break.wherePerson.other)}
      </CareBreak>
    }
  }
}