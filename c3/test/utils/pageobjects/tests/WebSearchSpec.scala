package utils.pageobjects.tests

import org.specs2.mutable._
import utils.WithBrowser
import utils.pageobjects.s_about_you.GYourDetailsPageContext
import controllers.ClaimScenarioFactory
import utils.pageobjects.s_information.GAdditionalInfoPage
import utils.pageobjects.s_pay_details.GHowWePayYouPageContext
import utils.pageobjects.{Page, PageObjectsContext, PageObjects, TestData}
import app._
import utils.pageobjects.s_claim_date.GClaimDatePage
import play.api.i18n.{MMessages, MessagesApi}
import play.api.Play.current

class WebSearchSpec extends Specification{
  "Web Search Actions " should {

    "be presented" in new WithBrowser with GYourDetailsPageContext {
      page goToThePage()
    }

    "be able to read Input, Select, Nino, YesNo, Address and Date elements." in new WithBrowser with PageObjects {
      val claim = ClaimScenarioFactory yourDetailsEnablingTimeOutsideUK()
      val yourDetailsPage = fillClaimDate(context) submitPage ()
      yourDetailsPage goToThePage()
      yourDetailsPage fillPageWith claim
      val firstName = yourDetailsPage readInput("#firstName")
      firstName.get mustEqual claim.AboutYouFirstName
      val title = yourDetailsPage readRadio("#title")
      title.get mustEqual claim.AboutYouTitle
      val nino  = yourDetailsPage readNino("#nationalInsuranceNumber")
      nino.get mustEqual claim.AboutYouNINO
      val maritalStatusPage = yourDetailsPage submitPage()
      maritalStatusPage fillPageWith claim
      val contactPage = maritalStatusPage submitPage()
      contactPage fillPageWith claim
      val address = contactPage readAddress ("#address")
      address.get mustEqual claim.AboutYouAddress
      val postCode =  contactPage readInput ("#postcode")
      postCode.get mustEqual claim.AboutYouPostcode
    }

    "be able to read SortCode" in new WithBrowser with GHowWePayYouPageContext {
      val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
      val claim = new TestData
      claim.HowWePayYouHowWouldYouLikeToGetPaid = "yes"
      claim.HowWePayYouHowOftenDoYouWantToGetPaid = messagesApi(PaymentFrequency.FourWeekly)
      claim.HowWePayYouNameOfAccountHolder = "Despicable me"
      claim.WhoseNameOrNamesIsTheAccountIn = "Your name"
      claim.HowWePayYouFullNameOfBankorBuildingSociety = "HSBC Plc"
      claim.HowWePayYouSortCode = "091234"
      claim.HowWePayYouAccountNumber = "987234987"
      page goToThePage()
      page fillPageWith claim
      val sortCode = page readSortCode ("#bankDetails_sortCode")
      sortCode.get mustEqual claim.HowWePayYouSortCode
      val bankPage: Page = page submitPage (throwException = true)
      bankPage.url must_== GAdditionalInfoPage.url
    }
  }
section("integration")

  "A page with Web Search Actions " should {
    "be able to populate a claim using data read with WebSearchActions." in new WithBrowser with PageObjects {
      val claimSource = ClaimScenarioFactory yourDetailsEnablingTimeOutsideUK()
      val claimRead = new TestData

      val claim = ClaimScenarioFactory yourDetailsEnablingTimeOutsideUK()
      val yourDetailsPage = fillClaimDate(context) submitPage ()

      yourDetailsPage goToThePage()
      yourDetailsPage fillPageWith claimSource
      yourDetailsPage populateClaim claimRead
      val maritalStatusPage = yourDetailsPage submitPage()
      maritalStatusPage fillPageWith claimSource
      maritalStatusPage populateClaim claimRead
      val contactDetailsPage  = maritalStatusPage.submitPage()
      contactDetailsPage fillPageWith claimSource
      contactDetailsPage populateClaim claimRead
      val outsideUkPage  = contactDetailsPage.submitPage()
      outsideUkPage fillPageWith claimSource
      outsideUkPage populateClaim claimRead
      claimRead.AboutYouFirstName  mustEqual claimSource.AboutYouFirstName
      claimRead.AboutYouTitle mustEqual claimSource.AboutYouTitle
      claimRead.AboutYouNINO mustEqual claimSource.AboutYouNINO
      claimRead.AboutYouAddress mustEqual claimSource.AboutYouAddress
      claimRead.AboutYouPostcode mustEqual claimSource.AboutYouPostcode
    }
  }
  section("integration")

  def fillClaimDate (context:PageObjectsContext):Page = {
    val claimDatePage = GClaimDatePage (context)
    val claimDate = ClaimScenarioFactory s12ClaimDate()

    claimDatePage goToThePage()
    claimDatePage fillPageWith claimDate
    claimDatePage
  }

}
