package controllers.preview

import org.specs2.mutable._
import utils.WithBrowser
import utils.pageobjects.{PageObjectsContext, PageObjects}
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_claim_date.GClaimDatePage
import controllers.ClaimScenarioFactory
import utils.pageobjects.your_income.GYourIncomePage

class PreviewPageEmploymentContentSpec extends Specification {
  section("preview")
  "Preview Page" should {
    "display employment data" in new WithBrowser with PageObjects {
      fillEmploymentSection(context)
      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source

      source must contain("Your income")
      source must contain("Have you been employed at any time since 10 April 2016?")
      source must contain("Yes")
      source must contain("Jobs")
      source must contain("Tesco's")
      source must contain("From 01/01/2013 To 01/07/2013")
      source must contain("£600 every time including expenses")
      source must contain("Have you been self-employed at any time since 3 October 2016?")
      source must contain("Started 11 September 2001")
      source must contain("Expenses included")
    }
  }
  section("preview")

  def fillEmploymentSection (context:PageObjectsContext) = {
    val employmentData = ClaimScenarioFactory.s7Employment()
    val selfEmploymentData = ClaimScenarioFactory.s9SelfEmployment

    val claimDatePage = GClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate
    claimDatePage submitPage()

    val employmentPage = GYourIncomePage(context)
    employmentPage goToThePage ()
    employmentPage fillPageWith employmentData

    val jobDetailsPage = employmentPage submitPage()
    jobDetailsPage fillPageWith employmentData

    val lastWagePage = jobDetailsPage submitPage()
    lastWagePage fillPageWith employmentData

    val pensionsPage = lastWagePage submitPage()
    pensionsPage fillPageWith employmentData

    val beenEmployedPage = pensionsPage submitPage()
    employmentData.EmploymentHaveYouBeenEmployedAtAnyTime_1 = "No"
    beenEmployedPage fillPageWith employmentData

    val additionalInfoPage = beenEmployedPage submitPage()
    additionalInfoPage fillPageWith employmentData

    val selfEmploymentPage = additionalInfoPage submitPage()
    selfEmploymentPage fillPageWith selfEmploymentData

    val selfEmployedPensionsPage = selfEmploymentPage submitPage()
    selfEmployedPensionsPage fillPageWith selfEmploymentData
    selfEmployedPensionsPage.submitPage()
  }
}
