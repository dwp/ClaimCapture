package xml

import app.XMLValues
import models.domain._
import controllers.Mappings.{yes, no}
import scala.xml.NodeSeq
import xml.XMLHelper._
import models.yesNo.YesNoWithText

object SelfEmployment {

  def xml(claim: Claim) = {
    val employment = claim.questionGroup[models.domain.Employment].getOrElse(models.domain.Employment(beenSelfEmployedSince1WeekBeforeClaim = no))

    val aboutSelfEmployment = claim.questionGroup[AboutSelfEmployment].getOrElse(AboutSelfEmployment(areYouSelfEmployedNow = no))

    val yourAccounts =  claim.questionGroup[SelfEmploymentYourAccounts].getOrElse(SelfEmploymentYourAccounts())

    val pensionAndExpenses = claim.questionGroup[SelfEmploymentPensionsAndExpenses].getOrElse(SelfEmploymentPensionsAndExpenses(pensionSchemeMapping = YesNoWithText(no, None), doYouPayToLookAfterYourChildren = no, didYouPayToLookAfterThePersonYouCaredFor = no))


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
          <HasAccountant>{yourAccounts.doYouHaveAnAccountant.orNull}</HasAccountant>
          <ContactAccountant>{yourAccounts.canWeContactYourAccountant.orNull}</ContactAccountant>
          {accountantDetails(claim)}
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



  def accountantDetails(claim:Claim) = {
    val yourAccountsOption = claim.questionGroup[SelfEmploymentYourAccounts]
    val yourAccounts =  yourAccountsOption.getOrElse(SelfEmploymentYourAccounts(doYouHaveAnAccountant = Some(no)))
    val accountantContactDetailsOption = claim.questionGroup[SelfEmploymentAccountantContactDetails]
    val accountantContactDetails = accountantContactDetailsOption.getOrElse(SelfEmploymentAccountantContactDetails())

    val hasAccountant = yourAccounts.doYouHaveAnAccountant == Some(yes)

    if(hasAccountant) {
      <AccountantDetails>
        <Name>{accountantContactDetails.accountantsName}</Name>
        <Address>{postalAddressStructure(accountantContactDetails.address, accountantContactDetails.postcode.orNull)}</Address>
        <ConfirmAddress>yes</ConfirmAddress>
        <PhoneNumber>{accountantContactDetails.telephoneNumber.orNull}</PhoneNumber>
        <FaxNumber>{accountantContactDetails.faxNumber.orNull}</FaxNumber>
      </AccountantDetails>
    } else NodeSeq.Empty
  }

  def childCareExpenses(claim:Claim) = {
    val pensionsAndExpensesOption = claim.questionGroup[SelfEmploymentPensionsAndExpenses]
    val pensionAndExpenses = pensionsAndExpensesOption.getOrElse(SelfEmploymentPensionsAndExpenses())

    val childCareExpensesOption =  claim.questionGroup[ChildcareExpensesWhileAtWork]
    val childCareExpenses =  childCareExpensesOption.getOrElse(ChildcareExpensesWhileAtWork())

    val childcareProviderOption = claim.questionGroup[ChildcareProvidersContactDetails]
    val childcareProvider = childcareProviderOption.getOrElse(ChildcareProvidersContactDetails())

    val hasChildCareExpenses = pensionAndExpenses.doYouPayToLookAfterYourChildren == yes

    if(hasChildCareExpenses) {
      <ChildCareExpenses>
        <CarerName>{childCareExpenses.nameOfPerson}</CarerName>
        <CarerAddress>{postalAddressStructure(childcareProvider.address, childcareProvider.postcode)}</CarerAddress>
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

  def careExpenses(claim:Claim) = {
    val pensionsAndExpensesOption = claim.questionGroup[SelfEmploymentPensionsAndExpenses]
    val pensionAndExpenses = pensionsAndExpensesOption.getOrElse(SelfEmploymentPensionsAndExpenses())

    val expensesWhileAtWorkOption = claim.questionGroup[ExpensesWhileAtWork]
    val expensesWhileAtWork =  expensesWhileAtWorkOption.getOrElse(ExpensesWhileAtWork())

    val careProviderContactDetailsOption = claim.questionGroup[CareProvidersContactDetails]
    val careProviderContactDetails = careProviderContactDetailsOption.getOrElse(CareProvidersContactDetails())

    val hasCareExpenses = pensionAndExpenses.didYouPayToLookAfterThePersonYouCaredFor == yes

    if(hasCareExpenses) {
      <CareExpenses>
        <CarerName>{expensesWhileAtWork.nameOfPerson}</CarerName>
        <CarerAddress>{postalAddressStructure(careProviderContactDetails.address, careProviderContactDetails.postcode)}</CarerAddress>
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

  def pensionScheme(claim:Claim) = {
    val pensionsAndExpensesOption = claim.questionGroup[SelfEmploymentPensionsAndExpenses]
    val pensionAndExpenses = pensionsAndExpensesOption.getOrElse(SelfEmploymentPensionsAndExpenses())

    val hasPensionScheme = pensionAndExpenses.pensionSchemeMapping.answer == yes

    if(hasPensionScheme) {
      <PensionScheme>
        <Type>personal_private</Type>
        <Payment>{moneyStructure(pensionAndExpenses.pensionSchemeMapping.text.orNull)}</Payment>
        <Frequency>12</Frequency>
      </PensionScheme>
    } else NodeSeq.Empty
  }
}