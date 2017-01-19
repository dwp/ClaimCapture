package xml.circumstances

import java.util.UUID._

import app.BreaksInCareGatherOptions
import models.domain._
import models.yesNo.{YesNoMandWithAddress, RadioWithText, YesNoWithDate}
import play.api.Play.current
import play.api.i18n.{Lang, MMessages, MessagesApi}
import xml.XMLHelper._

import scala.xml.NodeSeq

object BreaksInCare {
  val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  def xml(circs: Claim): NodeSeq = {
    val theirPersonalDetails = circs.questionGroup[TheirPersonalDetails].getOrElse(TheirPersonalDetails())
    val moreAboutTheCare = circs.questionGroup[MoreAboutTheCare].getOrElse(MoreAboutTheCare())
    val dpName = "@dpname"
    careBreak(circs, dpName)
  }


  def findSelectedMapping(breaks: List[CircsBreak]) = {
    val hospital = breaks.filter(b => b.typeOfCare == CircsBreaks.hospital).size
    breaks.filter(b => b.typeOfCare == CircsBreaks.carehome).size > 0 match {
      case true => if (hospital == 0) "Respite or care home" else "Hospital, Respite or care home"
      case _ => if (hospital == 0) "None" else "Hospital"
    }
  }

  def findOtherSelected(breaks: List[CircsBreak]) = {
    breaks.filter(b => b.typeOfCare == CircsBreaks.another).size > 0 match {
      case true => "yes"
      case _ => "no"
    }
  }

  // We show 2 questions on Breaks header and 2 questions on Breaks footer on the PDF that we store in xml as first-break and last-break respectively
  // These questions reflect the Summary page questions "Any Hospital/CareHome" and "Any other types of break".
  // We show them on the PDF for initial question status and final question answers.
  // The last break summary questions are always None and No as customer needs to answer None and No to exit the Breaks loop
  private def careBreak(circs: Claim, dp: String) = {
    val breaksInCare = circs.questionGroup[CircsBreaksInCare].getOrElse(CircsBreaksInCare())

    def breaksInCareLabel(label: String, answer: String, dp: String) = {
      val claimDateQG = circs.questionGroup[ClaimDate].getOrElse(ClaimDate())
      val defaultLangForPdf = Lang("en")
      question(<BreaksSinceClaim/>, label, answer, dp)
    }
    val your = "@yourname"
    val xmlFirstBreak = {
      <CareBreak>
        {breaksInCareLabel("circs.breaktype_first", findSelectedMapping(breaksInCare.breaks), dp)}{question(<BreaksOtherSinceClaim/>, "circs.breaktype_other_first", findOtherSelected(breaksInCare.breaks), dp)}
      </CareBreak>
    }

    val xmlLastBreak = {
      if (breaksInCare.breaks.size > 0) {
        <CareBreak>
          {breaksInCareLabel("circs.breaktype_another", "None", dp)}{question(<BreaksOtherSinceClaim/>, "circs.breaktype_other_another", "no", dp)}
        </CareBreak>
      } else {
        NodeSeq.Empty
      }
    }

    xmlFirstBreak ++ {
      for ((break, index) <- breaksInCare.breaks.zipWithIndex) yield {
        {
          {
            break.typeOfCare match {
              case CircsBreaks.hospital => {
                createHospitalBreak(break, dp, your)
              }
              case CircsBreaks.carehome => {
                createRespiteBreak(break, dp, your)
              }
              case CircsBreaks.another => {
                createOtherBreak(break, dp)
              }
            }
          }
        }
      }
    } ++ xmlLastBreak
  }

  def createHospitalBreak(break: CircsBreak, dp: String, your: String) = {
    val startDetails = break.whoWasAway match {
      case BreaksInCareGatherOptions.DP => (break.whenWasDpAdmitted.get, "whenWasDpAdmitted", dp)
      case _ => (break.whenWereYouAdmitted.get, "whenWereYouAdmitted", your)
    }
    val breakEndedDetails = break.whoWasAway match {
      case BreaksInCareGatherOptions.DP => (break.dpStayEnded.get, "dpStayEnded")
      case _ => (break.yourStayEnded.get, "yourStayEnded")
    }
    <CareBreak>
      {question(<BreaksType/>, "Type of Break", s"${break.whoWasAway}Hospital")}{question(<WhoWasAway/>, "whoWasInHospital", startDetails._3)}{question(<StartDate/>, startDetails._2, startDetails._1.`dd-MM-yyyy`, dp)}{breakEndedDetails._1.answer match {
      case "yes" => {
        question(<BreakEnded/>, s"${breakEndedDetails._2}.answer", breakEndedDetails._1.answer, dp) ++ question(<EndDate/>, s"${breakEndedDetails._2}.date", breakEndedDetails._1.date.get.`dd-MM-yyyy`, dp)
      }
      case "no" => question(<BreakEnded/>, s"${breakEndedDetails._2}.answer", breakEndedDetails._1.answer, dp)
      case _ => NodeSeq.Empty
    }}{if (break.whoWasAway == BreaksInCareGatherOptions.DP) {
      question(<BreaksInCareRespiteStillCaring/>, "breaksInCareStillCaring", break.breaksInCareStillCaring.get, dp)
    }}
    </CareBreak>
  }

  def createRespiteBreak(break: CircsBreak, dp: String, your: String) = {
    val startDetails = break.whoWasAway match {
      case BreaksInCareGatherOptions.DP => (break.whenWasDpAdmitted.get, "whenWasDpAdmitted", dp)
      case _ => (break.whenWereYouAdmitted.get, "whenWereYouAdmitted", your)
    }
    val breakEndedDetails = break.whoWasAway match {
      case BreaksInCareGatherOptions.DP => (break.dpStayEnded.get, "dpRespiteStayEnded")
      case _ => (break.yourStayEnded.get, "yourRespiteStayEnded")
    }
    <CareBreak>
      {question(<BreaksType/>, "Type of Break", s"${break.whoWasAway}Respite")}{question(<WhoWasAway/>, "whoWasInRespite", startDetails._3)}{question(<StartDate/>, startDetails._2, startDetails._1.`dd-MM-yyyy`, dp)}{breakEndedDetails._1.answer match {
      case "yes" => {
        question(<BreakEnded/>, s"${breakEndedDetails._2}.answer", breakEndedDetails._1.answer, dp) ++ question(<EndDate/>, s"${breakEndedDetails._2}.date", breakEndedDetails._1.date.get.`dd-MM-yyyy`, dp)
      }
      case "no" => question(<BreakEnded/>, s"${breakEndedDetails._2}.answer", breakEndedDetails._1.answer, dp)
      case _ => NodeSeq.Empty
    }}{break.whoWasAway match {
      case BreaksInCareGatherOptions.You => question(<MedicalCare/>, "yourMedicalProfessional", break.yourMedicalProfessional.get, dp)
      case BreaksInCareGatherOptions.DP =>
        question(<DpMedicalCare/>, "dpMedicalProfessional", break.dpMedicalProfessional.get, dp) ++
          question(<BreaksInCareRespiteStillCaring/>, "breaksInCareStillCaring", break.breaksInCareStillCaring.get, dp)
    }}
    </CareBreak>
  }

  def createOtherBreak(break: CircsBreak, dp: String) = {
    <CareBreak>
      {question(<BreaksType/>, "Type of Break", "Other")}{break.caringStarted.get.answer match {
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
      case "no" => {
        question(<BreakStarted/>, "caringStarted.answer", break.caringStarted.get.answer, dp)
      }
      case _ => NodeSeq.Empty
    }}{break.caringEndedTime match {
      case Some(e) => {
        question(<EndDate/>, "caringEnded.date", break.caringEnded.get.`dd-MM-yyyy`, dp) ++
          question(<EndTime/>, "caringEnded.time", e, dp)
      }
      case _ => question(<EndDate/>, "caringEnded.date", break.caringEnded.get.`dd-MM-yyyy`, dp)
    }}{break.whereWereYou match {
      case Some(e) =>
        questionOther(<ReasonClaimant/>, "whereWereYou", break.whereWereYou.get.answer, break.whereWereYou.get.text, dp)
      case _ =>
    }}{break.whereWasDp match {
      case Some(e) =>
        questionOther(<ReasonCaree/>, "whereWasDp", break.whereWasDp.get.answer, break.whereWasDp.get.text, dp)
      case _ =>
    }}
    </CareBreak>
  }

  def fromXml(xml: NodeSeq, circs: Claim): Claim = {
    // No need to reverse xml for Circs
    circs
  }

  private def createBreaksInCare(xml: NodeSeq) = {
    val breaksInCareXml = (xml \\ "Caree" \ "CareBreak")
    var breaks = List[CircsBreak]()
    breaksInCareXml.zip(Stream from 1).foreach(node => {
      val break = {
        (node._1 \ "BreaksType" \ "Answer").text match {
          case "YouHospital" | "DPHospital" => createHospitalBreak(node._1)
          case "YouRespite" | "DPRespite" => createRespiteBreak(node._1)
          case "Other" => createOtherBreak(node._1)
          case _ => CircsBreak()
        }
      }
      if (!break.iterationID.isEmpty) breaks = breaks :+ break
    })
    (CircsBreaksInCare(breaks))
  }

  private def createHospitalBreak(xml: NodeSeq) = {
    val whoWasAway = (xml \ "BreaksType" \ "Answer").text.startsWith("DP") match {
      case true => BreaksInCareGatherOptions.DP
      case _ => BreaksInCareGatherOptions.You
    }
    CircsBreak(
      iterationID = randomUUID.toString,
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

  private def createRespiteBreak(xml: NodeSeq) = {
    val whoWasAway = (xml \ "BreaksType" \ "Answer").text.startsWith("DP") match {
      case true => BreaksInCareGatherOptions.DP
      case _ => BreaksInCareGatherOptions.You
    }
    CircsBreak(
      iterationID = randomUUID.toString,
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

  private def createOtherBreak(xml: NodeSeq) = {
    CircsBreak(
      iterationID = randomUUID.toString,
      typeOfCare = Breaks.another,
      caringStarted = Some(YesNoWithDate(createYesNoText((xml \ "BreakStarted" \ "Answer").text), createFormattedDateOptional((xml \ "StartDate" \ "Answer").text))),
      caringStartedTime = createStringOptional((xml \ "StartTime" \ "Answer").text),
      whereWasDp = Some(RadioWithText((xml \ "ReasonCaree" \ "Answer").text, createStringOptional((xml \ "ReasonCaree" \ "Other").text))),
      whereWereYou = Some(RadioWithText((xml \ "ReasonClaimant" \ "Answer").text, createStringOptional((xml \ "ReasonClaimant" \ "Other").text))),
      caringEnded = createFormattedDateOptional((xml \ "EndDate" \ "Answer").text),
      caringEndedTime = createStringOptional((xml \ "EndTime" \ "Answer").text)
    )
  }

  private def createYourDetailsFromXml(xml: NodeSeq) = {
    val caree = (xml \\ "Caree")
    TheirPersonalDetails(
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
    MoreAboutTheCare(spent35HoursCaring = createStringOptional(createYesNoText((claimant \ "Cared35Hours" \ "Answer").text)), otherCarer = createStringOptional(createYesNoText((claimant \ "OtherCarer" \ "Answer").text)))
  }
}
