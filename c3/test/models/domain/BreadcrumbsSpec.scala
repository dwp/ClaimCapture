package models.domain

import org.specs2.mutable.Specification
import org.specs2.execute.PendingUntilFixed

class BreadcrumbsSpec extends Specification with PendingUntilFixed {
  "Breadcrumbs" should {
    "not display completed sections when in the first section" in {
      val breadcrumbs = Breadcrumbs(activeSection = CarersAllowance.id)

      breadcrumbs.completedSections mustEqual (Nil)
      breadcrumbs.activeSection mustEqual CarersAllowance.id
      breadcrumbs.futureSections mustEqual (Seq(AboutYou.id, YourPartner.id, CareYouProvide.id))
    }
        
    "display completed and future sections when in the middle section" in {
      val breadcrumbs = Breadcrumbs(activeSection = AboutYou.id)

      breadcrumbs.completedSections mustEqual (Seq(CarersAllowance.id))
      breadcrumbs.activeSection mustEqual AboutYou.id
      breadcrumbs.futureSections mustEqual (Seq(YourPartner.id, CareYouProvide.id))
    }
    
    "not display future sections when in the last section" in {
      val breadcrumbs = Breadcrumbs(activeSection = CareYouProvide.id)

      breadcrumbs.completedSections mustEqual (Seq(CarersAllowance.id, AboutYou.id, YourPartner.id))
      breadcrumbs.activeSection mustEqual CareYouProvide.id
      breadcrumbs.futureSections mustEqual (Nil)
    }
  }
}