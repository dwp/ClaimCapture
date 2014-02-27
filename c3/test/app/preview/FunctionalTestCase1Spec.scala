package app.preview

import play.api.test.WithBrowser
import utils.pageobjects.s1_carers_allowance.G1BenefitsPage
import utils.pageobjects._
import utils.pageobjects.xml_validation.{XMLClaimBusinessValidation, XMLBusinessValidation}
import app.FunctionalTestCommon
import utils.pageobjects.preview.PreviewPage
import org.fluentlenium.core.filter.FilterConstructor._
import org.openqa.selenium.By

/**
 * End-to-End functional tests using input files created by Steve Moody.
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
class FunctionalTestCase1Spec extends FunctionalTestCommon {
  isolated

  "The application Claim" should {
    "Successfully run absolute Claim Test Case 1" in new WithBrowser with PageObjects {


      val page = G1BenefitsPage(context)
      val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage()
      val lastPage = page runClaimWith(claim, PreviewPage.title)

      println(lastPage.source())

      val toFindData = Seq("Name" -> Seq("AboutYouTitle","AboutYouFirstName","AboutYouMiddleName","AboutYouSurname"),
          "National insurance number" -> Seq("AboutYouNINO"),
          "Date of birth" -> Seq(""))


      toFindData.foreach{t =>
        val elemValue = context.browser.webDriver.findElements(By.xpath(s"""//dt[text()="${t._1}"]/following-sibling::dd"""))
        elemValue
      }

      println (context.browser.webDriver.findElement(By.xpath("""//dt[text()="Name"]/following-sibling::dd""")).getText)
    }

  } section ("functional","preview")
}

