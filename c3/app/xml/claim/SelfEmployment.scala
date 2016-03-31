package xml.claim

import controllers.mappings.Mappings
import models.yesNo.YesNoWithText
import models.domain._
import scala.xml.NodeSeq
import app.XMLValues._
import xml.XMLHelper._
import xml.XMLComponent

object SelfEmployment extends XMLComponent {
  val datePattern = "dd-MM-yyyy"

  def xml(claim: Claim) = {
    val employment = claim.questionGroup[models.domain.YourIncomes].getOrElse(models.domain.YourIncomes())
    val selfEmploymentDates = claim.questionGroup[SelfEmploymentDates].getOrElse(SelfEmploymentDates())

    if (employment.beenSelfEmployedSince1WeekBeforeClaim.toLowerCase == yes) {
      <SelfEmployment>
        {question(<SelfEmployedNow/>, "stillSelfEmployed", selfEmploymentDates.stillSelfEmployed)}
        {question(<MoreThanYearAgo/>, "moreThanYearAgo", selfEmploymentDates.moreThanYearAgo)}
        {question(<DateStarted/>, "startThisWork", selfEmploymentDates.startThisWork)}
        {question(<DateEnded/>, "finishThisWork", selfEmploymentDates.finishThisWork)}
        {question(<GotAccounts/>, "haveAccounts", selfEmploymentDates.haveAccounts)}
        {question(<KnowTradingYear/>, "knowTradingYear", selfEmploymentDates.knowTradingYear)}
        {question(<TradingYearStart/>, "tradingYearStart", selfEmploymentDates.tradingYearStart)}
        {question(<PaidMoneyYet/>, "paidMoney", selfEmploymentDates.paidMoney)}
        {question(<PaidMoneyDate/>, "paidMoneyDate", selfEmploymentDates.paidMoneyDate)}
        {pensionExpensesXml(claim)}
        {jobExpensesXml(claim)}
      </SelfEmployment>
    } else NodeSeq.Empty
  }

  private def jobExpensesXml(claim: Claim): NodeSeq = {
    val aboutExpenses = claim.questionGroup[SelfEmploymentPensionsAndExpenses].getOrElse(SelfEmploymentPensionsAndExpenses())
    val showXml = aboutExpenses.haveExpensesForJob.answer.toLowerCase == "yes"

    if (showXml) {
      question(<PaidForJobExpenses/>, "haveExpensesForJob.answer", aboutExpenses.haveExpensesForJob.answer, questionLabelSelfEmployment(claim, "haveExpensesForJob.answer")) ++
        <JobExpenses>
          {question(<Expense/>, "haveExpensesForJob.text", aboutExpenses.haveExpensesForJob.text, questionLabelSelfEmployment(claim, "haveExpensesForJob.text"))}
        </JobExpenses>
    } else {
      question(<PaidForJobExpenses/>, "haveExpensesForJob.answer", aboutExpenses.haveExpensesForJob.answer, questionLabelSelfEmployment(claim, "haveExpensesForJob.answer"))
    }
  }

  private def pensionExpensesXml(claim: Claim): NodeSeq = {
    val aboutExpenses = claim.questionGroup[SelfEmploymentPensionsAndExpenses].getOrElse(SelfEmploymentPensionsAndExpenses())
    val showXml = aboutExpenses.payPensionScheme.answer.toLowerCase == "yes"

    if (showXml) {
      question(<PaidForPension/>, "payPensionScheme.answer", aboutExpenses.payPensionScheme.answer, questionLabelSelfEmployment(claim, "payPensionScheme.answer")) ++
        <PensionExpenses>
          {question(<Expense/>, "payPensionScheme.text", aboutExpenses.payPensionScheme.text, questionLabelSelfEmployment(claim, "payPensionScheme.text"))}
        </PensionExpenses>
    } else {
      question(<PaidForPension/>, "payPensionScheme.answer", aboutExpenses.payPensionScheme.answer, questionLabelSelfEmployment(claim, "payPensionScheme.answer"))
    }
  }

  def currencyAmount(currency: Option[String]): Option[String] = {
    val poundSign = "£"
    currency match {
      case Some(s) => {
        if (s.split(poundSign).size > 1) Some(s.split(poundSign)(1))
        else Some(s)
      }
      case _ => None
    }
  }

  def fromXml(xml: NodeSeq, claim: Claim): Claim = {
    (xml \\ "SelfEmployment").isEmpty match {
      case false =>
        claim
          .update(createSelfEmploymentPensionsAndExpensesFromXml(xml))
          .update(createEmploymentFromXml(xml))
      case true => claim
    }
  }

  private def createAboutSelfEmploymentWithJobDetailsFromXml(selfEmployment: NodeSeq, jobNode: NodeSeq) = {
    models.domain.SelfEmploymentDates(
      stillSelfEmployed = createYesNoText((selfEmployment \ "SelfEmployedNow" \ "Answer").text),
      startThisWork = createFormattedDateOptional((jobNode \ "DateStarted" \ "Answer").text),
      finishThisWork = createFormattedDateOptional((jobNode \ "DateEnded" \ "Answer").text),
      knowTradingYear = createYesNoTextOptional((jobNode \ "DoYouKnowYourTradingYear" \ "Answer").text),
      tradingYearStart = createFormattedDateOptional((jobNode \ "DateFrom" \ "Answer").text)
   )
  }

  private def createSelfEmploymentPensionsAndExpensesFromXml(xmlNode: NodeSeq) = {
    models.domain.SelfEmploymentPensionsAndExpenses(
      payPensionScheme = YesNoWithText(createYesNoText((xmlNode \ "PaidForPension" \ "Answer").text), createStringOptional((xmlNode \ "PensionExpenses" \ "Expense" \ "Answer").text)),
      haveExpensesForJob = YesNoWithText(createYesNoText((xmlNode \ "PaidForJobExpenses" \ "Answer").text), createStringOptional((xmlNode \ "JobExpenses" \ "Expense" \ "Answer").text))
    )
  }

  private def createEmploymentFromXml(xmlNode: NodeSeq) = {
    val selfEmployment = (xmlNode \\ "SelfEmployment")
    val jobDetails = (xmlNode \\ "Employment" \ "JobDetails")
    models.domain.YourIncomes(
      beenSelfEmployedSince1WeekBeforeClaim = selfEmployment.isEmpty match {
        case false => Mappings.yes
        case true => Mappings.no
      },
      beenEmployedSince6MonthsBeforeClaim = jobDetails.isEmpty match {
        case false => Mappings.yes
        case true => Mappings.no
      },
      yourIncome_sickpay = None,
      yourIncome_patmatadoppay = None,
      yourIncome_fostering = None,
      yourIncome_directpay = None,
      yourIncome_anyother = None,
      yourIncome_none = None
    )
  }
}
