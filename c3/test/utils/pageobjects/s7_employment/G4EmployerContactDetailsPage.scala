package utils.pageobjects.s7_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

final class G4EmployerContactDetailsPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) extends ClaimPage(browser, G4EmployerContactDetailsPage.url.replace(":jobID",iteration.toString), G4EmployerContactDetailsPage.title, previousPage, iteration) {
  declareAddress("#address", "EmploymentEmployerAddress_" + iteration)
  declareInput("#postcode","EmploymentEmployerPostcode_" + iteration)
  declareInput("#phoneNumber","EmploymentEmployerPhoneNumber_" + iteration)
}

object G4EmployerContactDetailsPage {
  val title = "Employer's contact details - Employment History".toLowerCase

  val url  = "/employment/employers-contact-details/:jobID"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) = new G4EmployerContactDetailsPage(browser,previousPage,iteration)
}

trait G4EmployerContactDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G4EmployerContactDetailsPage (browser,iteration = 1)
}
