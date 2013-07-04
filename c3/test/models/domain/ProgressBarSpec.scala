package models.domain

import org.specs2.mutable.Specification
import org.specs2.execute.PendingUntilFixed

class ProgressBarSpec extends Specification with PendingUntilFixed {
  "Breadcrumbs" should {
    "not display completed sections when in the first section" in {
      val progressBar = ProgressBar(currentSectionId = CarersAllowance.id)

      progressBar.completedSections mustEqual (Nil)
      progressBar.activeSection mustEqual "CarersAllowance"
      progressBar.futureSections mustEqual (Seq("AboutYou", "YourPartner", "CareYouProvide"))
    }
        
    "display completed and future sections when in the middle section" in {
      val progressBar = ProgressBar(currentSectionId = AboutYou.id)

      progressBar.completedSections mustEqual (Seq("CarersAllowance"))
      progressBar.activeSection mustEqual "AboutYou"
      progressBar.futureSections mustEqual (Seq("YourPartner", "CareYouProvide"))
    }
    
    "not display future sections when in the last section" in {
      val progressBar = ProgressBar(currentSectionId = CareYouProvide.id)

      progressBar.completedSections mustEqual (Seq("CarersAllowance", "AboutYou", "YourPartner"))
      progressBar.activeSection mustEqual "CareYouProvide"
      progressBar.futureSections mustEqual (Nil)
    }
  }
}