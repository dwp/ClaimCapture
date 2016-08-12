package xml.claim

import app.BreaksInCareGatherOptions
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
    val dpName=theirPersonalDetails.firstName+" "+theirPersonalDetails.surname
    <Caree>
      {question(<Surname/>, "surname", encrypt(theirPersonalDetails.surname))}
      {question(<OtherNames/>, "firstName", theirPersonalDetails.firstName)}
      {question(<MiddleNames/>, "middleName", theirPersonalDetails.middleName)}
      {question(<Title/>, "title", theirPersonalDetails.title)}
      {question(<DateOfBirth/>, "dateOfBirth", theirPersonalDetails.dateOfBirth.`dd-MM-yyyy`)}
      {question(<NationalInsuranceNumber/>,"nationalInsuranceNumber", encrypt(theirPersonalDetails.nationalInsuranceNumber.getOrElse("")))}
      {postalAddressStructure("address", theirPersonalDetails.theirAddress.address.getOrElse(MultiLineAddress()), encrypt(theirPersonalDetails.theirAddress.postCode.getOrElse("").toUpperCase))}
      {question(<RelationToClaimant/>,"relationship", theirPersonalDetails.relationship)}
      {question(<Cared35Hours/>,"spent35HoursCaring", moreAboutTheCare.spent35HoursCaring, dpName)}
      {question(<OtherCarer/>,"otherCarer", moreAboutTheCare.otherCarer, dpName)}
      {question(<OtherCarerUc/>,"otherCarerUc", moreAboutTheCare.otherCarerUc, dpName)}
      {question(<OtherCarerUcDetails/>,"otherCarerUcDetails", moreAboutTheCare.otherCarerUcDetails)}
      {careBreak(claim)}
      {question(<LiveSameAddress/>,"theirAddress.answer", theirPersonalDetails.theirAddress.answer)}
    </Caree>
  }

  private def dpDetails(claim: Claim) : String = {
    val theirPersonalDetails = claim.questionGroup(TheirPersonalDetails).getOrElse(TheirPersonalDetails()).asInstanceOf[TheirPersonalDetails]
    theirPersonalDetails.firstName + " " + theirPersonalDetails.surname
  }

  def findSelectedMapping(breaks: List[Break]) = {
    val hospital = breaks.filter(b => b.typeOfCare == Breaks.hospital).size
    breaks.filter(b => b.typeOfCare == Breaks.carehome).size>0 match {
      case true => if (hospital == 0) "Respite or care home" else "Hospital, Respite or care home"
      case _ => if (hospital == 0) "" else "Hospital"
    }
  }

  def findOtherSelected(breaks: List[Break]) = {
    breaks.filter(b => b.typeOfCare == Breaks.another).size>0 match {
      case true => "yes"
      case _ => "no"
    }
  }

  private def careBreak(claim: Claim) = {
    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())

    def breaksInCareLabel (label: String, answer: String, dp: String) = {
      val claimDateQG = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())
      question(<BreaksSinceClaim/>, label, answer, claimDateQG.dateWeRequireBreakInCareInformationFrom(claim.lang.getOrElse(Lang("en"))), dp)
    }
    val dp = dpDetails(claim);
    val xmlLastBreak = {
      if (breaksInCare.breaks.size > 0) {
        <CareBreak>
          {breaksInCareLabel("breaktype_another", "None", dp)}
          {question(<BreaksOtherSinceClaim/>, "breaktype_other_another", "no", dp)}
        </CareBreak>
      } else {NodeSeq.Empty}
    }

    val xmlFirstBreak = {
      <CareBreak>
        {breaksInCareLabel("breaktype_first", findSelectedMapping(breaksInCare.breaks), dp)}
        {question(<BreaksOtherSinceClaim/>, "breaktype_other_first", findOtherSelected(breaksInCare.breaks), dp)}
      </CareBreak>
    }

    xmlFirstBreak ++
    {for ((break, index) <- breaksInCare.breaks.zipWithIndex) yield {
        {
          {break.typeOfCare match {
            case Breaks.hospital => {createHospitalBreak(break, dp)}
            case Breaks.carehome => {createRespiteBreak(break, dp)}
            case Breaks.another => {createOtherBreak(break, dp)}
          }}
        }
    }} ++ xmlLastBreak
  }

  def createHospitalBreak(break: Break, dp: String) = {
    val startDetails = break.whoWasAway match { case BreaksInCareGatherOptions.DP => (break.whenWasDpAdmitted.get, "whenWasDpAdmitted") case _ => (break.whenWereYouAdmitted.get, "whenWereYouAdmitted") }
    val breakEndedDetails = break.whoWasAway match { case BreaksInCareGatherOptions.DP => (break.dpStayEnded.get, "dpStayEnded") case _ => (break.yourStayEnded.get, "yourStayEnded") }
    <CareBreak>
      {question(<BreaksType/>, "Type of Break", "Hospital")}
      {question(<WhoWasAway/>, "whoWasInHospital", break.whoWasAway)}
      {question(<StartDate/>, startDetails._2, startDetails._1.`dd-MM-yyyy`, dp)}
      {breakEndedDetails._1.answer match {
        case "yes" => {
          question(<BreakEnded/>, s"${breakEndedDetails._2}.answer", breakEndedDetails._1.answer, dp) ++ question(<EndDate/>, s"${breakEndedDetails._2}.date", breakEndedDetails._1.date.get.`dd-MM-yyyy`, dp)
        }
        case "no" => question(<BreakEnded/>, s"${breakEndedDetails._2}.answer", breakEndedDetails._1.answer, dp)
        case _ => NodeSeq.Empty
      }}
      {if (break.whoWasAway == BreaksInCareGatherOptions.DP) {
        question(<BreaksInCareRespiteStillCaring/>, "breaksInCareStillCaring", break.breaksInCareStillCaring.get, dp)
      }}
    </CareBreak>
  }

  def createRespiteBreak(break: Break, dp: String) = {
    val startDetails = break.whoWasAway match { case BreaksInCareGatherOptions.DP => (break.whenWasDpAdmitted.get, "whenWasDpAdmitted") case _ => (break.whenWereYouAdmitted.get, "whenWereYouAdmitted") }
    val breakEndedDetails = break.whoWasAway match { case BreaksInCareGatherOptions.DP => (break.dpStayEnded.get, "dpRespiteStayEnded") case _ => (break.yourStayEnded.get, "yourRespiteStayEnded") }
    <CareBreak>
      {question(<BreaksType/>, "Type of Break", "Respite")}
      {question(<WhoWasAway/>, "whoWasInHospital", break.whoWasAway)}
      {question(<StartDate/>, startDetails._2, startDetails._1.`dd-MM-yyyy`, dp)}
      {breakEndedDetails._1.answer match {
        case "yes" => {
          question(<BreakEnded/>, s"${breakEndedDetails._2}.answer", breakEndedDetails._1.answer, dp) ++ question(<EndDate/>, s"${breakEndedDetails._2}.date", breakEndedDetails._1.date.get.`dd-MM-yyyy`, dp)
        }
        case "no" => question(<BreakEnded/>, s"${breakEndedDetails._2}.answer", breakEndedDetails._1.answer, dp)
        case _ => NodeSeq.Empty
      }}
      {break.whoWasAway match {
        case BreaksInCareGatherOptions.You => question(<MedicalCare/>, "yourMedicalProfessional", break.yourMedicalProfessional.get, dp)
        case BreaksInCareGatherOptions.DP =>
          question(<DpMedicalCare/>, "dpMedicalProfessional", break.dpMedicalProfessional.get, dp) ++
          question(<BreaksInCareRespiteStillCaring/>, "breaksInCareStillCaring", break.breaksInCareStillCaring.get, dp)
      }}
      </CareBreak>
  }

  def createOtherBreak(break: Break, dp: String) = {
    <CareBreak>
      {question(<BreaksType/>, "Type of Break", "Other")}
      {question(<WhoWasAway/>, "whoWasInHospital", "You")}
      {break.caringStarted.get.answer match {
        case "yes" => {
          break.caringStartedTime match {
            case Some(e) => {
              question(<BreakStarted/>, "caringStarted.answer", break.caringStarted.get.answer, dp) ++
              question(<StartDate/>, "caringStarted.date", break.caringStarted.get.date.get.`dd-MM-yyyy`, dp) ++
              question(<StartTime/>, "caringStarted.time", e, dp)
            }
            case _ => {
              question(<BreakStarted/>, "caringStarted.answer", break.caringStarted.get.answer, dp) ++
              question(<StartDate/>, "caringStarted.date", break.caringStarted.get.date.get.`dd-MM-yyyy`, dp)
            }
          }
        }
        case "no" => {question(<BreakStarted/>, "caringStarted.answer", break.caringStarted.get.answer, dp)}
        case _ => NodeSeq.Empty
      }
    }
    {break.caringStartedTime match {
      case Some(e) => {
        question(<EndDate/>, "caringStarted.date", break.caringStarted.get.date.get.`dd-MM-yyyy`, dp) ++
        question(<EndTime/>, "caringStarted.time", e, dp)
      }
      case _ => question(<EndDate/>, "caringStarted.date", break.caringStarted.get.date.get.`dd-MM-yyyy`, dp)
    }}
    {questionOther(<ReasonClaimant/>,"whereWereYou", break.whereWereYou.get.answer, break.whereWereYou.get.text, dp)}
    {questionOther(<ReasonCaree/>,"whereWasDp", break.whereWasDp.get.answer, break.whereWasDp.get.text, dp)}
    </CareBreak>
  }

  def fromXml(xml: NodeSeq, claim: Claim) : Claim = {
    val breaksInCareTuple = createBreaksInCare(xml)
    val breaksInCareSummary = OldBreaksInCareSummary(answer = createYesNoText(breaksInCareTuple._2))
    val newClaim = claim.update(createYourDetailsFromXml(xml)).update(createMoreAboutCareFromXml(xml)).update(breaksInCareSummary)
    breaksInCareTuple._1.hasBreaks match {
      case true => newClaim.update(breaksInCareTuple._1)
      case false => newClaim.update(OldBreaksInCare())
    }
  }

  private def createBreaksInCare(xml: NodeSeq) = {
    val breaksInCareXml = (xml \\ "Caree" \ "CareBreak")
    var breaks = List[OldBreak]()
    var breaksSinceClaim = Mappings.yes
    breaksInCareXml.zip (Stream from 1).foreach(node =>
      {
        breaksSinceClaim = (node._1 \ "BreaksSinceClaim" \ "Answer").text
        breaksSinceClaim.toLowerCase match {
          case Mappings.yes =>
            breaks = breaks :+ OldBreak(
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
    (OldBreaksInCare(breaks), breaksSinceClaim)
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
    MoreAboutTheCare(spent35HoursCaring = createYesNoText((claimant \ "Cared35Hours" \ "Answer").text), otherCarer = createYesNoText((claimant \ "OtherCarer" \ "Answer").text))
  }
}
