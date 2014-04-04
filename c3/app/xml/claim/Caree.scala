package xml.claim

import scala.xml.NodeSeq
import models.domain._
import xml.XMLHelper._
import xml.XMLComponent

object Caree extends XMLComponent {

  def xml(claim: Claim) = {
    val theirPersonalDetails = claim.questionGroup[TheirPersonalDetails].getOrElse(TheirPersonalDetails())
    val theirContactDetails = claim.questionGroup[TheirContactDetails].getOrElse(TheirContactDetails())
    val moreAboutThePerson = claim.questionGroup[MoreAboutThePerson].getOrElse(MoreAboutThePerson())
    val moreAboutTheCare = claim.questionGroup[MoreAboutTheCare].getOrElse(MoreAboutTheCare())
    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())

    <Caree>
      {question(<Surname/>, "surname", theirPersonalDetails.surname)}
      {question(<OtherNames/>, "firstName", theirPersonalDetails.firstName+" "+ theirPersonalDetails.middleName.getOrElse(""))}
      {question(<Title/>, "title", theirPersonalDetails.title)}
      {question(<DateOfBirth/>, "dateOfBirth", theirPersonalDetails.dateOfBirth.`dd-MM-yyyy`)}
      {question(<NationalInsuranceNumber/>,"nationalInsuranceNumber", theirPersonalDetails.nationalInsuranceNumber)}
      {postalAddressStructure("address", theirContactDetails.address, theirContactDetails.postcode)}
      {question(<DayTimePhoneNumber/>,"phoneNumber", theirContactDetails.phoneNumber)}
      {question(<RelationToClaimant/>,"relationship", moreAboutThePerson.relationship)}
      {question(<Cared35Hours/>,"hours.answer", moreAboutTheCare.spent35HoursCaring)}
      {question(<BreaksSinceClaim/>,"answer.label",breaksInCare.hasBreaks,claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))}
      {careBreak(claim)}
      {question(<Cared35HoursBefore/>,"beforeClaimCaring.answer", moreAboutTheCare.spent35HoursCaringBeforeClaim.answer)}
      {question(<DateStartCaring/>,"beforeClaimCaring_date", moreAboutTheCare.spent35HoursCaringBeforeClaim.date)}
      {question(<LiveSameAddress/>,"liveAtSameAddressCareYouProvide", theirPersonalDetails.liveAtSameAddressCareYouProvide)}
      {question(<ArmedForcesIndependencePayment/>,"armedForcesPayment", moreAboutThePerson.armedForcesPayment)}
    </Caree>
  }

  private def careBreak(claim: Claim) = {
    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())

    for (break <- breaksInCare.breaks) yield {
      <CareBreak>
        {question(<StartDateTime/>, "start", break.start.`dd-MM-yyyy HH:mm`)}
        {break.end match {
          case Some(n) => {question(<EndDateTime/>,"end", break.end.get.`dd-MM-yyyy HH:mm`)}
          case None => NodeSeq.Empty
        }}
        {question(<MedicalCare/>,"medicalDuringBreak", break.medicalDuringBreak)}
        {questionOther(<ReasonClaimant/>,"whereYou", break.whereYou.location, break.whereYou.other)}
        {questionOther(<ReasonCaree/>,"wherePerson", break.wherePerson.location, break.wherePerson.other)}
      </CareBreak>
    }
  }
}