package controllers.preview

import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import utils.pageobjects.{TestData, PageObjectsContext, PageObjects}
import controllers.ClaimScenarioFactory
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.s6_education.G1YourCourseDetailsPage


class PreviewPageEducationContentSpec extends Specification with Tags {

  "Preview Page" should {
    "display education data - when in education" in new WithBrowser with PageObjects{

      fillEducationSection(context)
      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source

      source must contain("Education")
      source must contain("Course 101")
      source must contain("Lancaster University")
      source must contain("Dr. Ray Charles")
      source must contain("123456789")
      source must contain("10 April, 2013 - 10 April, 2013")
    }

    "display Question - when not in education" in new WithBrowser with PageObjects{

      val educationData = new TestData
      educationData.EducationHaveYouBeenOnACourseOfEducation = "No"

      fillEducationSection(context, educationData)
      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source

      source must contain("Education")
      source must contain("Have you been on a course of education since your claim date?")
      source must not contain "Course title"
      source must not contain "Name of school, college or university"
      source must not contain "Name of main teacher or tutor"
      source must not contain "Course contact number"
      source must not contain "Start/end dates"
    }

  } section "preview"

  def fillEducationSection(context:PageObjectsContext, educationData:TestData = ClaimScenarioFactory.s6Education) = {
    val claimDatePage = GClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate
    claimDatePage submitPage()

    val educationPage = G1YourCourseDetailsPage(context)
    educationPage goToThePage()
    educationPage fillPageWith educationData
    educationPage submitPage()
  }
}
