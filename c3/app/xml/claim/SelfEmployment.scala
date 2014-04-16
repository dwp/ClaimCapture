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
    val pensionAndExpenses = claim.questionGroup[SelfEmploymentPensionsAndExpenses].getOrElse(SelfEmploymentPensionsAndExpenses())

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
        {question(<CareExpensesChildren/>, "doYouPayToLookAfterYourChildren", pensionAndExpenses.doYouPayToLookAfterYourChildren, questionLabel(claim, "doYouPayToLookAfterYourChildren"))}
        {childCareExpenses(claim)}
        {question(<CareExpensesCaree/>, "didYouPayToLookAfterThePersonYouCaredFor", pensionAndExpenses.didYouPayToLookAfterThePersonYouCaredFor, questionLabel(claim, "didYouPayToLookAfterThePersonYouCaredFor"))}
        {careExpenses(claim)}
        {question(<PaidForPension/>, "doYouPayToPensionScheme.answer", pensionAndExpenses.doYouPayToPensionScheme, questionLabel(claim, "doYouPayToPensionScheme.answer"))}
        {pensionScheme(claim)}
      </SelfEmployment>
    } else NodeSeq.Empty
  }

  def childCareExpenses(claim: Claim) = {
    val pensionsAndExpensesOption = claim.questionGroup[SelfEmploymentPensionsAndExpenses]
    val pensionAndExpenses = pensionsAndExpensesOption.getOrElse(SelfEmploymentPensionsAndExpenses())
    val childCareExpensesOption =  claim.questionGroup[ChildcareExpensesWhileAtWork]
    val childCareExpenses =  childCareExpensesOption.getOrElse(ChildcareExpensesWhileAtWork())
    val hasChildCareExpenses = pensionAndExpenses.doYouPayToLookAfterYourChildren == yes

    if (hasChildCareExpenses) {
      <ChildCareExpenses>
        {question(<CarerName/>, "whoLooksAfterChildren", childCareExpenses.nameOfPerson)}
        <Expense>
          {questionCurrency(<Payment/>, "howMuchCostChildcare", Some(childCareExpenses.howMuchYouPay), questionLabel(claim, "howMuchCostChildcare"))}
          {questionOther(<Frequency/>, "howOftenPayChildCare", childCareExpenses.howOftenPayChildCare.frequency, childCareExpenses.howOftenPayChildCare.other, "", questionLabel(claim, "howOftenPayChildCare"))}
        </Expense>
        {question(<RelationshipCarerToClaimant/>, "relationToYou", childCareExpenses.whatRelationIsToYou)}
        {question(<RelationshipCarerToPartner/>, "relationToPartner",childCareExpenses.relationToPartner)}
      </ChildCareExpenses>
    } else NodeSeq.Empty
  }

  def careExpenses(claim: Claim) = {
    val pensionsAndExpensesOption = claim.questionGroup[SelfEmploymentPensionsAndExpenses]
    val pensionAndExpenses = pensionsAndExpensesOption.getOrElse(SelfEmploymentPensionsAndExpenses())
    val expensesWhileAtWorkOption = claim.questionGroup[ExpensesWhileAtWork]
    val expensesWhileAtWork =  expensesWhileAtWorkOption.getOrElse(ExpensesWhileAtWork())
    val hasCareExpenses = pensionAndExpenses.didYouPayToLookAfterThePersonYouCaredFor == yes

    if (hasCareExpenses) {
      <CareExpenses>
        {question(<CarerName/>, "whoDoYouPay", expensesWhileAtWork.nameOfPerson, questionLabel(claim, "whoDoYouPay"))}
        <Expense>
          {questionCurrency(<Payment/>, "howMuchCostCare", Some(expensesWhileAtWork.howMuchYouPay), questionLabel(claim, "howMuchCostCare"))}
          {questionOther(<Frequency/>, "howOftenPayExpenses", expensesWhileAtWork.howOftenPayExpenses.frequency, expensesWhileAtWork.howOftenPayExpenses.other, "", questionLabel(claim, "howOftenPayExpenses"))}
        </Expense>
        {question(<RelationshipCarerToClaimant/>, "whatRelationIsToYou", expensesWhileAtWork.whatRelationIsToYou)}
        {question(<RelationshipCarerToCaree/>, "whatRelationIsTothePersonYouCareFor", expensesWhileAtWork.whatRelationIsTothePersonYouCareFor)}
      </CareExpenses>
    } else NodeSeq.Empty
  }

  def pensionScheme(claim: Claim) = {
    val pensionsAndExpensesOption = claim.questionGroup[SelfEmploymentPensionsAndExpenses]
    val pensionAndExpenses = pensionsAndExpensesOption.getOrElse(SelfEmploymentPensionsAndExpenses())
    val hasPensionScheme = pensionAndExpenses.doYouPayToPensionScheme.toLowerCase == yes

    if (hasPensionScheme) {
        <PensionScheme>
          {questionCurrency(<Payment/>,"howMuchDidYouPay",currencyAmount(pensionAndExpenses.howMuchDidYouPay), questionLabel(claim, "howMuchDidYouPay"))}
          {questionOther(<Frequency/>, "doYouPayToPensionScheme.howOften", pensionAndExpenses.howOften.get.frequency, pensionAndExpenses.howOften.get.other, questionLabel(claim, "doYouPayToPensionScheme.howOften"))}
        </PensionScheme>
    } else NodeSeq.Empty
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