package utils.pageobjects.s7_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

final class G3EmployerContactDetailsPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) extends ClaimPage(browser, G3EmployerContactDetailsPage.url.replace(":jobID",iteration.toString), G3EmployerContactDetailsPage.title, previousPage, iteration) {
  declareAddress("#address", "EmploymentEmployerAddress_" + iteration)
  declareInput("#postcode","EmploymentEmployerPostcode_" + iteration)
  declareInput("#phoneNumber","EmploymentEmployerPhoneNumber_" + iteration)
}

object G3EmployerContactDetailsPage {
  val title = "Employer's contact details - Employment History".toLowerCase

  val url  = "/employment/employers-contact-details/:jobID"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) = new G3EmployerContactDetailsPage(browser,previousPage,iteration)
}

trait G3EmployerContactDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G3EmployerContactDetailsPage buildPageWith (browser,iteration = 1)
}
