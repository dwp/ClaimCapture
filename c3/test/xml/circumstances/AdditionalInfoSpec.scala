package xml.circumstances

import models.view.CachedChangeOfCircs
import org.specs2.mutable._
import models.domain._
import models.domain.Claim
import utils.WithApplication

class AdditionalInfoSpec extends Specification {
  val otherInfo = "Some other info"

  "Additional Info" should {
    "generate xml" in new WithApplication {
      val claim = Claim(CachedChangeOfCircs.key).update(CircumstancesOtherInfo(otherInfo))
      val xml = AdditionalInfo.xml(claim)

      (xml \\ "OtherChanges").text shouldEqual otherInfo
    }

  }
  section("unit")
}
