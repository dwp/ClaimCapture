package xml

import models.domain._
import controllers.Mappings.yes
import scala.xml.NodeSeq
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
          <DateStarted>{stringify(aboutSelfEmployment.whenDidYouStartThisJob)}</DateStarted>
          <NatureOfBusiness>{aboutSelfEmployment.natureOfYourBusiness.orNull}</NatureOfBusiness>
          <TradingYear>
            <DateFrom>{stringify(yourAccounts.whatWasOrIsYourTradingYearFrom)}</DateFrom>
            <DateTo>{stringify(yourAccounts.whatWasOrIsYourTradingYearTo)}</DateTo>
          </TradingYear>
        </CurrentJobDetails>
      } else {
        <RecentJobDetails>
          <DateStarted>{stringify(aboutSelfEmployment.whenDidYouStartThisJob)}</DateStarted>
          <NatureOfBusiness>{aboutSelfEmployment.natureOfYourBusiness.orNull}</NatureOfBusiness>
          <TradingYear>
            <DateFrom>{stringify(yourAccounts.whatWasOrIsYourTradingYearFrom)}</DateFrom>
            <DateTo>{stringify(yourAccounts.whatWasOrIsYourTradingYearTo)}</DateTo>
          </TradingYear>
          <DateEnded>{stringify(aboutSelfEmployment.whenDidTheJobFinish)}</DateEnded>
          <TradingCeased>{aboutSelfEmployment.haveYouCeasedTrading.orNull}</TradingCeased>
        </RecentJobDetails>
      }
    }

    if (employment.beenSelfEmployedSince1WeekBeforeClaim == yes) {

      <SelfEmployment>
        <SelfEmployedNow>{aboutSelfEmployment.areYouSelfEmployedNow}</SelfEmployedNow>
        {jobDetails()}
        <Accountant>
          <HasAccountant>no</HasAccountant>
          <ContactAccountant>no</ContactAccountant>
        </Accountant>
        <CareExpensesChildren>{pensionAndExpenses.doYouPayToLookAfterYourChildren}</CareExpensesChildren>
        {childCareExpenses(claim)}
        <CareExpensesCaree>{pensionAndExpenses.didYouPayToLookAfterThePersonYouCaredFor}</CareExpensesCaree>
        {careExpenses(claim)}
        <PaidForPension>{pensionAndExpenses.pensionSchemeMapping.answer}</PaidForPension>
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
        <CarerAddress>{postalAddressStructure(None, None)}</CarerAddress>
        <ConfirmAddress>yes</ConfirmAddress>
        <WeeklyPayment>{moneyStructure(childCareExpenses.howMuchYouPay)}</WeeklyPayment>
        <RelationshipCarerToClaimant>{childCareExpenses.whatRelationIsToYou}</RelationshipCarerToClaimant>
        <ChildDetails>
          <Name></Name>
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
        <CarerAddress>{postalAddressStructure(None, None)}</CarerAddress>
        <ConfirmAddress>yes</ConfirmAddress>
        <WeeklyPayment>
          <Currency>GBP</Currency>
          <Amount>{expensesWhileAtWork.howMuchYouPay}</Amount>
        </WeeklyPayment>
        <RelationshipCarerToClaimant>{expensesWhileAtWork.whatRelationIsToYou}</RelationshipCarerToClaimant>
        <RelationshipCarerToCaree>{expensesWhileAtWork.whatRelationIsTothePersonYouCareFor}</RelationshipCarerToCaree>
      </CareExpenses>
    } else NodeSeq.Empty
  }

  def pensionScheme(claim: Claim) = {
    val pensionsAndExpensesOption = claim.questionGroup[SelfEmploymentPensionsAndExpenses]
    val pensionAndExpenses = pensionsAndExpensesOption.getOrElse(SelfEmploymentPensionsAndExpenses())

    val hasPensionScheme = pensionAndExpenses.pensionSchemeMapping.answer == yes

    if (hasPensionScheme) {
      <PensionScheme>
        <Type>personal_private</Type>
        <Payment>{moneyStructure(pensionAndExpenses.pensionSchemeMapping.text1.orNull)}</Payment>
        <Frequency>{pensionAndExpenses.pensionSchemeMapping.text2.orNull}</Frequency>
      </PensionScheme>
    } else NodeSeq.Empty
  }
}