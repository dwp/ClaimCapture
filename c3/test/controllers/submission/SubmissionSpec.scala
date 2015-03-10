package controllers.submission

import org.specs2.mutable.Specification
import models.domain.{ChangeOfCircs, FullClaim, Claim}
import gov.dwp.carers.xml.validation.{CocFutureXmlValidatorImpl, CaFutureXmlValidatorImpl}

class SubmissionSpec extends Specification {
  "Submission" should {
    "get CA XML validator for a given full claim" in {
      xmlValidator(new Claim with FullClaim) should beAnInstanceOf[CaFutureXmlValidatorImpl]
    }

    "get COC XML validator for a given change of circs claim" in {
      xmlValidator(new Claim with ChangeOfCircs) should beAnInstanceOf[CocFutureXmlValidatorImpl]
    }
  }
}