package controllers.submission

import models.view.{CachedChangeOfCircs, CachedClaim}
import org.specs2.mutable._
import models.domain.Claim
import gov.dwp.carers.xml.validation.CaFutureXmlValidatorImpl
import utils.WithApplication

class SubmissionSpec extends Specification {
  section("unit", models.domain.Submit.id)
  "Submission" should {
    "get CA XML validator for a given full claim" in new WithApplication {
      xmlValidator(new Claim(CachedClaim.key)) should beAnInstanceOf[CaFutureXmlValidatorImpl]
    }

    "get COC XML validator for a given change of circs claim" in new WithApplication {
      xmlValidator(new Claim(CachedChangeOfCircs.key)) should beAnInstanceOf[CaFutureXmlValidatorImpl]
    }
  }
  section("unit", models.domain.Submit.id)
}
