package services.submission

import org.specs2.mutable.{Tags, Specification}
import controllers.submission.StatusRoutingController
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.{ClaimTransactionComponent, DBTests, WithApplicationAndDB}
import models.domain.{Claiming, Claim}
import models.view.CachedClaim
import play.api.cache.Cache

class StatusRoutingControllerSpec extends Specification with Tags {


  "Status routing controller" should {

    val transId = "1234567"

    val transactionComponent = new ClaimTransactionComponent {
      override val claimTransaction = new ClaimTransaction
    }
    "Handle case Thank You on successful submission" in new WithApplicationAndDB with Claiming {
      Cache.set(claimKey,new Claim(transactionId = Some(transId)))
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      // create id with status success in db, to mock that a successful submit took place
      DBTests.createId(transId)
      transactionComponent.claimTransaction.registerId(transId, AsyncClaimSubmissionService.SUCCESS, controllers.submission.FULL_CLAIM)


      // call controller which will check and find success status in db
      // how do we pass txId to controller or do we need to at all?
      val result = StatusRoutingController.submit(request)

      // ensure that the controller processed the success status properly
      // and that it redirects to the happy route

      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustEqual Some("/thankyou/apply-carers")


    }

    "Handled not yet submitted (retry)" in new WithApplicationAndDB{
      pending
      val request = FakeRequest()

      val result = StatusRoutingController.submit(request)

      status(result) mustEqual OK
      redirectLocation(result) mustEqual "/error-retry"

    }

    "Handled not yet submitted (retry)" in new WithApplicationAndDB{

      pending
      val request = FakeRequest()

      val result = StatusRoutingController.submit(request)

      status(result) mustEqual OK
      redirectLocation(result) mustEqual "/submitting"

    }

    "Handle error case on failed submission" in new WithApplicationAndDB{

      pending
      val request = FakeRequest()

      val result = StatusRoutingController.submit(request)

      status(result) mustEqual BAD_REQUEST

    }



  }

}
