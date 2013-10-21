package services.util

import org.specs2.mutable.{Tags, Specification}

class CharacterStripperSpec extends Specification with Tags {
  "Stripper" should {
    "strip non pdf chars" in {
      val input = "<xml>what the £ is going (on) here`s @</xml>"
      CharacterStripper.stripNonPdf(input) mustEqual "<xml>what the  is going on heres </xml>"
    }

  } section "unit"


}
