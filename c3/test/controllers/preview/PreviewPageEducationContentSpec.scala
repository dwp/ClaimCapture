package controllers.preview

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.{TestData, PageObjectsContext, PageObjects}
import controllers.ClaimScenarioFactory
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage
import utils.pageobjects.s6_education.G1YourCourseDetailsPage


class PreviewPageEducationContentSpec extends Specification with Tags {

  "Preview Page" should {
    "display education data - when in education" in new WithBrowser with PageObjects{

      fillEducationSection(context)
      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source()

      source.contains("Education") must beTrue
      source.contains("Course 101") must beTrue
      source.contains("Lancaster University") must beTrue
      source.contains("Dr. Ray Charles") must beTrue
      source.contains("123456789") must beTrue
      source.contains("10 April, 2013 - 10 April, 2013") must beTrue
    }

    "display Question - when not in education" in new WithBrowser with PageObjects{

      val educationData = new TestData
      educationData.EducationHaveYouBeenOnACourseOfEducation = "No"

      fillEducationSection(context, educationData)
      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source()

      source.contains("Education") must beTrue
      source.contains("Have you been on a course of education since your claim date?") must beTrue
      source.contains("Course title") must beFalse
      source.contains("Name of school, college or university") must beFalse
      source.contains("Name of main teacher or tutor") must beFalse
      source.contains("Course contact number") must beFalse
      source.contains("Start/end dates") must beFalse
    }

  }

  def fillEducationSection(context:PageObjectsContext, educationData:TestData = ClaimScenarioFactory.s6Education) = {
    val claimDatePage = G1ClaimDatePage(context)
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
