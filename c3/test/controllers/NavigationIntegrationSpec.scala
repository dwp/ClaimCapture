package controllers

import org.specs2.mutable.Specification
import org.specs2.mutable.Tags
import play.api.test.WithBrowser
import utils.pageobjects.ClaimScenario
import utils.pageobjects.s1_carers_allowance._
import org.fluentlenium.core.Fluent

class NavigationIntegrationSpec extends Specification with Tags {
  "About you" should {
    "go from 'contact details' to the previous 'your details'." in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      def data: Fluent = {
        click("#title option[value='mr']")
        fill("#firstName") `with` "Scooby"
        fill("#surname") `with` "Doo"
        fill("#nationalInsuranceNumber_ni1") `with` "AB"
        fill("#nationalInsuranceNumber_ni2") `with` "12"
        fill("#nationalInsuranceNumber_ni3") `with` "34"
        fill("#nationalInsuranceNumber_ni4") `with` "56"
        fill("#nationalInsuranceNumber_ni5") `with` "C"
        click("#dateOfBirth_day option[value='3']")
        click("#dateOfBirth_month option[value='4']")
        fill("#dateOfBirth_year") `with` "1950"
        fill("#nationality") `with` "British"
        click("#maritalStatus option[value='s']")
        click("#alwaysLivedUK_yes")
      }

      goTo("/about-you/your-details").title shouldEqual "Your details - About you - the carer"
      data

      next.title shouldEqual "Your contact details - About you - the carer"
      back.title shouldEqual "Your details - About you - the carer"
    }
  } section "integration"

  "Browser" should {
    "not cache pages" in new WithBrowser with G1BenefitsPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "yes"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "yes"
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "yes"
      page goToThePage()

      val s1g2 = page fillPageWith claim submitPage() 
      val s1g3 = s1g2 fillPageWith claim submitPage()
      val s1g4 = s1g3 fillPageWith claim submitPage()
      val approvalPage = s1g4 fillPageWith claim submitPage()
      val backToS1G1 = approvalPage goBack() goBack() goBack() goBack()
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "no"

      backToS1G1 fillPageWith claim
      val s1g2SecondTime = backToS1G1 submitPage()

      s1g2SecondTime should beLike { case p: G2HoursPage =>
        p numberSectionsCompleted() shouldEqual 1
        val completed = p.findTarget("div[class=completed] ul li")
        completed(0) must contain("Does the person you look after get one of these benefits?")
        completed(0) must contain("No")
      }
    }
  } section "integration"
}