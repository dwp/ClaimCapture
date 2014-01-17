package utils.pageobjects.tests

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s2_about_you.G1YourDetailsPageContext
import controllers.ClaimScenarioFactory
import utils.pageobjects.s10_pay_details.G1HowWePayYouPageContext
import utils.pageobjects.S11_consent_and_declaration.G3DisclaimerPageContext
import utils.pageobjects.TestData
import app._
import play.api.i18n.Messages

class WebSearchSpec extends Specification with Tags{
  "Web Search Actions " should {

    "be presented" in new WithBrowser with G1YourDetailsPageContext {
      page goToThePage()
    }

    "be able to read Input, Select, Nino, YesNo, Address and Date elements." in new WithBrowser with G1YourDetailsPageContext {
      val claim = ClaimScenarioFactory yourDetailsEnablingTimeOutsideUK()
      page goToThePage()
      page fillPageWith claim
      val firstName = page readInput("#firstName")
      firstName.get mustEqual claim.AboutYouFirstName
      val title = page readSelect("#title")
      title.get mustEqual claim.AboutYouTitle
      val nino  = page readNino("#nationalInsuranceNumber")
      nino.get mustEqual claim.AboutYouNINO
      val contactPage = page submitPage()
      contactPage fillPageWith claim
      val address = contactPage readAddress ("#address")
      address.get mustEqual claim.AboutYouAddress
      val postCode =  contactPage readInput ("#postcode")
      postCode.get mustEqual claim.AboutYouPostcode
    }

    "be able to read SortCode" in new WithBrowser with G1HowWePayYouPageContext {
      val claim = new TestData
      claim.HowWePayYouHowWouldYouLikeToGetPaid = Messages(AccountStatus.BankBuildingAccount)
      claim.HowWePayYouHowOftenDoYouWantToGetPaid = Messages(PaymentFrequency.FourWeekly)
      claim.HowWePayYouNameOfAccountHolder = "Despicable me"
      claim.WhoseNameOrNamesIsTheAccountIn = "Your name"
      claim.HowWePayYouFullNameOfBankorBuildingSociety = "HSBC Plc"
      claim.HowWePayYouSortCode = "091234"
      claim.HowWePayYouAccountNumber = "987234987"
      page goToThePage()
      page fillPageWith claim
      val bankPage = page submitPage(throwException = true)
      bankPage fillPageWith claim
      val sortCode = bankPage readSortCode("#sortCode")
      sortCode.get mustEqual claim.HowWePayYouSortCode
    }

    "be able to read Check box" in new WithBrowser with G3DisclaimerPageContext {
      val claim = new TestData
      claim.ConsentDeclarationDisclaimerTextAndTickBox = "yes"
      page goToThePage()
      val checked = page.readCheck("#read")
      assert(!checked.isDefined)
      page fillPageWith claim
      val checked2 = page.readCheck("#read")
      checked2.get mustEqual "yes"
    }
  } section "integration"

  "A page with Web Search Actions " should {
    "be able to populate a claim using data read with WebSearchActions." in new WithBrowser with G1YourDetailsPageContext {
      val claimSource = ClaimScenarioFactory yourDetailsEnablingTimeOutsideUK()
      val claimRead = new TestData
      page goToThePage()
      page fillPageWith claimSource
      page populateClaim claimRead
      val contactPage = page submitPage()
      contactPage fillPageWith claimSource
      contactPage populateClaim claimRead
      val outsideUkPage  = contactPage.submitPage()
      outsideUkPage fillPageWith claimSource
      outsideUkPage populateClaim claimRead
      claimRead.AboutYouFirstName  mustEqual claimSource.AboutYouFirstName
      claimRead.AboutYouTitle mustEqual claimSource.AboutYouTitle
      claimRead.AboutYouNINO mustEqual claimSource.AboutYouNINO
      claimRead.AboutYouAddress mustEqual claimSource.AboutYouAddress
      claimRead.AboutYouPostcode mustEqual claimSource.AboutYouPostcode
    }
  }section "integration"
}