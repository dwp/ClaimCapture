package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain._
import xml.circumstances.OtherChanges

class AdditionalInfoSpec extends Specification with Tags {
  val otherInfo = "Some other info"

  "Additional Info" should {
    "generate xml" in {
      val claim = Claim().update(CircumstancesOtherInfo(otherInfo))
      val xml = OtherChanges.xml(claim)
      (xml \\ "OtherChanges" \ "QuestionLabel").text shouldEqual "c2.g1"
      (xml \\ "OtherChanges" \ "Answer").text shouldEqual otherInfo
    }

  } section "unit"
}