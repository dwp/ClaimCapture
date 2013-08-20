package utils.pageobjects.s4_care_you_provide

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

/**
 * Page object for s4_care_you_provide g3_more_about_the_person.
 * @author Saqib Kayani
 *         Date: 25/07/2013
 */
final class G3RelationshipAndOtherClaimsPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G3RelationshipAndOtherClaimsPage.url, G3RelationshipAndOtherClaimsPage.title, previousPage)  {
  declareSelect("#relationship","AboutTheCareYouProvideWhatTheirRelationshipToYou")
  declareYesNo("#armedForcesPayment", "AboutTheCareYouProvideDoesPersonGetArmedForcesIndependencePayment")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G3RelationshipAndOtherClaimsPage {
  val title = "Relationship and other claims - About the care you provide".toLowerCase

  val url  = "/care-you-provide/relationship-and-other-claims"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G3RelationshipAndOtherClaimsPage(browser, previousPage)
}

/** The context for Specs tests */
trait G3RelationshipAndOtherClaimsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G3RelationshipAndOtherClaimsPage buildPageWith browser
}