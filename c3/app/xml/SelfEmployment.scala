package xml

import models.domain._
import controllers.Mappings.{yes, no}
import scala.xml.NodeSeq
import xml.XMLHelper.{stringify, postalAddressStructure, moneyStructure}
import models.yesNo.YesNoWithText

object SelfEmployment {

  def xml(claim: Claim) = {
    val aboutYouEmploymentOption = claim.questionGroup[Employment]
    val employment = aboutYouEmploymentOption.getOrElse(Employment(beenSelfEmployedSince1WeekBeforeClaim = no))

    val aboutSelfEmploymentOption = claim.questionGroup[AboutSelfEmployment]
    val aboutSelfEmployment = aboutSelfEmploymentOption.getOrElse(AboutSelfEmployment(areYouSelfEmployedNow = no))

    val yourAccountsOption = claim.questionGroup[SelfEmploymentYourAccounts]
    val yourAccounts =  yourAccountsOption.getOrElse(SelfEmploymentYourAccounts())

    val pensionsAndExpensesOption = claim.questionGroup[SelfEmploymentPensionsAndExpenses]
    val pensionAndExpenses = pensionsAndExpensesOption.getOrElse(SelfEmploymentPensionsAndExpenses(pensionSchemeMapping = YesNoWithText(no, None), lookAfterChildrenMapping = YesNoWithText(no, None), lookAfterCaredForMapping = YesNoWithText(no, None)))

    if (employment.beenSelfEmployedSince1WeekBeforeClaim == yes) {

      <SelfEmployment>
        <SelfEmployedNow>{aboutSelfEmployment.areYouSelfEmployedNow}</SelfEmployedNow>
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
        <Accountant>
          <HasAccountant>{yourAccounts.doYouHaveAnAccountant.orNull}</HasAccountant>
          <ContactAccountant>{yourAccounts.canWeContactYourAccountant.orNull}</ContactAccountant>
          {accountantDetails(claim)}
        </Accountant>
        <CareExpensesChildren>{pensionAndExpenses.lookAfterChildrenMapping.answer}</CareExpensesChildren>
        {childCareExpenses(claim)}
        <CareExpensesCaree>{pensionAndExpenses.lookAfterCaredForMapping.answer}</CareExpensesCaree>
        <PaidForPension>{pensionAndExpenses.pensionSchemeMapping.answer}</PaidForPension>
        {pensionScheme(claim)}
      </SelfEmployment>

    } else NodeSeq.Empty
  }

  def accountantDetails(claim: Claim) = {
    val yourAccountsOption = claim.questionGroup[SelfEmploymentYourAccounts]
    val yourAccounts =  yourAccountsOption.getOrElse(SelfEmploymentYourAccounts(doYouHaveAnAccountant = Some(no)))
    val accountantContactDetailsOption = claim.questionGroup[SelfEmploymentAccountantContactDetails]
    val accountantContactDetails = accountantContactDetailsOption.getOrElse(SelfEmploymentAccountantContactDetails())

    val hasAccountant = yourAccounts.doYouHaveAnAccountant == Some(yes)

    if(hasAccountant) {
      <AccountantDetails>
        <Name>{accountantContactDetails.accountantsName}</Name>
        <Address>{postalAddressStructure(accountantContactDetails.address, accountantContactDetails.postcode.orNull)}</Address>
        <ConfirmAddress>no</ConfirmAddress>
        <PhoneNumber>{accountantContactDetails.telephoneNumber.orNull}</PhoneNumber>
        <FaxNumber>{accountantContactDetails.faxNumber.orNull}</FaxNumber>
      </AccountantDetails>
    } else NodeSeq.Empty
  }

  def childCareExpenses(claim: Claim) = {
    val pensionsAndExpensesOption = claim.questionGroup[SelfEmploymentPensionsAndExpenses]
    val pensionAndExpenses = pensionsAndExpensesOption.getOrElse(SelfEmploymentPensionsAndExpenses())

    val childCareExpensesOption =  claim.questionGroup[ChildcareExpensesWhileAtWork]
    val childCareExpenses =  childCareExpensesOption.getOrElse(ChildcareExpensesWhileAtWork())

    val hasChildCareExpenses = pensionAndExpenses.lookAfterChildrenMapping.answer == yes

    if(hasChildCareExpenses) {
      <ChildCareExpenses>
        <CarerName>{childCareExpenses.nameOfPerson}</CarerName>
        <CarerAddress>
          <gds:Line></gds:Line>
          <gds:Line></gds:Line>
          <gds:Line></gds:Line>
          <gds:PostCode></gds:PostCode>
        </CarerAddress>
        <ConfirmAddress>no</ConfirmAddress>
        <WeeklyPayment>{moneyStructure(childCareExpenses.howMuchYouPay.orNull)}</WeeklyPayment>
        <RelationshipCarerToClaimant>{childCareExpenses.whatRelationIsToYou.orNull}</RelationshipCarerToClaimant>
        <ChildDetails>
          <Name></Name>
          <RelationToChild>{childCareExpenses.whatRelationIsTothePersonYouCareFor.orNull}</RelationToChild>
          </ChildDetails>
      </ChildCareExpenses>
    } else NodeSeq.Empty
  }

  def pensionScheme(claim: Claim) = {
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