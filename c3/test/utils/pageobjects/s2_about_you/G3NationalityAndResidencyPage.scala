package utils.pageobjects.s2_about_you

import play.api.i18n.Messages

final class G3NationalityAndResidencyPage(browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G3NationalityAndResidencyPage.url, G3NationalityAndResidencyPage.title, previousPage) {

}

object G3NationalityAndResidencyPage {
  val title = Messages("s2.g3") + " - " + Messages("s2.longName")

  vale url = "/about-you/nationality-and-residency"
}