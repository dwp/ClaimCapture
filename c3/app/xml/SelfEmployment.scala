package xml

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
          <Answer>{aboutSelfEmployment.areYouSelfEmployedNow}</Answer>
        </SelfEmployedNow>
        {jobDetails()}
        <CareExpensesChildren>{pensionAndExpenses.doYouPayToLookAfterYourChildren}</CareExpensesChildren>
        {childCareExpenses(claim)}
        <CareExpensesCaree>{pensionAndExpenses.didYouPayToLookAfterThePersonYouCaredFor}</CareExpensesCaree>
        {careExpenses(claim)}
        <PaidForPension>{pensionAndExpenses.doYouPayToPensionScheme}</PaidForPension>
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
        <CarerName>{childCareExpenses.nameOfPerson}</CarerName>
        <RelationshipCarerToClaimant>{childCareExpenses.whatRelationIsToYou}</RelationshipCarerToClaimant>
        <ChildDetails>
          <RelationToChild>{childCareExpenses.whatRelationIsTothePersonYouCareFor}</RelationToChild>
        </ChildDetails>
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
        <CarerName>{expensesWhileAtWork.nameOfPerson}</CarerName>
        <RelationshipCarerToClaimant>{expensesWhileAtWork.whatRelationIsToYou}</RelationshipCarerToClaimant>
        <RelationshipCarerToCaree>{expensesWhileAtWork.whatRelationIsTothePersonYouCareFor}</RelationshipCarerToCaree>
      </CareExpenses>
    } else NodeSeq.Empty
  }

  def pensionScheme(claim: Claim) = {
    val pensionsAndExpensesOption = claim.questionGroup[SelfEmploymentPensionsAndExpenses]
    val pensionAndExpenses = pensionsAndExpensesOption.getOrElse(SelfEmploymentPensionsAndExpenses())

    val hasPensionScheme = pensionAndExpenses.doYouPayToPensionScheme == yes

    if (hasPensionScheme) {
      <PensionScheme>
        <Type>personal_private</Type>
        <Payment>{moneyStructure(pensionAndExpenses.howMuchDidYouPay.orNull)}</Payment>
        <Frequency>{if(pensionAndExpenses.howOften.isEmpty){} else pensionAndExpenses.howOften.get.frequency}</Frequency>
      </PensionScheme>
    } else NodeSeq.Empty
  }
}