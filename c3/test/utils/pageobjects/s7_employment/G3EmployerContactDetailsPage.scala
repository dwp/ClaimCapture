package utils.pageobjects.s7_employment

import play.api.test.WithBrowser
import utils.pageobjects._

final class G3EmployerContactDetailsPage(ctx:PageObjectsContext, iteration:Int) extends ClaimPage(ctx, G3EmployerContactDetailsPage.url.replace(":jobID",iteration.toString), G3EmployerContactDetailsPage.title, iteration) {
  declareAddress("#address", "EmploymentEmployerAddress_" + iteration)
  declareInput("#postcode","EmploymentEmployerPostcode_" + iteration)
  declareInput("#phoneNumber","EmploymentEmployerPhoneNumber_" + iteration)
}

object G3EmployerContactDetailsPage {
  val title = "Employer's contact details - Employment History".toLowerCase

  val url  = "/employment/employers-contact-details/:jobID"

  def apply(ctx:PageObjectsContext, iteration:Int) = new G3EmployerContactDetailsPage(ctx,iteration)
}

trait G3EmployerContactDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G3EmployerContactDetailsPage (PageObjectsContext(browser),iteration = 1)
}
