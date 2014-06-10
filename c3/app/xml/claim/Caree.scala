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
      {question(<RelationToClaimant/>,"relationship", theirPersonalDetails.relationship)}
      {question(<Cared35Hours/>,"hours.answer", moreAboutTheCare.spent35HoursCaring)}
      {careBreak(claim)}
      {question(<Cared35HoursBefore/>,"beforeClaimCaring.answer", moreAboutTheCare.spent35HoursCaringBeforeClaim.answer)}
      {question(<DateStartCaring/>,"beforeClaimCaring_date", moreAboutTheCare.spent35HoursCaringBeforeClaim.date)}
      {question(<LiveSameAddress/>,"liveAtSameAddressCareYouProvide", theirPersonalDetails.liveAtSameAddressCareYouProvide)}
      {question(<ArmedForcesIndependencePayment/>,"armedForcesPayment", theirPersonalDetails.armedForcesPayment)}
    </Caree>
  }

  private def careBreak(claim: Claim) = {
    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())

    def breaksInCareLabel (label:String, answer:Boolean) = {
      question(<BreaksSinceClaim/>, label, answer, claim.dateOfClaim.get.`dd/MM/yyyy`)
    }

    val xmlNoBreaks = {
      <CareBreak>
        {if (breaksInCare.breaks.size > 0){
          {breaksInCareLabel("answer.more.label", false)}
        } else {
          {breaksInCareLabel("answer.label", false)}
        }}
      </CareBreak>
    }

    {for ((break, index) <- breaksInCare.breaks.zipWithIndex) yield {
      <CareBreak>
        {index > 0 match {
          case true =>  breaksInCareLabel("answer.more.label", true)
          case false => breaksInCareLabel("answer.label", true)
        }}
        {(break.start.hour, break.start.minutes) match {
            case (Some(h), Some(m)) => {question(<StartDate/>, "start", break.start.`dd-MM-yyyy`) ++
                                        question(<StartTime/>, "start.time", break.start.`HH:mm`)
                                       }
            case _ => question(<StartDate/>, "start", break.start.`dd-MM-yyyy`)
          }
        }
        {break.end match {
          case Some(n) => {
            (n.hour, n.minutes) match {
              case (Some(h), Some(m)) => {
                                          question(<EndDate/>,"end", break.end.get.`dd-MM-yyyy`) ++
                                          question(<EndTime/>, "end.time", break.end.get.`HH:mm`)
                                         }
              case _ => question(<EndDate/>,"end", break.end.get.`dd-MM-yyyy`)
            }
          }
          case None => NodeSeq.Empty
        }}
        {question(<MedicalCare/>,"medicalDuringBreak", break.medicalDuringBreak)}
        {questionOther(<ReasonClaimant/>,"whereYou", break.whereYou.location, break.whereYou.other)}
        {questionOther(<ReasonCaree/>,"wherePerson", break.wherePerson.location, break.wherePerson.other)}
      </CareBreak>
    }} ++ xmlNoBreaks
  }
}