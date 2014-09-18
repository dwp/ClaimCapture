package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{ClaimScenarioFactory, WithBrowserHelper, BrowserMatchers}
import utils.pageobjects._
import utils.pageobjects.s7_employment._
import utils.pageobjects.s8_self_employment.G1AboutSelfEmploymentPage
import scala.Some
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePageContext
import utils.pageobjects.s9_other_money.G1AboutOtherMoneyPage

class G2BeenEmployedIntegrationSpec extends Specification with Tags {
  "Been Employed" should {
    "present, having indicated that the carer has been employed" in new WithBrowser with WithBrowserHelper with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim()

      goTo("/employment/been-employed")
      back
      titleMustEqual("Your job - About self employment")
    }

    """be bypassed and go onto "other money" having indicated that "employment" is not required.""" in new WithBrowser with WithBrowserHelper with BrowserMatchers with NotEmployedSinceClaimDate {
      beginClaim()

      goTo("/employment/been-employed")

      titleMustEqual("Other Money")
    }

    """progress to next section i.e. "self employed".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers with EmployedSinceClaimDate{
      beginClaim()

      next
      titleMustEqual("Your job - About self employment")
    }

    "start employment entry" in new WithBrowser with WithBrowserHelper with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim()

      goTo("/employment/been-employed")
      click("#beenEmployed_yes")
      next
      titleMustEqual("Employer Details - Employment History")
    }

    "show 1 error upon submitting no mandatory data" in new WithBrowser with EmployedHistoryPage {
      val historyPage = goToHistoryPage
      historyPage must beAnInstanceOf[G2BeenEmployedPage]
      historyPage submitPage()

      historyPage.listErrors.size shouldEqual 1
    }

    """go back to "education".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers with EducatedSinceClaimDate {
      beginClaim()

      goTo("/education/your-course-details")

      goTo("/employment/employment")
      back
      titleMustEqual("Your course details - Education")
    }

    """remember "employment" upon stating "employment" and returning""" in new WithBrowser with EmployedHistoryPage {
      val employmentData = ClaimScenarioFactory.s7EmploymentMinimal()
      var historyPage = goToHistoryPage
      historyPage must beAnInstanceOf[G2BeenEmployedPage]
      employmentData.EmploymentHaveYouBeenEmployedAtAnyTime_1 = "No"
      historyPage fillPageWith employmentData
      val nextPage = historyPage submitPage()

      nextPage must beAnInstanceOf[G1AboutOtherMoneyPage]
      historyPage = nextPage goBack()
      historyPage.readYesNo("#beenEmployed") mustEqual Some("no")
    }

    """have job data after filling a job""" in new WithBrowser with EmployedHistoryPage {
      val employmentData = ClaimScenarioFactory.s7EmploymentMinimal()
      var historyPage = goToHistoryPage
      historyPage must beAnInstanceOf[G2BeenEmployedPage]
      historyPage.source().contains(("Tesco's")) mustEqual true
      historyPage.source().contains(("01/01/2013")) mustEqual true
    }

  } section("integration", models.domain.Employed.id)
}

trait EmployedHistoryPage extends G1ClaimDatePageContext {
  this: WithBrowser[_] =>

  def goToHistoryPage = {
    val claim = new TestData
    claim.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "03/05/2014"
    page goToThePage()
    page fillPageWith claim
    page submitPage()

    val employmentData = ClaimScenarioFactory.s7EmploymentMinimal()
    employmentData.EmploymentHaveYouBeenEmployedAtAnyTime_0 = "Yes"
    employmentData.EmploymentHaveYouBeenSelfEmployedAtAnyTime = "No"
    val employmentPage = page goToPage new G1EmploymentPage(PageObjectsContext(browser))
    employmentPage fillPageWith employmentData
    val jobDetailsPage = employmentPage submitPage()
    jobDetailsPage fillPageWith employmentData
    val lastWage = jobDetailsPage submitPage()
    lastWage fillPageWith employmentData
    val pensionSchmesPage = lastWage submitPage()
    pensionSchmesPage fillPageWith employmentData
    val expensesPage = pensionSchmesPage submitPage()
    expensesPage fillPageWith employmentData
    val historyPage = expensesPage submitPage()
    historyPage
  }
}