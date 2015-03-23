package utils.pageobjects.tests

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s2_about_you.G1YourDetailsPageContext
import controllers.ClaimScenarioFactory
import utils.pageobjects.s11_pay_details.G1HowWePayYouPageContext
import utils.pageobjects.s1_disclaimer.G1DisclaimerPagePageContext
import utils.pageobjects.{Page, PageObjectsContext, PageObjects, TestData}
import app._
import play.api.i18n.Messages
import utils.pageobjects.s1_2_claim_date.{G1ClaimDatePageContext, G1ClaimDatePage}

class WebSearchSpec extends Specification with Tags{
  "Web Search Actions " should {

    "be presented" in new WithBrowser with G1YourDetailsPageContext {
      page goToThePage()
    }

    "be able to read Input, Select, Nino, YesNo, Address and Date elements." in new WithBrowser with PageObjects {
      val claim = ClaimScenarioFactory yourDetailsEnablingTimeOutsideUK()
      val yourDetailsPage = fillClaimDate(context) submitPage ()
      yourDetailsPage goToThePage()
      yourDetailsPage fillPageWith claim
      val firstName = yourDetailsPage readInput("#firstName")
      firstName.get mustEqual claim.AboutYouFirstName
      val title = yourDetailsPage readSelect("#title")
      title.get mustEqual claim.AboutYouTitle
      val nino  = yourDetailsPage readNino("#nationalInsuranceNumber")
      nino.get mustEqual claim.AboutYouNINO
      val contactPage = yourDetailsPage submitPage()
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

    "be able to read Check box" in new WithBrowser with G1DisclaimerPagePageContext {
      val claim = new TestData
      claim.DisclaimerTextAndTickBox = "yes"
      page goToThePage()
      val checked = page.readCheck("#read")
      assert(!checked.isDefined)
      page fillPageWith claim
      val checked2 = page.readCheck("#read")
      checked2.get mustEqual "yes"
    }
  } section "integration"

  "A page with Web Search Actions " should {
    "be able to populate a claim using data read with WebSearchActions." in new WithBrowser with PageObjects {
      val claimSource = ClaimScenarioFactory yourDetailsEnablingTimeOutsideUK()
      val claimRead = new TestData

      val claim = ClaimScenarioFactory yourDetailsEnablingTimeOutsideUK()
      val yourDetailsPage = fillClaimDate(context) submitPage ()

      yourDetailsPage goToThePage()
      yourDetailsPage fillPageWith claimSource
      yourDetailsPage populateClaim claimRead
      val contactPage = yourDetailsPage submitPage()
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

  def fillClaimDate (context:PageObjectsContext):Page = {
    val claimDatePage = G1ClaimDatePage (context)
    val claimDate = ClaimScenarioFactory s12ClaimDate()

    claimDatePage goToThePage()
    claimDatePage fillPageWith claimDate
    claimDatePage
  }

}