package utils.pageobjects.s6_education

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

/**
 * TODO write description
 * @author Jorge Migueis
 *         Date: 06/08/2013
 */
class G1YourCourseDetailsPage (browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G1YourCourseDetailsPage.url, G1YourCourseDetailsPage.title, previousPage) {
  declareInput("#courseTitle","EducationCourseTitle")
  declareInput("#nameOfSchoolCollegeOrUniversity","EducationNameofSchool")
  declareInput("#nameOfMainTeacherOrTutor","EducationNameOfMainTeacherOrTutor")
  declareInput("#courseContactNumber","EducationPhoneNumber")
  declareDate("#startDate","EducationWhenDidYouStartTheCourse")
  declareDate("#expectedEndDate","EducationWhenDoYouExpectTheCourseToEnd")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G1YourCourseDetailsPage {
  val title = "Your course details - About your education".toLowerCase

  val url  = "/education/your-course-details"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None) = new G1YourCourseDetailsPage(browser,previousPage)
}

/** The context for Specs tests */
trait G1YourCourseDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1YourCourseDetailsPage (browser)
}