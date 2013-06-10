package models.claim

import org.specs2.mutable.Specification

class BenefitsFormSpec extends Specification {

  "BenefitsForm" should {
    "return the section id" in {
      BenefitsForm().section mustEqual "s1"
    }
  }
}