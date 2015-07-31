package monitoring

import org.specs2.mutable.Specification
import utils.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.s_eligibility.GBenefitsPage
import com.kenshoo.play.metrics.MetricsRegistry

class HistogramsSpec extends Specification {
  "Histogram metrics" should {
    "must measure play-cache-size" in new WithBrowser with PageObjects {
      val page = GBenefitsPage(context)
      page goToThePage()
      val count = MetricsRegistry.defaultRegistry.histogram("c3-cache-size").getCount
      count mustEqual 1
    }
  } section "unit"
}
