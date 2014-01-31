package utils.pageobjects.s7_employment

import play.api.test.WithBrowser
import utils.pageobjects._

final class G4EmployerContactDetailsPage(ctx:PageObjectsContext, iteration:Int) extends ClaimPage(ctx, G4EmployerContactDetailsPage.url.replace(":jobID",iteration.toString), G4EmployerContactDetailsPage.title, iteration) {
  declareAddress("#address", "EmploymentEmployerAddress_" + iteration)
  declareInput("#postcode","EmploymentEmployerPostcode_" + iteration)
  declareInput("#phoneNumber","EmploymentEmployerPhoneNumber_" + iteration)
}

object G4EmployerContactDetailsPage {
  val title = "Employer's contact details - Employment History".toLowerCase

  val url  = "/employment/employers-contact-details/:jobID"

  def apply(ctx:PageObjectsContext, iteration:Int=1) = new G4EmployerContactDetailsPage(ctx,iteration)
}

trait G4EmployerContactDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G4EmployerContactDetailsPage (PageObjectsContext(browser),iteration = 1)
}
