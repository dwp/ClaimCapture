package xml.circumstances

import models.domain._
import xml.XMLHelper._

import scala.xml.NodeSeq
import controllers.mappings.Mappings
import models.yesNo.{OptYesNoWithText, YesNoWithText}
import play.api.i18n.{MMessages, MessagesApi}
import play.api.Play.current

object EmploymentChange {
  val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def xml(circs :Claim): NodeSeq = {
    val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
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
    val pensionExpenses=circs.questionGroup[CircumstancesEmploymentPensionExpenses].getOrElse(CircumstancesEmploymentPensionExpenses())
    buff = buff ++ {
      circs.questionGroup[CircumstancesEmploymentPay] match {
        case Some(change) if change.pastpresentfuture == "present" => presentEmploymentChange(circsEmploymentChange, change, pensionExpenses)
        case _ => NodeSeq.Empty
      }
    }

    buff = buff ++ {
      circs.questionGroup[CircumstancesEmploymentPay] match {
        case Some(change) if change.pastpresentfuture == "past" => pastEmploymentChange(circsEmploymentChange, change, pensionExpenses)
        case _ => NodeSeq.Empty
      }
    }

    buff = buff ++ {
      circs.questionGroup[CircumstancesEmploymentPay] match {
        case Some(change) if change.pastpresentfuture == "future" => futureEmploymentChange(circsEmploymentChange, change, pensionExpenses)
        case _ => NodeSeq.Empty
      }
    }

    buff
  }

  def selfEmploymentChange(circsEmploymentChange: CircumstancesEmploymentChange): NodeSeq = {
    <SelfEmployment>
      {question(<TypeOfSelfEmployment/>, "typeOfWork.selfEmployedTypeOfWork",circsEmploymentChange.typeOfWork.text2a.get)}
      {question(<PaidYet/>,"paidMoneyYet.answer", circsEmploymentChange.paidMoneyYet.answer)}
      {
        circsEmploymentChange.paidMoneyYet.answer match {
          case Some("yes") => {question(<DatePaid/>, "paidMoneyYet.date",circsEmploymentChange.paidMoneyYet.date.get)}
          case _ => NodeSeq.Empty
      }
      }
      {question(<TotalIncome/>, "typeOfWork.selfEmployedTotalIncome", messagesApi(circsEmploymentChange.typeOfWork.answer2.get))}
      {question(<MoreAboutChanges/>, "typeOfWork.selfEmployedMoreAboutChanges", circsEmploymentChange.typeOfWork.text2b)}
    </SelfEmployment>
  }

  def presentEmploymentChange(circsEmploymentChange: CircumstancesEmploymentChange, change: CircumstancesEmploymentPay, pensionAndExpenses: CircumstancesEmploymentPensionExpenses): NodeSeq = {
   <StartedEmploymentAndOngoing>
      {question(<EmployerName/>, "typeOfWork.employerName", circsEmploymentChange.typeOfWork.employerName)}

      {postalAddressStructureOpt("typeOfWork.employerNameAndAddress", circsEmploymentChange.typeOfWork.address, circsEmploymentChange.typeOfWork.postCode.getOrElse("").toUpperCase)}

      {question(<EmployerContactNumber/>, "typeOfWork.employerContactNumber", circsEmploymentChange.typeOfWork.text1a)}

      {question(<EmployerPayroll/>, "typeOfWork.employerPayroll", circsEmploymentChange.typeOfWork.text1b)}

      {question(<BeenPaidYet/>, "paid.present", change.paid)}

      {
        change.paid match {
          case Some("yes") => {
            var buff = NodeSeq.Empty
            buff = buff ++ {question(<HowMuchPaid/>, "howmuch.present", currencyAmount(change.howMuch.getOrElse("0")))}
            buff = buff ++ {question(<PaymentDate/>, "paydate.present", change.payDate)}
            buff
          }
          case Some("no") => {
            var buff = NodeSeq.Empty
            buff = buff ++ {question(<HowMuchPaid/>, "howmuch.expect.present", currencyAmount(change.howMuch.getOrElse("0")))}
            buff = buff ++ {question(<PaymentDate/>, "paydate.expect.present", change.payDate)}
            buff
          }
          case _ => NodeSeq.Empty
        }
      }
      {question(<MonthlyPayDay/>, "monthlyPayDay.present", change.monthlyPayDay)}

      <PayFrequency>
        {
          change.paid match {
            case Some("yes") => questionOther(<Frequency/>, "howOften.present", change.howOften.frequency, change.howOften.other)
            case Some("no") => questionOther(<Frequency/>, "howOften.expect.present", change.howOften.frequency, change.howOften.other)
            case _ => NodeSeq.Empty
          }
        }
      </PayFrequency>

      {
      val labelToUse = change.paid match {
          case Some("yes") => {
            change.howOften.frequency match {
              case "Weekly" => "sameAmount.weekly.present"
              case "Fortnightly" => "sameAmount.fortnightly.present"
              case "Four-Weekly" => "sameAmount.fourweekly.present"
              case "Monthly" => "sameAmount.monthly.present"
              case _ => "sameAmount.other.present"
            }
          }
          case _ => {
            change.howOften.frequency match {
              case "Weekly" => "sameAmount.weekly.expect.present"
              case "Fortnightly" => "sameAmount.fortnightly.expect.present"
              case "Four-Weekly" => "sameAmount.fourweekly.expect.present"
              case "Monthly" => "sameAmount.monthly.expect.present"
              case _ => "sameAmount.other.expect.present"
            }
          }
        }

        {
          question(<UsuallyPaidSameAmount/>, labelToUse, change.sameAmount)
        }
      }
     {pensionAndExpensesXml(circsEmploymentChange, pensionAndExpenses)}
    </StartedEmploymentAndOngoing>
  }

  def pastEmploymentChange(circsEmploymentChange: CircumstancesEmploymentChange, change: CircumstancesEmploymentPay, pensionAndExpenses: CircumstancesEmploymentPensionExpenses): NodeSeq = {
    <StartedEmploymentAndFinished>
      {question(<EmployerName/>, "typeOfWork.employerName", circsEmploymentChange.typeOfWork.employerName)}

      {postalAddressStructureOpt("typeOfWork.employerNameAndAddress", circsEmploymentChange.typeOfWork.address, circsEmploymentChange.typeOfWork.postCode.getOrElse("").toUpperCase)}

      {question(<EmployerContactNumber/>, "typeOfWork.employerContactNumber", circsEmploymentChange.typeOfWork.text1a)}

      {question(<EmployerPayroll/>, "typeOfWork.employerPayroll", circsEmploymentChange.typeOfWork.text1b)}

      {question(<BeenPaidYet/>, "paid.past", change.paid)}

      {
        change.paid match {
          case Some("yes") => {
            var buff = NodeSeq.Empty
            buff = buff ++ {question(<HowMuchPaid/>, "howmuch.past", currencyAmount(change.howMuch.getOrElse("0")))}
            buff = buff ++ {question(<PaymentDate/>, "paydate.past", change.payDate)}
            buff = buff ++ {question(<WhatWasIncluded/>, "whatWasIncluded", change.whatWasIncluded)}
            buff
          }
          case Some("no") => {
            var buff = NodeSeq.Empty
            buff = buff ++ {question(<HowMuchPaid/>, "howmuch.expect.past", currencyAmount(change.howMuch.getOrElse("0")))}
            buff = buff ++ {question(<PaymentDate/>, "paydate.expect.past", change.payDate)}
            buff = buff ++ {question(<WhatWasIncluded/>, "whatWasIncluded.expect", change.whatWasIncluded)}
            buff
          }
          case _ => NodeSeq.Empty
        }
      }
      {question(<MonthlyPayDay/>, "monthlyPayDay", change.monthlyPayDay)}

      {questionOther(<EmployerOwesYouMoney/>, "employerOwesYouMoney", change.owedMoney, change.owedMoneyInfo)}

      <PayFrequency>
        {questionOther(<Frequency/>,"howOften.past", change.howOften.frequency, change.howOften.other)}
      </PayFrequency>

      {
        val labelToUse = change.howOften.frequency match {
          case "Weekly" => "sameAmount.weekly.past"
          case "Fortnightly" => "sameAmount.fortnightly.past"
          case "Four-Weekly" => "sameAmount.fourweekly.past"
          case "Monthly" => "sameAmount.monthly.past"
          case _ => "sameAmount.other.past"
        }
        {
          question(<UsuallyPaidSameAmount/>, labelToUse, change.sameAmount)
        }
      }
      {pensionAndExpensesXml(circsEmploymentChange, pensionAndExpenses)}
    </StartedEmploymentAndFinished>
  }

  def futureEmploymentChange(circsEmploymentChange: CircumstancesEmploymentChange, change: CircumstancesEmploymentPay, pensionAndExpenses: CircumstancesEmploymentPensionExpenses): NodeSeq = {
    <NotStartedEmployment>
      {question(<EmployerName/>, "typeOfWork.employerName", circsEmploymentChange.typeOfWork.employerName)}

      {postalAddressStructureOpt("typeOfWork.employerNameAndAddress", circsEmploymentChange.typeOfWork.address, circsEmploymentChange.typeOfWork.postCode.getOrElse("").toUpperCase)}

      {question(<EmployerContactNumber/>, "typeOfWork.employerContactNumber", circsEmploymentChange.typeOfWork.text1a)}

      {question(<EmployerPayroll/>, "typeOfWork.employerPayroll", circsEmploymentChange.typeOfWork.text1b)}

      {question(<BeenPaidYet/>, "paid.future", change.paid)}

      {
        change.paid match {
          case Some("yes") => {
            var buff = NodeSeq.Empty
            buff = buff ++  {question(<HowMuchPaid/>, "howmuch.future", Some(currencyAmount(change.howMuch.getOrElse(""))))}
            buff = buff ++ {question(<PaymentDate/>, "paydate.future", change.payDate)}
            if (change.howOften.frequency.size > 0){
              buff = buff ++
                {<PayFrequency>
                  {questionOther(<Frequency/>,"howOften.future", change.howOften.frequency, change.howOften.other)}
                </PayFrequency>}
            }
            buff
          }
          case _ => NodeSeq.Empty
        }
      }

      {
        change.sameAmount.isDefined match {
          case true => {
            val labelToUse = change.howOften.frequency match {
              case "Weekly" => "sameAmount.weekly.future"
              case "Fortnightly" => "sameAmount.fortnightly.future"
              case "Four-Weekly" => "sameAmount.fourweekly.future"
              case "Monthly" => "sameAmount.monthly.future"
              case _ => "sameAmount.other.future"
            }
            {
              question(<UsuallyPaidSameAmount/>, labelToUse, change.sameAmount)
            }
          }
          case _ => NodeSeq.Empty
        }
      }
      {pensionAndExpensesXml(circsEmploymentChange, pensionAndExpenses)}
    </NotStartedEmployment>
  }

  def pensionExpenseQuestionWithTense(labelkey: String, employment: CircumstancesEmploymentChange) = {
    messagesApi(labelkey + "." + CircumstancesEmploymentPensionExpenses.presentPastOrFuture(employment))
  }

  def pensionAndExpensesXml(circsEmploymentChange: CircumstancesEmploymentChange, pensionAndExpenses: CircumstancesEmploymentPensionExpenses) = {
    var buff = NodeSeq.Empty
    buff ++= {question(<PayIntoPension/>, pensionExpenseQuestionWithTense("payIntoPension", circsEmploymentChange), pensionAndExpenses.payIntoPension.answer)}
    buff ++= {pensionAndExpenses.payIntoPension.answer match {
      case Mappings.yes => {question(<PayIntoPensionWhatFor/>, pensionExpenseQuestionWithTense("payIntoPension.whatFor", circsEmploymentChange), pensionAndExpenses.payIntoPension.text)}
      case _ => NodeSeq.Empty
    }}

    buff ++= {question(<PaidForThingsToDoJob/>, pensionExpenseQuestionWithTense("payForThings", circsEmploymentChange), pensionAndExpenses.payIntoPension.answer)}
    buff ++= {pensionAndExpenses.payForThings.answer match {
        case Mappings.yes => {question(<PaidForThingsWhatFor/>, pensionExpenseQuestionWithTense("payForThings.whatFor", circsEmploymentChange), pensionAndExpenses.payForThings.text)}
        case _ => NodeSeq.Empty
    }}

    buff ++= {question(<CareCostsForThisWork/>, pensionExpenseQuestionWithTense("careCosts", circsEmploymentChange), pensionAndExpenses.careCosts.answer)}
    buff ++= {pensionAndExpenses.careCosts.answer match {
        case Mappings.yes => {question(<CareCostsForThisWorkWhatCosts/>, pensionExpenseQuestionWithTense("careCosts.whatFor", circsEmploymentChange), pensionAndExpenses.careCosts.text)}
        case _ => NodeSeq.Empty
    }}

    buff ++= {pensionAndExpenses.moreAboutChanges match {
        case Some(moreAboutChanges) => {question(<MoreAboutChanges/>, "moreAboutChanges", moreAboutChanges)}
        case None => NodeSeq.Empty
    }}
    buff
  }
}
