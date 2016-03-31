package xml.claim

import controllers.mappings.Mappings
import models.yesNo.YesNoWithText
import models.domain._
import scala.xml.NodeSeq
import app.XMLValues._
import xml.XMLHelper._
import xml.XMLComponent

object SelfEmployment extends XMLComponent{
  val datePattern = "dd-MM-yyyy"
  def xml(claim: Claim) = {
    val employment = claim.questionGroup[models.domain.YourIncomes].getOrElse(models.domain.YourIncomes())
    val aboutSelfEmployment = claim.questionGroup[AboutSelfEmployment].getOrElse(AboutSelfEmployment())
    val yourAccounts =  claim.questionGroup[SelfEmploymentYourAccounts].getOrElse(SelfEmploymentYourAccounts())

    def jobDetails() = {
      if (aboutSelfEmployment.areYouSelfEmployedNow.toLowerCase == yes) {
        <CurrentJobDetails>
          {question(<DateStarted/>, "whenDidYouStartThisJob", aboutSelfEmployment.whenDidYouStartThisJob)}
          {question(<NatureBusiness/>, "natureOfYourBusiness", aboutSelfEmployment.natureOfYourBusiness)}
          {question(<DoYouKnowYourTradingYear/>, "doYouKnowYourTradingYear", yourAccounts.doYouKnowYourTradingYear)}
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
          {question(<DoYouKnowYourTradingYear/>, "doYouKnowYourTradingYear", yourAccounts.doYouKnowYourTradingYear)}
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

  def fromXml(xml: NodeSeq, claim: Claim) : Claim = {
    (xml \\ "SelfEmployment").isEmpty match {
      case false =>
        claim
          .update(createAboutSelfEmploymentFromXml(xml))
          .update(createSelfEmploymentYourAccountsFromXml(xml))
          .update(createSelfEmploymentPensionsAndExpensesFromXml(xml))
          .update(createEmploymentFromXml(xml))
      case true => claim
    }
  }

  private def createAboutSelfEmploymentFromXml(xml: NodeSeq) = {
    val selfEmployment = (xml \\ "SelfEmployment")
    (xml \\ "CurrentJobDetails") match {
      case currentJob if(currentJob.nonEmpty) => createAboutSelfEmploymentWithJobDetailsFromXml(selfEmployment, currentJob)
      case _ => createAboutSelfEmploymentWithJobDetailsFromXml(selfEmployment, (xml \\ "RecentJobDetails"))
    }
  }

  private def createAboutSelfEmploymentWithJobDetailsFromXml(selfEmployment: NodeSeq, jobNode: NodeSeq) = {
    models.domain.AboutSelfEmployment(
      areYouSelfEmployedNow = createYesNoText((selfEmployment \ "SelfEmployedNow" \ "Answer").text),
      whenDidYouStartThisJob = createFormattedDate((jobNode \ "DateStarted" \ "Answer").text),
      whenDidTheJobFinish = createFormattedDateOptional((jobNode \ "DateEnded" \ "Answer").text),
      haveYouCeasedTrading = Some((jobNode \ "TradingCeased" \ "Answer").text),
      natureOfYourBusiness = (jobNode \ "NatureBusiness" \ "Answer").text
    )
  }

  private def createSelfEmploymentYourAccountsFromXml(xml: NodeSeq) = {
    val selfEmployment = (xml \\ "SelfEmployment")
    (xml \\ "CurrentJobDetails") match {
      case currentJob if(currentJob.nonEmpty) => createSelfEmploymentYourAccountsJobDetailsFromXml(selfEmployment, currentJob)
      case _ => createSelfEmploymentYourAccountsJobDetailsFromXml(selfEmployment, (xml \\ "RecentJobDetails"))
    }
  }

  private def createSelfEmploymentYourAccountsJobDetailsFromXml(selfEmployment: NodeSeq, jobNode: NodeSeq) = {
    models.domain.SelfEmploymentYourAccounts(
      doYouKnowYourTradingYear = createYesNoText((jobNode \ "DoYouKnowYourTradingYear" \ "Answer").text),
      whatWasOrIsYourTradingYearFrom = createFormattedDateOptional((jobNode \ "DateFrom" \ "Answer").text),
      whatWasOrIsYourTradingYearTo = createFormattedDateOptional((jobNode \ "DateTo" \ "Answer").text),
      areIncomeOutgoingsProfitSimilarToTrading = createYesNoTextOptional((jobNode \ "SameIncomeOutgoingLevels" \ "Answer").text),
      tellUsWhyAndWhenTheChangeHappened = createStringOptional((jobNode \ "WhyWhenChange" \ "Answer").text)
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
      beenSelfEmployedSince1WeekBeforeClaim = selfEmployment.isEmpty match { case false => Mappings.yes case true => Mappings.no },
      beenEmployedSince6MonthsBeforeClaim = jobDetails.isEmpty match { case false => Mappings.yes case true => Mappings.no },
      yourIncome_sickpay = None,
      yourIncome_patmatadoppay = None,
      yourIncome_fostering = None,
      yourIncome_directpay = None,
      yourIncome_anyother = None,
      yourIncome_none = None
    )
  }
}
