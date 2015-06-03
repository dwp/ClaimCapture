package utils.pageobjects.s6_education

import utils.WithBrowser
import utils.pageobjects._

/**
 * TODO write description
 * @author Jorge Migueis
 *         Date: 06/08/2013
 */
class G1YourCourseDetailsPage (ctx:PageObjectsContext) extends ClaimPage(ctx, G1YourCourseDetailsPage.url) {
  declareYesNo("#beenInEducationSinceClaimDate", "EducationHaveYouBeenOnACourseOfEducation")
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
  val url  = "/education/your-course-details"

  def apply(ctx:PageObjectsContext) = new G1YourCourseDetailsPage(ctx)
}

/** The context for Specs tests */
trait G1YourCourseDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1YourCourseDetailsPage (PageObjectsContext(browser))
}