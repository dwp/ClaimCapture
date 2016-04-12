package xml.claim

import controllers.mappings.Mappings
import models.yesNo.{YesNoWithDate, RadioWithText, YesNoMandWithAddress}
import models.MultiLineAddress
import models.domain._
import play.api.i18n.Lang
import xml.XMLComponent
import xml.XMLHelper._
import scala.language.postfixOps
import scala.xml.NodeSeq

object Caree extends XMLComponent {
  def xml(claim: Claim) = {
    val theirPersonalDetails = claim.questionGroup[TheirPersonalDetails].getOrElse(TheirPersonalDetails())
    val moreAboutTheCare = claim.questionGroup[MoreAboutTheCare].getOrElse(MoreAboutTheCare())

    <Caree>
      {question(<Surname/>, "surname", encrypt(theirPersonalDetails.surname))}
      {question(<OtherNames/>, "firstName", theirPersonalDetails.firstName)}
      {question(<MiddleNames/>, "middleName", theirPersonalDetails.middleName)}
      {question(<Title/>, "title", theirPersonalDetails.title)}
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
      question(<BreaksSinceClaim/>, label, answer, claimDateQG.dateWeRequireBreakInCareInformationFrom(claim.lang.getOrElse(Lang("en"))))
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
                  question(<EndDate/>,"hasBreakEnded.date", break.hasBreakEnded.date.get.`dd-MM-yyyy`) ++
                  question(<EndTime/>, "endTime", e)
              }
              case _ => {
                  question(<EndDateDoNotKnow/>, "hasBreakEnded.answer", break.hasBreakEnded.answer) ++
                  question(<EndDate/>, "hasBreakEnded.date", break.hasBreakEnded.date.get.`dd-MM-yyyy`)
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

  def fromXml(xml: NodeSeq, claim: Claim) : Claim = {
    val breaksInCareTuple = createBreaksInCare(xml)
    val breaksInCareSummary = BreaksInCareSummary(answer = createYesNoText(breaksInCareTuple._2))
    val newClaim = claim.update(createYourDetailsFromXml(xml)).update(createMoreAboutCareFromXml(xml)).update(breaksInCareSummary)
    breaksInCareTuple._1.hasBreaks match {
      case true => newClaim.update(breaksInCareTuple._1)
      case false => newClaim.update(BreaksInCare())
    }
  }

  private def createBreaksInCare(xml: NodeSeq) = {
    val breaksInCareXml = (xml \\ "Caree" \ "CareBreak")
    var breaks = List[Break]()
    var breaksSinceClaim = Mappings.yes
    breaksInCareXml.zip (Stream from 1).foreach(node =>
      {
        breaksSinceClaim = (node._1 \ "BreaksSinceClaim" \ "Answer").text
        breaksSinceClaim.toLowerCase match {
          case Mappings.yes =>
            breaks = breaks :+ Break(
              iterationID = s"${node._2}",
              start = createFormattedDate((node._1 \ "StartDate" \ "Answer").text),
              startTime = createStringOptional((node._1 \ "StartTime" \ "Answer").text),
              wherePerson = RadioWithText((node._1 \ "ReasonCaree" \ "Answer").text, createStringOptional((node._1 \ "ReasonCaree" \ "Other").text)),
              whereYou = RadioWithText((node._1 \ "ReasonClaimant" \ "Answer").text, createStringOptional((node._1 \ "ReasonClaimant" \ "Other").text)),
              hasBreakEnded = YesNoWithDate(createYesNoText((node._1 \ "EndDateDoNotKnow" \ "Answer").text), createFormattedDateOptional((node._1 \ "EndDate" \ "Answer").text)),
              endTime = createStringOptional((node._1 \ "EndTime" \ "Answer").text),
              medicalDuringBreak = createYesNoText((node._1 \ "MedicalCare" \ "Answer").text)
            )
          case _ =>
        }
      })
    (BreaksInCare(breaks), breaksSinceClaim)
  }

  private def createYourDetailsFromXml(xml: NodeSeq) = {
    val caree = (xml \\ "Caree")
    TheirPersonalDetails (
      title = (caree \ "Title" \ "Answer").text,
      firstName = (caree \ "OtherNames" \ "Answer").text,
      middleName = createStringOptional((caree \ "MiddleNames" \ "Answer").text),
      surname = decrypt((caree \ "Surname" \ "Answer").text),
      dateOfBirth = createFormattedDate((caree \ "DateOfBirth" \ "Answer").text),
      nationalInsuranceNumber = createNationalInsuranceNumberOptional(caree),
      relationship = (caree \ "RelationToClaimant" \ "Answer").text,
      theirAddress = YesNoMandWithAddress(
                        answer = createYesNoText((caree \ "LiveSameAddress" \ "Answer").text),
                        address = createAddressOptionalFromXml(caree),
                        postCode = createStringOptional(decrypt((caree \ "Address" \ "Answer" \ "PostCode").text))
                      )
    )
  }

  private def createMoreAboutCareFromXml(xml: NodeSeq) = {
    val claimant = (xml \\ "Caree")
    MoreAboutTheCare(spent35HoursCaring = createYesNoText((claimant \ "Cared35Hours" \ "Answer").text))
  }
}
