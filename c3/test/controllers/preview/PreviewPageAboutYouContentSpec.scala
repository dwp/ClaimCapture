package controllers.preview

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.{TestData, PageObjectsContext, PageObjects}
import utils.pageobjects.preview.PreviewPage
import controllers.ClaimScenarioFactory
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage
import utils.pageobjects.s2_about_you.G7OtherEEAStateOrSwitzerlandPage

class PreviewPageAboutYouContentSpec extends Specification with Tags {
  "Preview Page" should {
    "display about you - the carer data with nationality as British" in new WithBrowser with PageObjects{
      fillAboutYouTheCarerSection(context)
      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source()
      source must contain("Name")
      source must contain("Mr John middlename Appleseed")
      source must contain("National Insurance number")
      source must contain("AB123456C")
      source must contain("Date of birth")
      source must contain("03 April, 1950")
      source must contain("Address")
      source must contain("101 Clifton Street, Blackpool FY1 2RW")
      source must contain("Contact phone or mobile number")
      source must contain("01772 888901")
      source must contain("Your claim date")
      source must contain("10 October, 2014")
      source must contain("Your nationality")
      source must contain("British")
      source must contain("Time outside of England, Scotland or Wales")
      source must contain("Yes")
      source must contain("Have you or anyone in your family claimed or been paid any benefits or pensions from any of these countries?")
      source must contain("Yes")
      source must contain("Do you or anyone in your family work or pay insurance in any of these countries?")
      source must contain("Yes")
      source must not contain "Marital status"
    }

    "display about you - the carer data with nationality another country" in new WithBrowser with PageObjects{

      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      claim.AboutYouNationalityAndResidencyNationality = "Another Country"
      claim.AboutYouNationalityAndResidencyActualNationality = "French"
      claim.AboutYouWhatIsYourMaritalOrCivilPartnershipStatus = "Single"

      fillAboutYouTheCarerSection(context, claim)
      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source()
      source must not contain "British"
      source must contain("French")
      source must contain("Marital status")
      source must contain("Single")
    }
  } section "preview"

  def fillAboutYouTheCarerSection(context:PageObjectsContext, claim:TestData = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()) = {
    val claimDatePage = G1ClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate

    val aboutYouPage =  claimDatePage submitPage()
    claim.AboutYouMiddleName = "middlename"
    aboutYouPage goToThePage()
    aboutYouPage fillPageWith claim

    val contactDetailsPage = aboutYouPage submitPage()
    contactDetailsPage fillPageWith claim

    val addressPage = contactDetailsPage submitPage()
    addressPage fillPageWith claim

    val nationalityPage = addressPage submitPage()
    nationalityPage fillPageWith claim

    var timeOutsideUk = nationalityPage submitPage()
    val timeOutsideData = ClaimScenarioFactory.abroadForMoreThan52WeeksConfirmationYes()
    timeOutsideUk fillPageWith timeOutsideData

    val periodAbroadPage = timeOutsideUk submitPage()
    val periodAbroadData = ClaimScenarioFactory.abroadForMoreThan52WeeksTrip1()
    periodAbroadPage fillPageWith periodAbroadData

    timeOutsideUk = periodAbroadPage submitPage()
    timeOutsideUk fillPageWith ClaimScenarioFactory.abroadForMoreThan52WeeksConfirmationNo()
    timeOutsideUk submitPage()

    val paymentFromAbroadPage = G7OtherEEAStateOrSwitzerlandPage(context)
    paymentFromAbroadPage goToThePage ()
    paymentFromAbroadPage fillPageWith ClaimScenarioFactory.otherEuropeanEconomicArea()

    paymentFromAbroadPage submitPage()
  }

}

