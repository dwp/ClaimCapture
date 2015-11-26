package utils.pageobjects.s_education

import utils.WithBrowser
import utils.pageobjects._

/**
 * TODO write description
 * @author Jorge Migueis
 *         Date: 06/08/2013
 */
class GYourCourseDetailsPage (ctx:PageObjectsContext) extends ClaimPage(ctx, GYourCourseDetailsPage.url) {
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
object GYourCourseDetailsPage {
  val url  = "/education/your-course-details"

  def apply(ctx:PageObjectsContext) = new GYourCourseDetailsPage(ctx)
}

/** The context for Specs tests */
trait GYourCourseDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GYourCourseDetailsPage (PageObjectsContext(browser))
}
