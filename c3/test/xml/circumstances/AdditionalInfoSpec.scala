package xml.circumstances

import models.view.CachedChangeOfCircs
import org.specs2.mutable.{Tags, Specification}
import models.domain._
import models.domain.Claim

class AdditionalInfoSpec extends Specification with Tags {
  val otherInfo = "Some other info"

  "Additional Info" should {
    "generate xml" in {
      val claim = Claim(CachedChangeOfCircs.key).update(CircumstancesOtherInfo(otherInfo))
      val xml = AdditionalInfo.xml(claim)

      (xml \\ "OtherChanges").text shouldEqual otherInfo
    }

  } section "unit"
}