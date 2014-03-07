package xml.circumstances

import org.specs2.mutable.{Tags, Specification}
import models.domain._

class OtherChangesSpec extends Specification with Tags {
  val otherInfo = "Some other info"

  "Additional Info" should {
    "generate xml" in {
      val circs = Claim().update(CircumstancesOtherInfo(otherInfo))
      val xml = AdditionalInfo.xml(circs)
      (xml \\ "OtherChanges" \ "QuestionLabel").text shouldEqual "c2.g1"
      (xml \\ "OtherChanges" \ "Answer").text shouldEqual otherInfo
    }

  } section "unit"
}