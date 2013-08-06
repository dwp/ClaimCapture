package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain.{Claim, Employment}
import controllers.Mappings.{yes, no}

class SelfEmployedSpec extends Specification with Tags {

  "SelfEmployed" should {

    "generate SelfEmployed xml with answer 'yes' when claimer is self employed" in {
      val claim = Claim().update(Employment(beenSelfEmployedSince1WeekBeforeClaim = yes))
      val selfEmployedXml = SelfEmployed.xml(claim)
      (selfEmployedXml \\ "SelfEmployed").text mustEqual yes
    }

    "generate SelfEmployed xml with answer 'no' when claimer is NOT self employed" in {
      val claim = Claim().update(Employment(beenSelfEmployedSince1WeekBeforeClaim = no))
      val selfEmployedXml = SelfEmployed.xml(claim)
      (selfEmployedXml \\ "SelfEmployed").text mustEqual no
    }
  } section "unit"
}