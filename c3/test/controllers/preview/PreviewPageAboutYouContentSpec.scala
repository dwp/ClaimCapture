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
      source.contains("Name") must beTrue
      source.contains("Mr John middlename Appleseed") must beTrue
      source.contains("National Insurance number") must beTrue
      source.contains("AB 12 34 56 C") must beTrue
      source.contains("Date of birth") must beTrue
      source.contains("03 April, 1950") must beTrue
      source.contains("Address") must beTrue
      source.contains("101 Clifton Street, Blackpool FY1 2RW") must beTrue
      source.contains("Contact phone or mobile number") must beTrue
      source.contains("01772 888901") must beTrue
      source.contains("Your claim date") must beTrue
      source.contains("10 October, 2014") must beTrue
      source.contains("Your nationality") must beTrue
      source.contains("British") must beTrue
      source.contains("Time outside of England, Scotland or Wales") must beTrue
      source.contains("Yes") must beTrue
      source.contains("Have you or anyone in your family claimed or been paid any benefits or pensions from any of these countries?") must beTrue
      source.contains("Yes") must beTrue
      source.contains("Do you or anyone in your family work or pay insurance in any of these countries?") must beTrue
      source.contains("Yes") must beTrue
      source.contains("Marital status") must beFalse
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
      source.contains("British") must beFalse
      source.contains("French") must beTrue
      source.contains("Marital status") must beTrue
      source.contains("Single") must beTrue
    }
  }

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

