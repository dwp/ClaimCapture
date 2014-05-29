package monitoring

import org.specs2.mutable.Specification
import play.api.test.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.s1_carers_allowance.G1BenefitsPage

class HistogramsSpec extends Specification {
  "Histogram metrics" should {
    "must measure play-cache-size" in new WithBrowser with PageObjects {
      val page = G1BenefitsPage(context)
      page goToThePage()
      val count = Histograms.cacheSize.getCount
      count mustEqual (1)
    }
  } section "unit"
}
