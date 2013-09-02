package utils.pageobjects.tests

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s2_about_you.{G2ContactDetailsPage, G1YourDetailsPageContext, G2ContactDetailsPageContext}
import controllers.ClaimScenarioFactory

/**
 * TODO write description
 * @author Jorge Migueis
 *         Date: 29/08/2013
 */
class WebSearchSpec  extends Specification with Tags{
  "Contact Details" should {


    "contain 1 completed form" in new WithBrowser with G1YourDetailsPageContext {
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
      val outsideUkPage  = contactPage.submitPage()
      outsideUkPage fillPageWith claim
      val arriveUk = outsideUkPage readDate("#livingInUK_arrivalDate")
      arriveUk.get mustEqual claim.AboutYouWhenDidYouArriveInYheUK
    }
  } section "integration"
}
