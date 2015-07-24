package controllers.s8_employment

import play.api.test.TestBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.PageObjectsContext
import utils.pageobjects.s3_your_partner.G1YourPartnerPersonalDetailsPage
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage

trait AboutYouAndYourPartner {

  def aboutYouAndPartner(browser: TestBrowser) = {
    val claimDate = ClaimScenarioFactory.s2AboutYouWithTimeOutside()
    val pageClaimDate = new G1ClaimDatePage(PageObjectsContext(browser))
    pageClaimDate goToThePage()
    pageClaimDate fillPageWith claimDate

    val nationality = pageClaimDate.submitPage(throwException = true)
    nationality fillPageWith claimDate
    
    val abroadForMoreThan52Weeks = nationality.submitPage(throwException = true)
    abroadForMoreThan52Weeks fillPageWith claimDate

    val otherEAAStateOrSwitzerland = abroadForMoreThan52Weeks.submitPage(throwException = true)
    otherEAAStateOrSwitzerland fillPageWith claimDate

    val pageMoreAboutYou = otherEAAStateOrSwitzerland.submitPage(throwException = true)
    pageMoreAboutYou fillPageWith claimDate
    pageMoreAboutYou.submitPage(throwException = true)


    val claimAboutYourPartner = ClaimScenarioFactory.s3YourPartnerNotThePersonYouCareFor

    val pageAboutYourPartner = new G1YourPartnerPersonalDetailsPage(PageObjectsContext(browser))
    pageAboutYourPartner goToThePage()
    pageAboutYourPartner fillPageWith claimAboutYourPartner
    pageAboutYourPartner.submitPage(throwException = true)
  }
}