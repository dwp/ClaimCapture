package utils.pageobjects.tests

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s2_about_you.{G2ContactDetailsPage, G1YourDetailsPageContext, G2ContactDetailsPageContext}
import controllers.ClaimScenarioFactory
import utils.pageobjects.s10_pay_details.G1HowWePayYouPageContext
import utils.pageobjects.ClaimScenario
import utils.pageobjects.S11_consent_and_declaration.G3DisclaimerPageContext

/**
 * TODO write description
 * @author Jorge Migueis
 *         Date: 29/08/2013
 */
class WebSearchSpec  extends Specification with Tags{
  "Web Search Actions " should {


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
      val liveUk = page readYesNo("#alwaysLivedUK")
      liveUk.get mustEqual claim.AboutYouHaveYouAlwaysLivedInTheUK.toLowerCase()
      val contactPage = page submitPage()
      contactPage fillPageWith claim
      val address = contactPage readAddress ("#address")
      address.get mustEqual claim.AboutYouAddress
      val postCode =  contactPage readInput ("#postcode")
      postCode.get mustEqual claim.AboutYouPostcode
      val outsideUkPage  = contactPage.submitPage()
      outsideUkPage fillPageWith claim
      val arriveUk = outsideUkPage readDate("#livingInUK_arrivalDate")
      arriveUk.get mustEqual claim.AboutYouWhenDidYouArriveInYheUK
    }

    "be able to read SortCode" in new WithBrowser with G1HowWePayYouPageContext {
      val claim = new ClaimScenario
      claim.HowWePayYouHowWouldYouLikeToGetPaid = "bankBuildingAccount"
      claim.HowWePayYouHowOftenDoYouWantToGetPaid = "fourWeekly"
      claim.HowWePayYouNameOfAccountHolder = "Despicable me"
      claim.WhoseNameOrNamesIsTheAccountIn = "yourName"
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
      val claim = new ClaimScenario
      claim.ConsentDeclarationDisclaimerTextAndTickBox = "yes"
      page goToThePage()
      val checked = page.readCheck("#read")
      assert(!checked.isDefined)
      page fillPageWith claim
      val checked2 = page.readCheck("#read")
      checked2.get mustEqual "yes"
    }

  } section "integration"
}
