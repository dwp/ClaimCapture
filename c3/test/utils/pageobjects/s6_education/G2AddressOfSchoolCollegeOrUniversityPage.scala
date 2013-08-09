package utils.pageobjects.s6_education

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

/**
 * TODO write description
 * @author Jorge Migueis
 *         Date: 06/08/2013
 */
class G2AddressOfSchoolCollegeOrUniversityPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G2AddressOfSchoolCollegeOrUniversityPage.url, G2AddressOfSchoolCollegeOrUniversityPage.title, previousPage) {

  declareInput("#nameOfSchoolCollegeOrUniversity","EducationNameofSchool")
  declareInput("#nameOfMainTeacherOrTutor","EducationNameOfMainTeacherOrTutor")
  declareAddress("#address","EducationAddress")
  declareInput("#postcode","EducationPostcode")
  declareInput("#phoneNumber","EducationPhoneNumber")
  declareInput("#faxNumber","EducationFaxNumber")

}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G2AddressOfSchoolCollegeOrUniversityPage {
  val title = "School, college or university's contact details - About your education"
  val url  = "/education/addressOfSchoolCollegeOrUniversity"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G2AddressOfSchoolCollegeOrUniversityPage(browser,previousPage)
}

/** The context for Specs tests */
trait G2AddressOfSchoolCollegeOrUniversityPageContext extends PageContext {
  this: WithBrowser[_] =>
  val page = G2AddressOfSchoolCollegeOrUniversityPage buildPageWith browser
}