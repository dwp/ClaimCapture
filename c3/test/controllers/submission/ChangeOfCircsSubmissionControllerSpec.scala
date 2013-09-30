package controllers.submission

import org.specs2.mutable.Specification
import models.domain._
import controllers.circs.submission.ChangeOfCircsSubmissionController
import org.specs2.mock.Mockito

class ChangeOfCircsSubmissionControllerSpec extends Specification with Mockito {
  val controller = new ChangeOfCircsSubmissionController(mock[Submitter])

  "Claim submission" should {
    "returns false given CircumstancesDeclaration answered yes and honeyPot not filled" in {
      val circs = Claim().update(CircumstancesDeclaration(obtainInfoAgreement = "yes", obtainInfoWhy = None))
      controller.honeyPot(circs) should beFalse
    }

    "returns false given CircumstancesDeclaration answered no and honeyPot filled" in {
      val circs = Claim().update(CircumstancesDeclaration(obtainInfoAgreement = "no", obtainInfoWhy = Some("stuff")))
      controller.honeyPot(circs) should beFalse
    }

    "return true given CircumstancesDeclaration answered yes and honeypot filled in" in {
      val circs = Claim().update(CircumstancesDeclaration(obtainInfoAgreement = "yes", obtainInfoWhy = Some("stuff")))
      controller.honeyPot(circs) should beTrue
    }
  }
}