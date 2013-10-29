package xml

import app.{PensionPaymentFrequency, XMLValues}
import models.domain._
import scala.xml.NodeSeq
import app.XMLValues._
import xml.XMLHelper._

object SelfEmployment {

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
          <NatureBusiness>
            <QuestionLabel>selfemployed.business</QuestionLabel>
            <Answer>{aboutSelfEmployment.natureOfYourBusiness.orNull}</Answer>
          </NatureBusiness>
          <TradingYear>
            <DateFrom>
              <QuestionLabel>trading.from</QuestionLabel>
              <Answer>{stringify(yourAccounts.whatWasOrIsYourTradingYearFrom)}</Answer>
            </DateFrom>
            <DateTo>
              <QuestionLabel>trading.to</QuestionLabel>
              <Answer>{stringify(yourAccounts.whatWasOrIsYourTradingYearTo)}</Answer>
            </DateTo>
          </TradingYear>
        </CurrentJobDetails>
      } else {
        <RecentJobDetails>
          <DateStarted>
            <QuestionLabel>selfemployed.started</QuestionLabel>
            <Answer>{stringify(Some(aboutSelfEmployment.whenDidYouStartThisJob))}</Answer>
          </DateStarted>
          <NatureBusiness>
            <QuestionLabel>selfemployed.business</QuestionLabel>
            <Answer>{aboutSelfEmployment.natureOfYourBusiness.orNull}</Answer>
          </NatureBusiness>
          <TradingYear>
            <DateFrom>
              <QuestionLabel>trading.from</QuestionLabel>
              <Answer>{stringify(yourAccounts.whatWasOrIsYourTradingYearFrom)}</Answer>
            </DateFrom>
            <DateTo>
              <QuestionLabel>trading.to</QuestionLabel>
              <Answer>{stringify(yourAccounts.whatWasOrIsYourTradingYearTo)}</Answer>
            </DateTo>
          </TradingYear>
          <DateEnded>{stringify(aboutSelfEmployment.whenDidTheJobFinish)}</DateEnded>
          <TradingCeased>{aboutSelfEmployment.haveYouCeasedTrading.orNull}</TradingCeased>
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
            <Answer>{if(pensionAndExpenses.howOften.isEmpty){} else PensionPaymentFrequency.mapToHumanReadableString(pensionAndExpenses.howOften.get)}</Answer>
          </Frequency>
        </PensionScheme>
    } else NodeSeq.Empty
  }
}