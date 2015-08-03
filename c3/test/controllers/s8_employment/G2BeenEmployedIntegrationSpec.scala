package controllers.s8_employment

import controllers.ClaimScenarioFactory
import org.specs2.mutable.{Tags, Specification}
import play.api.test.FakeApplication
import controllers.ClaimScenarioFactory._
import utils.WithBrowser
import utils.pageobjects._
import utils.pageobjects.s_education.GYourCourseDetailsPage
import utils.pageobjects.s8_employment._
import utils.pageobjects.s_self_employment.GAboutSelfEmploymentPage
import scala.Some
import utils.pageobjects.s_claim_date.{GClaimDatePage, GClaimDatePageContext}
import utils.pageobjects.s9_other_money.G1AboutOtherMoneyPage

class G2BeenEmployedIntegrationSpec extends Specification with Tags {
  "Been Employed" should {
    "present, having indicated that the carer has been employed" in new WithBrowser with PageObjects {
      val claimDate = new GClaimDatePage(context) goToThePage()
      claimDate.fillPageWith(s7SelfEmployedAndEmployed())
      claimDate.submitPage()

      override implicit def implicitApp: FakeApplication = super.implicitApp

      val employment = new G1EmploymentPage(claimDate.ctx) goToThePage()

      employment must beAnInstanceOf[G1EmploymentPage]
    }

    """be bypassed and go onto "other money" having indicated that "employment" is not required.""" in new WithBrowser with PageObjects {
      val claimDate = new GClaimDatePage(context) goToThePage()
      claimDate.fillPageWith(s7NotEmployedNorSelfEmployed())
      claimDate.submitPage()

      val employment = new G1EmploymentPage(claimDate.ctx) goToThePage()
      employment.fillPageWith(s7NotEmployedNorSelfEmployed())
      val otherMoney = employment.submitPage()


      otherMoney must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

    """progress to next section i.e. "self employed".""" in new WithBrowser with PageObjects{
      val claimDate = new GClaimDatePage(context) goToThePage()
      claimDate.fillPageWith(s7SelfEmployedAndEmployed())
      claimDate.submitPage()

      val employment = new G1EmploymentPage(claimDate.ctx) goToThePage()
      employment.fillPageWith(s7SelfEmployedAndEmployed())
      val selfEmployment = employment.submitPage()


      selfEmployment must beAnInstanceOf[GAboutSelfEmploymentPage]
    }

    "start employment entry" in new WithBrowser with PageObjects {
      val claimDate = new GClaimDatePage(context) goToThePage()
      claimDate.fillPageWith(s7EmployedNotSelfEmployed())
      claimDate.submitPage()

      val employment = new G1EmploymentPage(claimDate.ctx) goToThePage()
      employment.fillPageWith(s7EmployedNotSelfEmployed())
      val jobDetails = employment.submitPage()


      jobDetails must beAnInstanceOf[G3JobDetailsPage]
    }

    "show 1 error upon submitting no mandatory data" in new WithBrowser with PageObjects with EmployedHistoryPage {
      val historyPage = goToHistoryPage(ClaimScenarioFactory.s7EmploymentMinimal())
      historyPage must beAnInstanceOf[G2BeenEmployedPage]
      historyPage submitPage()

      historyPage.listErrors.size shouldEqual 1
    }

    """go back to "education".""" in new WithBrowser with PageObjects {
      val claimDate = new GClaimDatePage(context) goToThePage()
      claimDate.fillPageWith(s7EmployedNotSelfEmployed())
      claimDate.submitPage()

      val education = new GYourCourseDetailsPage(claimDate.ctx) goToThePage()
      val employment = new G1EmploymentPage(education.ctx) goToThePage()
      employment.goBack() must beAnInstanceOf[GYourCourseDetailsPage]
    }

    """remember been employed question""" in new WithBrowser with EmployedHistoryPage {
      val employmentData = ClaimScenarioFactory.s7EmploymentMinimal()
      var historyPage = goToHistoryPage(ClaimScenarioFactory.s7EmploymentMinimal())
      historyPage must beAnInstanceOf[G2BeenEmployedPage]
      employmentData.EmploymentHaveYouBeenEmployedAtAnyTime_1 = "No"
      historyPage fillPageWith employmentData
      val nextPage = historyPage submitPage()

      nextPage must beAnInstanceOf[G9EmploymentAdditionalInfoPage]
      historyPage = nextPage goBack()
      historyPage.readYesNo("#beenEmployed") mustNotEqual None
    }

    """have job data after filling a job""" in new WithBrowser with EmployedHistoryPage {
      val employmentData = ClaimScenarioFactory.s7EmploymentMinimal()
      var historyPage = goToHistoryPage(ClaimScenarioFactory.s7EmploymentMinimal())
      historyPage must beAnInstanceOf[G2BeenEmployedPage]
      historyPage.source must contain("Tesco's")
      historyPage.source must contain("01/01/2013")
    }

    """Display start date with text "Before" when start date is before claim date""" in new WithBrowser with EmployedHistoryPage {
      var historyPage = goToHistoryPage(ClaimScenarioFactory.s7EmploymentBeforeClamDateYes())
      historyPage must beAnInstanceOf[G2BeenEmployedPage]
      historyPage.source must contain("Before 10/09/2016") // This is one month before claim date
    }

  } section("integration", models.domain.Employed.id)
}

trait EmployedHistoryPage extends GClaimDatePageContext {
  this: WithBrowser[_] =>

  def goToHistoryPage(employmentData:TestData) = {
    val claim = ClaimScenarioFactory.s12ClaimDate()
    page goToThePage()
    page fillPageWith claim
    page submitPage()

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