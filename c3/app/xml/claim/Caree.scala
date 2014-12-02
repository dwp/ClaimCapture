package xml.claim

import controllers.Mappings
import models.domain._
import xml.XMLComponent
import xml.XMLHelper._
import scala.language.postfixOps
import utils.helpers.HtmlLabelHelper.displayPlaybackDatesFormat

import scala.xml.NodeSeq
import play.api.i18n.Lang

object Caree extends XMLComponent {

  def xml(claim: Claim) = {
    val theirPersonalDetails = claim.questionGroup[TheirPersonalDetails].getOrElse(TheirPersonalDetails())
    val theirContactDetails = claim.questionGroup[TheirContactDetails].getOrElse(TheirContactDetails())
    val moreAboutTheCare = claim.questionGroup[MoreAboutTheCare].getOrElse(MoreAboutTheCare())
    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())

    <Caree>
      {question(<Surname/>, "surname", encrypt(theirPersonalDetails.surname))}
      {question(<OtherNames/>, "firstName", theirPersonalDetails.firstName)}
      {question(<MiddleNames/>, "middleName", theirPersonalDetails.middleName)}
      {question(<Title/>, "title", theirPersonalDetails.title)}
      {question(<DateOfBirth/>, "dateOfBirth", theirPersonalDetails.dateOfBirth.`dd-MM-yyyy`)}
      {question(<NationalInsuranceNumber/>,"nationalInsuranceNumber", encrypt(theirPersonalDetails.nationalInsuranceNumber.getOrElse("")))}
      {postalAddressStructure("address", theirContactDetails.address, encrypt(theirContactDetails.postcode.getOrElse("").toUpperCase))}
      {question(<RelationToClaimant/>,"relationship", theirPersonalDetails.relationship)}
      {question(<Cared35Hours/>,"hours.answer", moreAboutTheCare.spent35HoursCaring)}
      {careBreak(claim)}
      {question(<Cared35HoursBefore/>,"spent35HoursCaringBeforeClaim.label", moreAboutTheCare.spent35HoursCaringBeforeClaim.answer, claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy => displayPlaybackDatesFormat(Lang("en"),dmy)))}
      {if(moreAboutTheCare.spent35HoursCaringBeforeClaim.date.isDefined){
        {question(<DateStartCaring/>,"beforeClaimCaring_date", moreAboutTheCare.spent35HoursCaringBeforeClaim.date)}
      }}
      {question(<LiveSameAddress/>,"liveAtSameAddressCareYouProvide", theirPersonalDetails.liveAtSameAddressCareYouProvide)}
      {question(<ArmedForcesIndependencePayment/>,"armedForcesPayment", theirPersonalDetails.armedForcesPayment)}
    </Caree>
  }

  private def careBreak(claim: Claim) = {
    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())

    def breaksInCareLabel (label:String, answer:Boolean) = {
      val date = claim.questionGroup[MoreAboutTheCare] match {
        case Some(moreAboutTheCare) if moreAboutTheCare.spent35HoursCaringBeforeClaim.answer == Mappings.yes =>  claim.dateOfClaim.get - 6 months
        case _ => claim.dateOfClaim.get
      }

      question(<BreaksSinceClaim/>, label, answer, displayPlaybackDatesFormat(Lang("en"),date))
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