package xml.circumstances

import models.view.CachedChangeOfCircs
import org.specs2.mutable._
import models.domain._
import utils.WithApplication

class OtherChangesSpec extends Specification {
  val otherInfo = "Some other info"

  section("unit")
  "Additional Info" should {
    "generate xml" in new WithApplication() {
      val circs = Claim(CachedChangeOfCircs.key).update(CircumstancesOtherInfo(otherInfo))
      val xml = OtherChanges.xml(circs)
      (xml \\ "OtherChanges" \ "QuestionLabel").text shouldEqual "Something else"
      (xml \\ "OtherChanges" \ "Answer").text shouldEqual otherInfo
    }
  }
  section("unit")
}
