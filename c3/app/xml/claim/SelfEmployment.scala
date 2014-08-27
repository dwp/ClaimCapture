package xml.claim

import models.domain._
import scala.xml.NodeSeq
import app.XMLValues._
import xml.XMLHelper._
import xml.XMLComponent

object SelfEmployment extends XMLComponent{

  def xml(claim: Claim) = {
    val employment = claim.questionGroup[models.domain.Employment].getOrElse(models.domain.Employment())
    val aboutSelfEmployment = claim.questionGroup[AboutSelfEmployment].getOrElse(AboutSelfEmployment())
    val yourAccounts =  claim.questionGroup[SelfEmploymentYourAccounts].getOrElse(SelfEmploymentYourAccounts())

    def jobDetails() = {
      if (aboutSelfEmployment.areYouSelfEmployedNow.toLowerCase == yes) {
        <CurrentJobDetails>
          {question(<DateStarted/>, "whenDidYouStartThisJob", aboutSelfEmployment.whenDidYouStartThisJob)}
          {question(<NatureBusiness/>, "natureOfYourBusiness", aboutSelfEmployment.natureOfYourBusiness)}
          <TradingYear>
            {question(<DateFrom/>, "whatWasOrIsYourTradingYearFrom", yourAccounts.whatWasOrIsYourTradingYearFrom, questionLabel(claim,"whatWasOrIsYourTradingYearFrom"))}
            {question(<DateTo/>, "whatWasOrIsYourTradingYearTo", yourAccounts.whatWasOrIsYourTradingYearTo, questionLabel(claim,"whatWasOrIsYourTradingYearTo"))}
          </TradingYear>
          {question(<SameIncomeOutgoingLevels/>, "areIncomeOutgoingsProfitSimilarToTrading", yourAccounts.areIncomeOutgoingsProfitSimilarToTrading)}
          {question(<WhyWhenChange/>, "tellUsWhyAndWhenTheChangeHappened", yourAccounts.tellUsWhyAndWhenTheChangeHappened)}
        </CurrentJobDetails>
      } else {
        <RecentJobDetails>
          {question(<DateStarted/>, "whenDidYouStartThisJob", aboutSelfEmployment.whenDidYouStartThisJob)}
          {question(<NatureBusiness/>, "natureOfYourBusiness", aboutSelfEmployment.natureOfYourBusiness)}
          <TradingYear>
            {question(<DateFrom/>, "whatWasOrIsYourTradingYearFrom", yourAccounts.whatWasOrIsYourTradingYearFrom, questionLabel(claim,"whatWasOrIsYourTradingYearFrom"))}
            {question(<DateTo/>, "whatWasOrIsYourTradingYearTo", yourAccounts.whatWasOrIsYourTradingYearTo, questionLabel(claim,"whatWasOrIsYourTradingYearTo"))}
          </TradingYear>
          {question(<SameIncomeOutgoingLevels/>, "areIncomeOutgoingsProfitSimilarToTrading", yourAccounts.areIncomeOutgoingsProfitSimilarToTrading)}
          {question(<WhyWhenChange/>, "tellUsWhyAndWhenTheChangeHappened", yourAccounts.tellUsWhyAndWhenTheChangeHappened)}
          {question(<DateEnded/>, "whenDidTheJobFinish", aboutSelfEmployment.whenDidTheJobFinish)}
          {question(<TradingCeased/>, "haveYouCeasedTrading", aboutSelfEmployment.haveYouCeasedTrading)}
        </RecentJobDetails>
      }
    }

    if (employment.beenSelfEmployedSince1WeekBeforeClaim.toLowerCase == yes) {
      <SelfEmployment>
        {question(<SelfEmployedNow/>, "areYouSelfEmployedNow", aboutSelfEmployment.areYouSelfEmployedNow)}
        {jobDetails()}
        {pensionExpensesXml(claim)}
        {jobExpensesXml(claim)}
      </SelfEmployment>
    } else NodeSeq.Empty
  }

  private def jobExpensesXml(claim:Claim):NodeSeq = {
    val aboutExpenses = claim.questionGroup[SelfEmploymentPensionsAndExpenses].getOrElse(SelfEmploymentPensionsAndExpenses())
    val showXml = aboutExpenses.haveExpensesForJob.answer.toLowerCase == "yes"

    if (showXml) {
      question(<PaidForJobExpenses/>,"haveExpensesForJob.answer",aboutExpenses.haveExpensesForJob.answer,questionLabelSelfEmployment(claim, "haveExpensesForJob.answer")) ++
        <JobExpenses>
          {question(<Expense/>,"haveExpensesForJob.text",aboutExpenses.haveExpensesForJob.text,questionLabelSelfEmployment(claim, "haveExpensesForJob.text"))}
        </JobExpenses>
    } else {
      question(<PaidForJobExpenses/>,"haveExpensesForJob.answer",aboutExpenses.haveExpensesForJob.answer,questionLabelSelfEmployment(claim, "haveExpensesForJob.answer"))
    }
  }

  private def pensionExpensesXml(claim:Claim):NodeSeq = {
    val aboutExpenses = claim.questionGroup[SelfEmploymentPensionsAndExpenses].getOrElse(SelfEmploymentPensionsAndExpenses())
    val showXml = aboutExpenses.payPensionScheme.answer.toLowerCase == "yes"

    if (showXml) {
      question(<PaidForPension/>,"payPensionScheme.answer",aboutExpenses.payPensionScheme.answer,questionLabelSelfEmployment(claim, "payPensionScheme.answer")) ++
        <PensionExpenses>
          {question(<Expense/>,"payPensionScheme.text",aboutExpenses.payPensionScheme.text,questionLabelSelfEmployment(claim, "payPensionScheme.text"))}
        </PensionExpenses>
    } else {
      question(<PaidForPension/>,"payPensionScheme.answer",aboutExpenses.payPensionScheme.answer,questionLabelSelfEmployment(claim, "payPensionScheme.answer"))
    }
  }

  def currencyAmount(currency: Option[String]):Option[String] = {
    val poundSign = "£"
    currency match {
      case Some(s) => {
        if(s.split(poundSign).size >1) Some(s.split(poundSign)(1))
        else Some(s)
      }
      case _ => None
    }
  }
}