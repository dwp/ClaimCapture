package xml.claim

import controllers.mappings.Mappings
import models.{MultiLineAddress, DayMonthYear}
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
    val moreAboutTheCare = claim.questionGroup[MoreAboutTheCare].getOrElse(MoreAboutTheCare())

    <Caree>
      {question(<Surname/>, "surname", encrypt(theirPersonalDetails.surname))}
      {question(<OtherNames/>, "firstName", theirPersonalDetails.firstName)}
      {question(<MiddleNames/>, "middleName", theirPersonalDetails.middleName)}
      {question(<Title/>, "title", theirPersonalDetails.title)}
      {question(<TitleOther/>, "titleOther", theirPersonalDetails.titleOther)}
      {question(<DateOfBirth/>, "dateOfBirth", theirPersonalDetails.dateOfBirth.`dd-MM-yyyy`)}
      {question(<NationalInsuranceNumber/>,"nationalInsuranceNumber", encrypt(theirPersonalDetails.nationalInsuranceNumber.getOrElse("")))}
      {postalAddressStructure("address", theirPersonalDetails.theirAddress.address.getOrElse(MultiLineAddress()), encrypt(theirPersonalDetails.theirAddress.postCode.getOrElse("").toUpperCase))}
      {question(<RelationToClaimant/>,"relationship", theirPersonalDetails.relationship)}
      {question(<Cared35Hours/>,"hours.answer", moreAboutTheCare.spent35HoursCaring)}
      {careBreak(claim)}
      {question(<LiveSameAddress/>,"theirAddress.answer", theirPersonalDetails.theirAddress.answer)}
    </Caree>
  }

  private def careBreak(claim: Claim) = {
    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())

    def breaksInCareLabel (label:String, answer:Boolean) = {

      val claimDateQG = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())
      question(<BreaksSinceClaim/>, label, answer, claimDateQG.dateWeRequireBreakInCareInformationFrom)

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
        {break.hasBreakEnded.answer match {
          case "yes" => {
            break.endTime match {
              case Some(e) => {
                  question(<EndDateDoNotKnow/>,"hasBreakEnded.answer",break.hasBreakEnded.answer) ++
                  question(<EndDate/>,"hasBreakEnded_date", break.hasBreakEnded.date.get.`dd-MM-yyyy`) ++
                  question(<EndTime/>, "endTime", e)
              }
              case _ => {
                  question(<EndDateDoNotKnow/>, "hasBreakEnded.answer", break.hasBreakEnded.answer) ++
                  question(<EndDate/>, "hasBreakEnded_date", break.hasBreakEnded.date.get.`dd-MM-yyyy`)
              }
            }
          }
          case "no" => question(<EndDateDoNotKnow/>,"hasBreakEnded.answer",break.hasBreakEnded.answer)
          case _ => NodeSeq.Empty
        }}
        {question(<MedicalCare/>,"medicalDuringBreak", break.medicalDuringBreak)}
        {questionOther(<ReasonClaimant/>,"whereYou", break.whereYou.answer, break.whereYou.text)}
        {questionOther(<ReasonCaree/>,"wherePerson", break.wherePerson.answer, break.wherePerson.text)}
      </CareBreak>
    }} ++ xmlNoBreaks
  }
}