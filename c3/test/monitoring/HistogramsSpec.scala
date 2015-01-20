package monitoring

import org.specs2.mutable.Specification
import play.api.test.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.s1_carers_allowance.G1BenefitsPage
import com.kenshoo.play.metrics.MetricsRegistry

class HistogramsSpec extends Specification {
  "Histogram metrics" should {
    "must measure play-cache-size" in new WithBrowser with PageObjects {
      val page = G1BenefitsPage(context)
      page goToThePage()
      val count = MetricsRegistry.defaultRegistry.histogram("c3-cache-size").getCount
      count mustEqual 1
    }
  } section "unit"
}
