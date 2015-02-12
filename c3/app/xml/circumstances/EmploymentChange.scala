package xml.circumstances

import models.domain._
import xml.XMLHelper._

import scala.xml.NodeSeq
import play.api.i18n.{MMessages => Messages}


object EmploymentChange {

  def xml(circs :Claim): NodeSeq = {
    val circsEmploymentChangeOption: Option[CircumstancesEmploymentChange] = circs.questionGroup[CircumstancesEmploymentChange]

    circsEmploymentChangeOption match {
      case Some(circsEmploymentChange) => {
        <EmploymentChange>
          {question(<StillCaring/>,"stillCaring.answer", circsEmploymentChange.stillCaring.answer)}
          {
            circsEmploymentChange.stillCaring.answer match {
              case "no" => {question(<DateStoppedCaring/>, "stillCaring.date",circsEmploymentChange.stillCaring.date.get)}
              case _ => NodeSeq.Empty
            }
          }
          {question(<HasWorkStartedYet/>, "hasWorkStartedYet.answer",circsEmploymentChange.hasWorkStartedYet.answer)}
          {
            circsEmploymentChange.hasWorkStartedYet.answer match {
              case "yes" => {
                var buff = NodeSeq.Empty
                buff = buff ++ question(<DateWorkedStarted/>, "hasWorkStartedYet.dateWhenStarted", circsEmploymentChange.hasWorkStartedYet.date1.get)
                buff = buff ++ question(<HasWorkFinishedYet/>, "hasWorkFinishedYet.answer", circsEmploymentChange.hasWorkFinishedYet.answer.get)
                circsEmploymentChange.hasWorkFinishedYet.answer.get match {
                  case "yes" => buff = buff ++ {question(<DateWorkedFinished/>, "hasWorkFinishedYet.dateWhenFinished",circsEmploymentChange.hasWorkFinishedYet.date.get)}
                  case _ => buff = buff ++ NodeSeq.Empty
                }
                buff
              }
              case "no"  => {question(<DateWhenWillWorkStart/>, "hasWorkStartedYet.dateWhenWillItStart",circsEmploymentChange.hasWorkStartedYet.date2.get)}
              case _ => NodeSeq.Empty
            }
          }
          {question(<TypeOfWork/>, "typeOfWork.answer",circsEmploymentChange.typeOfWork.answer)}
          {
            circsEmploymentChange.typeOfWork.answer match {
              case "employed" => employmentChange(circs, circsEmploymentChange)
              case "self-employed" => selfEmploymentChange(circsEmploymentChange)
              case _ => NodeSeq.Empty
            }
          }
        </EmploymentChange>
        }
      case _ => NodeSeq.Empty
    }
  }

  def employmentChange(circs :Claim, circsEmploymentChange: CircumstancesEmploymentChange): NodeSeq = {
    var buff = NodeSeq.Empty

    buff = buff ++ {
      circs.questionGroup[CircumstancesStartedEmploymentAndOngoing] match {
        case Some(change) => startedEmploymentAndOngoingChange(circsEmploymentChange, change)
        case _ => NodeSeq.Empty
      }
    }

    buff = buff ++ {
      circs.questionGroup[CircumstancesStartedAndFinishedEmployment] match {
        case Some(change) => startedAndFinishedEmploymentChange(circsEmploymentChange, change)
        case _ => NodeSeq.Empty
      }
    }

    buff = buff ++ {
      circs.questionGroup[CircumstancesEmploymentNotStarted] match {
        case Some(change) => employmentNotStartedChange(circsEmploymentChange, change)
        case _ => NodeSeq.Empty
      }
    }

    buff
  }

  def selfEmploymentChange(circsEmploymentChange: CircumstancesEmploymentChange): NodeSeq = {
    <SelfEmployment>
      {question(<TypeOfSelfEmployment/>, "typeOfWork.selfEmployedTypeOfWork",circsEmploymentChange.typeOfWork.text2a.get)}
      {question(<TotalIncome/>, "typeOfWork.selfEmployedTotalIncome", Messages(circsEmploymentChange.typeOfWork.answer2.get))}
      {question(<MoreAboutChanges/>, "typeOfWork.selfEmployedMoreAboutChanges", circsEmploymentChange.typeOfWork.text2b)}
    </SelfEmployment>
  }

  def startedEmploymentAndOngoingChange(circsEmploymentChange: CircumstancesEmploymentChange, change: CircumstancesStartedEmploymentAndOngoing): NodeSeq = {
    <StartedEmploymentAndOngoing>
      {postalAddressStructureOpt("typeOfWork.employerNameAndAddress", circsEmploymentChange.typeOfWork.address, circsEmploymentChange.typeOfWork.postCode.getOrElse("").toUpperCase)}

      {question(<EmployerContactNumber/>, "typeOfWork.employerContactNumber", circsEmploymentChange.typeOfWork.text1a)}

      {question(<EmployerPayroll/>, "typeOfWork.employerPayroll", circsEmploymentChange.typeOfWork.text1b)}

      {question(<BeenPaidYet/>, "beenPaidYet", change.beenPaid)}

      {
        change.beenPaid match {
          case "yes" => {
            var buff = NodeSeq.Empty
            buff = buff ++ {question(<HowMuchPaid/>, "howMuchPaid", currencyAmount(change.howMuchPaid))}
            buff = buff ++ {question(<PaymentDate/>, "whatDatePaid", change.date)}
            buff
          }
          case "no" => {
            var buff = NodeSeq.Empty
            buff = buff ++ {question(<HowMuchPaid/>, "howMuchPaid.expect", currencyAmount(change.howMuchPaid))}
            buff = buff ++ {question(<PaymentDate/>, "whatDatePaid.expect", change.date)}
            buff
          }
          case _ => NodeSeq.Empty
        }
      }
      {question(<MonthlyPayDay/>, "monthlyPayDay", change.monthlyPayDay)}

      <PayFrequency>
        {
          change.beenPaid match {
            case "yes" => questionOther(<Frequency/>, "circs.howOften", change.howOften.frequency, change.howOften.other)
            case "no" => questionOther(<Frequency/>, "circs.howOften.expect", change.howOften.frequency, change.howOften.other)
            case _ => NodeSeq.Empty
          }
        }
      </PayFrequency>

      {
      val labelToUse = change.beenPaid match {
          case "yes" => {
            change.howOften.frequency match {
              case "Weekly" => "usuallyPaidSameAmount.week"
              case "Fortnightly" => "usuallyPaidSameAmount.fortnight"
              case "Four-Weekly" => "usuallyPaidSameAmount.fourweekly"
              case "Monthly" => "usuallyPaidSameAmount.month"
              case _ => "usuallyPaidSameAmount.other"
            }
          }
          case _ => {
            change.howOften.frequency match {
              case "Weekly" => "usuallyPaidSameAmount.week.expect"
              case "Fortnightly" => "usuallyPaidSameAmount.fortnight.expect"
              case "Four-Weekly" => "usuallyPaidSameAmount.fourweekly.expect"
              case "Monthly" => "usuallyPaidSameAmount.month.expect"
              case _ => "usuallyPaidSameAmount.other.expect"
            }
          }
        }

        {
          question(<UsuallyPaidSameAmount/>, labelToUse, change.usuallyPaidSameAmount)
        }
      }

      {question(<PayIntoPension/>, "doYouPayIntoPension", change.payIntoPension.answer)}

      {
        change.payIntoPension.answer match {
          case "yes" => {question(<PayIntoPensionWhatFor/>, "doYouPayIntoPension.whatFor", change.payIntoPension.text)}
          case _ => NodeSeq.Empty
        }
      }

      {question(<CareCostsForThisWork/>, "doCareCostsForThisWork", change.careCostsForThisWork.answer)}
      {
        change.careCostsForThisWork.answer match {
          case "yes" => {question(<CareCostsForThisWorkWhatCosts/>, "doCareCostsForThisWork.whatCosts", change.careCostsForThisWork.text)}
          case _ => NodeSeq.Empty
        }
      }

      {
        change.moreAboutChanges match {
          case Some(moreAboutChanges) => {question(<MoreAboutChanges/>, "moreAboutChanges.helper", moreAboutChanges)}
          case None => NodeSeq.Empty
        }
      }
    </StartedEmploymentAndOngoing>
  }

  def startedAndFinishedEmploymentChange(circsEmploymentChange: CircumstancesEmploymentChange, change: CircumstancesStartedAndFinishedEmployment): NodeSeq = {
    <StartedEmploymentAndFinished>
      {postalAddressStructureOpt("typeOfWork.employerNameAndAddress", circsEmploymentChange.typeOfWork.address, circsEmploymentChange.typeOfWork.postCode.getOrElse("").toUpperCase)}

      {question(<EmployerContactNumber/>, "typeOfWork.employerContactNumber", circsEmploymentChange.typeOfWork.text1a)}

      {question(<EmployerPayroll/>, "typeOfWork.employerPayroll", circsEmploymentChange.typeOfWork.text1b)}

      {question(<BeenPaidYet/>, "beenPaidYet.have", change.beenPaid)}

      {
        change.beenPaid match {
          case "yes" => {
            var buff = NodeSeq.Empty
            buff = buff ++ {question(<HowMuchPaid/>, "howMuchPaid.have", currencyAmount(change.howMuchPaid))}
            buff = buff ++ {question(<PaymentDate/>, "dateLastPaid", change.dateLastPaid)}
            buff = buff ++ {question(<WhatWasIncluded/>, "whatWasIncluded", change.whatWasIncluded)}
            buff
          }
          case "no" => {
            var buff = NodeSeq.Empty
            buff = buff ++ {question(<HowMuchPaid/>, "howMuchPaid.have.expect", currencyAmount(change.howMuchPaid))}
            buff = buff ++ {question(<PaymentDate/>, "dateLastPaid.expect", change.dateLastPaid)}
            buff = buff ++ {question(<WhatWasIncluded/>, "whatWasIncluded.expect", change.whatWasIncluded)}
            buff
          }
          case _ => NodeSeq.Empty
        }
      }
      {question(<MonthlyPayDay/>, "monthlyPayDay", change.monthlyPayDay)}

      {questionOther(<EmployerOwesYouMoney/>, "employerOwesYouMoney", change.employerOwesYouMoney, change.employerOwesYouMoneyInfo)}

      <PayFrequency>
        {questionOther(<Frequency/>,"circs.howOften.were", change.howOften.frequency, change.howOften.other)}
      </PayFrequency>

      {
        val labelToUse = change.howOften.frequency match {
          case "Weekly" => "usuallyPaidSameAmount.did.week"
          case "Fortnightly" => "usuallyPaidSameAmount.did.fortnight"
          case "Four-Weekly" => "usuallyPaidSameAmount.did.time"
          case "Monthly" => "usuallyPaidSameAmount.did.month"
          case _ => "usuallyPaidSameAmount.did.time"
        }
        {
          question(<UsuallyPaidSameAmount/>, labelToUse, change.usuallyPaidSameAmount)
        }
      }

      {question(<PayIntoPension/>, "didYouPayIntoPension", change.payIntoPension.answer)}

      {
        change.payIntoPension.answer match {
          case "yes" => {question(<PayIntoPensionWhatFor/>, "didYouPayIntoPension.whatFor", change.payIntoPension.text)}
          case _ => NodeSeq.Empty
        }
      }

      {question(<CareCostsForThisWork/>, "didCareCostsForThisWork", change.careCostsForThisWork.answer)}
      {
        change.careCostsForThisWork.answer match {
          case "yes" => {question(<CareCostsForThisWorkWhatCosts/>, "didCareCostsForThisWork.whatCosts", change.careCostsForThisWork.text)}
          case _ => NodeSeq.Empty
        }
      }

      {
        change.moreAboutChanges match {
          case Some(moreAboutChanges) => {question(<MoreAboutChanges/>, "moreAboutChanges.helper", moreAboutChanges)}
          case None => NodeSeq.Empty
        }
      }
    </StartedEmploymentAndFinished>
  }

  def employmentNotStartedChange(circsEmploymentChange: CircumstancesEmploymentChange, change: CircumstancesEmploymentNotStarted): NodeSeq = {
    <NotStartedEmployment>
      {postalAddressStructureOpt("typeOfWork.employerNameAndAddress", circsEmploymentChange.typeOfWork.address, circsEmploymentChange.typeOfWork.postCode.getOrElse("").toUpperCase)}

      {question(<EmployerContactNumber/>, "typeOfWork.employerContactNumber", circsEmploymentChange.typeOfWork.text1a)}

      {question(<EmployerPayroll/>, "typeOfWork.employerPayroll", circsEmploymentChange.typeOfWork.text1b)}

      {question(<BeenPaidYet/>, "beenPaidYet.will", change.beenPaid)}

      {
        change.beenPaid match {
          case "yes" => {
            var buff = NodeSeq.Empty
            buff = buff ++  {question(<HowMuchPaid/>, "howMuchPaid.will", currencyAmount(change.howMuchPaid))}
            buff = buff ++ {question(<PaymentDate/>, "whenExpectedToBePaidDate", change.whenExpectedToBePaidDate)}
            if (change.howOften.frequency.size > 0){
              buff = buff ++
                {<PayFrequency>
                  {questionOther(<Frequency/>,"circs.howOften.will", change.howOften.frequency, change.howOften.other)}
                </PayFrequency>}
            }
            buff
          }
          case _ => NodeSeq.Empty
        }
      }

      {
        change.usuallyPaidSameAmount.isDefined match {
          case true => {
            val labelToUse = change.howOften.frequency match {
              case "Weekly" => "usuallyPaidSameAmount.will.week"
              case "Fortnightly" => "usuallyPaidSameAmount.will.fortnight"
              case "Four-Weekly" => "usuallyPaidSameAmount.will.time"
              case "Monthly" => "usuallyPaidSameAmount.will.month"
              case _ => "usuallyPaidSameAmount.will.time"
            }
            {
              question(<UsuallyPaidSameAmount/>, labelToUse, change.usuallyPaidSameAmount)
            }
          }
          case _ => NodeSeq.Empty
        }
      }

      {question(<PayIntoPension/>, "willYouPayIntoPension", change.payIntoPension.answer)}
      {
        change.payIntoPension.answer match {
          case "yes" => {question(<PayIntoPensionWhatFor/>, "willYouPayIntoPension.whatFor", change.payIntoPension.text)}
          case _ => NodeSeq.Empty
        }
      }

      {question(<CareCostsForThisWork/>, "willCareCostsForThisWork", change.careCostsForThisWork.answer)}
      {
        change.careCostsForThisWork.answer match {
          case "yes" => {question(<CareCostsForThisWorkWhatCosts/>, "willCareCostsForThisWork.whatCosts", change.careCostsForThisWork.text)}
          case _ => NodeSeq.Empty
        }
      }

      {
        change.moreAboutChanges match {
          case Some(moreAboutChanges) => {question(<MoreAboutChanges/>, "moreAboutChanges.helper", moreAboutChanges)}
          case None => NodeSeq.Empty
        }
      }

    </NotStartedEmployment>
  }
}
