package utils.pageobjects.s6_pay_details

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * Page object for s6_pay_details, g2_address_of_school_college_or_university.
 * @author Saqib Kayani
 *         Date: 01/08/2013
 */
final class G2AddressOfSchoolCollegeOrUniversityPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G2AddressOfSchoolCollegeOrUniversityPage.url, G2AddressOfSchoolCollegeOrUniversityPage.title, previousPage) {
  /**
   * Reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillInput("#nameOfSchoolCollegeOrUniversity", theClaim.EducationNameofSchool)
    fillInput("#nameOfMainTeacherOrTutor", theClaim.EducationNameOfMainTeacherOrTutor)
    fillAddress("#address", theClaim.EducationAddress)
    fillInput("#postcode", theClaim.EducationPostcode)
    fillInput("#phoneNumber", theClaim.EducationPhoneNumber)
    fillInput("#faxNumber", theClaim.EducationFaxNumber)
  }
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G2AddressOfSchoolCollegeOrUniversityPage {
  val title = "Address Of School College Or University - Education"
  val url  = "/education/addressOfSchoolCollegeOrUniversity"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G2AddressOfSchoolCollegeOrUniversityPage(browser,previousPage)
}

/** The context for Specs tests */
trait G2AddressOfSchoolCollegeOrUniversityPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G2AddressOfSchoolCollegeOrUniversityPage buildPageWith browser
}