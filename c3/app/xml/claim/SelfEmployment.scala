package xml.claim

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
          <DateStarted>{stringify(Some(aboutSelfEmployment.whenDidYouStartThisJob))}</DateStarted>
          <NatureOfBusiness>{aboutSelfEmployment.natureOfYourBusiness.orNull}</NatureOfBusiness>
          <TradingYear>
            <DateFrom>{stringify(yourAccounts.whatWasOrIsYourTradingYearFrom)}</DateFrom>
            <DateTo>{stringify(yourAccounts.whatWasOrIsYourTradingYearTo)}</DateTo>
          </TradingYear>
        </CurrentJobDetails>
      } else {
        <RecentJobDetails>
          <DateStarted>{stringify(Some(aboutSelfEmployment.whenDidYouStartThisJob))}</DateStarted>
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
          <HasAccountant>{NotAsked}</HasAccountant>
          <ContactAccountant>{NotAsked}</ContactAccountant>
        </Accountant>
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
        <CarerAddress>{postalAddressStructure(None, None)}</CarerAddress>
        <ConfirmAddress>{yes}</ConfirmAddress>
        <WeeklyPayment>
          <Currency></Currency>
          <Amount>{NotAsked}</Amount>
        </WeeklyPayment>
        <RelationshipCarerToClaimant>{childCareExpenses.whatRelationIsToYou}</RelationshipCarerToClaimant>
        <ChildDetails>
          <Name>{NotAsked}</Name>
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
        <ConfirmAddress>{yes}</ConfirmAddress>
        <WeeklyPayment>
          <Currency></Currency>
          <Amount>{NotAsked}</Amount>
        </WeeklyPayment>
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
        <Payment>{moneyStructure(currencyAmount(pensionAndExpenses.howMuchDidYouPay))}</Payment>
        <Frequency>{if(pensionAndExpenses.howOften.isEmpty){} else pensionAndExpenses.howOften.get.frequency}</Frequency>
      </PensionScheme>
    } else NodeSeq.Empty
  }

  def currencyAmount(currency: Option[String]) = {
    val poundSign = "£"
    currency match {
      case Some(s) => {
        if(s.split(poundSign).size >1) s.split(poundSign)(1)
        else s
      }
      case _ =>""
    }
  }
}