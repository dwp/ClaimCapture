package controllers.circs.submission

import org.specs2.mutable.{Tags, Specification}
import services.submission.{ClaimSubmissionService, WebServiceClientComponent}
import monitoring.ChangeBotChecking
import models.view.{CachedClaim, CachedChangeOfCircs}
import services.ClaimTransactionComponent
import models.domain.{CircumstancesDeclaration, Claim}
import org.specs2.mock.Mockito
import controllers.submission.SubmissionController

class ChangeOfCircsSubmissionControllerSpec extends Specification with Tags with Mockito with CachedClaim {

  val changeOfCircsSubmissionController = new SubmissionController
    with ClaimSubmissionService
    with ClaimTransactionComponent
    with WebServiceClientComponent
    with ChangeBotChecking
    with CachedChangeOfCircs {

    val webServiceClient = mock[WebServiceClient]
    val claimTransaction = mock[ClaimTransaction]
  }

  "Claim submission" should {
    "returns false given CircumstancesDeclaration answered yes and honeyPot not filled" in {
      val circs = Claim().update(CircumstancesDeclaration(obtainInfoAgreement = "yes", obtainInfoWhy = None))
      changeOfCircsSubmissionController.honeyPot(circs) should beFalse
    }

    "returns false given CircumstancesDeclaration answered no and honeyPot filled" in {
      val circs = Claim().update(CircumstancesDeclaration(obtainInfoAgreement = "no", obtainInfoWhy = Some("stuff")))
      changeOfCircsSubmissionController.honeyPot(circs) should beFalse
    }

    "return true given CircumstancesDeclaration answered yes and honeypot filled in" in {
      val circs = Claim().update(CircumstancesDeclaration(obtainInfoAgreement = "yes", obtainInfoWhy = Some("stuff")))
      changeOfCircsSubmissionController.honeyPot(circs) should beTrue
    }

    "be flagged for completing sections too quickly e.g. a bot" in {
      val circs = copyInstance(Claim().update(CircumstancesDeclaration(obtainInfoAgreement = "no", obtainInfoWhy = Some("stuff"))))
      changeOfCircsSubmissionController.checkTimeToCompleteAllSections(circs, currentTime = 0) should beTrue
    }

    "be completed slow enough to be human" in {
      val circs = copyInstance(Claim().update(CircumstancesDeclaration(obtainInfoAgreement = "no", obtainInfoWhy = Some("stuff"))))
      changeOfCircsSubmissionController.checkTimeToCompleteAllSections(circs, currentTime = Long.MaxValue) should beFalse
    }
  }
}
