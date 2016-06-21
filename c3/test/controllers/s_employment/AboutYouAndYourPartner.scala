package controllers.s_employment

import play.api.test.TestBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.PageObjectsContext
import utils.pageobjects.s_your_partner.GYourPartnerPersonalDetailsPage
import utils.pageobjects.s_claim_date.GClaimDatePage

trait AboutYouAndYourPartner {

  def aboutYouAndPartner(browser: TestBrowser) = {
    val claimDate = ClaimScenarioFactory.s2AboutYouWithTimeOutside()
    val pageClaimDate = new GClaimDatePage(PageObjectsContext(browser))
    pageClaimDate goToThePage()
    pageClaimDate fillPageWith claimDate

    val nationality = pageClaimDate.submitPage(throwException = true)
    nationality fillPageWith claimDate

    val paymentsFromAbroad = nationality.submitPage(throwException = true)
    paymentsFromAbroad fillPageWith claimDate

    val pageMoreAboutYou = paymentsFromAbroad.submitPage(throwException = true)
    pageMoreAboutYou fillPageWith claimDate
    pageMoreAboutYou.submitPage(throwException = true)

    val claimAboutYourPartner = ClaimScenarioFactory.s3YourPartnerNotThePersonYouCareFor

    val pageAboutYourPartner = new GYourPartnerPersonalDetailsPage(PageObjectsContext(browser))
    pageAboutYourPartner goToThePage()
    pageAboutYourPartner fillPageWith claimAboutYourPartner
    pageAboutYourPartner.submitPage(throwException = true)
  }
}
