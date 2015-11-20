package xml.circumstances

import models.view.CachedChangeOfCircs
import org.specs2.mutable._
import models.domain._

class OtherChangesSpec extends Specification {

  val otherInfo = "Some other info"

  "Additional Info" should {
    "generate xml" in {
      val circs = Claim(CachedChangeOfCircs.key).update(CircumstancesOtherInfo(otherInfo))
      val xml = OtherChanges.xml(circs)
      (xml \\ "OtherChanges" \ "QuestionLabel").text shouldEqual "c2.g1"
      (xml \\ "OtherChanges" \ "Answer").text shouldEqual otherInfo
    }.pendingUntilFixed("Pending till schema changes and modifying the code to new structure")

  }
section("unit")
}
