package controllers.s_education

import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import controllers.{PreviewTestUtils, ClaimScenarioFactory, BrowserMatchers, Formulate}
import utils.pageobjects._
import utils.pageobjects.s_breaks.GBreaksInCarePage
import utils.pageobjects.s_education.GYourCourseDetailsPage
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_employment.{GEmploymentPage, GBeenEmployedPage}
import utils.helpers.PreviewField._

class GYourCourseDetailsIntegrationSpec extends Specification with Tags {
  "Your course details Page" should {
    "be presented" in new WithBrowser with PageObjects {
      val educationPage = GYourCourseDetailsPage(context)
      educationPage goToThePage()
    }

    "contain errors on invalid submission" in new WithBrowser with PageObjects {
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = ClaimScenarioFactory.s12ClaimDate()
      claimDatePage fillPageWith claimDate
      claimDatePage submitPage()

      val educationPage = GYourCourseDetailsPage(context)
      val claim = new TestData
      claim.EducationHaveYouBeenOnACourseOfEducation = "Yes"
      claim.EducationCourseTitle = "Course 101"
      claim.EducationNameofSchool = "Lancaster University"
      claim.EducationNameOfMainTeacherOrTutor = "Dr. Ray Charles"
      claim.EducationPhoneNumber = "123456789"
      claim.EducationWhenDoYouExpectTheCourseToEnd = "10/04/2013"

      educationPage goToThePage()
      educationPage fillPageWith claim
      val pageWithErrors = educationPage submitPage()
      pageWithErrors.listErrors.size mustEqual 1

    }

    "navigate to next page on valid submission with all fields filled in" in new WithBrowser with PageObjects {
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = ClaimScenarioFactory.s12ClaimDate()
      claimDatePage fillPageWith claimDate
      claimDatePage submitPage()

      val educationPage = GYourCourseDetailsPage(context)
      val claim = ClaimScenarioFactory.s6Education()
      educationPage goToThePage()
      educationPage fillPageWith claim
      educationPage submitPage() must beAnInstanceOf[GEmploymentPage]
     }

    "navigate back" in new WithBrowser with PageObjects {
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = ClaimScenarioFactory.s12ClaimDate()
      claimDatePage fillPageWith claimDate
      claimDatePage submitPage()

      val educationPage = GYourCourseDetailsPage(context)
      educationPage goToThePage()
      educationPage.url mustEqual GYourCourseDetailsPage.url
      val previousPage = educationPage goBack()
      previousPage.url mustNotEqual GYourCourseDetailsPage.url

    }

    "Navigate back and Course title is displayed when Have you been on a course of education is yes" in new WithBrowser with PageObjects{
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = ClaimScenarioFactory.s12ClaimDate()
      claimDatePage fillPageWith claimDate
      claimDatePage submitPage()

      val educationPage = GYourCourseDetailsPage(context)
      val claim = ClaimScenarioFactory.s6Education()
      educationPage goToThePage()
      educationPage fillPageWith claim
      val employmentPage = educationPage submitPage()
      val courseTitle = employmentPage goBack() readInput("#courseTitle")
      courseTitle.get mustEqual "Course 101"
    }

  } section("integration", models.domain.Education.id)

}