package utils.pageobjects.s6_education

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

/**
 * TODO write description
 * @author Jorge Migueis
 *         Date: 06/08/2013
 */
class G1YourCourseDetailsPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G1YourCourseDetailsPage.url, G1YourCourseDetailsPage.title, previousPage) {
  declareInput("#courseType","EducationTypeOfCourse")
  declareInput("#courseTitle","EducationCourseTitle")
  declareDate("#startDate","EducationWhenDidYouStartTheCourse")
  declareDate("#expectedEndDate","EducationWhenDoYouExpectTheCourseToEnd")
  declareDate("#finishedDate","EducationWhenDidYouFinish")
  declareInput("#studentReferenceNumber","EducationYourStudentReferenceNumber")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G1YourCourseDetailsPage {
  val title = "Your Course Details - About your education"

  val url  = "/education/your-course-details"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G1YourCourseDetailsPage(browser,previousPage)
}

/** The context for Specs tests */
trait G1YourCourseDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1YourCourseDetailsPage buildPageWith browser
}