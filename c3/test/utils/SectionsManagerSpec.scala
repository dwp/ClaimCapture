package utils

import org.specs2.mutable.{Tags, Specification}
import models.domain._
import models.domain.Claim


class SectionsManagerSpec extends Specification with Tags {

  "Sections Manager" should {

    "Retrieve the correct number of sections" in {

      SectionsManager.claimSectionsNum(Claim()) mustEqual 8
    }

    "Retrieve the correct section position for some Sections" in {

      implicit val claim = Claim()
      SectionsManager.currentSection(AboutYou) mustEqual 1
      SectionsManager.currentSection(ConsentAndDeclaration) mustEqual 8
      SectionsManager.currentSection(Employed) mustEqual 5
    }

    "Retrieve the correct section positions when there are hidden sections" in {
      implicit val claim = Claim().showHideSection(visible = false,PayDetails)

      SectionsManager.currentSection(AboutYou) mustEqual 1
      SectionsManager.currentSection(ConsentAndDeclaration) mustEqual 7
    }

  }

}
