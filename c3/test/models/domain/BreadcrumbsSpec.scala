package models.domain

import org.specs2.mutable.Specification
import org.specs2.execute.PendingUntilFixed

class BreadcrumbsSpec extends Specification with PendingUntilFixed {
  "Breadcrumbs" should {
    "not display completed sections when in the first section" in {
      val breadcrumbs = Breadcrumbs(currentSectionId = CarersAllowance.id)

      breadcrumbs.completedSections mustEqual (Nil)
      breadcrumbs.activeSection mustEqual "CarersAllowance"
      breadcrumbs.futureSections mustEqual (Seq("AboutYou", "YourPartner", "CareYouProvide"))
    }
        
    "display completed and future sections when in the middle section" in {
      val breadcrumbs = Breadcrumbs(currentSectionId = AboutYou.id)

      breadcrumbs.completedSections mustEqual (Seq("CarersAllowance"))
      breadcrumbs.activeSection mustEqual "AboutYou"
      breadcrumbs.futureSections mustEqual (Seq("YourPartner", "CareYouProvide"))
    }
    
    "not display future sections when in the last section" in {
      val breadcrumbs = Breadcrumbs(currentSectionId = CareYouProvide.id)

      breadcrumbs.completedSections mustEqual (Seq("CarersAllowance", "AboutYou", "YourPartner"))
      breadcrumbs.activeSection mustEqual "CareYouProvide"
      breadcrumbs.futureSections mustEqual (Nil)
    }
  }
}