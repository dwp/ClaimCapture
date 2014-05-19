package xml.circumstances

import app.CircsBreaksWhereabouts
import models.domain._
import scala.xml.NodeSeq
import xml.XMLHelper._
import play.api.i18n.{MMessages => Messages}
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime
import models.domain.Claim
import scala.Some
import controllers.Mappings
import app.ConfigProperties._
import play.Logger
import models.domain.Claim
import scala.Some
import models.domain.Claim
import scala.Some

object CircsEvidenceList {
  def xml(circs: Claim) = {
    <EvidenceList>
      {xmlGenerated()}{furtherInfo(circs)}{theirInfo(circs)}{selfEmployed(circs)}{paymentChange(circs)}{addressChange(circs)}{breaksFromCaring(circs)}{breaksFromCaringSummary(circs)}
    </EvidenceList>
  }

  def xmlGenerated() = {
    textLine("XML Generated at: " + DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").print(DateTime.now()))
  }

  def selfEmployed(circs: Claim): NodeSeq = {
    var buffer = NodeSeq.Empty

    Logger.debug("circs.employment.active = " + getProperty("circs.employment.active", default = false))
    if(getProperty("circs.employment.active", default = false)) {
      val employmentChangeOption: Option[CircumstancesEmploymentChange] = circs.questionGroup[CircumstancesEmploymentChange]
      employmentChangeOption match {
        case Some(employmentChange) => {
          buffer ++= textSeparatorLine(Messages("c2.g9"))

          buffer ++= textLine(Messages("stillCaring.answer") + " = " + Messages("label." + employmentChange.stillCaring.answer))

          employmentChange.stillCaring.answer match {
            case "no" => buffer ++= textLine(Messages("whenStoppedCaring") + " = " + employmentChange.stillCaring.date.get.`dd/MM/yyyy`)
            case _ =>
          }

          buffer ++= textLine(Messages("hasWorkStartedYet.answer") + " = " + Messages("label." + employmentChange.hasWorkStartedYet.answer))

          employmentChange.hasWorkStartedYet.answer match {
            case "yes" => {
              buffer ++= textLine(Messages("hasWorkStartedYet.dateWhenStarted") + " = " + employmentChange.hasWorkStartedYet.date1.get.`dd/MM/yyyy`)

              employmentChange.hasWorkFinishedYet.answer match {
                case Some(answer) => {
                  buffer ++= textLine(Messages("hasWorkFinishedYet.answer") + " = " + Messages("label." + employmentChange.hasWorkFinishedYet.answer.get))

                  employmentChange.hasWorkFinishedYet.answer.getOrElse("no") match {
                    case "yes" => {
                      buffer ++= textLine(Messages("hasWorkFinishedYet.dateWhenFinished") + " = " + employmentChange.hasWorkFinishedYet.date.get.`dd/MM/yyyy`)
                    }
                    case _ =>
                  }
                }
                case _ =>
              }

            }
            case _ => {
              buffer ++= textLine(Messages("hasWorkStartedYet.dateWhenWillItStart") + " = " + employmentChange.hasWorkStartedYet.date2.get.`dd/MM/yyyy`)
            }
          }

          buffer ++= textLine(Messages("typeOfWork.answer") + " = " + Messages("typeOfWork." + employmentChange.typeOfWork.answer))

          buffer ++= textSeparatorLine(Messages("typeOfWork." + employmentChange.typeOfWork.answer))

          employmentChange.typeOfWork.answer match {
            case "self-employed" => {
              buffer ++= textLine(Messages("typeOfWork.selfEmployedTypeOfWork") + " = " + employmentChange.typeOfWork.text2a.get)
              buffer ++= textLine(Messages("typeOfWork.selfEmployedTotalIncome") + " = " + Messages("label." + employmentChange.typeOfWork.answer2.get))
              buffer ++= textLine(Messages("typeOfWork.selfEmployedMoreAboutChanges") + " = " + employmentChange.typeOfWork.text2b.get)
            }
            case _ => {
              buffer ++= textLine(Messages("typeOfWork.employerNameAndAddress") + " = " + employmentChange.typeOfWork.address.get.lineOne.get + " " + employmentChange.typeOfWork.address.get.lineTwo.get + " " + employmentChange.typeOfWork.address.get.lineThree.getOrElse(""))
              buffer ++= textLine(Messages("typeOfWork.employerPostcode") + " = " + employmentChange.typeOfWork.postCode.getOrElse(""))
              buffer ++= textLine(Messages("typeOfWork.employerContactNumber") + " = " + employmentChange.typeOfWork.text1a.getOrElse(""))
              buffer ++= textLine(Messages("typeOfWork.employerPayroll") + " = " + employmentChange.typeOfWork.text1b.getOrElse(""))

              val startedEmploymentAndOngoingOption: Option[CircumstancesStartedEmploymentAndOngoing] = circs.questionGroup[CircumstancesStartedEmploymentAndOngoing]
              startedEmploymentAndOngoingOption match {
                case Some(startedEmploymentAndOngoing) => {
                  buffer ++= textLine(Messages("beenPaidYet") + " = " + Messages("label." + startedEmploymentAndOngoing.beenPaid))
                  buffer ++= textLine(Messages("howMuchPaid") + " = " + startedEmploymentAndOngoing.howMuchPaid)
                  buffer ++= textLine(Messages("whatDatePaid") + " = " + startedEmploymentAndOngoing.date.`dd/MM/yyyy`)
                  buffer ++= textLine(Messages("circs.howOften") + " = " + Messages(startedEmploymentAndOngoing.howOften.frequency))
                  if (startedEmploymentAndOngoing.howOften.other.isDefined) buffer ++= textLine(Messages("other") + " = " + Messages(startedEmploymentAndOngoing.howOften.other.get))
                  if (startedEmploymentAndOngoing.monthlyPayDay.isDefined) buffer ++= textLine(Messages("monthlyPayDay") + " = " + startedEmploymentAndOngoing.monthlyPayDay)

                  val frequencyContext = startedEmploymentAndOngoing.howOften.frequency match {
                    case "weekly" => "week"
                    case "fortnightly" => "fortnight"
                    case "monthly" => "month"
                    case _ => "other"
                  }
                  buffer ++= textLine(Messages("usuallyPaidSameAmount." + frequencyContext) + " = " + Messages("label." + startedEmploymentAndOngoing.usuallyPaidSameAmount))

                  buffer ++= textLine(Messages("doYouPayIntoPension.answer") + " = " + Messages("label." + startedEmploymentAndOngoing.payIntoPension.answer))
                  buffer ++= textLine(startedEmploymentAndOngoing.payIntoPension.answer match {
                    case "yes" if (startedEmploymentAndOngoing.payIntoPension.text.isDefined) => Messages("doYouPayIntoPension.whatFor") + " = " + startedEmploymentAndOngoing.payIntoPension.text.get
                    case _ => ""
                  })
                  buffer ++= textLine(Messages("doCareCostsForThisWork.answer") + " = " + Messages("label." + startedEmploymentAndOngoing.careCostsForThisWork.answer))
                  buffer ++= textLine(startedEmploymentAndOngoing.careCostsForThisWork.answer match {
                    case "yes" if (startedEmploymentAndOngoing.careCostsForThisWork.text.isDefined) => Messages("doCareCostsForThisWork.whatFor") + " = " + startedEmploymentAndOngoing.careCostsForThisWork.text.get
                    case _ => ""
                  })
                }
                case _ =>
              }
            }
          }
        }
        case _ =>
      }
    }
    else {
      val circsSelfEmploymentOption: Option[CircumstancesSelfEmployment] = circs.questionGroup[CircumstancesSelfEmployment]
      circsSelfEmploymentOption match {
        case Some(circsSelfEmployment) => {
          buffer ++= textSeparatorLine(Messages("c2.g2"))

          buffer ++= textLine(Messages("stillCaring.answer") + " = " + circsSelfEmployment.stillCaring.answer)

          circsSelfEmployment.stillCaring.answer match {
            case "no" => buffer ++= textLine(Messages("whenStoppedCaring") + " = " + circsSelfEmployment.stillCaring.date.get.`dd/MM/yyyy`)
            case _ =>
          }
          buffer ++= textLine(Messages("whenThisSelfEmploymentStarted") + " = " + circsSelfEmployment.whenThisSelfEmploymentStarted.`dd/MM/yyyy`)
          buffer ++= textLine(Messages("typeOfBusiness") + " = " + circsSelfEmployment.typeOfBusiness)
          buffer ++= textLine(Messages("totalOverWeeklyIncomeThreshold") + " = " + circsSelfEmployment.totalOverWeeklyIncomeThreshold)
        }
        case _ =>
      }
    }

    buffer
  }

  def furtherInfo(circs: Claim): NodeSeq = {
    val declaration = circs.questionGroup[CircumstancesDeclaration].getOrElse(CircumstancesDeclaration())

    var buffer = NodeSeq.Empty ++ textSeparatorLine(Messages("furtherinfo.title"))

    buffer ++= textLine(Messages("furtherInfoContact") + " = " + declaration.furtherInfoContact)

    buffer
  }

  def theirInfo(circs: Claim): NodeSeq = {
    val reportChange = circs.questionGroup[CircumstancesReportChange].getOrElse(CircumstancesReportChange())

    var buffer = NodeSeq.Empty ++ textSeparatorLine(Messages("c1.personYouAreCaringFor"))

    buffer ++= textLine(Messages("theirRelationshipToYou") + " = " + reportChange.theirRelationshipToYou)

    buffer
  }

  def paymentChange(circs: Claim): NodeSeq = {
    val paymentChangeOption = circs.questionGroup[CircumstancesPaymentChange]

    var buffer = NodeSeq.Empty

    paymentChangeOption match {
      case Some(paymentChange) => {
        buffer ++= textSeparatorLine(Messages("c2.g5"))

        buffer ++= textLine(Messages("currentlyPaidIntoBank.label") + " = " + paymentChange.currentlyPaidIntoBank.answer)
        if (paymentChange.currentlyPaidIntoBank.answer.toLowerCase == "yes") {
          buffer ++= textLine(Messages("currentlyPaidIntoBank.text1.label") + " = " + paymentChange.currentlyPaidIntoBank.text1.get)
        } else {
          buffer ++= textLine(Messages("currentlyPaidIntoBank.text2.label") + " = " + paymentChange.currentlyPaidIntoBank.text2.get)
        }

        buffer ++= textSeparatorLine(Messages("c2.g5.newPaymentDetails"))

        buffer ++= textLine(Messages("accountHolderName") + " = " + paymentChange.accountHolderName)

        buffer ++= textLine(Messages("whoseNameIsTheAccountIn") + " = " + getAccountNameOption(paymentChange.whoseNameIsTheAccountIn))

        buffer ++= textLine(Messages("bankFullName") + " = " + paymentChange.bankFullName)

        buffer ++= textLine(Messages("sortCode") + " = " + stringify(Some(paymentChange.sortCode)))

        buffer ++= textLine(Messages("accountNumber") + " = " + paymentChange.accountNumber)

        // check if empty
        buffer ++= textLine(Messages("rollOrReferenceNumber") + " = " + paymentChange.rollOrReferenceNumber)

        buffer ++= textLine(Messages("paymentFrequency") + " = " + getPaymentChange(paymentChange.paymentFrequency))
      }
      case _ =>
    }

    buffer
  }

  def getPaymentChange(paymentOption: String): String = {
    paymentOption match {
      case "everyWeek" => Messages("reportChanges.everyWeek")
      case "fourWeekly" => Messages("reportChanges.fourWeekly")
    }
  }

  def getAccountNameOption(accountOption: String): String = {
    accountOption match {
      case "yourName" => Messages("reportChanges.yourName")
      case "partner" => Messages("reportChanges.partner")
      case "bothNames" => Messages("reportChanges.bothNames")
      case "onBehalfOfYou" => Messages("reportChanges.onBehalfOfYou")
      case "allNames" => Messages("reportChanges.allNames")
    }
  }

  def addressChange(circs: Claim): NodeSeq = {
    val addressChangeOption = circs.questionGroup[CircumstancesAddressChange]

    var buffer = NodeSeq.Empty

    addressChangeOption match {
      case Some(addressChange) => {
        // previous address
        buffer ++= textSeparatorLine(Messages("c2.g6.previousAddress"))

        buffer ++= textLine(Messages("previousAddress") + " = ", addressChange.previousAddress.lineOne)
        buffer ++= textLine("", addressChange.previousAddress.lineTwo)
        buffer ++= textLine("", addressChange.previousAddress.lineThree)
        buffer ++= textLine(Messages("previousPostcode")  + " = ", addressChange.previousPostcode)

        // Still caring section
        buffer ++= textSeparatorLine(Messages("c2.g6"))

        // still caring answer
        buffer ++= textLine(Messages("stillCaring.answer") + " = ", addressChange.stillCaring.answer)

        // still caring date if it exists
        addressChange.stillCaring.answer match {
          case "no" => buffer ++= textLine(Messages("whenStoppedCaring") + " = ", addressChange.stillCaring.date.get.`dd/MM/yyyy`)
          case _ =>
        }

        // new address
        buffer ++= textSeparatorLine(Messages("c2.g6.newAddress"))

        buffer ++= textLine(Messages("newAddress") + " = ", addressChange.newAddress.lineOne)
        buffer ++= textLine("", addressChange.newAddress.lineTwo)
        buffer ++= textLine("", addressChange.newAddress.lineThree)
        buffer ++= textLine(Messages("newPostcode")  + " = ", addressChange.newPostcode)

        addressChange.stillCaring.answer match {
          case "yes" => {
            buffer ++= textSeparatorLine(Messages("c2.g6.caredForChangedAddress"))
            buffer ++= textLine(Messages("caredForChangedAddress.answer") + " = ",addressChange.caredForChangedAddress.answer)
            buffer ++= textLine(Messages("sameAddress.answer") + " = ", addressChange.sameAddress.answer)

            addressChange.sameAddress.address match {
              case Some(address) => {
                buffer ++= textLine(Messages("sameAddress.theirNewAddress") + " = ", address.lineOne)
                buffer ++= textLine("", address.lineTwo)
                buffer ++= textLine("", address.lineThree)

              }
              case _ =>
            }
            buffer ++= textLine(Messages("sameAddress.theirNewPostcode") + " = ", addressChange.sameAddress.postCode)
          }
          case _ =>
        }
      }
      case _ =>
    }

    buffer
  }

  def breaksFromCaring(circs: Claim): NodeSeq = {
    val breaksFromCaringOption = circs.questionGroup[CircumstancesBreaksInCare]

    var buffer = NodeSeq.Empty

    breaksFromCaringOption match {

      case Some(breaksFromCaring) => {

        buffer ++=  textSeparatorLine(Messages("c2.g7"))

        buffer ++= textLine(Messages("breaksInCareStartDate") + " = " + breaksFromCaring.breaksInCareStartDate.`dd/MM/yyyy`)

        if (breaksFromCaring.breaksInCareStartTime.isDefined) buffer ++= textLine(Messages("breaksInCareStartTime") + " = " + breaksFromCaring.breaksInCareStartTime.get)

        if(breaksFromCaring.wherePersonBreaksInCare.answer == CircsBreaksWhereabouts.SomewhereElse){
          buffer ++= textLine(Messages("wherePersonBreaksInCare.answer") + " Somewhere else = " +breaksFromCaring.wherePersonBreaksInCare.text.get)
        } else {
          buffer ++= textLine(Messages("wherePersonBreaksInCare.answer") + " = " + breaksFromCaring.wherePersonBreaksInCare.answer.replace("_", " "))
        }

        if(breaksFromCaring.whereYouBreaksInCare.answer == CircsBreaksWhereabouts.SomewhereElse){
          buffer ++= textLine(Messages("whereYouBreaksInCare.answer") + " Somewhere else = " + breaksFromCaring.whereYouBreaksInCare.text.get)
        } else {
          buffer ++= textLine(Messages("whereYouBreaksInCare.answer") + " = " + breaksFromCaring.whereYouBreaksInCare.answer.replace("_", " "))
        }

        buffer ++= textLine(Messages("breakEnded.answer") + " = " + breaksFromCaring.breakEnded.answer)

        breaksFromCaring.breakEnded.answer match {
          case Mappings.yes => {
            buffer ++= textLine(Messages("breakEnded.endDate") + " = " + breaksFromCaring.breakEnded.date.get.`dd/MM/yyyy`)
            if (breaksFromCaring.breakEnded.time.isDefined) buffer ++= textLine(Messages("breakEnded_endTime") + " = " + breaksFromCaring.breakEnded.time.get)
          }
          case Mappings.no => {
            val expectStartCaringAnswer = if(breaksFromCaring.expectStartCaring.answer.get == Mappings.dontknow) "Don't Know" else breaksFromCaring.expectStartCaring.answer.get
            buffer ++= textLine(Messages("expectStartCaring.answer") + " = " + expectStartCaringAnswer)
          }
        }

        breaksFromCaring.expectStartCaring.answer match {
          case Some(n) => n match {
            case Mappings.yes => if (breaksFromCaring.expectStartCaring.expectStartCaringDate.isDefined) buffer ++= textLine(Messages("expectStartCaring_expectStartCaringDate") + " = " + breaksFromCaring.expectStartCaring.expectStartCaringDate.get.`dd/MM/yyyy`)
            case Mappings.no => buffer ++= textLine(Messages("expectStartCaring_permanentBreakDate") + " = " + breaksFromCaring.expectStartCaring.permanentBreakDate.get.`dd/MM/yyyy`)
            case _ =>
          }
          case _ =>
        }

        buffer ++= textLine(Messages("medicalCareDuringBreak") + " = " + breaksFromCaring.medicalCareDuringBreak)

        if (breaksFromCaring.moreAboutChanges.isDefined) buffer ++= textLine(Messages("moreAboutChanges") + " = " + breaksFromCaring.moreAboutChanges.get)
      }
      case _ =>
    }
    buffer
  }

  def breaksFromCaringSummary(circs: Claim): NodeSeq = {
    val breaksFromCaringSummaryOption = circs.questionGroup[CircumstancesBreaksInCareSummary]
    var buffer = NodeSeq.Empty

    breaksFromCaringSummaryOption match {
      case Some(breaksFromCaringSummary) => {
        if (breaksFromCaringSummary.additionalBreaks.answer == Mappings.yes) buffer ++= textLine(Messages("additionalBreaks.label") + "yes = " + breaksFromCaringSummary.additionalBreaks.text.get)
        else buffer ++= textLine(Messages("additionalBreaks.label") + " = " + breaksFromCaringSummary.additionalBreaks.answer)
      }
      case _ =>
    }
    buffer
  }
}
