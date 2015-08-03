package controllers.s6_education

import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import controllers.{PreviewTestUtils, ClaimScenarioFactory, BrowserMatchers, Formulate}
import utils.pageobjects._
import utils.pageobjects.s6_education.G1YourCourseDetailsPage
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s4_care_you_provide.G10BreaksInCarePage
import utils.pageobjects.s8_employment.{G1EmploymentPage, G2BeenEmployedPage}

class G1YourCourseDetailsIntegrationSpec extends Specification with Tags {
  "Your course details Page" should {
    "be presented" in new WithBrowser with PageObjects {
      val educationPage = G1YourCourseDetailsPage(context)
      educationPage goToThePage()
    }

    "contain errors on invalid submission" in new WithBrowser with PageObjects {
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = ClaimScenarioFactory.s12ClaimDate()
      claimDatePage fillPageWith claimDate
      claimDatePage submitPage()

      val educationPage = G1YourCourseDetailsPage(context)
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

      val educationPage = G1YourCourseDetailsPage(context)
      val claim = ClaimScenarioFactory.s6Education()
      educationPage goToThePage()
      educationPage fillPageWith claim
      educationPage submitPage() must beAnInstanceOf[G1EmploymentPage]
     }

    "navigate back" in new WithBrowser with PageObjects {
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = ClaimScenarioFactory.s12ClaimDate()
      claimDatePage fillPageWith claimDate
      claimDatePage submitPage()

      val educationPage = G1YourCourseDetailsPage(context)
      educationPage goToThePage()
      educationPage.url mustEqual G1YourCourseDetailsPage.url
      val previousPage = educationPage goBack()
      previousPage.url mustNotEqual G1YourCourseDetailsPage.url

    }

    "Navigate back and Course title is displayed when Have you been on a course of education is yes" in new WithBrowser with PageObjects{
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = ClaimScenarioFactory.s12ClaimDate()
      claimDatePage fillPageWith claimDate
      claimDatePage submitPage()

      val educationPage = G1YourCourseDetailsPage(context)
      val claim = ClaimScenarioFactory.s6Education()
      educationPage goToThePage()
      educationPage fillPageWith claim
      val employmentPage = educationPage submitPage()
      val courseTitle = employmentPage goBack() readInput("#courseTitle")
      courseTitle.get mustEqual "Course 101"
    }

    "Modify been in education since claim date from preview page" in new WithBrowser with PageObjects{
      val modifiedData = new TestData
      modifiedData.EducationHaveYouBeenOnACourseOfEducation = "No"

      verifyPreviewData(context, "education_beenInEducationSinceClaimDate", "Yes", modifiedData, "No")
    }

    "Modify course title from preview page" in new WithBrowser with PageObjects{
      val modifiedData = new TestData
      modifiedData.EducationCourseTitle = "Oxford University"

      verifyPreviewData(context, "education_courseTitle", "Course 101", modifiedData, "Oxford University")
    }

    "Modify name of school from preview page" in new WithBrowser with PageObjects{
      val modifiedData = new TestData
      modifiedData.EducationNameofSchool = "Oxford University"

      verifyPreviewData(context, "education_nameOfSchool", "Lancaster University", modifiedData, "Oxford University")
    }

    "Modify name of tutor from preview page" in new WithBrowser with PageObjects{
      val modifiedData = new TestData
      modifiedData.EducationNameOfMainTeacherOrTutor = "Dr Jones"

      verifyPreviewData(context, "education_nameOfTutor", "Dr. Ray Charles", modifiedData, "Dr Jones")
    }

    "Modify contact number from preview page" in new WithBrowser with PageObjects{
      val modifiedData = new TestData
      modifiedData.EducationPhoneNumber = "111111111"

      verifyPreviewData(context, "education_contactNumber", "123456789", modifiedData, "111111111")
    }

    "Modify start and end dates from preview page" in new WithBrowser with PageObjects{
      val modifiedData = new TestData
      modifiedData.EducationWhenDidYouStartTheCourse = "10/05/2013"
      modifiedData.EducationWhenDoYouExpectTheCourseToEnd = "10/05/2014"

      verifyPreviewData(context, "education_startEndDates", "10 April, 2013 - 10 April, 2013", modifiedData, "10 May, 2013 - 10 May, 2014")
    }

  } section("integration", models.domain.Education.id)

  def goToPreviewPage(context:PageObjectsContext):Page = {
    val claimDatePage = GClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate
    claimDatePage submitPage()

    val educationPage = G1YourCourseDetailsPage(context)
    val claim = ClaimScenarioFactory.s6Education()
    educationPage goToThePage()
    educationPage fillPageWith claim
    educationPage submitPage()

    val previewPage = PreviewPage(context)
    previewPage goToThePage()
  }

  def verifyPreviewData(context:PageObjectsContext, id:String, initialData:String, modifiedTestData:TestData, modifiedData:String) = {
    val previewPage = goToPreviewPage(context)
    val answerText = PreviewTestUtils.answerText(id, _:Page)

    answerText(previewPage) mustEqual initialData
    val educationPage = previewPage.clickLinkOrButton(s"#$id")

    educationPage must beAnInstanceOf[G1YourCourseDetailsPage]

    educationPage fillPageWith modifiedTestData
    val previewPageModified = educationPage submitPage()

    previewPageModified must beAnInstanceOf[PreviewPage]
    answerText(previewPageModified) mustEqual modifiedData
  }

}