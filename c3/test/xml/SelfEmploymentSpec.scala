package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain.{AboutSelfEmployment, Employment, Claim}
import controllers.Mappings._

class SelfEmploymentSpec extends Specification with Tags {

  "SelfEmployment" should {

    "generate xml when data is present" in {
      val claim = Claim().update(Employment(beenSelfEmployedSince1WeekBeforeClaim = yes))
      .update(AboutSelfEmployment(areYouSelfEmployedNow = yes))

      val selfEmploymentXml = SelfEmployment.xml(claim)

      (selfEmploymentXml \\ "SelfEmployedNow").text mustEqual yes

      println(selfEmploymentXml)
    }

    "generate xml when data is missing" in {
      val claim = Claim().update(Employment(beenSelfEmployedSince1WeekBeforeClaim = no))
      val selfEmploymentXml = SelfEmployment.xml(claim)
      selfEmploymentXml.text mustEqual ""
    }

  } section "unit"

}