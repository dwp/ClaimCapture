package services.submission

import org.specs2.mutable._
import controllers.submission.ClaimStatusRoutingController
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.{ClaimTransactionComponent, DBTests, WithApplicationAndDB}
import models.domain.{Claiming, Claim}
import models.view.CachedClaim
import play.api.cache.CacheApi

class StatusRoutingControllerSpec extends Specification {
  "Status routing controller" should {
    val transId = "1234567"

    val transactionComponent = new ClaimTransactionComponent {
      override val claimTransaction = new ClaimTransaction
    }

    "Handle case Thank You on successful submission" in new WithApplicationAndDB with Claiming {
      cache.set(claimKey,new Claim(CachedClaim.key,transactionId = Some(transId)))
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      // create id with status success in db, to mock that a successful submit took place
      DBTests.createId(transId)
      transactionComponent.claimTransaction.registerId(transId, ClaimSubmissionService.SUCCESS, controllers.submission.FULL_CLAIM, 1)


      // call controller which will check and find success status in db
      // how do we pass txId to controller or do we need to at all?
      val result = ClaimStatusRoutingController.submit(request)

      // ensure that the controller processed the success status properly
      // and that it redirects to the happy route

      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustEqual Some("/thankyou/apply-carers")
    }

    "Handled service unavailable" in new WithApplicationAndDB with Claiming{
      cache.set(claimKey,new Claim(CachedClaim.key,transactionId = Some(transId)))
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      DBTests.createId(transId)
      transactionComponent.claimTransaction.registerId(transId, ClaimSubmissionService.SERVICE_UNAVAILABLE, controllers.submission.FULL_CLAIM, 1)

      val result = ClaimStatusRoutingController.submit(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustEqual Some("/error-retry")
    }

    "Handled not yet submitted (retry)" in new WithApplicationAndDB with Claiming {
      cache.set(claimKey,new Claim(CachedClaim.key,transactionId = Some(transId)))
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      DBTests.createId(transId)
      transactionComponent.claimTransaction.registerId(transId, AsyncClaimSubmissionService.GENERATED, controllers.submission.FULL_CLAIM, 1)

      val result = ClaimStatusRoutingController.submit(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustEqual Some("/async-submitting")
    }

    "Handle error case on failed submission" in new WithApplicationAndDB with Claiming {
      cache.set(claimKey,new Claim(CachedClaim.key,transactionId = Some(transId)))
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      DBTests.createId(transId)
      transactionComponent.claimTransaction.registerId(transId, ClaimSubmissionService.BAD_REQUEST_ERROR, controllers.submission.FULL_CLAIM, 1)

      val result = ClaimStatusRoutingController.submit(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustEqual Some("/async-error")
    }
  }
  section("unit")
}
