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
    val dpName="@dpname"
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
      {careBreak(claim, dpName)}
      {question(<LiveSameAddress/>,"theirAddress.answer", theirPersonalDetails.theirAddress.answer)}
    </Caree>
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

  private def careBreak(claim: Claim, dp: String) = {
    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())

    def breaksInCareLabel (label: String, answer: String, dp: String) = {
      val claimDateQG = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())
      question(<BreaksSinceClaim/>, label, answer, claimDateQG.dateWeRequireBreakInCareInformationFrom(claim.lang.getOrElse(Lang("en"))), dp)
    }
    val your = "@yourname"
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
            case Breaks.hospital => {createHospitalBreak(break, dp, your)}
            case Breaks.carehome => {createRespiteBreak(break, dp, your)}
            case Breaks.another => {createOtherBreak(break, dp)}
          }}
        }
    }} ++ xmlLastBreak
  }

  def createHospitalBreak(break: Break, dp: String, your: String) = {
    val startDetails = break.whoWasAway match { case BreaksInCareGatherOptions.DP => (break.whenWasDpAdmitted.get, "whenWasDpAdmitted", dp) case _ => (break.whenWereYouAdmitted.get, "whenWereYouAdmitted", your) }
    val breakEndedDetails = break.whoWasAway match { case BreaksInCareGatherOptions.DP => (break.dpStayEnded.get, "dpStayEnded") case _ => (break.yourStayEnded.get, "yourStayEnded") }
    <CareBreak>
      {question(<BreaksType/>, "Type of Break", s"${break.whoWasAway}Hospital")}
      {question(<WhoWasAway/>, "whoWasInHospital", startDetails._3)}
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

  def createRespiteBreak(break: Break, dp: String, your: String) = {
    val startDetails = break.whoWasAway match { case BreaksInCareGatherOptions.DP => (break.whenWasDpAdmitted.get, "whenWasDpAdmitted", dp) case _ => (break.whenWereYouAdmitted.get, "whenWereYouAdmitted", your) }
    val breakEndedDetails = break.whoWasAway match { case BreaksInCareGatherOptions.DP => (break.dpStayEnded.get, "dpRespiteStayEnded") case _ => (break.yourStayEnded.get, "yourRespiteStayEnded") }
    <CareBreak>
      {question(<BreaksType/>, "Type of Break", s"${break.whoWasAway}Respite")}
      {question(<WhoWasAway/>, "whoWasInRespite", startDetails._3)}
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
    {break.caringEndedTime match {
      case Some(e) => {
        question(<EndDate/>, "caringEnded.date", break.caringEnded.get.`dd-MM-yyyy`, dp) ++
        question(<EndTime/>, "caringEnded.time", e, dp)
      }
      case _ => question(<EndDate/>, "caringEnded.date", break.caringEnded.get.`dd-MM-yyyy`, dp)
    }}
    {questionOther(<ReasonClaimant/>,"whereWereYou", break.whereWereYou.get.answer, break.whereWereYou.get.text, dp)}
    {questionOther(<ReasonCaree/>,"whereWasDp", break.whereWasDp.get.answer, break.whereWasDp.get.text, dp)}
    </CareBreak>
  }

  def fromXml(xml: NodeSeq, claim: Claim) : Claim = {
    val breaksInCareTuple = createBreaksInCare(xml)
    val breaksInCareType = BreaksInCareType(none = Mappings.someTrue, other = Some(Mappings.no))
    val newClaim = claim.update(createYourDetailsFromXml(xml)).update(createMoreAboutCareFromXml(xml)).update(breaksInCareType)
    breaksInCareTuple.hasBreaks match {
      case true => newClaim.update(breaksInCareTuple).update(breaksInCareType)
      case false => newClaim.update(BreaksInCare()).update(breaksInCareType)
    }
  }

  private def createBreaksInCare(xml: NodeSeq) = {
    val breaksInCareXml = (xml \\ "Caree" \ "CareBreak")
    var breaks = List[Break]()
    breaksInCareXml.zip (Stream from 1).foreach(node =>
    {
      val break = {(node._1 \ "BreaksType" \ "Answer").text match {
          case "YouHospital" | "DPHospital" => createHospitalBreak(node._1, node._2)
          case "YouRespite" | "DPRespite" => createRespiteBreak(node._1, node._2)
          case "Other" => createOtherBreak(node._1, node._2)
          case _ => Break()
        }
      }
      if (!break.iterationID.isEmpty) breaks = breaks :+ break
    })
    (BreaksInCare(breaks))
  }

  private def createHospitalBreak(xml: NodeSeq, iterationId: Integer) = {
    val whoWasAway = (xml \ "BreaksType" \ "Answer").text.startsWith("DP") match { case true => BreaksInCareGatherOptions.DP case _ => BreaksInCareGatherOptions.You }
    Break(
      iterationID = s"${iterationId}",
      typeOfCare = Breaks.hospital,
      whoWasAway = whoWasAway,
      whenWereYouAdmitted = whoWasAway match {
        case BreaksInCareGatherOptions.You => createFormattedDateOptional((xml \ "StartDate" \ "Answer").text)
        case _ => None
      },
      whenWasDpAdmitted = whoWasAway match {
        case BreaksInCareGatherOptions.DP => createFormattedDateOptional((xml \ "StartDate" \ "Answer").text)
        case _ => None
      },
      dpStayEnded = whoWasAway match {
        case BreaksInCareGatherOptions.DP => Some(YesNoWithDate(createYesNoText((xml \ "BreakEnded" \ "Answer").text), createFormattedDateOptional((xml \ "EndDate" \ "Answer").text)))
        case _ => None
      },
      yourStayEnded = whoWasAway match {
        case BreaksInCareGatherOptions.You => Some(YesNoWithDate(createYesNoText((xml \ "BreakEnded" \ "Answer").text), createFormattedDateOptional((xml \ "EndDate" \ "Answer").text)))
        case _ => None
      },
      breaksInCareStillCaring = whoWasAway match {
        case BreaksInCareGatherOptions.DP => createYesNoTextOptional((xml \ "BreaksInCareRespiteStillCaring" \ "Answer").text)
        case _ => None
      }
    )
  }

  private def createRespiteBreak(xml: NodeSeq, iterationId: Integer) = {
    val whoWasAway = (xml \ "BreaksType" \ "Answer").text.startsWith("DP") match { case true => BreaksInCareGatherOptions.DP case _ => BreaksInCareGatherOptions.You }
    Break(
      iterationID = s"${iterationId}",
      typeOfCare = Breaks.carehome,
      whoWasAway = whoWasAway,
      whenWereYouAdmitted = whoWasAway match {
        case BreaksInCareGatherOptions.You => createFormattedDateOptional((xml \ "StartDate" \ "Answer").text)
        case _ => None
      },
      whenWasDpAdmitted = whoWasAway match {
        case BreaksInCareGatherOptions.DP => createFormattedDateOptional((xml \ "StartDate" \ "Answer").text)
        case _ => None
      },
      dpStayEnded = whoWasAway match {
        case BreaksInCareGatherOptions.DP => Some(YesNoWithDate(createYesNoText((xml \ "BreakEnded" \ "Answer").text), createFormattedDateOptional((xml \ "EndDate" \ "Answer").text)))
        case _ => None
      },
      yourStayEnded = whoWasAway match {
        case BreaksInCareGatherOptions.You => Some(YesNoWithDate(createYesNoText((xml \ "BreakEnded" \ "Answer").text), createFormattedDateOptional((xml \ "EndDate" \ "Answer").text)))
        case _ => None
      },
      breaksInCareStillCaring = whoWasAway match {
        case BreaksInCareGatherOptions.DP => createYesNoTextOptional((xml \ "BreaksInCareRespiteStillCaring" \ "Answer").text)
        case _ => None
      },
      dpMedicalProfessional = whoWasAway match {
        case BreaksInCareGatherOptions.DP => createYesNoTextOptional((xml \ "DpMedicalCare" \ "Answer").text)
        case _ => None
      },
      yourMedicalProfessional = whoWasAway match {
        case BreaksInCareGatherOptions.You => createYesNoTextOptional((xml \ "MedicalCare" \ "Answer").text)
        case _ => None
      }
    )
  }

  private def createOtherBreak(xml: NodeSeq, iterationId: Integer) = {
    Break(
      iterationID = s"${iterationId}",
      typeOfCare = Breaks.another,
      caringStarted = Some(YesNoWithDate(createYesNoText((xml \ "BreakStarted" \ "Answer").text), createFormattedDateOptional((xml \ "StartDate" \ "Answer").text))),
      caringStartedTime = createStringOptional((xml \ "StartTime" \ "Answer").text),
      whereWasDp = Some(RadioWithText((xml \ "ReasonCaree" \ "Answer").text, createStringOptional((xml \ "ReasonCaree" \ "Other").text))),
      whereWereYou = Some(RadioWithText((xml  \ "ReasonClaimant" \ "Answer").text, createStringOptional((xml \ "ReasonClaimant" \ "Other").text))),
      caringEnded = createFormattedDateOptional((xml \ "EndDate" \ "Answer").text),
      caringEndedTime = createStringOptional((xml \ "EndTime" \ "Answer").text)
    )
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
