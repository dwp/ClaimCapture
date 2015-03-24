package monitoring

import org.specs2.mutable.Specification
import play.api.test.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.s1_carers_allowance.G1BenefitsPage
import com.kenshoo.play.metrics.MetricsRegistry

class MetersSpec extends Specification {

  "Meters metrics" should {
    "must measure com.kenshoo.play.metrics.MetricsFilter.200" in new WithBrowser with PageObjects {
      val page = G1BenefitsPage(context)
      page goToThePage()
      val count = MetricsRegistry.defaultRegistry.meter("com.kenshoo.play.metrics.MetricsFilter.200").getCount
      count mustEqual 3
    }
  } section "unit"
}
