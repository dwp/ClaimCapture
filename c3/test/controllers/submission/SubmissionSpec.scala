package controllers.submission

import models.view.{CachedChangeOfCircs, CachedClaim}
import org.specs2.mutable.Specification
import models.domain.Claim
import gov.dwp.carers.xml.validation.{CocFutureXmlValidatorImpl, CaFutureXmlValidatorImpl}

class SubmissionSpec extends Specification {
  "Submission" should {
    "get CA XML validator for a given full claim" in {
      xmlValidator(new Claim(CachedClaim.key)) should beAnInstanceOf[CaFutureXmlValidatorImpl]
    }

    "get COC XML validator for a given change of circs claim" in {
      xmlValidator(new Claim(CachedChangeOfCircs.key)) should beAnInstanceOf[CocFutureXmlValidatorImpl]
    }
  }
}