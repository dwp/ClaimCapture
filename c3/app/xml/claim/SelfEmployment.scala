package xml.claim

import app.{PensionPaymentFrequency, XMLValues}
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
      if (aboutSelfEmployment.areYouSelfEmployedNow == yes) {
        <CurrentJobDetails>
          {question(<DateStarted/>, "whenDidYouStartThisJob", aboutSelfEmployment.whenDidYouStartThisJob)}
          {question(<NatureBusiness/>, "natureOfYourBusiness", aboutSelfEmployment.natureOfYourBusiness)}
          <TradingYear>
            {question(<DateFrom/>, "whatWasOrIsYourTradingYearFrom", yourAccounts.whatWasOrIsYourTradingYearFrom)}
            {question(<DateTo/>, "whatWasOrIsYourTradingYearTo", yourAccounts.whatWasOrIsYourTradingYearTo)}
          </TradingYear>
          {question(<SameIncomeOutgoingLevels/>, "areIncomeOutgoingsProfitSimilarToTrading", yourAccounts.areIncomeOutgoingsProfitSimilarToTrading)}
          {question(<WhyWhenChange/>, "tellUsWhyAndWhenTheChangeHappened", yourAccounts.tellUsWhyAndWhenTheChangeHappened)}
        </CurrentJobDetails>
      } else {
        <RecentJobDetails>
          {question(<DateStarted/>, "whenDidYouStartThisJob", aboutSelfEmployment.whenDidYouStartThisJob)}
          {question(<NatureBusiness/>, "natureOfYourBusiness", aboutSelfEmployment.natureOfYourBusiness)}
          <TradingYear>
            {question(<DateFrom/>, "whatWasOrIsYourTradingYearFrom", yourAccounts.whatWasOrIsYourTradingYearFrom)}
            {question(<DateTo/>, "whatWasOrIsYourTradingYearTo", yourAccounts.whatWasOrIsYourTradingYearTo)}
          </TradingYear>
          {question(<SameIncomeOutgoingLevels/>, "areIncomeOutgoingsProfitSimilarToTrading", yourAccounts.areIncomeOutgoingsProfitSimilarToTrading)}
          {question(<WhyWhenChange/>, "tellUsWhyAndWhenTheChangeHappened", yourAccounts.tellUsWhyAndWhenTheChangeHappened)}
          {question(<DateEnded/>, "whenDidTheJobFinish", aboutSelfEmployment.whenDidTheJobFinish)}
          {question(<TradingCeased/>, "haveYouCeasedTrading", aboutSelfEmployment.haveYouCeasedTrading)}
        </RecentJobDetails>
      }
    }

    if (employment.beenSelfEmployedSince1WeekBeforeClaim == yes) {
      <SelfEmployment>
        {question(<SelfEmployedNow/>, "areYouSelfEmployedNow", aboutSelfEmployment.areYouSelfEmployedNow)}
        {jobDetails()}
        {question(<CareExpensesChildren/>, "doYouPayToLookAfterYourChildren", pensionAndExpenses.doYouPayToLookAfterYourChildren)}
        {childCareExpenses(claim)}
        {question(<CareExpensesCaree/>, "didYouPayToLookAfterThePersonYouCaredFor", pensionAndExpenses.didYouPayToLookAfterThePersonYouCaredFor)}
        {careExpenses(claim)}
        {question(<PaidForPension/>, "doYouPayToPensionScheme.answer", pensionAndExpenses.doYouPayToPensionScheme)}
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
          {questionCurrency(<Payment/>, "howMuchCostChildcare", Some(childCareExpenses.howMuchYouPay))}
          {questionOther(<Frequency/>, "howOftenPayChildCare", Some(childCareExpenses.howOftenPayChildCare.frequency), childCareExpenses.howOftenPayChildCare.other)}
        </Expense>
        {question(<RelationshipCarerToClaimant/>, "relationToYou", childCareExpenses.whatRelationIsToYou)}
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
        {question(<CarerName/>, "whoDoYouPay", expensesWhileAtWork.nameOfPerson)}
        <Expense>
          {questionCurrency(<Payment/>, "howMuchCostCare", Some(expensesWhileAtWork.howMuchYouPay))}
          {questionOther(<Frequency/>, "howOftenPayExpenses", Some(expensesWhileAtWork.howOftenPayExpenses.frequency), expensesWhileAtWork.howOftenPayExpenses.other)}
        </Expense>
        {question(<RelationshipCarerToClaimant/>, "whatRelationIsToYou", expensesWhileAtWork.whatRelationIsToYou)}
        {question(<RelationshipCarerToCaree/>, "whatRelationIsTothePersonYouCareFor", expensesWhileAtWork.whatRelationIsTothePersonYouCareFor)}
      </CareExpenses>
    } else NodeSeq.Empty
  }

  def pensionScheme(claim: Claim) = {
    val pensionsAndExpensesOption = claim.questionGroup[SelfEmploymentPensionsAndExpenses]
    val pensionAndExpenses = pensionsAndExpensesOption.getOrElse(SelfEmploymentPensionsAndExpenses())

    val hasPensionScheme = pensionAndExpenses.doYouPayToPensionScheme == yes

    if (hasPensionScheme) {
        <PensionScheme>
          {questionCurrency(<Payment/>,"howMuchDidYouPay",pensionAndExpenses.howMuchDidYouPay)}
          {questionOther(<Frequency/>, "doYouPayToPensionScheme.howOften", pensionAndExpenses.howOften.orElse(None).get.frequency, pensionAndExpenses.howOften.orElse(None).get.other)}
        </PensionScheme>
    } else NodeSeq.Empty
  }
}