package xml

import models.domain.{CircumstancesReportChange, CircumstancesDeclaration, CircumstancesSelfEmployment, CircumstancesPaymentChange, CircumstancesAddressChange, Claim}
import scala.xml.NodeSeq
import xml.XMLHelper._
import play.api.i18n.Messages
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime
import scala.Some

object CircsEvidenceList {
  def xml(circs: Claim) = {
    <EvidenceList>
      {xmlGenerated()}{selfEmployed(circs)}{furtherInfo(circs)}{theirInfo(circs)}{paymentChange(circs)}{addressChange(circs)}
    </EvidenceList>
  }

  def xmlGenerated() = {
    textLine("XML Generated at: " + DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").print(DateTime.now()))
  }

  def selfEmployed(circs: Claim): NodeSeq = {
    val circsSelfEmploymentOption: Option[CircumstancesSelfEmployment] = circs.questionGroup[CircumstancesSelfEmployment]

    var buffer = NodeSeq.Empty

    circsSelfEmploymentOption match {
      case Some(circsSelfEmployment) => {
        buffer ++=  textSeparatorLine(Messages("c2.g2"))

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
        if (paymentChange.currentlyPaidIntoBank.answer.toLowerCase() == "yes") {
          buffer ++= textLine(Messages("currentlyPaidIntoBank.text1.label") + " = " + paymentChange.currentlyPaidIntoBank.text1.get)
        } else {
          buffer ++= textLine(Messages("currentlyPaidIntoBank.text2.label") + " = " + paymentChange.currentlyPaidIntoBank.text2.get)
        }

        buffer ++= textSeparatorLine(Messages("c2.g5.newPaymentDetails"))

        buffer ++= textLine(Messages("accountHolderName") + " = " + paymentChange.accountHolderName)

        buffer ++= textLine(Messages("whoseNameIsTheAccountIn") + " = " + paymentChange.whoseNameIsTheAccountIn.toString())

        buffer ++= textLine(Messages("bankFullName") + " = " + paymentChange.bankFullName)

        buffer ++= textLine(Messages("sortCode") + " = " + stringify(Some(paymentChange.sortCode)))

        buffer ++= textLine(Messages("accountNumber") + " = " + paymentChange.accountNumber)

        // check if empty
        buffer ++= textLine(Messages("rollOrReferenceNumber") + " = " + paymentChange.rollOrReferenceNumber)

        buffer ++= textLine(Messages("paymentFrequency") + " = " + paymentChange.paymentFrequency)
      }
      case _ =>
    }

    buffer
  }

  def addressChange(circs: Claim): NodeSeq = {
    val addressChangeOption = circs.questionGroup[CircumstancesAddressChange]

    var buffer = NodeSeq.Empty

    addressChangeOption match {
      case Some(addressChange) => {
        var caredForChangedAddress = false

        // Still caring section
        buffer ++= textSeparatorLine(Messages("c2.g6"))

        // still caring answer
        buffer ++= textLine(Messages("stillCaring.answer") + " = " + addressChange.stillCaring.answer)

        // still caring date if it exists
        addressChange.stillCaring.answer match {
          case "no" => buffer ++= textLine(Messages("whenStoppedCaring") + " = " + addressChange.stillCaring.date.get.`dd/MM/yyyy`)
          case "yes" => caredForChangedAddress = true
        }

        // new address - it is mandatory so should have at least one line
        buffer ++= textSeparatorLine(Messages("c2.g6.newAddress"))

        buffer ++= textLine(Messages("newAddress") + " = " + addressChange.newAddress.lineOne.get)
        if("None"!=addressChange.newAddress.lineTwo) buffer ++= textLine(addressChange.newAddress.lineTwo.get)
        if("None"!=addressChange.newAddress.lineThree) buffer ++= textLine(addressChange.newAddress.lineThree.get)

        // new postcode if it exists
        addressChange.newPostcode match {
          case Some(addressChange.newPostcode) => {
            buffer ++= textSeparatorLine(Messages("c2.g6.newPostcode"))
            buffer ++= textLine(Messages("newPostcode") + " = " + addressChange.newPostcode)
          }
          case _ =>
        }

        buffer ++= textSeparatorLine(Messages("c2.g6.caredForChangedAddress"))
        // caredForChangedAddress answer if it exists
        if(caredForChangedAddress)  buffer ++= textLine(Messages("caredForChangedAddress.answer") + " = " + addressChange.caredForChangedAddress.answer.get)

        // cared for new address if it exists
        addressChange.caredForChangedAddress.answer match {
          case Some("yes") => {
            buffer ++= textLine(Messages("sameAddress.answer") + " = " + addressChange.sameAddress.answer.get)
            addressChange.sameAddress.answer match {
              case Some("no") => {
                buffer ++= textLine(Messages("sameAddress.theirNewAddress") + " = " + addressChange.sameAddress.address.get.lineOne.orNull)
                buffer ++= textLine(addressChange.sameAddress.address.get.lineTwo.orNull)
                buffer ++= textLine(addressChange.sameAddress.address.get.lineThree.orNull)

                // cared for new postcode if it exists
                addressChange.sameAddress.postCode match {
                  case Some(addressChange.sameAddress.postCode) =>
                    buffer ++= textLine(Messages("sameAddress.theirNewPostcode") + " = " + addressChange.sameAddress.postCode.get)
                  case _ =>
                }
              }
              case _ =>
            }
          }
          case _ =>
        }
      }

      case _ =>
    }

    buffer
  }
}
