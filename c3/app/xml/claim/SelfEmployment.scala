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
          <DateStarted>
            <QuestionLabel>selfemployed.started</QuestionLabel>
            <Answer>{stringify(Some(aboutSelfEmployment.whenDidYouStartThisJob))}</Answer>
          </DateStarted>
          {aboutSelfEmployment.natureOfYourBusiness match {
            case Some(n) =>{<NatureBusiness>
              <QuestionLabel>selfemployed.business</QuestionLabel>
              <Answer>{aboutSelfEmployment.natureOfYourBusiness.orNull}</Answer>
            </NatureBusiness>}
            case None => NodeSeq.Empty
          }}
          <TradingYear>
            {yourAccounts.whatWasOrIsYourTradingYearFrom match {
              case Some(n) => {
                <DateFrom>
                  <QuestionLabel>trading.from</QuestionLabel>
                  <Answer>{stringify(yourAccounts.whatWasOrIsYourTradingYearFrom)}</Answer>
                </DateFrom>
              }
              case None => NodeSeq.Empty
            }}
            {yourAccounts.whatWasOrIsYourTradingYearTo match {
              case Some(n) => {
                <DateTo>
                  <QuestionLabel>trading.to</QuestionLabel>
                  <Answer>{stringify(yourAccounts.whatWasOrIsYourTradingYearTo)}</Answer>
                </DateTo>
              }
              case None => NodeSeq.Empty
            }}
          </TradingYear>
          {yourAccounts.areIncomeOutgoingsProfitSimilarToTrading match {
            case Some(areIncomeOutgoingsProfitSimilarToTrading) =>
              <SameIncomeOutgoingLevels>
                <QuestionLabel>SameIncomeOutgoingLevels?</QuestionLabel>
                <Answer>{
                  areIncomeOutgoingsProfitSimilarToTrading match {
                    case "yes" => XMLValues.Yes
                    case "no" => XMLValues.No
                    case n => n
                  }}</Answer>
              </SameIncomeOutgoingLevels>
            case None => NodeSeq.Empty
          }}
          {yourAccounts.tellUsWhyAndWhenTheChangeHappened match {
            case Some(n) =>
              <WhyWhenChange>
                <QuestionLabel>why.when</QuestionLabel>
                <Answer>{n}</Answer>
              </WhyWhenChange>
            case None => NodeSeq.Empty
          }}
        </CurrentJobDetails>
      } else {
        <RecentJobDetails>
          <DateStarted>
            <QuestionLabel>selfemployed.started</QuestionLabel>
            <Answer>{stringify(Some(aboutSelfEmployment.whenDidYouStartThisJob))}</Answer>
          </DateStarted>
          {aboutSelfEmployment.natureOfYourBusiness match {
            case Some(n) =>{<NatureBusiness>
              <QuestionLabel>selfemployed.business</QuestionLabel>
              <Answer>{aboutSelfEmployment.natureOfYourBusiness.orNull}</Answer>
            </NatureBusiness>}
            case None => NodeSeq.Empty
          }}
          <TradingYear>
            {yourAccounts.whatWasOrIsYourTradingYearFrom match {
              case Some(n) => {
                <DateFrom>
                  <QuestionLabel>trading.from</QuestionLabel>
                  <Answer>{stringify(yourAccounts.whatWasOrIsYourTradingYearFrom)}</Answer>
                </DateFrom>
              }
              case None => NodeSeq.Empty
            }}
            {yourAccounts.whatWasOrIsYourTradingYearTo match {
              case Some(n) => {
                <DateTo>
                  <QuestionLabel>trading.to</QuestionLabel>
                  <Answer>{stringify(yourAccounts.whatWasOrIsYourTradingYearTo)}</Answer>
                </DateTo>
              }
              case None => NodeSeq.Empty
            }}
          </TradingYear>
          {yourAccounts.areIncomeOutgoingsProfitSimilarToTrading match {
            case Some(areIncomeOutgoingsProfitSimilarToTrading) =>
              <SameIncomeOutgoingLevels>
                <QuestionLabel>SameIncomeOutgoingLevels?</QuestionLabel>
                <Answer>{
                  areIncomeOutgoingsProfitSimilarToTrading match {
                    case "yes" => XMLValues.Yes
                    case "no" => XMLValues.No
                    case n => n
                  }}</Answer>
              </SameIncomeOutgoingLevels>
            case None => NodeSeq.Empty
          }}
          {yourAccounts.tellUsWhyAndWhenTheChangeHappened match {
            case Some(n) =>
              <WhyWhenChange>
                <QuestionLabel>why.when</QuestionLabel>
                <Answer>{n}</Answer>
              </WhyWhenChange>
            case None => NodeSeq.Empty
          }}
          {aboutSelfEmployment.whenDidTheJobFinish match {
            case Some(n) => {
              <DateEnded>
                <QuestionLabel>aboutSelfEmployment.whenDidTheJobFinish</QuestionLabel>
                <Answer>{stringify(aboutSelfEmployment.whenDidTheJobFinish)}</Answer>
              </DateEnded>
            }
            case None => NodeSeq.Empty
          }}
          {aboutSelfEmployment.haveYouCeasedTrading match {
          case Some(n) => {
            <TradingCeased>
              <QuestionLabel>aboutSelfEmployment.haveYouCeasedTrading</QuestionLabel>
              <Answer>{aboutSelfEmployment.haveYouCeasedTrading match {
                case Some(n) => n match {
                  case "yes" => XMLValues.Yes
                  case "no" => XMLValues.No
                  case n => n
                }
                case _ => NodeSeq.Empty
              }}</Answer>
            </TradingCeased>
          }
          case None => NodeSeq.Empty
        }}

        </RecentJobDetails>
      }
    }

    if (employment.beenSelfEmployedSince1WeekBeforeClaim == yes) {

      <SelfEmployment>
        <SelfEmployedNow>
          <QuestionLabel>selfepmloyed.now</QuestionLabel>
          <Answer>{aboutSelfEmployment.areYouSelfEmployedNow match {
            case "yes" => XMLValues.Yes
            case "no" => XMLValues.No
            case n => n
          }}</Answer>
        </SelfEmployedNow>
        {jobDetails()}
        <CareExpensesChildren>
          <QuestionLabel>chld.expenses</QuestionLabel>
          <Answer>{pensionAndExpenses.doYouPayToLookAfterYourChildren match {
            case "yes" => XMLValues.Yes
            case "no" => XMLValues.No
            case n => n
          }}</Answer>
        </CareExpensesChildren>


        {childCareExpenses(claim)}
        <CareExpensesCaree>
          <QuestionLabel>care.expenses</QuestionLabel>
          <Answer>{pensionAndExpenses.didYouPayToLookAfterThePersonYouCaredFor match {
            case "yes" => XMLValues.Yes
            case "no" => XMLValues.No
            case n => n
          }}</Answer>
        </CareExpensesCaree>
        {careExpenses(claim)}
        <PaidForPension>
          <QuestionLabel>self.pension</QuestionLabel>
          <Answer>{pensionAndExpenses.doYouPayToPensionScheme match {
            case "yes" => XMLValues.Yes
            case "no" => XMLValues.No
            case n => n
          }}</Answer>
        </PaidForPension>
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
        <CarerName>
          <QuestionLabel>child.carer</QuestionLabel>
          <Answer>{childCareExpenses.nameOfPerson}</Answer>
        </CarerName>
        <Expense>
          <Payment>
            <QuestionLabel>ChildCareExpensesHowMuch?</QuestionLabel>
            <Answer>
              <Currency>{GBP}</Currency>
              <Amount>{childCareExpenses.howMuchYouPay}</Amount>
            </Answer>
          </Payment>
          <Frequency>
            <QuestionLabel>ChildCareExpensesFrequency?</QuestionLabel>
            {childCareExpenses.howOftenPayChildCare.other match {
            case Some(n) => {
              <Other>{n}</Other>
            }
            case _ => NodeSeq.Empty
          }}
            <Answer>{PensionPaymentFrequency.mapToHumanReadableString(childCareExpenses.howOftenPayChildCare)}</Answer>
          </Frequency>
        </Expense>
        <RelationshipCarerToClaimant>
          <QuestionLabel>child.care.rel.claimant</QuestionLabel>
          <Answer>{childCareExpenses.whatRelationIsToYou}</Answer>
        </RelationshipCarerToClaimant>
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
        <CarerName>
          <QuestionLabel>child.carer</QuestionLabel>
          <Answer>{expensesWhileAtWork.nameOfPerson}</Answer>
        </CarerName>
        <Expense>
          <Payment>
            <QuestionLabel>CareExpensesHowMuch?</QuestionLabel>
            <Answer>
              <Currency>{GBP}</Currency>
              <Amount>{expensesWhileAtWork.howMuchYouPay}</Amount>
            </Answer>
          </Payment>
          <Frequency>
            <QuestionLabel>CareExpensesFrequency?</QuestionLabel>
            {expensesWhileAtWork.howOftenPayExpenses.other match {
            case Some(n) => {
              <Other>{n}</Other>
            }
            case _ => NodeSeq.Empty
          }}
            <Answer>{PensionPaymentFrequency.mapToHumanReadableString(expensesWhileAtWork.howOftenPayExpenses)}</Answer>
          </Frequency>
        </Expense>
        <RelationshipCarerToClaimant>
          <QuestionLabel>child.care.rel.claimant</QuestionLabel>
          <Answer>{expensesWhileAtWork.whatRelationIsToYou}</Answer>
        </RelationshipCarerToClaimant>
        <RelationshipCarerToCaree>
          <QuestionLabel>care.carer.rel.caree</QuestionLabel>
          <Answer>{expensesWhileAtWork.whatRelationIsTothePersonYouCareFor}</Answer>
        </RelationshipCarerToCaree>
      </CareExpenses>
    } else NodeSeq.Empty
  }

  def pensionScheme(claim: Claim) = {
    val pensionsAndExpensesOption = claim.questionGroup[SelfEmploymentPensionsAndExpenses]
    val pensionAndExpenses = pensionsAndExpensesOption.getOrElse(SelfEmploymentPensionsAndExpenses())

    val hasPensionScheme = pensionAndExpenses.doYouPayToPensionScheme == yes

    if (hasPensionScheme) {
        <PensionScheme>
          <Payment>
            <QuestionLabel>self.pension.amount</QuestionLabel>
            <Answer>
              {moneyStructure(pensionAndExpenses.howMuchDidYouPay.orNull)}
            </Answer>
          </Payment>
          <Frequency>
            <QuestionLabel>self.pension.frequency</QuestionLabel>
            {if(!pensionAndExpenses.howOften.isEmpty && PensionPaymentFrequency.mapToHumanReadableString(pensionAndExpenses.howOften.get) == "Other"){
              <Other>{pensionAndExpenses.howOften.get.other}</Other>
            }}
            <Answer>{if(pensionAndExpenses.howOften.isEmpty){} else PensionPaymentFrequency.mapToHumanReadableString(pensionAndExpenses.howOften.get)}</Answer>
          </Frequency>
        </PensionScheme>
    } else NodeSeq.Empty
  }
}