package xml

import models.domain.{Claim, MoreAboutTheCare}
import org.specs2.mutable.{Tags, Specification}


class EvidenceListSpec extends Specification with Tags {

  "Assisted section" should {

    "Create a assisted decision section if care less than 35 hours" in {
      val moreAboutTheCare = MoreAboutTheCare("no")
      val claim = Claim().update(moreAboutTheCare)
      val xml = EvidenceList.assistedDecision(claim)
      (xml \\ "TextLine").text must contain("Do not spend 35 hours or more each week caring. NIL decision, but need to check advisory additional notes.")
    }

  } section "unit"

}
