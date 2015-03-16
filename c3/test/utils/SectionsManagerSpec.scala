package utils

import models.view.CachedClaim
import org.specs2.mutable.{Tags, Specification}
import models.domain._
import models.domain.Claim


class SectionsManagerSpec extends Specification with Tags {

  "Sections Manager" should {

    "Retrieve the correct number of sections" in {

      SectionsManager.claimSectionsNum(Claim(CachedClaim.key)) mustEqual 10
    }

    "Retrieve the correct section position for some Sections" in {

      implicit val claim = Claim(CachedClaim.key)
      SectionsManager.currentSection(AboutYou) mustEqual 2
      SectionsManager.currentSection(Employed) mustEqual 6
    }

    "Retrieve the correct section positions when there are hidden sections" in {
      implicit val claim = Claim(CachedClaim.key).showHideSection(visible = false,PayDetails)

      SectionsManager.currentSection(AboutYou) mustEqual 2
      SectionsManager.currentSection(OtherMoney) mustEqual 8
    }

    "Retrieve the correct section positions when there are hidden employment" in {
      implicit val claim = Claim(CachedClaim.key).showHideSection(visible = false,Employed)

      SectionsManager.currentSection(SelfEmployment) mustEqual 6
      SectionsManager.currentSection(PayDetails) mustEqual 8
    }

  }

}
