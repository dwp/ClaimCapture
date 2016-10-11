package xml.claim

import controllers.mappings.Mappings
import models.DayMonthYear
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
        {question(<TypeOfWork/>, "typeOfWork", selfEmploymentDates.typeOfWork)}
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

  def fromXml(xml: NodeSeq, claim: Claim): Claim = {
    (xml \\ "SelfEmployment").isEmpty match {
      case false => claim.update(createAboutSelfEmploymentWithJobDetailsFromXml(xml)).update(createSelfEmploymentPensionsAndExpensesFromXml(xml))
      case true => claim
    }
  }

  private def createAboutSelfEmploymentWithJobDetailsFromXml(xml: NodeSeq) = {
    val selfEmployment = (xml \\ "SelfEmployment")
    models.domain.SelfEmploymentDates(
      typeOfWork = (selfEmployment \ "TypeOfWork" \ "Answer").text,
      stillSelfEmployed = createYesNoText((selfEmployment \ "SelfEmployedNow" \ "Answer").text),
      moreThanYearAgo = createYesNoText((selfEmployment \ "MoreThanYearAgo" \ "Answer").text),
      startThisWork = createFormattedDateOptional((selfEmployment \ "DateStarted" \ "Answer").text),
      finishThisWork = createFormattedDateOptional((selfEmployment \ "DateEnded" \ "Answer").text),
      haveAccounts = createYesNoTextOptional((selfEmployment \ "GotAccounts" \ "Answer").text),
      knowTradingYear = createYesNoTextOptional((selfEmployment \ "KnowTradingYear" \ "Answer").text),
      tradingYearStart = createFormattedDateOptional((selfEmployment \ "TradingYearStart" \ "Answer").text),
      paidMoney = createYesNoTextOptional((selfEmployment \ "PaidMoneyYet" \ "Answer").text),
      paidMoneyDate = createFormattedDateOptional((selfEmployment \ "PaidMoneyDate" \ "Answer").text)
   )
  }

  private def createSelfEmploymentPensionsAndExpensesFromXml(xmlNode: NodeSeq) = {
    models.domain.SelfEmploymentPensionsAndExpenses(
      payPensionScheme = YesNoWithText(createYesNoText((xmlNode \ "PaidForPension" \ "Answer").text), createStringOptional((xmlNode \ "PensionExpenses" \ "Expense" \ "Answer").text)),
      haveExpensesForJob = YesNoWithText(createYesNoText((xmlNode \ "PaidForJobExpenses" \ "Answer").text), createStringOptional((xmlNode \ "JobExpenses" \ "Expense" \ "Answer").text))
    )
  }
}
