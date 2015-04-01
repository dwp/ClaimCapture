package xml.claim

import controllers.mappings.Mappings
import models.domain._
import xml.XMLComponent
import xml.XMLHelper._
import scala.language.postfixOps
import utils.helpers.HtmlLabelHelper.displayPlaybackDatesFormat

import scala.xml.NodeSeq
import play.api.i18n.{MMessages, Lang}

object Caree extends XMLComponent {

  def xml(claim: Claim) = {
    val theirPersonalDetails = claim.questionGroup[TheirPersonalDetails].getOrElse(TheirPersonalDetails())
    val theirContactDetails = claim.questionGroup[TheirContactDetails].getOrElse(TheirContactDetails())
    val moreAboutTheCare = claim.questionGroup[MoreAboutTheCare].getOrElse(MoreAboutTheCare())

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
      {question(<LiveSameAddress/>,"liveAtSameAddressCareYouProvide", theirPersonalDetails.liveAtSameAddressCareYouProvide)}
    </Caree>
  }

  private def careBreak(claim: Claim) = {
    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())

    def breaksInCareLabel (label:String, answer:Boolean) = {
      val date = claim.questionGroup[ClaimDate] match {
        case Some(claimDate) if claimDate.spent35HoursCaringBeforeClaim.answer == Mappings.yes =>  claim.dateOfClaim.get - 6 months
        case _ => claim.dateOfClaim.get
      }

      question(<BreaksSinceClaim/>, label, answer, displayPlaybackDatesFormat(Lang("en"),date))
    }

    val lastValue = claim.questionGroup[BreaksInCareSummary].getOrElse(BreaksInCareSummary()).answer == Mappings.yes
    val xmlNoBreaks = {
      <CareBreak>
        {if (breaksInCare.breaks.size > 0){
          {breaksInCareLabel("answer.more.label", lastValue)}
        } else {
          {breaksInCareLabel("answer.label", lastValue)}
        }}
      </CareBreak>
    }

    {for ((break, index) <- breaksInCare.breaks.zipWithIndex) yield {
      <CareBreak>
        {index > 0 match {
          case true =>  breaksInCareLabel("answer.more.label", true)
          case false => breaksInCareLabel("answer.label", true)
        }}
        {break.startTime match {
            case Some(s) => {question(<StartDate/>, "start", break.start.`dd-MM-yyyy`) ++
                             question(<StartTime/>, "startTime", s)
                             }
            case _ => question(<StartDate/>, "start", break.start.`dd-MM-yyyy`)
          }
        }
        {break.end match {
          case Some(n) => {
            break.endTime match {
              case Some(e) => {
                                question(<EndDate/>,"end", break.end.get.`dd-MM-yyyy`) ++
                                question(<EndTime/>, "endTime", e)
                              }
              case _ => question(<EndDate/>,"end", break.end.get.`dd-MM-yyyy`)
            }
          }
          case _ => NodeSeq.Empty
        }}
        {break.doNotKnowEndDate match {
          case Some(v) => question(<EndDateDoNotKnow/>,"end",MMessages("doNotKnowEndDate"))
          case _ => NodeSeq.Empty
        }}
        {question(<MedicalCare/>,"medicalDuringBreak", break.medicalDuringBreak)}
        {questionOther(<ReasonClaimant/>,"whereYou", break.whereYou.answer, break.whereYou.text)}
        {questionOther(<ReasonCaree/>,"wherePerson", break.wherePerson.answer, break.wherePerson.text)}
      </CareBreak>
    }} ++ xmlNoBreaks
  }
}