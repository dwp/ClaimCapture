package xml

import models.domain.{SelfEmploymentYourAccounts, AboutSelfEmployment, Claim, Employment}
import controllers.Mappings.{yes, no}
import scala.xml.NodeSeq
import xml.XMLHelper.stringify

object SelfEmployment {

    def xml(claim: Claim) = {
      val aboutYouEmploymentOption = claim.questionGroup[Employment]
      val employment = aboutYouEmploymentOption.getOrElse(Employment(beenSelfEmployedSince1WeekBeforeClaim = no))
      val aboutSelfEmploymentOption = claim.questionGroup[AboutSelfEmployment]
      val aboutSelfEmployment = aboutSelfEmploymentOption.getOrElse(AboutSelfEmployment(areYouSelfEmployedNow = no))
      val yourAccountsOption = claim.questionGroup[SelfEmploymentYourAccounts]
      val yourAccounts =  yourAccountsOption.getOrElse(SelfEmploymentYourAccounts())

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
            <HasAccountant></HasAccountant>
            <ContactAccountant></ContactAccountant>
            {accountantDetails}
          </Accountant>
          <CareExpensesChildren>no</CareExpensesChildren>
          <CareExpensesCaree>no</CareExpensesCaree>
          <PaidForPension>no</PaidForPension>
         </SelfEmployment>

      } else NodeSeq.Empty
    }

    def accountantDetails = {
      <AccountantDetails>
        <Name>empty</Name>
        <Address>
          <gds:Line></gds:Line>
          <gds:Line></gds:Line>
          <gds:Line></gds:Line>
          <gds:PostCode></gds:PostCode>
        </Address>
        <ConfirmAddress>no</ConfirmAddress>
        <PhoneNumber></PhoneNumber>
        <FaxNumber></FaxNumber>
      </AccountantDetails>
    }
}